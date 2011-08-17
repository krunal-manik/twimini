package twitter.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.json.simple.parser.Yytoken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.ranges.RangeException;
import twitter.models.Contact;
import twitter.models.User;
import twitter.services.Follow;
import twitter.services.UserAuthentication;

import javax.servlet.http.HttpSession;
import javax.tools.JavaCompiler;
import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rahul.pl
 * Date: 7/5/11
 * Time: 5:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class EditProfileController {

    final SimpleJdbcTemplate db;
    public static final String prefixPath = System.getProperty("user.home") + "\\Desktop\\photos\\";

    @Autowired
    public EditProfileController(SimpleJdbcTemplate db) {
        this.db = db;
    }

    private static String validateName( String name ) {
        if( name == null || name.equals("") )
            return "Name cannot be blank";
        for(int i=0;i<name.length();i++)
            if( !(Character.isLetter(name.charAt(i)) || name.charAt(i) == ' ') )
                return "Name should only contain charcters a-z,' ',A-Z";
        return " ";
    }

    private static String validateUsername( String username ) {
        if( username == null || username.equals("") )
            return "Username cannot be blank";
        if( !Character.isLowerCase(username.charAt(0)))
            return "Username should start with a lower case letter";
        for(int i=0;i<username.length();i++)
            if( !(Character.isLowerCase(username.charAt(i)) ||
                    username.charAt(i) == '-' ||
                    Character.isDigit(username.charAt(i)) ) )
                return "Username should only contain characters a-z,'-',0-9";
        return " ";
    }

    @RequestMapping(value = {"/edit_profile"})
    public ModelAndView editProfileGet(String nameError,String usernameError,String saveMessage,HttpSession session) {
        if (session.getAttribute("username") == null) {
            return new ModelAndView("/error404");
        } else {
            String username = session.getAttribute("username").toString();
            ModelAndView mv = new ModelAndView("/profile-edit");
            mv.addObject( "image" , "/photos/" + session.getAttribute("userId") + ".jpg" );
            User user = UserAuthentication.getUserByUsername( username );
            mv.addObject("name",user.getName());
            mv.addObject("aboutMe",user.getAboutMe());
            mv.addObject("username",user.getUsername());
            mv.addObject( "nameError" , nameError );
            mv.addObject( "usernameError" , usernameError );
            mv.addObject("saveMessage",saveMessage);
            return mv;
        }
    }

    @RequestMapping(value = "/edit_profile", method = RequestMethod.POST)
    public ModelAndView uploadPost(HttpSession session, @RequestParam MultipartFile file, @RequestParam String name, @RequestParam String username ,@RequestParam String aboutMe) throws Exception {
        String loggedInusername = session.getAttribute("username").toString();
        String photoPath = prefixPath + session.getAttribute("userId").toString() + ".jpg";
        ModelAndView mv = new ModelAndView("/profile-edit");
        if( !file.isEmpty() ) {
            try {
                File photo = new File(photoPath);

                if ( photo.exists()) {
                    photo.delete();
                }
                photo.createNewFile();

                if (!file.isEmpty()) {
                    byte imagesBytes[] = file.getBytes();
                    FileOutputStream outputStream = new FileOutputStream(photo);
                    outputStream.write(imagesBytes);
                    outputStream.close();
                }
            } catch (Exception ex) {
                System.out.println("Bug in fileupload :(");
                ex.printStackTrace();
            }
       }

        String isValidName = validateName(name);
        String isValidUsername = validateUsername(username);

        boolean isUsernameAvailable = UserAuthentication.checkUsernameAvailabilty(loggedInusername,username);
        System.out.println( isUsernameAvailable );
        if( !(isValidName.equals(" ") && isValidUsername.equals(" ") && isUsernameAvailable) ) {
            if( !isValidName.equals( " " ) )
                mv.addObject( "nameError" , isValidName );
            if( !isValidUsername.equals(" ") )
                mv.addObject( "usernameError" ,isValidUsername );
            else if ( !isUsernameAvailable )
                mv.addObject( "usernameError" , "Username already taken" );

            mv.addObject("username" , username );
            mv.addObject("name",name);
            mv.addObject("aboutMe",aboutMe);
        }
        else {
            UserAuthentication.updateUserInformation( loggedInusername , username , name , aboutMe );
            session.setAttribute("username",username);
            mv.addObject("saveMessage","Profile saved successfully");
        }
        mv.setViewName("redirect:/edit_profile");
        return mv;
    }




}

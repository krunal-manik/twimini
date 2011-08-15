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
public class FileUploadController {

    final SimpleJdbcTemplate db;
    public static final String prefixPath = "C:\\Users\\krunal.ma\\Desktop\\photos\\";

    @Autowired
    public FileUploadController(SimpleJdbcTemplate db) {
        this.db = db;
    }

    private static String validateName( String name ) {
        if( name.equals("") || name == null )
            return "Name cannot be empty";

        for(int i=0;i<name.length();i++)
            if( !(Character.isLetter(name.charAt(i)) || name.charAt(i) == ' ') )
                return "Name should only contain charcters a-z,' ',A-Z";
        return " ";
    }

    private static String validateUsername( String username ) {
        if( username.equals("") || username == null )
            return "Username cannot be empty";

        for(int i=0;i<username.length();i++)
            if( !(Character.isLowerCase(username.charAt(i)) ||
                    username.charAt(i) == '-' ||
                    Character.isDigit(username.charAt(i)) ) )
                return "Username should only contain characters a-z,'-',0-9";
        return " ";
    }

    @RequestMapping(value = {"/edit_profile"})
    public ModelAndView editProfileGet(String nameError,String usernameError,HttpSession session) {
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
        }
        mv.setViewName("redirect:/edit_profile");
        return mv;
    }


    @RequestMapping(value = "/import_contacts")
    public ModelAndView contactImporterGet(HttpSession session) {
        if( session.getAttribute("userId") != null ) {
            return new ModelAndView("/contact-import");
        }
        return new ModelAndView(){{
            setViewName("redirect:/");
        }};
    }


    @RequestMapping(value = "/gmail")
    @ResponseBody
    public List<Contact> contactImporter(String access_token, String token_type, String expires_in, HttpSession session) throws Exception {
        if( session.getAttribute("userId") == null ) {
            return new ArrayList<Contact>();
        }

        System.out.println(access_token);
        if (access_token == null) {
            return new ArrayList<Contact>();
        }

        URL url = new URL(String.format("https://www.google.com/m8/feeds/contacts/default/full?oauth_token=%s&max-results=500&alt=json", access_token));
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer json = new StringBuffer("");
        String s = "";
        while ((s = br.readLine()) != null) {
            json.append(s);
        }

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> emails = new ArrayList<String>();

        Object obj = JSONValue.parse(json.toString());
        JSONObject jsonObject = (JSONObject) obj;
        obj = jsonObject.get("feed");
        obj = ((JSONObject) obj).get("entry");
        JSONArray array = (JSONArray) obj;
        for (int i = 0; i < array.size(); i++) {
            JSONObject entryObject = (JSONObject) array.get(i);
            entryObject = (JSONObject) entryObject.get("title");
            String contactName = entryObject.get("$t").toString();

            JSONObject emailObject = (JSONObject) array.get(i);
            JSONArray arr = (JSONArray) emailObject.get("gd$email");
            if (arr != null && arr.size() > 0) {
                JSONObject emailIdObject = (JSONObject) arr.get(0);
                String email = emailIdObject.get("address").toString();
                if (contactName.equals("")) contactName = email;

                names.add(contactName);
                emails.add(email);
            }
        }


        List<User> followingList = Follow.getFollowedList(session.getAttribute("userId").toString());

        ArrayList<Contact> contactList = new ArrayList<Contact>();
        for (int i = 0; i < emails.size(); i++) {
            User user = UserAuthentication.getUserByEmail(emails.get(i));
            Contact contact = new Contact();
            contact.setEmail(emails.get(i));
            contact.setName(names.get(i));

            if (user == null) {
                contact.setStatus("Invite");
                contact.setUsername("");
                contact.setBio("");
            } else {
                contact.setUsername(user.getUsername());
                contact.setBio(user.getAboutMe());
                contact.setUserId(user.getUserId());
                for (User u : followingList) {
                    if (emails.get(i).equals(u.getEmail())) {
                        contact.setStatus("Following");
                        break;
                    }
                }
            }
            contactList.add(contact);
        }

        return contactList;
    }

    @RequestMapping("/test")
    public ModelAndView tp(){
        return new ModelAndView("/testing");
    }

}

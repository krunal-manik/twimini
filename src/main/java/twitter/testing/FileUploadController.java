package twitter.testing;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.ranges.RangeException;

import javax.servlet.http.HttpSession;
import javax.tools.JavaCompiler;
import java.io.*;
import java.net.URL;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
    public static final String prefixPath = "C:\\Users\\rahul.pl\\twimini\\src\\main\\webapp\\";
    public static final String tempPath = "C://photos/username.jpg";

    @Autowired
    public FileUploadController(SimpleJdbcTemplate db) {
        this.db = db;
    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST)
    public ModelAndView uploadPost(HttpSession session, @RequestParam MultipartFile file) throws Exception {

        try {
            InputStream in = file.getInputStream();
            System.out.println(file.getName());
            System.out.println(file.getOriginalFilename());
            OutputStream out = new BufferedOutputStream(new FileOutputStream(prefixPath + "\\static\\photo\\" + file.getOriginalFilename()));

            int bytesRead = 0;
            byte[] buffer = new byte[8192];

            while ((bytesRead = in.read(buffer, 0, 8192)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();
        } catch (Exception ex) {
            System.out.println("Bug in image upload :((");
            ex.printStackTrace();
        }

        ModelAndView mv = new ModelAndView("/testing");
        mv.addObject("image", "/static/photo/" + file.getOriginalFilename());
        return mv;
    }


    @RequestMapping( value = "/gmail" )
    public ModelAndView contactImporter( String access_token , String token_type , String expires_in )throws Exception {
        if( access_token == null ) {
            return new ModelAndView("/login");
        }
        System.out.println( access_token );
        System.out.println( token_type );
        System.out.println( expires_in );

        URL url = new URL( String.format( "https://www.google.com/m8/feeds/contacts/default/full?oauth_token=%s&max-results=500&alt=json"  , access_token ) );
        BufferedReader br = new BufferedReader( new InputStreamReader( url.openStream() ));
        StringBuffer json = new StringBuffer("");
        String s = "";
        while(  (s = br.readLine()) != null ) {
            json.append( s );
        }

        Object obj = JSONValue.parse( json.toString() );
        JSONObject jsonObject = (JSONObject)obj;
        obj = jsonObject.get( "feed" );
        obj = ((JSONObject) obj).get("entry") ;
        JSONArray array = (JSONArray)obj;
        for(int i=0;i<array.size();i++) {
            JSONObject entryObject = (JSONObject)array.get(i);
            entryObject = (JSONObject)entryObject.get("title") ;
            String contactName = entryObject.get("$t").toString();

            JSONObject emailObject = (JSONObject)array.get(i);
            JSONArray arr = (JSONArray)emailObject.get( "gd$email" );
            if( arr != null && arr.size() > 0 ) {
                JSONObject emailIdObject = (JSONObject)arr.get(0);
                String email = emailIdObject.get("address").toString();
                System.out.println( "Name : " +  contactName );
                System.out.println( "Email : " + email );
            }
        }

        ModelAndView mv = new ModelAndView( "/contact_import" );
        String tp = "bah";
        mv.addObject( "hello" , tp );
        return mv;
    }

}

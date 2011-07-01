package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/1/11
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserController {
    public final SimpleJdbcTemplate db;

    @Autowired
    public UserController(SimpleJdbcTemplate db) { this.db = db; }

    @RequestMapping("/login")
    public ModelAndView loginGet(){
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password ,
                               HttpSession session ){
        ModelAndView mv = new ModelAndView("/login");
        System.out.println("username" + username );
        System.out.println("password" + password );
        Integer userId = null;
        try{
            System.out.println( "Just before query" );
            List < Map<String,Object> > userData = db.queryForList("SELECT user_id,username,password from user where username = ?", username);
            System.out.println( "Query success" );
            if( userData.size() == 0 || !(userData.get(0).get("password").toString().equals(password)) ){
                mv.addObject( "message" , "Invalid password" );
                return mv;
            }
            userId = (Integer)userData.get(0).get("user_id");
            username = (String)userData.get(0).get("username");
        }catch( Exception e ){
            System.out.println( "Exception :(((((((" );
            e.printStackTrace();
        }
        session.setAttribute( "username" , username );
        session.setAttribute( "userId" , userId );
        mv.addObject( "message" , "Login successful");
        mv.setViewName("redirect:/tweet");
        return mv;
    }

    @RequestMapping( value = "/register" , method = RequestMethod.GET )
    public String registerGet(){
        System.out.println( "In get" );
        return "register";
    }

    @RequestMapping(value = "/register" , method = RequestMethod.POST )
    public ModelAndView register( @RequestParam("username") String username ,
                                @RequestParam("password") String password ,
                                @RequestParam("password2") String password2 ,
                                @RequestParam("name") String name ,
                                @RequestParam("email") String email ){
        System.out.println( "In post" );
        db.update( "INSERT INTO User(username,password,name,email) VALUES ( ? , ? , ? , ? )" , username , password, name, email );
        System.out.println( "insert done" );
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/login");
        return mv;
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session){
        session.invalidate();
        ModelAndView mv = new ModelAndView();
        mv.setViewName("redirect:/login");
        return mv;
    }
}

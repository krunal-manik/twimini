package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.User;
import twitter.services.UserAuthentication;

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

    public UserController() {
    }

    @RequestMapping("/login")
    public ModelAndView loginGet(){
        return new ModelAndView("/login");
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password ,
                               HttpSession session ){

        ModelAndView mv = new ModelAndView("/login");
        User currentUser = UserAuthentication.authenticateUser( username , password );
        if( currentUser == null ){
            mv.addObject( "message" , "Invalid password" );
            return mv;
        }
        session.setAttribute( "username" , currentUser.getUsername() );
        session.setAttribute( "userId" , currentUser.getUserId() );
        mv.addObject( "message" , "Login successful");
        mv.setViewName("redirect:/tweet");
        return mv;
    }

    @RequestMapping( value = "/register" , method = RequestMethod.GET )
    public String registerGet(){
        return "register";
    }

    @RequestMapping(value = "/register" , method = RequestMethod.POST )
    public ModelAndView register( @RequestParam("username") String username ,
                                @RequestParam("password") String password ,
                                @RequestParam("name") String name ,
                                @RequestParam("email") String email ){
        UserAuthentication.registerUser( username , password , name ,email );
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

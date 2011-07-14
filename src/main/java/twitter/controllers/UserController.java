package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.User;
import twitter.services.Email;
import twitter.services.Follow;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import javax.servlet.http.HttpSession;
import java.util.Hashtable;
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

    @Autowired
    Email ee;
    public UserController() {
    }


    @RequestMapping( value = { "/" , "/login" } )
    public ModelAndView loginGet(HttpSession session) {
        if( session.getAttribute( "username" ) == null )
            return new ModelAndView("/login");
        return TweetController.tweetsList( session );
    }

    @RequestMapping(value = { "/" , "/login" } , method = RequestMethod.POST)
    public ModelAndView login(@RequestParam("username") String username,
                              @RequestParam("password") String password ,
                               HttpSession session ){

        if( session.getAttribute( "username" ) != null )
            return TweetController.tweetsList( session );

        ModelAndView mv = new ModelAndView("/login");
        User currentUser = UserAuthentication.authenticateUser( username , password );
        if( currentUser == null ){
            mv.addObject( "message" , "Invalid password" );
            return mv;
        }
        session.setAttribute( "username" , currentUser.getUsername() );
        session.setAttribute( "userId" , currentUser.getUserId() );
        mv.setViewName("redirect:/");
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
        ModelAndView mv = new ModelAndView( "/login" );
        return mv;
    }

    @RequestMapping("/{username}")
    public ModelAndView getUserProfile(@PathVariable String username, HttpSession session){
        User urlMappedUser = UserAuthentication.getUserByUsername(username);

        if( urlMappedUser == null ) {
            ModelAndView mv = new ModelAndView("/error404");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/profile");
        String userId = String.valueOf( urlMappedUser.getUserId() );
        mv.addObject("userTweets", UserTweetList.userTweetList( userId ));
        mv.addObject("followerList", Follow.getFollowerListLimited( userId ));
        mv.addObject("followedList", Follow.getFollowedListLimited( userId ));
        mv.addObject("followerCount", Follow.getFollowerList( userId ).size() );
        mv.addObject("followingCount", Follow.getFollowedList( userId ).size() );
        if( session.getAttribute("userId") != null )
            mv.addObject("allUserList", Follow.allUsersList(session.getAttribute("userId").toString()));

        mv.addObject( "currentUsername" , username );
        return mv;
    }

    @RequestMapping( "/test" )
    public ModelAndView getTestPage(){
        ModelAndView mv = new ModelAndView("testing");
        return mv;
    }

    @RequestMapping( value = "/email" , method = RequestMethod.POST )
    public ModelAndView test( @RequestParam String email ){
          String password = UserAuthentication.getPassword( email );
          ee.sendMail( "manikkrunal@gmail.com" , email , "Password recovery" ,
                  String.format( "Email : %s\nPassword : %s\n" , email , password ) );
          ModelAndView mv = new ModelAndView("/login");
          return mv;
    }
}

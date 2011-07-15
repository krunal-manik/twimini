package twitter.controllers;

import org.hsqldb.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.User;
import twitter.services.Follow;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import javax.servlet.http.HttpSession;
import java.io.File;
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
        ModelAndView mv = new ModelAndView("test_page");
        return mv;
    }

    @RequestMapping( "/test_upload_image" )
    public ModelAndView getTestUploadImagePage(){
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @RequestMapping( value = "/test_upload_image", method = RequestMethod.POST)
    public ModelAndView getTestUploadImagePOST(@RequestParam File img, HttpSession session){
        System.out.println(img.getAbsolutePath());
        File f = new File(session.getAttribute("username").toString() + ".jpg");
        System.out.println(f.getAbsolutePath());
        System.out.println("File Exits - > " + f.exists());
        ModelAndView mv = new ModelAndView();
        return mv;
    }


    @RequestMapping("/{username}/followers")
    public ModelAndView getUserFollowers(@PathVariable String username){
        User urlMappedUser = UserAuthentication.getUserByUsername(username);

        if( urlMappedUser == null ) {
            ModelAndView mv = new ModelAndView("/error404");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/followers");
        String userId = String.valueOf( urlMappedUser.getUserId() );
        mv.addObject("followerList", Follow.getFollowerList( userId ));
        mv.addObject( "username" , username );
        return mv;
    }

    @RequestMapping("/{username}/followings")
    public ModelAndView getUserFollowings(@PathVariable String username){
        User urlMappedUser = UserAuthentication.getUserByUsername(username);

        if( urlMappedUser == null ) {
            ModelAndView mv = new ModelAndView("/error404");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/followings");
        String userId = String.valueOf( urlMappedUser.getUserId() );
        mv.addObject("followedList", Follow.getFollowedList( userId ));
        mv.addObject( "username" , username );
        return mv;
    }
}

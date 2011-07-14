package twitter.controllers;

import org.hsqldb.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.Tweet;
import twitter.models.User;
import twitter.services.Follow;
import twitter.services.UserTweetList;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/1/11
 * Time: 3:36 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TweetController {

    public TweetController(){
    }

    @RequestMapping( value = "/tweet/addTweet" , method = RequestMethod.POST ) @ResponseBody // Ajax call
    public Tweet addTweet( @RequestParam String tweetContent , HttpSession session ){
        Tweet t = UserTweetList.addTweet( tweetContent , session.getAttribute("userId").toString() );
        return t;
    }


    @RequestMapping("/tweet")
    public static ModelAndView tweetsList(HttpSession session) {
        ModelAndView mv = new ModelAndView( "/tweet" );
        //System.out.println("HAHAHA" + UserTweetList.userTweetList(session.getAttribute("userId").toString()));
        mv.addObject("tweetsList_o", UserTweetList.userTweetList_o(session.getAttribute("userId").toString()));
        mv.addObject("followerList", Follow.getFollowerList(session.getAttribute("userId").toString()));
        mv.addObject("followedList", Follow.getFollowedList(session.getAttribute("userId").toString()));
        mv.addObject("allUserList", Follow.allUsersList( session.getAttribute("userId").toString()));
        return mv;
    }

    @RequestMapping("/new_profile")
    public ModelAndView profile_data(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        mv.addObject("tweetsList", UserTweetList.userTweetList(session.getAttribute("userId").toString()));
        //System.out.println("HAHAHA" + UserTweetList.userTweetList(session.getAttribute("userId").toString()));
        mv.addObject("followerList", Follow.getFollowerList(session.getAttribute("userId").toString()));
        mv.addObject("followedList", Follow.getFollowedList(session.getAttribute("userId").toString()));
        return mv;
    }
}

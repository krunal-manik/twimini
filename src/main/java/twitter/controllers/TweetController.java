package twitter.controllers;

import org.apache.commons.lang.StringEscapeUtils;
import org.hsqldb.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.Tweet;
import twitter.models.User;
import twitter.services.Follow;
import twitter.services.Mention;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import javax.servlet.http.HttpSession;
import java.util.*;

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

    @RequestMapping( value = "/nTweetsAfterTimestamp" ) @ResponseBody // Ajax call
    public List<Tweet> nTweetsOfNewsfeedAfterTimestamp( HttpSession session, @RequestParam String timestamp, @RequestParam String n) {
        List<Tweet> ret = UserTweetList.nTweetsOfNewsfeedByTimestamp(session.getAttribute("userId").toString(), timestamp, null, true);
        return ret;
    }

    @RequestMapping( value = "/nTweetsBeforeTimestamp" ) @ResponseBody // Ajax call
    public List<Tweet> nTweetsOfNewsfeedBeforeTimestamp( HttpSession session, @RequestParam String timestamp, @RequestParam String n) {
        List<Tweet> ret = UserTweetList.nTweetsOfNewsfeedByTimestamp(session.getAttribute("userId").toString(), timestamp, n, false);
        return ret;
    }

    @RequestMapping( value = "/firstNewsfeed" ) @ResponseBody // Ajax call
    public List<Tweet> firstNewsfeed( HttpSession session) {
        List<Tweet> ret = UserTweetList.nTweetsOfNewsfeedByTimestamp(session.getAttribute("userId").toString(), null, "10", true);
        return ret;
    }

    @RequestMapping( value = "/tweet/addTweet" ) @ResponseBody // Ajax call
    public Tweet addTweet( @RequestParam String tweetContent , HttpSession session ){
        Tweet t = UserTweetList.addTweet( tweetContent , session.getAttribute("userId").toString() );
        return t;
    }

    @RequestMapping("/tweet")
    public static ModelAndView tweetsList(HttpSession session) {
        ModelAndView mv = new ModelAndView( "/tweet" );
        String userId = session.getAttribute( "userId" ).toString();
        List<Tweet> tweetList = UserTweetList.newsFeed(userId);
        for( Tweet tweet : tweetList )
            tweet.setTweet( StringEscapeUtils.escapeJavaScript(UserTweetList.escapeHTML(tweet.getTweet())) );
        List<Tweet> mentionList = UserTweetList.mentionFeed(userId, userId);
        for( Tweet tweet : mentionList )
            tweet.setTweet( StringEscapeUtils.escapeJavaScript(UserTweetList.escapeHTML(tweet.getTweet())) );
        List<Tweet> favoritesList = UserTweetList.favoritesFeed(userId, userId);
        for( Tweet tweet : favoritesList )
            tweet.setTweet( StringEscapeUtils.escapeJavaScript(UserTweetList.escapeHTML(tweet.getTweet())) );
        mv.addObject("newsFeed", tweetList);
        mv.addObject("favoritesFeed", favoritesList);
        mv.addObject("mentionFeed", mentionList);
        mv.addObject("followerList", Follow.getFollowerListLimited( userId ));
        mv.addObject("followedList", Follow.getFollowedListLimited( userId ));
        mv.addObject("followerCount", Follow.getFollowerList( userId ).size() );
        mv.addObject("followingCount", Follow.getFollowedList( userId ).size() );
        mv.addObject("allUserList", Follow.allUsersList( userId ));
        mv.addObject("currentUsername", session.getAttribute("username").toString());
        return mv;
    }

    @RequestMapping( value = "/tweet/markFavorite" , method = RequestMethod.POST ) @ResponseBody // Ajax call
    public static void markFavorite( @RequestParam String tweetId , HttpSession session ){
        UserTweetList.markFavorite( tweetId , session.getAttribute("userId").toString() );
    }

    @RequestMapping( value = "/tweet/deleteFavorite" , method = RequestMethod.POST ) @ResponseBody // Ajax call
    public static void deleteFavorite( @RequestParam String tweetId , HttpSession session ){
        UserTweetList.deleteFavorite( tweetId , session.getAttribute("userId").toString() );
    }

    @RequestMapping( value = "/tweet/replyToTweet" , method = RequestMethod.POST )
    @ResponseBody
    public static Tweet replyToTweet(String tweetContent,String replyTo,HttpSession session) {
        Tweet tweet = UserTweetList.replyToTweet( tweetContent , replyTo , session.getAttribute("userId").toString() );
        return tweet;
    }

    @RequestMapping( value = "/tweet/retweet" ) @ResponseBody // Ajax call
    public Tweet retweet( @RequestParam String tweetId , HttpSession session ){
        Tweet t = UserTweetList.addRetweet( tweetId , session.getAttribute("username").toString(), session.getAttribute("userId").toString() );
        return t;
    }
}

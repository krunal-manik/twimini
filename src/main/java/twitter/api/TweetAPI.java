package twitter.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter.models.Tweet;
import twitter.models.User;
import twitter.models.UserProfile;
import twitter.services.Follow;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/28/11
 * Time: 4:42 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class TweetAPI {

    @RequestMapping("/api/tweets/{username}")
    @ResponseBody
    public static Hashtable<String,Object> getUserTweets( @PathVariable String username ) {
        System.out.println( username );
        Hashtable<String,Object> userTweets = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            List<Tweet> tweetsList = UserTweetList.userTweetList(String.valueOf(user.getUserId()), null);
            userTweets.put( "tweets" , tweetsList );
            userTweets.put("success", "true");
            userTweets.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            userTweets.put("Error", "No such user exists");
            userTweets.put("status code", "401 User not found");
        } catch (Exception ex) {
            userTweets.put("Error", "Server error");
            userTweets.put("status code", "500 Internal server error");
        }

        return userTweets;
    }

    @RequestMapping("/api/newsfeed/{username}")
    @ResponseBody
    public static Hashtable<String,Object> getUsersNewsFeed( @PathVariable String username , String token ) {
        Hashtable<String,Object> userTweets = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            List<Tweet> newsFeed = UserTweetList.newsFeed(String.valueOf(user.getUserId()));
            userTweets.put( "tweets" , newsFeed );
            userTweets.put("success", "true");
            userTweets.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            userTweets.put("Error", "No such user exists");
            userTweets.put("status code", "401 User not found");
        } catch (Exception ex) {
            userTweets.put("Error", "Server error");
            userTweets.put("status code", "500 Internal server error");
        }

        return userTweets;
    }

    @RequestMapping("/api/publish-tweet/{username}")
    @ResponseBody
    public static Hashtable<String,Object> publistTweet( @PathVariable String username , String token , String tweetContent ) {
        Hashtable<String,Object> userTweet = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            Tweet tweet = UserTweetList.addTweet( tweetContent ,String.valueOf(user.getUserId()));
            userTweet.put("tweetInformation", tweet);
            userTweet.put("success", "true");
            userTweet.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            userTweet.put("Error", "No such user exists");
            userTweet.put("status code", "401 User not found");
        } catch (Exception ex) {
            userTweet.put("Error", "Server error");
            userTweet.put("status code", "500 Internal server error");
        }

        return userTweet;
    }
}

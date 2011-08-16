package twitter.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @RequestMapping("/api/{username}/tweets")
    @ResponseBody
    public static Hashtable<String, Object> getUserTweets(@PathVariable String username,String timestamp,String count,String password) {
        System.out.println(username);
        Hashtable<String, Object> userTweets = new Hashtable<String, Object>();
        try {
            List<Tweet> tweetsList = null;
            if( password != null ) {
                if( timestamp == null && count == null ) {
                    User user = UserAuthentication.authenticateUser(username,password);
                    if( user == null ) {
                        userTweets.put("error", "User Authentication failed");
                        userTweets.put("success", "false");
                        return userTweets;
                    }
                    tweetsList = UserTweetList.nTweetsOfUserfeedByTimestamp( String.valueOf(user.getUserId()) , String.valueOf(user.getUserId()) , null , "10" , true );
                }
                else if (timestamp != null && count != null ) {
                    User user = UserAuthentication.authenticateUser(username,password);
                    if( user == null ) {
                        userTweets.put("error", "User Authentication failed");
                        userTweets.put("success", "false");
                        return userTweets;
                    }
                    tweetsList = UserTweetList.nTweetsOfUserfeedByTimestamp( String.valueOf(user.getUserId()) , String.valueOf(user.getUserId()) , timestamp , count , false );
                }
                else {
                    userTweets.put("success","false");
                    userTweets.put("error","Bad request");
                    return userTweets;
                }
            }
            else {
                if( timestamp == null && count == null ) {
                    User user = UserAuthentication.getUserByUsername(username);
                    tweetsList = UserTweetList.nTweetsOfUserfeedByTimestamp( String.valueOf(user.getUserId()) , null , null , "10" , true );
                }
                else if( timestamp != null && count != null ) {
                    User user = UserAuthentication.getUserByUsername(username);
                    tweetsList = UserTweetList.nTweetsOfUserfeedByTimestamp( String.valueOf(user.getUserId()) , null , timestamp , count , false );
                }
                else {
                    userTweets.put("success","false");
                    userTweets.put("error","Bad request");
                    return userTweets;
                }
            }
            userTweets.put("tweets", tweetsList);
            userTweets.put("success", "true");
            userTweets.put("error", "none");
        } catch (NullPointerException ex) {
            userTweets.put("error", "User " + username + " does not exist");
            userTweets.put("success", "false");
        } catch (Exception ex) {
            userTweets.put("error", "Server error");
            userTweets.put("success", "false");
        }

        return userTweets;
    }

    @RequestMapping("/api/{username}/newsfeed")
    @ResponseBody
    public static Hashtable<String, Object> getUsersNewsFeed(@PathVariable String username,@RequestParam String password,String timestamp,String count) {
        Hashtable<String, Object> userTweets = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            List<Tweet> newsFeed = null;

            if( timestamp == null && count == null ) {
                newsFeed = UserTweetList.nTweetsOfNewsfeedByTimestamp(String.valueOf(user.getUserId())  , null , "10" , true );
            }
            else if( timestamp != null && count != null ) {
                newsFeed = UserTweetList.nTweetsOfNewsfeedByTimestamp(String.valueOf(user.getUserId()) , timestamp , count , false );
            }
            else {
                userTweets.put("success","false");
                userTweets.put("error","Bad request");
                return userTweets;
            }

            userTweets.put("tweets", newsFeed);
            userTweets.put("success", "true");
            userTweets.put("error", "none");
        } catch (NullPointerException ex) {
            userTweets.put("error", "User Authentication failed");
            userTweets.put("success", "false");
        } catch (Exception ex) {
            userTweets.put("error", "Server error");
            userTweets.put("success", "false");
        }

        return userTweets;
    }

    @RequestMapping("/api/{username}/publish-tweet")
    @ResponseBody
    public static Hashtable<String, Object> publistTweet(@PathVariable String username, @RequestParam String password,@RequestParam String tweetContent) {
        Hashtable<String, Object> userTweet = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            Tweet tweet = UserTweetList.addTweet(tweetContent, String.valueOf(user.getUserId()));
            userTweet.put("tweetInformation", tweet);
            userTweet.put("success", "true");
            userTweet.put("error", "none");
        } catch (NullPointerException ex) {
            userTweet.put("error", "User Authentication failed");
            userTweet.put("success", "false");
        } catch (Exception ex) {
            userTweet.put("error", "Server error");
            userTweet.put("success", "false");
        }

        return userTweet;
    }

    @RequestMapping("/api/{username}/favorite")
    @ResponseBody
    public static Hashtable<String, Object> markFavorite(@PathVariable String username, @RequestParam String password, @RequestParam String tweetId) {
        Hashtable<String, Object> favoriteStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            boolean success = UserTweetList.markFavorite(tweetId, String.valueOf(user.getUserId()));
            if (!success)
                throw new Exception();
            favoriteStatus.put("success", "true");
            favoriteStatus.put("error", "none");
        } catch (NullPointerException ex) {
            favoriteStatus.put("error", "User Authentication failed");
            favoriteStatus.put("success", "false");
        } catch (Exception ex) {
            favoriteStatus.put("error", "Server error");
            favoriteStatus.put("success", "false");
        }

        return favoriteStatus;
    }

    @RequestMapping("/api/{username}/unfavorite")
    @ResponseBody
    public static Hashtable<String, Object> markUnfavorite(@PathVariable String username, @RequestParam String password, @RequestParam String tweetId) {
        Hashtable<String, Object> unFavoriteStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            boolean success = UserTweetList.deleteFavorite(tweetId, String.valueOf(user.getUserId()));
            if (!success)
                throw new Exception();
            unFavoriteStatus.put("success", "true");
            unFavoriteStatus.put("error", "none");
        } catch (NullPointerException ex) {
            unFavoriteStatus.put("error", "No such user exists");
            unFavoriteStatus.put("success", "false");
        } catch (Exception ex) {
            unFavoriteStatus.put("error", "Server error");
            unFavoriteStatus.put("success", "false");
        }

        return unFavoriteStatus;
    }
}

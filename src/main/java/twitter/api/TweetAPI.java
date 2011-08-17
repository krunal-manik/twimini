package twitter.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
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
                    User user = UserAuthentication.authenticateUser(username,password);
                    if( user == null ) {
                        userTweets.put("error", "User Authentication failed");
                        userTweets.put("success", "false");
                        return userTweets;
                    }
                    tweetsList = UserTweetList.nTweetsOfUserfeedByTimestamp( String.valueOf(user.getUserId()) , String.valueOf(user.getUserId()) , timestamp , count == null ? "10" : count , true );
            }
            else {
                User user = UserAuthentication.getUserByUsername(username);
                tweetsList = UserTweetList.nTweetsOfUserfeedByTimestamp( String.valueOf(user.getUserId()) , String.valueOf(user.getUserId()) , timestamp , count == null ? "10" : count , false );
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
            List<Tweet> newsFeed = UserTweetList.nTweetsOfNewsfeedByTimestamp(String.valueOf(user.getUserId()),timestamp , count == null ? "10" : count , timestamp == null );
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

    @RequestMapping(value = "/api/{username}/publish-tweet", method = RequestMethod.POST )
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

    @RequestMapping("/api/{username}/favoriteTweet")
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

    @RequestMapping("/api/{username}/unfavoriteTweet")
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

    @RequestMapping("/api/{username}/favorites")
    @ResponseBody
    public static Hashtable<String,Object> getFavoriteTweets(@PathVariable String username,@RequestParam String currentUsername,@RequestParam String password,String timestamp,String count) {
        Hashtable<String,Object> favorites = new Hashtable<String, Object>();
        try {
            List<Tweet> tweetList;
            if( currentUsername != null && password != null ) {
                User user = UserAuthentication.authenticateUser(currentUsername,password);
                User urlMappedUser = UserAuthentication.getUserByUsername(username);
                if( user == null ) {
                    favorites.put("success","false");
                    favorites.put("error","User Authentication failed");
                    return favorites;
                }
                tweetList = UserTweetList.nTweetsOfFavoritesByTimestamp(String.valueOf(urlMappedUser.getUserId()), String.valueOf(user.getUserId()), timestamp, count == null ? "10" : count, timestamp == null);
            }
            else {
                User urlMappedUser = UserAuthentication.getUserByUsername(username);
                tweetList = UserTweetList.nTweetsOfFavoritesByTimestamp(String.valueOf(urlMappedUser.getUserId()),null,timestamp, count == null ? "10" : count , timestamp == null );
            }
            favorites.put("favorites",tweetList);
            favorites.put("success", "true");
            favorites.put("error", "none");
        } catch (NullPointerException ex) {
            favorites.put("error", "User " + username + " does not exist");
            favorites.put("success", "false");
        } catch (Exception ex) {
            favorites.put("error", "Server error");
            favorites.put("success", "false");
        }
        return favorites;
    }

    @RequestMapping("/api/{username}/mentions")
    @ResponseBody
    public static Hashtable<String, Object> getUsersMentions(@PathVariable String username,@RequestParam String password,String timestamp,String count) {
        Hashtable<String, Object> mentionTweets = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            List<Tweet> mentions = UserTweetList.nTweetsOfNewsfeedByTimestamp(String.valueOf(user.getUserId()),timestamp , count == null ? "10" : count , timestamp == null );
            mentionTweets.put("mentions", mentions);
            mentionTweets.put("success", "true");
            mentionTweets.put("error", "none");
        } catch (NullPointerException ex) {
            mentionTweets.put("error", "User Authentication failed");
            mentionTweets.put("success", "false");
        } catch (Exception ex) {
            mentionTweets.put("error", "Server error");
            mentionTweets.put("success", "false");
        }

        return mentionTweets;
    }

    @RequestMapping("/api/{username}/retweets")
    @ResponseBody
    public static Hashtable<String, Object> getUsersRetweets(@PathVariable String username,@RequestParam String password,String timestamp,String count) {
        Hashtable<String, Object> retweetedTweets = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            List<Tweet> retweets = UserTweetList.nTweetsOfNewsfeedByTimestamp(String.valueOf(user.getUserId()),timestamp , count == null ? "10" : count , timestamp == null );
            retweetedTweets.put("retweets", retweets);
            retweetedTweets.put("success", "true");
            retweetedTweets.put("error", "none");
        } catch (NullPointerException ex) {
            retweetedTweets.put("error", "User Authentication failed");
            retweetedTweets.put("success", "false");
        } catch (Exception ex) {
            retweetedTweets.put("error", "Server error");
            retweetedTweets.put("success", "false");
        }

        return retweetedTweets;
    }

    @RequestMapping(value ="/api/{username}/replyToTweet", method = RequestMethod.POST )
    @ResponseBody
    public static Hashtable<String, Object> replyToTweet(@PathVariable String username,@RequestParam String password,@RequestParam String tweet,@RequestParam String tweetId) {
        Hashtable<String,Object> userTweet = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            Tweet reply = UserTweetList.replyToTweet(tweet, tweetId, String.valueOf(user.getUserId()));
            userTweet.put("reply", reply);
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
}

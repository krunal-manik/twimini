package twitter.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import twitter.models.Tweet;
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

    public TweetController() {
    }

    @RequestMapping(value = "/first_newsfeed")
    @ResponseBody
    public List<Tweet> firstNewsfeed(HttpSession session, @RequestParam String user, @RequestParam String favoriter) {
        if( session.getAttribute("userId") == null ) return new ArrayList<Tweet>();
        List<Tweet> ret = UserTweetList.nTweetsOfNewsfeedByTimestamp(user, null, "10", true);
        return ret;
    }

    @RequestMapping(value = "/more_newsfeed")
    @ResponseBody
    public List<Tweet> moreNewsfeed(HttpSession session, @RequestParam String timestamp, @RequestParam String n, @RequestParam String favoriter) {
        if( session.getAttribute("userId") == null ) return new ArrayList<Tweet>();
        List<Tweet> ret = UserTweetList.nTweetsOfNewsfeedByTimestamp(session.getAttribute("userId").toString(), timestamp, n, false);
        return ret;
    }

    @RequestMapping(value = "/latest_newsfeed")
    @ResponseBody
    public List<Tweet> latestNewsfeed(HttpSession session, @RequestParam String timestamp, @RequestParam String favoriter) {
        if( session.getAttribute("userId") == null ) return new ArrayList<Tweet>();
        List<Tweet> ret = UserTweetList.nTweetsOfNewsfeedByTimestamp(session.getAttribute("userId").toString(), timestamp, null, true);
        return ret;
    }

    @RequestMapping(value = "/first_mentions")
    @ResponseBody
    public List<Tweet> firstMentions(HttpSession session, @RequestParam String user, @RequestParam String favoriter) {
        List<Tweet> ret = UserTweetList.nTweetsOfMentionsByTimestamp(user, favoriter, null, "10", true);
        return ret;
    }

    @RequestMapping(value = "/more_mentions")
    @ResponseBody
    public List<Tweet> moreMentions(@RequestParam String user, @RequestParam String favoriter, @RequestParam String timestamp, @RequestParam String n) {
        List<Tweet> ret = UserTweetList.nTweetsOfMentionsByTimestamp(user, favoriter, timestamp, n, false);
        return ret;
    }

    @RequestMapping(value = "/first_favorites")
    @ResponseBody
    public List<Tweet> firstFavorites(HttpSession session, @RequestParam String user, @RequestParam String favoriter) {
        List<Tweet> ret = UserTweetList.nTweetsOfFavoritesByTimestamp(user, favoriter, null, "10", true);
        return ret;
    }

    @RequestMapping(value = "/more_favorites")
    @ResponseBody
    public List<Tweet> moreFavorites(HttpSession session, @RequestParam String favoriter, @RequestParam String timestamp, @RequestParam String n) {
        List<Tweet> ret = UserTweetList.nTweetsOfFavoritesByTimestamp(session.getAttribute("userId").toString(), favoriter, timestamp, n, false);
        return ret;
    }

    @RequestMapping(value = "/first_userfeed")
    @ResponseBody
    public List<Tweet> firstUserfeed(HttpSession session, @RequestParam String user, @RequestParam String favoriter) {
        List<Tweet> ret = UserTweetList.nTweetsOfUserfeedByTimestamp(user, favoriter, null, "10", true);
        return ret;
    }

    @RequestMapping(value = "/more_userfeed")
    @ResponseBody
    public List<Tweet> moreUserfeed(@RequestParam String user, @RequestParam String favoriter, @RequestParam String timestamp, @RequestParam String n) {
        List<Tweet> ret = UserTweetList.nTweetsOfUserfeedByTimestamp(user, favoriter, timestamp, n, false);
        return ret;
    }


    @RequestMapping(value = "/latest_userfeed")
    @ResponseBody
    public List<Tweet> latestUserfeed(HttpSession session, @RequestParam String favoriter, @RequestParam String timestamp) {
        List<Tweet> ret = UserTweetList.nTweetsOfUserfeedByTimestamp(session.getAttribute("userId").toString(), favoriter, timestamp, null, true);
        return ret;
    }

    @RequestMapping(value = "/first_retweets")
    @ResponseBody
    public List<Tweet> firstRetweets(HttpSession session, @RequestParam String user, @RequestParam String favoriter) {
        List<Tweet> ret = UserTweetList.nTweetsOfRetweetsByTimestamp(user, favoriter, null, "10", true);
        return ret;
    }

    @RequestMapping(value = "/more_retweets")
    @ResponseBody
    public List<Tweet> moreRetweets(@RequestParam String user, @RequestParam String favoriter, @RequestParam String timestamp, @RequestParam String n) {
        List<Tweet> ret = UserTweetList.nTweetsOfRetweetsByTimestamp(user, favoriter, timestamp, n, false);
        return ret;
    }

    @RequestMapping(value = "/tweet/addTweet")
    @ResponseBody
    public Tweet addTweet(@RequestParam String tweetContent, HttpSession session) {
        Tweet t = UserTweetList.addTweet(tweetContent, session.getAttribute("userId").toString());
        return t;
    }

    @RequestMapping(value = "/tweet/markFavorite", method = RequestMethod.POST)
    @ResponseBody
    public static void markFavorite(@RequestParam String tweetId, HttpSession session) {
        UserTweetList.markFavorite(tweetId, session.getAttribute("userId").toString());
    }

    @RequestMapping(value = "/tweet/deleteFavorite", method = RequestMethod.POST)
    @ResponseBody
    public static void deleteFavorite(@RequestParam String tweetId, HttpSession session) {
        UserTweetList.deleteFavorite(tweetId, session.getAttribute("userId").toString());
    }

    @RequestMapping(value = "/tweet/replyToTweet", method = RequestMethod.POST)
    @ResponseBody
    public static Tweet replyToTweet(String tweetContent, String replyTo, HttpSession session) {
        Tweet tweet = UserTweetList.replyToTweet(tweetContent, replyTo, session.getAttribute("userId").toString());
        return tweet;
    }

    @RequestMapping(value = "/tweet/retweet")
    @ResponseBody
    public Tweet retweet(@RequestParam String tweetId, HttpSession session) {
        Tweet t = UserTweetList.addRetweet(tweetId, session.getAttribute("username").toString(), session.getAttribute("userId").toString());
        return t;
    }
}

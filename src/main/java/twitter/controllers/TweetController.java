package twitter.controllers;

import org.apache.commons.lang.StringEscapeUtils;
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

    @RequestMapping( value = "/tweet/addTweet" ) @ResponseBody // Ajax call
    public Tweet addTweet( @RequestParam String tweetContent , HttpSession session ){
        Set<User> tags = searchTags(tweetContent);
        Tweet t = UserTweetList.addTweet( escapeHTML(tweetContent) , session.getAttribute("userId").toString() );
        for (Object u : tags.toArray()) {
            User user = (User) u;
            Mention.mentionUserInTweet(user.getUserId(), t.getTweetId());
        }
        return t;
    }

    private String escapeHTML(String tweetContent) {
        String[] parts = tweetContent.split("@");
        tweetContent = StringEscapeUtils.escapeHtml(parts[0]);
        for (int i = 1; i < parts.length; i++) {
            String toLink;
            String toEscape;
            if (parts[i].indexOf(" ") == -1) {
                toLink = parts[i];
                toEscape = "";
            } else {
                toLink = parts[i].substring(0, parts[i].indexOf(" "));
                toEscape = parts[i].substring(parts[i].indexOf(" "));
            }
            tweetContent += "<a href=\"/" + toLink + "\">" + toLink + "</a>" + StringEscapeUtils.escapeHtml(toEscape);
        }
        return tweetContent;
    }

    private Set<User> searchTags(String tweetContent) {
        Set<User> setOfTags = new HashSet<User>();
        String[] parts = tweetContent.split("@");
        System.out.println("TAGS -> ");
        for (int i = 1; i < parts.length; i++) {
            String toTag = parts[i].split(" ")[0];
            User taggedUser = UserAuthentication.getUserByUsername(toTag);
            if (taggedUser != null) {
                setOfTags.add(taggedUser);
            }
        }
        return setOfTags;
    }


    @RequestMapping("/tweet")
    public static ModelAndView tweetsList(HttpSession session) {
        ModelAndView mv = new ModelAndView( "/tweet" );
        String userId = session.getAttribute( "userId" ).toString();
        mv.addObject("newsFeed", UserTweetList.newsFeed( userId ));
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
}

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
    public final SimpleJdbcTemplate db;

    @Autowired
    public TweetController(SimpleJdbcTemplate db){
        this.db = db;
    }

    @RequestMapping(value = "/tweet/addTweet" , method = RequestMethod.POST ) @ResponseBody
    public Map<String,Object> addTweet(@RequestParam String tweetContent,HttpSession session){

        System.out.println( "here : " + tweetContent );
        db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )" ,session.getAttribute("userId").toString() , tweetContent  );
        System.out.println( "inserted into db" );
        int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");
        System.out.println( "Tweet id : " + tweetId );
        List< Map<String,Object> > tweetObj = db.queryForList("SELECT tweet_id as ID ,tweeted_by as USER_ID,tweet as TWEET,timestamp as TIMESTAMP FROM tweets WHERE tweet_id = ?", tweetId);
        System.out.println( tweetObj.get(0) );
        return tweetObj.get(0);
    }

    @RequestMapping("/tweet")
    public ModelAndView newsFeedGet(HttpSession session){
        System.out.println( "In news feed" );
        List< Map<String,Object> > tweetsForNewsFeeds = db.queryForList("SELECT tweet_id as ID, tweeted_by as USER_ID, " +
                "tweet as TWEET, timestamp as TIMESTAMP FROM tweets" +
                " where tweeted_by = ? ORDER BY TIMESTAMP", session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView();
        mv.addObject( "tweetsList" , tweetsForNewsFeeds );
        System.out.println( tweetsForNewsFeeds );
        System.out.println( "Success" );
        return mv;
    }
}

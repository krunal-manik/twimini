package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.Tweet;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/4/11
 * Time: 6:18 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class UserTweetList {
    public static SimpleJdbcTemplate db;

    @Autowired
    public UserTweetList( SimpleJdbcTemplate db ) {
        this.db = db;
    }

    public static Tweet addTweet( String tweet , String userId ){
        Tweet ret = new Tweet();
        try{
            db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )" ,userId , tweet  );
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");
            ret = db.queryForObject( "SELECT tweet_id ,tweeted_by ,tweet ,timestamp FROM tweets WHERE tweet_id = ?",
                    Tweet.rowMapper , tweetId );
        }
        catch( EmptyResultDataAccessException ex ){
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> userTweetList( String userId ){
        List<Tweet> ret = null;
        try{
            ret = db.query("SELECT tweet_id ,tweeted_by ,tweet ,timestamp FROM tweets WHERE tweeted_by = ? ORDER BY timestamp",
                Tweet.rowMapper, userId );
        }
        catch( Exception ex ){
            ex.printStackTrace();
        }
        return ret;
    }

}
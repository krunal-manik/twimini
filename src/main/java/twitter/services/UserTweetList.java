package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.Tweet;

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
        db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )" ,userId , tweet  );
        int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");
        return db.queryForObject( "SELECT tweet_id ,tweeted_by ,tweet ,timestamp FROM tweets WHERE tweet_id = ?",
                Tweet.rowMapper , tweetId );
    }

    public static List<Tweet> userTweetList( String userId ){
        return db.query("SELECT tweet_id ,tweeted_by ,tweet ,timestamp FROM tweets WHERE tweeted_by = ? ORDER BY timestamp",
                Tweet.rowMapper, userId );
    }

}
package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.Tweet;
import twitter.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
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

    public static final RowMapper<Tweet> newsFeedMapper = new RowMapper<Tweet>() {
        @Override public Tweet mapRow( ResultSet rs , int i ) throws SQLException {
            Tweet ret = new Tweet();
            ret.setTweetId( rs.getInt( "tweet_id" ) );
            ret.setName( rs.getString( "name" ) );
            ret.setTweet( rs.getString( "tweet" ) );
            ret.setTimestamp( rs.getString( "timestamp" ) );
            ret.setUserId( rs.getString( "user_id" ) );
            ret.setTweetedBy( rs.getInt( "tweeted_by" ) );
            ret.setUsername( rs.getString( "username" ) );
            return ret;
        }
    };

    @Autowired
    public UserTweetList( SimpleJdbcTemplate db ) {
        this.db = db;
    }

    public static Tweet addTweet( String tweet , String userId ){
        Tweet ret = new Tweet();
        try{
            db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )" ,userId , tweet  );
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");

            ret = db.queryForObject("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? AND T.tweet_id = ? " ,
                    UserTweetList.newsFeedMapper , userId , tweetId );

            /*ret = db.queryForObject( "SELECT tweet_id ,tweeted_by ,tweet ,timestamp FROM tweets WHERE tweet_id = ?",
                    Tweet.rowMapper , tweetId );*/
        }
        catch( EmptyResultDataAccessException ex ){
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> userTweetList( String userId ){
        List<Tweet> ret = null;
        try{
            ret = db.query("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? ORDER BY timestamp DESC" ,
                    UserTweetList.newsFeedMapper , userId );
        }
        catch( Exception ex ){
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> newsFeed( String userId ){
        List<Tweet> ret = null;
        try{
            ret = db.query("SELECT T.tweet_id as tweet_id ,T.tweeted_by as tweeted_by, T.tweet as tweet ,T.timestamp as timestamp, " +
                    "U.name as name ,U.username as username, U.user_id as user_id " +
                    "FROM tweets as T INNER JOIN user as U ON T.tweeted_by = U.user_id " +
                    "WHERE T.tweeted_by in (" +
                    "select followed from follower_followed where follower = ?" +
                    "UNION " +
                    "select user_id from user where user_id = ? ) " +
                    "ORDER BY timestamp DESC",
                UserTweetList.newsFeedMapper, userId , userId );
        }
        catch( Exception ex ){
            ex.printStackTrace();
        }
        return ret;
    }
}
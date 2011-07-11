package twitter.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;

import org.springframework.jdbc.core.RowMapper;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 6/30/11
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class Tweet {

    int tweetId , tweetedBy;
    String tweet;
    int inReplyTo;
    String timestamp;
    int retweetedFrom;

    String userId;
    String username;
    String name;

    public static final RowMapper<Tweet> rowMapper = new RowMapper<Tweet>() {
        @Override public Tweet mapRow( ResultSet rs , int i ) throws SQLException {
            return new Tweet(rs);
        }
    };

    public Tweet(){}

    public Tweet( ResultSet rs ) throws SQLException {
        tweetId = rs.getInt("tweet_id");
        tweet = rs.getString("tweet");
        tweetedBy = rs.getInt("tweeted_by");
        timestamp = rs.getString("timestamp");
    }

    public int getTweetId(){
        return tweetId;
    }

    public String getTweet(){
        return tweet;
    }

    public int getTweetedBy(){
        return tweetedBy;
    }

    public String getTimestamp(){
        return timestamp;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public void setTweetId( int tweetId ) {
        this.tweetId = tweetId;
    }

    public void setTweet( String tweet ) {
        this.tweet = tweet;
    }

    public void setTweetedBy( int tweetedBy ) {
        this.tweetedBy = tweetedBy;
    }

    public void setUserId( String userId ) {
        this.userId = userId;
    }

    public void setTimestamp( String timestamp ) {
        this.timestamp = timestamp;
    }

    public void setUsername( String username ) {
        this.username = username;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public String toString(){
        Hashtable<String,String> ret = new Hashtable<String, String>();
        ret.put( "tweetId" , "" + tweetId );
        ret.put( "tweet" , tweet );
        ret.put( "tweetedBy" , "" + tweetedBy );
        ret.put( "timestamp" , timestamp );
        return ret.toString();
    }
}

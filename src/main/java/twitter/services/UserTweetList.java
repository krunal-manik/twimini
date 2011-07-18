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
import java.util.*;

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

    public static Tweet addTweet( String tweet , String userId ) {
        Tweet ret = new Tweet();
        try{
            db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )" ,userId , tweet  );
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");

            ret = db.queryForObject("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? AND T.tweet_id = ? " ,
                    UserTweetList.newsFeedMapper , userId , tweetId );
        }
        catch( EmptyResultDataAccessException ex ){
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> userTweetList( String userId, String favoriter ){
        List<Tweet> ret = null;
        try{
            ret = db.query("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? ORDER BY timestamp DESC" ,
                    UserTweetList.newsFeedMapper , userId );
            if (favoriter != null) {
                int favoritesList[] = getFavoriteTweetsOfUser( favoriter );
                for(int i=0;i<ret.size();i++) {
                   ret.get(i).setFavorite( binarySearch(favoritesList, ret.get(i).getTweetId()) );
                }
            }
        }
        catch( Exception ex ) {
            System.out.println( "Bug in userTweetList :((" );
            ex.printStackTrace();
        }
        return ret;
    }

    public static int[] getFavoriteTweetsOfUser( String userId ) {

        int favoritesList[] = new int[0];
        try{
            List< Map<String,Object> > favorites = db.queryForList("SELECT tweet_id from favorite where user_id = ? ORDER BY tweet_id", userId);
            favoritesList = new int[ favorites.size() ];
            for(int i=0;i<favorites.size();i++) favoritesList[i] = ( Integer.valueOf( favorites.get(i).get("tweet_id").toString() ) ).intValue();
        }
        catch ( Exception ex ) {
            System.out.println( "Bug in favoriteTweetsOfUser :((" );
            ex.printStackTrace();
        }
        return favoritesList;
    }

    public static boolean binarySearch( int a[] , int key ) {
        int low = 0 , high = a.length - 1;
        while( low <= high ) {
            int mid = ( low + high ) >> 1;
            if( a[mid] == key )return true;
            if( a[mid] < key ) low = mid+1;
            else high = mid-1;
        }
        return false;
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

            int favoritesList[] = getFavoriteTweetsOfUser( userId );
            for(int i=0;i<ret.size();i++) {
               ret.get(i).setFavorite( binarySearch(favoritesList, ret.get(i).getTweetId()) );
            }
        }
        catch( Exception ex ) {
            System.out.println( "Bug in newsFeed :((" );
            ex.printStackTrace();
        }
        return ret;
    }

    public static void markFavorite( String tweetId , String userId ) {
        try{
            db.update("INSERT INTO favorite ( user_id , tweet_id ) VALUES ( ? , ? )"
                    , userId, tweetId);
        }
        catch( Exception ex ) {
            System.out.println( "Bug in markFavorite :((" );
            ex.printStackTrace();
        }
    }

    public static void deleteFavorite( String tweetId , String userId ) {
        try{
            db.update("DELETE from favorite where user_id = ? AND tweet_id = ?"
                    , userId, tweetId );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in deleteFavorite :((" );
            ex.printStackTrace();
        }
    }
}
package twitter.services;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.controllers.TweetController;
import twitter.models.Tweet;
import twitter.models.User;

import javax.crypto.interfaces.PBEKey;
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

    public static final RowMapper<Tweet> newsFeedMapperWithRetweet = new RowMapper<Tweet>() {
        @Override public Tweet mapRow( ResultSet rs , int i ) throws SQLException {
            Tweet ret = new Tweet();
            ret.setTweetId(rs.getInt("tweet_id"));
            ret.setName(rs.getString("name"));
            ret.setTweet(rs.getString("tweet"));
            ret.setTimestamp(rs.getString("timestamp"));
            ret.setUserId(rs.getString("user_id"));
            ret.setTweetedBy(rs.getInt("tweeted_by"));
            ret.setUsername(rs.getString("username"));
            ret.setInReplyTo(rs.getInt("in_reply_to"));
            ret.setRetweetedBy(rs.getString("retweeted_by"));
            return ret;
        }
    };

    public static final RowMapper<Tweet> newsFeedMapper = new RowMapper<Tweet>() {
        @Override public Tweet mapRow( ResultSet rs , int i ) throws SQLException {
            Tweet ret = new Tweet();
            ret.setTweetId(rs.getInt("tweet_id"));
            ret.setName(rs.getString("name"));
            ret.setTweet(rs.getString("tweet"));
            ret.setTimestamp(rs.getString("timestamp"));
            ret.setUserId(rs.getString("user_id"));
            ret.setTweetedBy(rs.getInt("tweeted_by"));
            ret.setUsername(rs.getString("username"));
            ret.setInReplyTo( rs.getInt("in_reply_to"));
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
            db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )" ,userId , tweet);
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");

            ret = db.queryForObject("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id , T.in_reply_to as in_reply_to " +
                    "FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? AND T.tweet_id = ? " ,
                    UserTweetList.newsFeedMapper , userId , tweetId );
            Set<Integer> tags = searchTags(tweet);
            for (Object u : tags.toArray()) {
                Integer user = (Integer) u;
                Mention.mentionUserInTweet(user, ret.getTweetId());
            }
        }
        catch( EmptyResultDataAccessException ex ){
            ex.printStackTrace();
        }
        return ret;
    }

    public static Set<Integer> searchTags(String tweetContent) {
        Set<Integer> setOfTags = new HashSet<Integer>();
        String[] parts = tweetContent.split("@");
        System.out.println("TAGS -> ");
        for (int i = 1; i < parts.length; i++) {
            String toTag = parts[i].split(" ")[0];
            Integer taggedUser = UserAuthentication.getUserByUsername(toTag).getUserId();
            if (taggedUser != null && !setOfTags.contains(taggedUser)) {
                setOfTags.add(taggedUser);
            }
            for (Object o : setOfTags.toArray()) {
                System.out.println(((Integer) o));
            }
        }
        return setOfTags;
    }

    public static String escapeHTML(String tweetContent) {
        return StringEscapeUtils.escapeHtml(tweetContent);
    }

    public static String addTags(String tweetContent) {
        String[] parts = tweetContent.split("@");
        tweetContent = (parts[0]);
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
            tweetContent += "<a href=\"/" + toLink + "\">" + toLink + "</a>" + (toEscape);
        }
        return tweetContent;
    }

    public static int getUserTweetsCount( String userId ) {
        int userTweetsCount = 0;
        try{
            userTweetsCount = db.queryForInt("SELECT COUNT(T.tweet_id) FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? ORDER BY timestamp DESC" , userId );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in userTweetsCount :((" );
            ex.printStackTrace();
        }
        return userTweetsCount;
    }

    public static List<Tweet> userTweetList( String userId, String favoriter ){
        List<Tweet> ret = null;
        try{
            ret = db.query("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id, T.in_reply_to as in_reply_to " +
                    "FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? " ,
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

        if( userId == null )return new int[0];
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


    public static boolean markFavorite( String tweetId , String userId ) {
        try{
            db.update("INSERT INTO favorite ( user_id , tweet_id ) VALUES ( ? , ? )"
                    , userId, tweetId);
        }
        catch( Exception ex ) {
            System.out.println( "Bug in markFavorite :((" );
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteFavorite( String tweetId , String userId ) {
        try{
            db.update("DELETE from favorite where user_id = ? AND tweet_id = ?"
                    , userId, tweetId );
        }
        catch( Exception ex ) {
            System.out.println( "Bug in deleteFavorite :((" );
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static Tweet replyToTweet( String tweetContent, String inReplyToTweetId , String userId ) {
        Tweet ret = new Tweet();
        try{
            db.update( "INSERT into Tweets(tweeted_by,tweet,timestamp,in_reply_to) VALUES ( ? , ? , NOW() , ? )" ,userId , tweetContent , inReplyToTweetId );
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets");

            ret = db.queryForObject("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id , T.in_reply_to as in_reply_to " +
                    "FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? AND T.tweet_id = ? " ,
                    UserTweetList.newsFeedMapper , userId , tweetId );
        }
        catch( EmptyResultDataAccessException ex ){
            ex.printStackTrace();
        }
        return ret;
    }

    public static Tweet addRetweet(String tweetId, String username, String userId) {
        Tweet ret = new Tweet();
        try {
            ret = db.queryForObject("SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet, " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id, T.in_reply_to as in_reply_to  " +
                    "FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id " +
                    "WHERE T.tweet_id = ? " +
                    "AND T.tweeted_by != ? " +
                    "AND T.tweet_id NOT IN (select tweet_id from retweets where user_id = ?)" ,
                    UserTweetList.newsFeedMapper, tweetId, userId, userId);
            ret.setRetweetedBy(username);
            db.update( "INSERT into retweets(username, user_id, tweet_id, timestamp, original_user_id) VALUES ( ?, ? , ? , NOW() , ? )" , username, userId , tweetId , ret.getUserId());
        }
        catch( EmptyResultDataAccessException ex ) {
            ex.printStackTrace();
        }
        return ret;
    }

public static List<Tweet> newsFeed( String userId ){
        List<Tweet> ret = null;
        try{
            ret = db.query("( SELECT T.tweet_id as tweet_id ,T.tweeted_by as tweeted_by, T.tweet as tweet ,T.timestamp as timestamp, " +
                    "U.name as name ,U.username as username, U.user_id as user_id, T.in_reply_to as in_reply_to, " +
                    "'0' as retweeted_by " +
                    "FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id " +
                    "WHERE " +
                    "(T.tweeted_by in (select followed from follower_followed where follower = ?) AND T.in_reply_to IS NULL) " +
                    "OR T.tweeted_by = ? ) " +
                    "UNION " +
                    "( SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet," +
                    "R.max_timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id, T.in_reply_to as in_reply_to , " +
                    "R.username as retweeted_by " +
                    "FROM tweets as T INNER JOIN user as U ON T.tweeted_by = U.user_id INNER JOIN " +
                    "(select username, user_id, tweet_id, MIN(timestamp) as max_timestamp from retweets " +
                    "where user_id in (select followed from follower_followed where follower = ?) GROUP BY tweet_id) as R " +
                    "ON R.tweet_id = T.tweet_id ) " +
                    "ORDER BY timestamp DESC "
                    , UserTweetList.newsFeedMapperWithRetweet, userId , userId, userId );

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


    public static List<Tweet> mentionFeed(String userId) {
        System.out.println(userId);
        List<Tweet> ret = null;
        try{
            ret = db.query("SELECT T.tweet_id as tweet_id ,T.tweeted_by as tweeted_by, T.tweet as tweet ,T.timestamp as timestamp, " +
                    "U.name as name ,U.username as username, U.user_id as user_id, T.in_reply_to as in_reply_to," +
                    "'0' as retweeted_by " +
                    "from tweets as T inner join user as U " +
                    "ON T.tweeted_by = U.user_id " +
                    "WHERE " +
                    "T.tweet_id in (SELECT tweet_id from mentions where user_id = ?)"
                    , UserTweetList.newsFeedMapperWithRetweet, userId );

            System.out.println("mention_count " +ret.size());

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
}
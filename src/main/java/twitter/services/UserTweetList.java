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
        @Override
        public Tweet mapRow(ResultSet rs, int i) throws SQLException {
            Tweet ret = new Tweet();
            ret.setTweetId(rs.getInt("tweet_id"));
            ret.setName(rs.getString("name"));
            ret.setTweet(rs.getString("tweet"));
            ret.setTimestamp(rs.getString("timestamp"));
            ret.setUserId(rs.getString("user_id"));
            ret.setTweetedBy(rs.getInt("tweeted_by"));
            ret.setUsername(rs.getString("username"));
            ret.setInReplyToUserId(rs.getInt("in_reply_to_user_id"));
            ret.setInReplyToTweetId(rs.getInt("in_reply_to_tweet_id"));
            ret.setRetweetedBy(rs.getString("retweeted_by"));
            return ret;
        }
    };

    public static final RowMapper<Tweet> newsFeedMapper = new RowMapper<Tweet>() {
        @Override
        public Tweet mapRow(ResultSet rs, int i) throws SQLException {
            Tweet ret = new Tweet();
            ret.setTweetId(rs.getInt("tweet_id"));
            ret.setName(rs.getString("name"));
            ret.setTweet(rs.getString("tweet"));
            ret.setTimestamp(rs.getString("timestamp"));
            ret.setUserId(rs.getString("user_id"));
            ret.setTweetedBy(rs.getInt("tweeted_by"));
            ret.setUsername(rs.getString("username"));
            ret.setInReplyToUserId(rs.getInt("in_reply_to_user_id"));
            ret.setInReplyToTweetId(rs.getInt("in_reply_to_tweet_id"));
            return ret;
        }
    };

    @Autowired
    public UserTweetList(SimpleJdbcTemplate simpleJdbcTemplate) {
        db = simpleJdbcTemplate;
    }

    public static Tweet addTweet(String tweet, String userId) {
        Tweet ret = new Tweet();
        try {
            db.update("INSERT into Tweets(tweeted_by,tweet,timestamp) VALUES ( ? , ? , NOW() )", userId, tweet);
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets where tweeted_by = ?", userId);

            ret = db.queryForObject(
                    "SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet,                                                   " +
                            "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id ,                              " +
                            "T.in_reply_to_user_id as in_reply_to_user_id, T.in_reply_to_tweet_id as in_reply_to_tweet_id                          " +
                            "FROM tweets as T INNER JOIN user as U                                                                                 " +
                            "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? AND T.tweet_id = ?                                                 ",
                    UserTweetList.newsFeedMapper, userId, tweetId);
            Set<Integer> tags = searchTags(tweet);
            for (Object u : tags.toArray()) {
                Integer user = (Integer) u;
                UserTweetList.mentionUserInTweet(user, ret.getTweetId());
            }
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
        }
        return ret;
    }
    public static boolean mentionUserInTweet(int userId, int tweetId) {
        boolean b = false;
        try {
            System.out.println(userId + ":" + tweetId);
            db.update("insert into mentions (tweet_id, user_id) values (?, ?)", tweetId, userId);
        } catch (Exception ex) {
            System.out.println("Error in updating mentions !!!");
            ex.printStackTrace();
            return false;
        }
        return true;
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

    public static int getUserTweetsCount(String userId) {
        int userTweetsCount = 0;
        try {
            userTweetsCount = db.queryForInt("SELECT COUNT(T.tweet_id) FROM tweets as T INNER JOIN user as U " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? ORDER BY timestamp DESC", userId);
        } catch (Exception ex) {
            System.out.println("Bug in userTweetsCount :((");
            ex.printStackTrace();
        }
        return userTweetsCount;
    }

    public static List<Tweet> nTweetsOfUserfeedByTimestamp(String userId, String favoriter, String timestamp, String n, boolean after) {
        List<Tweet> ret = null;
        String tsLimiter = "";
        String whereTsLimiter = "";
        String andTsLimiter = "";
        String numLimiter = "";
        if (timestamp != null) {
            if (after) {
                tsLimiter = " timestamp > '" + timestamp + "' ";
            } else {
                tsLimiter = " timestamp < '" + timestamp + "' ";
            }
            whereTsLimiter = " WHERE " + tsLimiter;
            andTsLimiter = " AND " + tsLimiter;
        }
        if (n != null) {
            numLimiter = " LIMIT 0, " + n + " ";
        }
        try {
            String query =
                    "SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet,                          " +
                            "'0' as retweeted_by, T.timestamp as timestamp, U.name as name ,                              " +
                            "U.username as username, U.user_id as user_id,                                                " +
                            "T.in_reply_to_user_id as in_reply_to_user_id, T.in_reply_to_tweet_id as in_reply_to_tweet_id " +
                            "FROM tweets as T INNER JOIN user as U                                                        " +
                            "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ?                                           " +
                            andTsLimiter + " ORDER BY timestamp DESC " + numLimiter;
            ret = db.query(query, UserTweetList.newsFeedMapperWithRetweet, userId);
            if (favoriter != null) {
                int favoritesList[] = getFavoriteTweetsOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setFavorite(binarySearch(favoritesList, ret.get(i).getTweetId()));
                }
                int cantRetweetList[] = getCantRetweetOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setCanRetweet(!binarySearch(cantRetweetList, ret.get(i).getTweetId()));
                }
            }
        } catch (Exception ex) {
            System.out.println("Bug in newsFeed :((");
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> nTweetsOfRetweetsByTimestamp(String userId, String favoriter, String timestamp, String n, boolean after) {
        List<Tweet> ret = null;
        String tsLimiter = "";
        String whereTsLimiter = "";
        String andTsLimiter = "";
        String numLimiter = "";
        if (timestamp != null) {
            if (after) {
                tsLimiter = " timestamp > '" + timestamp + "' ";
            } else {
                tsLimiter = " timestamp < '" + timestamp + "' ";
            }
            whereTsLimiter = " WHERE " + tsLimiter;
            andTsLimiter = " AND " + tsLimiter;
        }
        if (n != null) {
            numLimiter = " LIMIT 0, " + n + " ";
        }
        try {
            String query =
                    "SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet,                          " +
                            "R.username as retweeted_by, R.timestamp as timestamp, U.name as name ,                              " +
                            "U.username as username, U.user_id as user_id,                                                " +
                            "T.in_reply_to_user_id as in_reply_to_user_id, T.in_reply_to_tweet_id as in_reply_to_tweet_id " +
                            "FROM tweets as T INNER JOIN retweets as R                                                    " +
                            "on R.tweet_id = T.tweet_id                                                                   " +
                            "INNER JOIN user as U ON T.tweeted_by = U.user_id                                             " +
                            "WHERE R.user_id = ?                                                                          " +
                            andTsLimiter + " ORDER BY timestamp DESC " + numLimiter;
            ret = db.query(query, UserTweetList.newsFeedMapperWithRetweet, userId);
            if (favoriter != null) {
                int favoritesList[] = getFavoriteTweetsOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setFavorite(binarySearch(favoritesList, ret.get(i).getTweetId()));
                }
            }
        } catch (Exception ex) {
            System.out.println("Bug in newsFeed :((");
            ex.printStackTrace();
        }
        return ret;
    }

    private static int[] getFavoriteTweetsOfUser(String userId) {

        if (userId == null) return new int[0];
        int favoritesList[] = new int[0];
        try {
            List<Map<String, Object>> favorites = db.queryForList("SELECT tweet_id from favorite where user_id = ? ORDER BY tweet_id", userId);
            favoritesList = new int[favorites.size()];
            for (int i = 0; i < favorites.size(); i++)
                favoritesList[i] = (Integer.valueOf(favorites.get(i).get("tweet_id").toString())).intValue();
        } catch (Exception ex) {
            System.out.println("Bug in favoriteTweetsOfUser :((");
            ex.printStackTrace();
        }
        return favoritesList;
    }

    private static int[] getCantRetweetOfUser(String userId) {

        if (userId == null) return new int[0];
        int favoritesList[] = new int[0];
        try {
            List<Map<String, Object>> favorites = db.queryForList("SELECT tweet_id from ((SELECT tweet_id from tweets where tweeted_by = ?) UNION (SELECT tweet_id from retweets WHERE user_id = ?)) as A ORDER BY tweet_id", userId, userId);
            System.out.println("------------ > " + userId);
            System.out.println("++++++++" + favorites.size());
            favoritesList = new int[favorites.size()];
            for (int i = 0; i < favorites.size(); i++) {
                favoritesList[i] = (Integer.valueOf(favorites.get(i).get("tweet_id").toString())).intValue();
            }
        } catch (Exception ex) {
            System.out.println("Bug in favoriteTweetsOfUser :((");
            ex.printStackTrace();
        }
        return favoritesList;
    }

    private static boolean binarySearch(int a[], int key) {
        int low = 0, high = a.length - 1;
        while (low <= high) {
            int mid = (low + high) >> 1;
            if (a[mid] == key) return true;
            if (a[mid] < key) low = mid + 1;
            else high = mid - 1;
        }
        return false;
    }

    public static boolean markFavorite(String tweetId, String userId) {
        try {
            db.update("INSERT INTO favorite ( user_id , tweet_id ) VALUES ( ? , ? )"
                    , userId, tweetId);
        } catch (Exception ex) {
            System.out.println("Bug in markFavorite :((");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean deleteFavorite(String tweetId, String userId) {
        try {
            db.update("DELETE from favorite where user_id = ? AND tweet_id = ?"
                    , userId, tweetId);
        } catch (Exception ex) {
            System.out.println("Bug in deleteFavorite :((");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static Tweet replyToTweet(String tweetContent, String inReplyToTweetId, String userId) {
        Tweet ret = new Tweet();
        try {
            int inReplyToUserId = db.queryForInt("SELECT tweeted_by WHERE tweet_id = ?" , inReplyToTweetId );
            db.update("INSERT into Tweets(tweeted_by,tweet,timestamp,in_reply_to_tweet_id,in_reply_to_user_id) VALUES ( ? , ? , NOW() , ? )",
                    userId, tweetContent, inReplyToTweetId, inReplyToUserId);
            int tweetId = db.queryForInt("SELECT max(tweet_id) FROM tweets WHERE tweeted_by = ?" , userId );
            ret = db.queryForObject(
                    "SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet,                 " +
                    "T.timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id ,    " +
                    "T.in_reply_to_user_id as in_reply_to, T.in_reply_to_tweet_id as in_reply_to_tweet_id        " +
                    "FROM tweets as T INNER JOIN user as U                                                       " +
                    "ON T.tweeted_by = U.user_id WHERE T.tweeted_by = ? AND T.tweet_id = ?                       ",
                    UserTweetList.newsFeedMapper, userId, tweetId);
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public static Tweet addRetweet(String tweetId, String username, String userId) {
        Tweet ret = new Tweet();
        try {
            ret = db.queryForObject(
                    "SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet,                           " +
                            "T.timestamp as timestamp, U.name as name ,                                                    " +
                            "U.username as username, U.user_id as user_id,                                                 " +
                            "T.in_reply_to_user_id as in_reply_to_user_id, T.in_reply_to_tweet_id as in_reply_to_tweet_id  " +
                            "FROM tweets as T INNER JOIN user as U                                                         " +
                            "ON T.tweeted_by = U.user_id                                                                   " +
                            "WHERE T.tweet_id = ?                                                                          " +
                            "AND T.tweeted_by != ?                                                                         " +
                            "AND T.tweet_id NOT IN (select tweet_id from retweets where user_id = ?)                       ",
                    UserTweetList.newsFeedMapper, tweetId, userId, userId);
            ret.setRetweetedBy(username);
            db.update("INSERT into retweets(username, user_id, tweet_id, timestamp, original_user_id) VALUES ( ?, ? , ? , NOW() , ? )", username, userId, tweetId, ret.getUserId());
        } catch (EmptyResultDataAccessException ex) {
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> nTweetsOfNewsfeedByTimestamp(String userId, String timestamp, String n, boolean after) {
        List<Tweet> ret = null;
        String tsLimiter = "";
        String whereTsLimiter = "";
        String andTsLimiter = "";
        String numLimiter = "";
        if (timestamp != null) {
            if (after) {
                tsLimiter = " timestamp > '" + timestamp + "' ";
            } else {
                tsLimiter = " timestamp < '" + timestamp + "' ";
            }
            whereTsLimiter = " WHERE " + tsLimiter;
            andTsLimiter = " AND " + tsLimiter;
        }
        if (n != null) {
            numLimiter = " LIMIT 0, " + n + " ";
        }
        try {
            String query =
                    "SELECT tweet_id ,tweeted_by, tweet , MIN(timestamp) as timestamp, name , username, user_id,                                                        " +
                            "in_reply_to_user_id,in_reply_to_tweet_id , retweeted_by from                                                                             " +
                            "   (                                                                                                                                       " +
                            "       ( SELECT T.tweet_id as tweet_id ,T.tweeted_by as tweeted_by, T.tweet as tweet ,T.timestamp as timestamp,                            " +
                            "           U.name as name ,U.username as username, U.user_id as user_id,                                                                   " +
                            "           T.in_reply_to_user_id as in_reply_to_user_id, T.in_reply_to_tweet_id as in_reply_to_tweet_id ,'0' as retweeted_by               " +
                            "           FROM tweets as T INNER JOIN user as U                                                                                           " +
                            "           ON T.tweeted_by = U.user_id                                                                                                     " +
                            "           WHERE                                                                                                                           " +
                            "           (   (T.tweeted_by in (select followed from follower_followed where follower = ? and T.timestamp <= IFNULL(last_followed,NOW())) " +
                            "               AND (T.in_reply_to_user_id IS NULL OR T.in_reply_to_user_id = U.user_id ))                                                  " +
                            "               OR T.tweeted_by = ? )                                       " + andTsLimiter + " ORDER BY timestamp DESC " + numLimiter + " " +
                            "       ) UNION                                                                                                                             " +
                            "       ( SELECT T.tweet_id as tweet_id,T.tweeted_by as tweeted_by ,T.tweet as tweet,                                                       " +
                            "           R.max_timestamp as timestamp, U.name as name ,U.username as username, U.user_id as user_id,                                     " +
                            "           T.in_reply_to_user_id as in_reply_to_user_id , T.in_reply_to_tweet_id as in_reply_to_tweet_id,R.username as retweeted_by        " +
                            "           FROM                                                                                                                            " +
                            "           (tweets as T INNER JOIN                                                                                                         " +
                            "           user as U ON T.tweeted_by = U.user_id                                                                                           " +
                            "           INNER JOIN                                                                                                                      " +
                            "               (select username, user_id, tweet_id, MIN(timestamp) as max_timestamp from retweets                                          " +
                            "                WHERE  user_id in (select followed from follower_followed where follower = ?                                               " +
                            "               and timestamp <= IFNULL(last_followed,NOW())) GROUP BY tweet_id) as R                                                       " +
                            "               ON R.tweet_id = T.tweet_id)                               " + whereTsLimiter + " ORDER BY timestamp DESC " + numLimiter + " " +
                            "         )                                                                                                                                 " +
                            " ORDER BY timestamp DESC) as G GROUP by tweet_id ORDER BY timestamp DESC" + numLimiter;
            System.out.println(query);
            ret = db.query(query
                    , UserTweetList.newsFeedMapperWithRetweet, userId, userId, userId);

            int favoritesList[] = getFavoriteTweetsOfUser(userId);
            for (int i = 0; i < ret.size(); i++) {
                ret.get(i).setFavorite(binarySearch(favoritesList, ret.get(i).getTweetId()));
            }
            int cantRetweetList[] = getCantRetweetOfUser(userId);
            for (int i = 0; i < ret.size(); i++) {
                System.out.println("-- ++ ++ ->" + ret.get(i).getTweetId());
                ret.get(i).setCanRetweet(!binarySearch(cantRetweetList, ret.get(i).getTweetId()));
                System.out.println("-- ++ ++ ->" + ret.get(i).getCanRetweet());
            }
            System.out.println(ret.size());
        } catch (Exception ex) {
            System.out.println("Bug in newsFeed :((");
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> nTweetsOfMentionsByTimestamp(String userId, String favoriter, String timestamp, String n, boolean after) {
        List<Tweet> ret = null;
        String tsLimiter = "";
        String whereTsLimiter = "";
        String andTsLimiter = "";
        String numLimiter = "";
        if (timestamp != null) {
            if (after) {
                tsLimiter = " timestamp > '" + timestamp + "' ";
            } else {
                tsLimiter = " timestamp < '" + timestamp + "' ";
            }
            whereTsLimiter = " WHERE " + tsLimiter;
            andTsLimiter = " AND " + tsLimiter;
        }
        if (n != null) {
            numLimiter = " LIMIT 0, " + n + " ";
        }
        try {
            String query =
                    "SELECT T.tweet_id as tweet_id ,T.tweeted_by as tweeted_by, T.tweet as tweet ,T.timestamp as timestamp,         " +
                            "U.name as name ,U.username as username, U.user_id as user_id,                                          " +
                            "T.in_reply_to_user_id as in_reply_to_user_id, T.in_reply_to_tweet_id as in_reply_to_tweet_id ,         " +
                            "'0' as retweeted_by                                                                                    " +
                            "from tweets as T inner join user as U                                                                  " +
                            "ON T.tweeted_by = U.user_id                                                                            " +
                            "WHERE                                                                                                  " +
                            "T.tweet_id in                                                                                          " +
                            "   (SELECT tweet_id from mentions where user_id = ?)                                    " + andTsLimiter +
                            "   ORDER BY timestamp DESC " + numLimiter;
            ret = db.query(query, UserTweetList.newsFeedMapperWithRetweet, userId);
            if (favoriter != null) {
                int favoritesList[] = getFavoriteTweetsOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setFavorite(binarySearch(favoritesList, ret.get(i).getTweetId()));
                }
                int cantRetweetList[] = getCantRetweetOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setCanRetweet(!binarySearch(cantRetweetList, ret.get(i).getTweetId()));
                }
            }
        } catch (Exception ex) {
            System.out.println("Bug in newsFeed :((");
            ex.printStackTrace();
        }
        return ret;
    }

    public static List<Tweet> nTweetsOfFavoritesByTimestamp(String userId, String favoriter, String timestamp, String n, boolean after) {
        List<Tweet> ret = null;
        String tsLimiter = "";
        String whereTsLimiter = "";
        String andTsLimiter = "";
        String numLimiter = "";
        if (timestamp != null) {
            if (after) {
                tsLimiter = " timestamp > '" + timestamp + "' ";
            } else {
                tsLimiter = " timestamp < '" + timestamp + "' ";
            }
            whereTsLimiter = " WHERE " + tsLimiter;
            andTsLimiter = " AND " + tsLimiter;
        }
        if (n != null) {
            numLimiter = " LIMIT 0, " + n + " ";
        }
        try {
            String query =
                    "SELECT T.tweet_id as tweet_id ,T.tweeted_by as tweeted_by, T.tweet as tweet ,T.timestamp as timestamp,         " +
                            "U.name as name ,U.username as username, U.user_id as user_id,                                          " +
                            "T.in_reply_to_user_id as in_reply_to_user_id,T.in_reply_to_tweet_id as in_reply_to_tweet_id,           " +
                            "'0' as retweeted_by                                                                                    " +
                            "from tweets as T inner join user as U                                                                  " +
                            "ON T.tweeted_by = U.user_id                                                                            " +
                            "WHERE                                                                                                  " +
                            "T.tweet_id in                                                                                          " +
                            "   (SELECT tweet_id from favorite where user_id = ?)                                    " + andTsLimiter +
                            " ORDER BY timestamp DESC " + numLimiter;
            ret = db.query(query, UserTweetList.newsFeedMapperWithRetweet, userId);
            if (favoriter != null) {
                int favoritesList[] = getFavoriteTweetsOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setFavorite(binarySearch(favoritesList, ret.get(i).getTweetId()));
                }
                int cantRetweetList[] = getCantRetweetOfUser(favoriter);
                for (int i = 0; i < ret.size(); i++) {
                    ret.get(i).setCanRetweet(!binarySearch(cantRetweetList, ret.get(i).getTweetId()));
                }
            }
        } catch (Exception ex) {
            System.out.println("Bug in newsFeed :((");
            ex.printStackTrace();
        }
        return ret;
    }
}
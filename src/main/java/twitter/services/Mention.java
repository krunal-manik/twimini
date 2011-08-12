package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: rahul.pl
 * Date: 7/31/11
 * Time: 12:48 AM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class Mention {
    public static SimpleJdbcTemplate db;

    public static RowMapper<User> rowMapperForMention = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User ret = new User();
            ret.setUserId(rs.getInt("user_id"));
            ret.setUsername(rs.getString("username"));
            ret.setName(rs.getString("name"));
            return ret;
        }
    };

    @Autowired
    public Mention(SimpleJdbcTemplate db) {
        this.db = db;
    }

    public static List<User> mentionedList(String tweetId) {
        List<User> userList = null;
        try {
            userList = db.query("select user_id, username, name from user " +
                    "where user_id in (select user_id from mentions where tweet_id = ?)"
                    , Mention.rowMapperForMention, tweetId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
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

}

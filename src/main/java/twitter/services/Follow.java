package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/5/11
 * Time: 12:37 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class Follow {

    public static SimpleJdbcTemplate db;
    public static RowMapper<User> rowMapperForFollow = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            User ret = new User();
            ret.setUserId(rs.getInt("user_id"));
            ret.setUsername(rs.getString("username"));
            ret.setName(rs.getString("name"));
            ret.setAboutMe(rs.getString("about_me"));
            return ret;
        }
    };

    @Autowired
    public Follow(SimpleJdbcTemplate db) {
        this.db = db;
    }

    public static Boolean ifFollow(String followed, String follower) {
        if (followed.equals(follower)) {
            return true;
        } else {
            List<User> followers = db.query(
                    "SELECT user_id, username, name, about_me from user                      " +
                            "where user_id IN (select followed from follower_followed      " +
                            "where follower = ? and followed = ? AND last_followed IS NULL)",
                    Follow.rowMapperForFollow, follower, followed);
            if (followers.size() > 0) {
                return false;
            } else return true;
        }
    }

    public static List<User> allUsersListbyPattern(String pattern, String userId) {
        List<User> userList = null;
        try {
            userList = db.query(
                    "SELECT user_id, username, name, about_me from user               " +
                            "where user_id != ? and username like '" + pattern + "%'",
                    Follow.rowMapperForFollow, userId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
    }

    public static List<User> allUsersListContainingSubstring(String pattern, String userId) {
        List<User> userList = null;
        try {
            userList = db.query(
                    "SELECT user_id, username, name, about_me from user                   " +
                            "where user_id != ? and username like '%" + pattern + "%'   " +
                            "and user_id not in (select followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL)              ",
                    Follow.rowMapperForFollow, userId, userId);
            for (int i = 0; i < userList.size(); i++)
                userList.get(i).setFollowStatus("Follow");
            List<User> followers = db.query(
                    "SELECT user_id, username, name, about_me from user                         " +
                            "where user_id != ? and username like '%" + pattern + "%'         " +
                            "and user_id IN (select followed from follower_followed           " +
                            "where follower = ? AND last_followed IS NULL)                    ",
                    Follow.rowMapperForFollow, userId, userId);
            for (int i = 0; i < followers.size(); i++)
                followers.get(i).setFollowStatus("Following");
            for (int i = 0; i < followers.size(); i++)
                userList.add(followers.get(i));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return userList;
    }

    public static List<User> allUsersList(String userId) {
        List<User> userList = null;
        try {
            userList = db.query(
                    "SELECT user_id, username, name, about_me from user                                      " +
                            "where user_id != ? and user_id not in (select followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL)                                 ",
                    Follow.rowMapperForFollow, userId, userId);
            for (int i = 0; i < userList.size(); i++)
                userList.get(i).setFollowStatus("Follow");
            List<User> followers = db.query(
                    "SELECT user_id, username, name, about_me from user                                  " +
                            "where user_id != ? and user_id IN (select followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL)                             ",
                    Follow.rowMapperForFollow, userId, userId);
            for (int i = 0; i < followers.size(); i++)
                followers.get(i).setFollowStatus("Following");
            for (int i = 0; i < followers.size(); i++)
                userList.add(followers.get(i));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        if (userList == null) return new ArrayList<User>();
        return userList;
    }

    public static boolean addFollowing(String userId, String currentUser) {
        try {
            Map<String, Object> mm = db.queryForMap("SELECT * from follower_followed WHERE followed = ? AND follower = ?",
                    userId, currentUser);
            //db.update("INSERT INTO follower_followed (followed, follower) values (?, ?)", userId, currentUser );
            db.update("UPDATE follower_followed SET last_followed = NULL WHERE followed = ? AND follower = ? ", userId, currentUser);
        } catch (EmptyResultDataAccessException ex) {
            db.update("INSERT INTO follower_followed (followed, follower,last_followed) values (?, ?, NULL)", userId, currentUser);
        } catch (Exception ex) {
            System.out.println("Add Following-Followers Exception :((((((");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean removeFollowing(String userId, String currentUser) {
        try {
            db.update("UPDATE follower_followed SET last_followed = NOW() where followed = ? and follower = ?", userId, currentUser);
        } catch (Exception ex) {
            System.out.println("Remove Following-Followers Exception :((((((");
            ex.printStackTrace();
            return false;
        }
        return true;
    }

    public static List<User> getFollowedList(String userId) {
        List<User> followedList = null;
        try {
            followedList = db.query(
                    "SELECT user_id, username, name, about_me from user                 " +
                            "where user_id in (SELECT followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL)            ",
                    Follow.rowMapperForFollow, userId);
        } catch (Exception ex) {
            System.out.println("Followed List Exception :((((((");
            ex.printStackTrace();
        }
        if (followedList == null) return new ArrayList<User>();
        for (int i = 0; i < followedList.size(); i++)
            followedList.set(i, UserAuthentication.getUserByUsername(followedList.get(i).getUsername()));
        return followedList;
    }

    public static List<User> nFollowedInLimits(String userId, String loggedInUser, String from, String n) {
        String numLimiter = "";
        if ( !(n.equals(null) || n == null || n.equals("null")) ) {
            if (from == null) {
                numLimiter = " LIMIT 0, " + n + " ";
            } else {
                System.out.println(from + " " + n);
                int to = Integer.parseInt(from) + Integer.parseInt(n);
                numLimiter = " LIMIT " + from + ", " + to + " ";
            }
        }

        List<User> followedList = null;
        List<User> loggedInFollowedList = null;
        try {
            String query =
                    "SELECT user_id, username, name, about_me from user                 " +
                            "where user_id in (SELECT followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL)" +
                            numLimiter;
            followedList = db.query(query, Follow.rowMapperForFollow, userId);
            query = "SELECT user_id, username, name, about_me from user                " +
                    "where user_id in (SELECT followed from follower_followed " +
                    "where follower = ? AND last_followed IS NULL)";

            if (loggedInUser != null) {
                loggedInFollowedList = db.query(query, Follow.rowMapperForFollow, loggedInUser);
                if (followedList == null) return new ArrayList<User>();
                System.out.println("logged " + loggedInFollowedList.size());
                System.out.println("followed " + followedList.size());
                for (int i = 0; i < followedList.size(); i++) {
                    if (loggedInFollowedList.contains(followedList.get(i))) {
                        followedList.get(i).setFollowStatus("Following");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Followed List Exception :((((((");
            ex.printStackTrace();
        }
        return followedList;
    }

    public static List<User> nFollowingInLimits(String userId, String loggedInUser, String from, String n) {
        System.out.println( "n is " + n );
        String numLimiter = "";
        if ( !(n.equals(null) || n == null || n.equals("null")) ) {
            if (from == null) {
                numLimiter = " LIMIT 0, " + n + " ";
            } else {
                System.out.println(from + " " + n);
                int to = Integer.parseInt(from) + Integer.parseInt(n);
                numLimiter = " LIMIT " + from + ", " + to + " ";
            }
        }
        System.out.println("numLimiter is " + numLimiter);
        List<User> followedList = null;
        List<User> loggedInFollowedList = null;
        try {
            String query =
                    "SELECT user_id, username, name, about_me from user                " +
                            "where user_id in (SELECT follower from follower_followed " +
                            "where followed = ? AND last_followed IS NULL)" +
                            numLimiter;
            followedList = db.query(query, Follow.rowMapperForFollow, userId);
            query = "SELECT user_id, username, name, about_me from user                 " +
                    "where user_id in (SELECT followed from follower_followed " +
                    "where follower = ? AND last_followed IS NULL)";
            if (loggedInUser != null) {
                loggedInFollowedList = db.query(query, Follow.rowMapperForFollow, loggedInUser);
                if (followedList == null) return new ArrayList<User>();
                for (int i = 0; i < followedList.size(); i++) {
                    if (loggedInFollowedList.contains(followedList.get(i))) {
                        followedList.get(i).setFollowStatus("Following");
                    }
                }
            }
        } catch (Exception ex) {
            System.out.println("Followed List Exception :((((((");
            ex.printStackTrace();
        }
        return followedList;
    }

    public static List<User> getFollowerList(String userId) {
        List<User> followerList = null;
        try {
            followerList = db.query(
                    "SELECT user_id, username, name, about_me from user                 " +
                            "where user_id in (SELECT follower from follower_followed " +
                            "where followed = ? AND last_followed IS NULL)            ",
                    Follow.rowMapperForFollow, userId);
        } catch (Exception ex) {
            System.out.println("Follower List Exception :(((((");
            ex.printStackTrace();
        }
        if (followerList == null) return new ArrayList<User>();
        for (int i = 0; i < followerList.size(); i++)
            followerList.set(i, UserAuthentication.getUserByUsername(followerList.get(i).getUsername()));
        return followerList;
    }

    //Redundant functions, clean this up later
    public static List<User> getFollowedListLimited(String userId) {
        List<User> followedList = null;
        try {
            followedList = db.query(
                    "SELECT user_id, username, name, about_me from user                 " +
                            "where user_id in (SELECT followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL) LIMIT 0,7  ",
                    Follow.rowMapperForFollow, userId);
        } catch (Exception ex) {
            System.out.println("Followed List Limited Exception :((((((");
            ex.printStackTrace();
        }
        return followedList;
    }

    //Redundant functions, clean this up later
    public static List<User> getFollowerListLimited(String userId) {
        List<User> followerList = null;
        try {
            followerList = db.query(
                    "SELECT user_id, username, name, about_me from user                 " +
                            "where user_id in (SELECT follower from follower_followed " +
                            "where followed = ? AND last_followed IS NULL) LIMIT 0,7  ",
                    Follow.rowMapperForFollow, userId);
        } catch (Exception ex) {
            System.out.println("Follower List Limited Exception :(((((");
            ex.printStackTrace();
        }
        return followerList;
    }

    public static int getFollowerCount(String userId) {
        int followerCount = 0;
        try {
            followerCount = db.queryForInt(
                    "SELECT COUNT(user_id) from user                          " +
                            "where user_id in (SELECT follower from follower_followed " +
                            "where followed = ? AND last_followed IS NULL)            ", userId);
        } catch (Exception ex) {
            System.out.println("FollowerCount Exception :(((((");
            ex.printStackTrace();
        }
        return followerCount;
    }

    public static int getFollowingCount(String userId) {
        int followingCount = 0;
        try {
            followingCount = db.queryForInt(
                    "SELECT COUNT(user_id) from user                          " +
                            "where user_id in (SELECT followed from follower_followed " +
                            "where follower = ? AND last_followed IS NULL)            ", userId);
        } catch (Exception ex) {
            System.out.println("FollowingCount Exception :((((((");
            ex.printStackTrace();
        }
        return followingCount;
    }
}

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
        public User mapRow(ResultSet rs , int i) throws SQLException {
            User ret = new User();
            ret.setUserId( rs.getInt("user_id"));
            ret.setUsername( rs.getString("username"));
            ret.setName( rs.getString("name"));
            return ret;
        }
    };

    @Autowired
    public Follow( SimpleJdbcTemplate db ){
        this.db = db;
    }

    public static List<User> allUsersList( String userId ){
        List<User> userList = null;
        try{
            userList = db.query("SELECT user_id, username, name from user " +
                    "where user_id != ? and user_id not in (select followed from follower_followed " +
                    "where follower = ?)",
                     Follow.rowMapperForFollow,  userId , userId );
            for(int i=0;i<userList.size();i++)
                    userList.get(i).setFollowStatus( "Follow" );
            List<User> followers =  db.query("SELECT user_id, username, name from user " +
                    "where user_id != ? and user_id IN (select followed from follower_followed " +
                    "where follower = ?)",
                     Follow.rowMapperForFollow,  userId , userId );
            for(int i=0;i<followers.size();i++)
                    followers.get(i).setFollowStatus( "Unfollow" );
            for(int i=0;i<followers.size();i++)
                userList.add( followers.get(i) );
        }
        catch( Exception ex ){
            ex.printStackTrace();
        }
        return userList;
    }

    public static void addFollowing( String userId , String currentUser ){
        try{
            db.update("INSERT INTO follower_followed (followed, follower) values (?, ?)", userId, currentUser );
        }
        catch ( Exception ex ){
            System.out.println( "Add Following-Followers Exception :((((((" );
            ex.printStackTrace();
        }
    }

    public static void removeFollowing( String userId , String currentUser ){
        try{
            db.update("DELETE FROM follower_followed where followed = ? and follower = ?", userId, currentUser );
        }
        catch ( Exception ex ){
            System.out.println( "Remove Following-Followers Exception :((((((" );
            ex.printStackTrace();
        }
    }

    public static List<User> getFollowedList( String userId ){
        List<User> followedList = null;
        try{
            followedList = db.query("SELECT user_id, username, name from user " +
                                    "where user_id in (SELECT followed from follower_followed " +
                                    "where follower = ?)"
                    , Follow.rowMapperForFollow , userId );
        }
        catch( Exception ex ){
            System.out.println( "Followed List Exception :((((((" );
            ex.printStackTrace();
        }
        return followedList;
    }

    public static List<User> getFollowerList( String userId ){
        List<User> followerList = null;
        try{
            followerList = db.query("SELECT user_id, username, name from user " +
                                    "where user_id in (SELECT follower from follower_followed " +
                                    "where followed = ?)"
                    , Follow.rowMapperForFollow , userId );
        }
        catch( Exception ex ){
            System.out.println( "Follower List Exception :(((((" );
            ex.printStackTrace();
        }
        return followerList;
    }



    //Redundant functions, clean this up later
    public static List<User> getFollowedListLimited( String userId ){
        List<User> followedList = null;
        try{
            followedList = db.query("SELECT user_id, username, name from user " +
                                    "where user_id in (SELECT followed from follower_followed " +
                                    "where follower = ?) LIMIT 0,7"
                    , Follow.rowMapperForFollow , userId );
        }
        catch( Exception ex ){
            System.out.println( "Followed List Limited Exception :((((((" );
            ex.printStackTrace();
        }
        return followedList;
    }
     //Redundant functions, clean this up later
    public static List<User> getFollowerListLimited( String userId ){
        List<User> followerList = null;
        try{
            followerList = db.query("SELECT user_id, username, name from user " +
                                    "where user_id in (SELECT follower from follower_followed " +
                                    "where followed = ?) LIMIT 0,7"
                    , Follow.rowMapperForFollow , userId );
        }
        catch( Exception ex ){
            System.out.println( "Follower List Limited Exception :(((((" );
            ex.printStackTrace();
        }
        return followerList;
    }
}

package twitter.models;

import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 6/30/11
 * Time: 11:24 PM
 * To change this template use File | Settings | File Templates.
 */
public class User {
    private int userId;
    private String username;
    private String name;
    private String password;
    private String email;
    private String followStatus;
    private String aboutMe;

    public User() {
        followStatus = "Follow";
    }

    public static final RowMapper<User> rowMapper = new RowMapper<User>() {
        @Override
        public User mapRow(ResultSet rs, int i) throws SQLException {
            return new User(rs);
        }
    };

    public User(ResultSet rs) throws SQLException {
        userId = rs.getInt("user_id");
        username = rs.getString("username");
        name = rs.getString("name");
        password = rs.getString("password");
        email = rs.getString("email");
    }

    public int getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getFollowStatus() {
        return followStatus;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFollowStatus(String followStatus) {
        this.followStatus = followStatus;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    @Override
    public boolean equals(Object o) {
        User u = (User) o;
        return (this.userId == u.getUserId());
    }

    @Override
    public int hashCode() {
        return 0;
    }
}

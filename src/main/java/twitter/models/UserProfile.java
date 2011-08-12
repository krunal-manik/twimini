package twitter.models;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/28/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProfile {
    int userId;
    String name;
    String username;
    String email;

    public UserProfile(User user) {
        userId = user.getUserId();
        name = user.getName();
        username = user.getUsername();
        email = user.getEmail();
    }

    public UserProfile() {

    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

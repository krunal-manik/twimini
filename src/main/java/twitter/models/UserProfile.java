package twitter.models;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/28/11
 * Time: 4:04 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserProfile {
    private int userId;
    private String name;
    private String username;
    private String email;
    private String aboutMe;

    public UserProfile(User user) {
        userId = user.getUserId();
        name = user.getName();
        username = user.getUsername();
        email = user.getEmail();
        aboutMe = user.getAboutMe();
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

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }
}

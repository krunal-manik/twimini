package twitter.models;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/10/11
 * Time: 2:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class Contact {
    private String name;
    private String email;
    private String status;
    private String bio;
    private String username;
    private int userId;

    public Contact() {
        status = "Follow";
    }

    public Contact(String name, String email, String status) {
        this.name = name;
        this.email = email;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getBio() {
        return bio;
    }

    public String getUsername() {
        return username;
    }

    public int getUserId() {
        return userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

package twitter.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter.models.Tweet;
import twitter.models.User;
import twitter.models.UserProfile;
import twitter.services.Follow;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/28/11
 * Time: 6:52 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class FollowAPI {

    @RequestMapping("/api/{username}/followers")
    @ResponseBody
    public static Hashtable<String, Object> getFollowerList(@PathVariable String username,String password,String currentUsername,String from,String count) {
        Hashtable<String, Object> followers = new Hashtable<String, Object>();
        try {
            List<User> followerList;
            if( currentUsername != null && password != null ) {
                User user = UserAuthentication.authenticateUser(currentUsername,password);
                User urlMappedUser = UserAuthentication.getUserByUsername(username);
                if( user == null ) {
                    followers.put("success","false");
                    followers.put("error","User Authentication failed");
                    return followers;
                }
                followerList = Follow.nFollowedInLimits(String.valueOf(urlMappedUser.getUserId()),String.valueOf(user.getUserId()),from, count == null ? "10" : count );
            }
            else {
                User urlMappedUser = UserAuthentication.getUserByUsername(username);
                followerList = Follow.nFollowedInLimits(String.valueOf(urlMappedUser.getUserId()),null,from,count == null ? "10" : count );
            }

            followers.put("followers", followerList);
            followers.put("success", "true");
            followers.put("error", "none");
        } catch (NullPointerException ex) {
            followers.put("error", "User " + username + " does not exist");
            followers.put("success", "false");
        } catch (Exception ex) {
            followers.put("error", "Server error");
            followers.put("success", "false");
        }

        return followers;
    }

    @RequestMapping("/api/{username}/following")
    @ResponseBody
    public static Hashtable<String, Object> getFollowingList(@PathVariable String username,String password,String currentUsername,String from,String count) {
        Hashtable<String, Object> followings = new Hashtable<String, Object>();
        try {
            List<User> followingList;
            if( currentUsername != null && password != null ) {
                User user = UserAuthentication.authenticateUser(currentUsername,password);
                User urlMappeduser = UserAuthentication.getUserByUsername(username);
                if( user == null ) {
                    followings.put("success","false");
                    followings.put("error","User Authentication failed");
                    return followings;
                }
                followingList = Follow.nFollowingInLimits(String.valueOf(urlMappeduser.getUserId()),String.valueOf(user.getUserId()),from, count == null ? "10" : count );
            }
            else {
                User urlMappedUser = UserAuthentication.getUserByUsername(username);
                followingList = Follow.nFollowingInLimits(String.valueOf(urlMappedUser.getUserId()),null,from,count == null ? "10" : count );
            }

            followings.put("followings", followingList);
            followings.put("success", "true");
            followings.put("error", "none");
        } catch (NullPointerException ex) {
            followings.put("error", "User " + username + " does not exist");
            followings.put("success", "false");
        } catch (Exception ex) {
            followings.put("error", "Server error");
            followings.put("success", "false");
        }
        return followings;
    }

    @RequestMapping("/api/{username}/follow")
    @ResponseBody
    public static Hashtable<String, Object> followUser(@PathVariable String username, @RequestParam String toFollow, @RequestParam String password) {
        Hashtable<String, Object> followStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            if( user == null ) {
                followStatus.put("success","false");
                followStatus.put("error","User Authentication failed");
                return followStatus;
            }
            User userToFollow = UserAuthentication.getUserByUsername(toFollow);
            boolean success = Follow.addFollowing(String.valueOf(userToFollow.getUserId()),
                    String.valueOf(user.getUserId()));
            if (!success)
                throw new Exception();
            followStatus.put("success", "true");
            followStatus.put("error", "none");
        } catch (NullPointerException ex) {
            followStatus.put("error", "User " + username + " does not exist");
            followStatus.put("success", "false");
        } catch (Exception ex) {
            followStatus.put("error", "Server error");
            followStatus.put("success", "false");
        }

        return followStatus;
    }

    @RequestMapping("/api/{username}/unfollow")
    @ResponseBody
    public static Hashtable<String, Object> unFollowUser(@PathVariable String username, @RequestParam String toUnFollow, @RequestParam String password) {
        Hashtable<String, Object> unfollowStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.authenticateUser(username,password);
            if( user == null ) {
                unfollowStatus.put("success","false");
                unfollowStatus.put("error","User Authentication failed");
                return unfollowStatus;
            }
            User userToFollow = UserAuthentication.getUserByUsername(toUnFollow);
            boolean success = Follow.removeFollowing(String.valueOf(userToFollow.getUserId()),
                    String.valueOf(user.getUserId()));
            if (!success)
                throw new Exception();
            unfollowStatus.put("success", "true");
            unfollowStatus.put("error", "none");
        } catch (NullPointerException ex) {
            unfollowStatus.put("error", "User " + username + " does not exist");
            unfollowStatus.put("success", "false");
        } catch (Exception ex) {
            unfollowStatus.put("error", "Server error");
            unfollowStatus.put("success", "false");
        }

        return unfollowStatus;
    }

}

package twitter.api;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/28/11
 * Time: 3:30 PM
 * To change this template use File | Settings | File Templates.
 */

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter.models.User;
import twitter.models.UserProfile;
import twitter.services.Follow;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import java.util.HashMap;
import java.util.Hashtable;

@Controller
public class UserAPI {

    @RequestMapping("/api/{username}")
    @ResponseBody
    public static HashMap<String, Object> getUserInformation(@PathVariable String username) {

        HashMap<String, Object> userInformation = new HashMap<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            int followerCount = Follow.getFollowerCount(String.valueOf(user.getUserId()));
            int followingCount = Follow.getFollowingCount(String.valueOf(user.getUserId()));
            int tweetCount = UserTweetList.getUserTweetsCount(String.valueOf(user.getUserId()));
            UserProfile userProfile = new UserProfile(user);
            userInformation.put("profileInformation", userProfile);
            userInformation.put("following", followingCount);
            userInformation.put("followers", followerCount);
            userInformation.put("tweetCount", tweetCount);
            userInformation.put("success", "true");
            userInformation.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            userInformation.put("Error", "No such user exists");
            userInformation.put("status code", "401 User not found");
        } catch (Exception ex) {
            userInformation.put("Error", "Server error");
            userInformation.put("status code", "500 Internal server error");
        }

        return userInformation;
    }
}

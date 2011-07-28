package twitter.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
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

    @RequestMapping( "/api/{username}/followers" )
    @ResponseBody
    public static Hashtable<String,Object> getFollowerList( @PathVariable String username ) {
        Hashtable<String,Object> followers = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            List<User> userList = Follow.getFollowerList(String.valueOf(user.getUserId()));
            List<UserProfile> followerList = new ArrayList<UserProfile>();
            for( User u : userList ) {
                followerList.add( new UserProfile(u) );
            }
            followers.put("followers", followerList);
            followers.put("success", "true");
            followers.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            followers.put("Error", "No such user exists");
            followers.put("status code", "401 User not found");
        } catch (Exception ex) {
            followers.put("Error", "Server error");
            followers.put("status code", "500 Internal server error");
        }

        return followers;
    }

    @RequestMapping( "/api/{username}/following" )
    @ResponseBody
    public static Hashtable<String,Object> getFollowingList( @PathVariable String username ) {
        Hashtable<String,Object> following = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            List<User> userList = Follow.getFollowedList(String.valueOf(user.getUserId()));
            List<UserProfile> followingList = new ArrayList<UserProfile>();
            for( User u : userList ) {
                followingList.add(new UserProfile(u));
            }
            following.put("followers", followingList);
            following.put("success", "true");
            following.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            following.put("Error", "No such user exists");
            following.put("status code", "401 User not found");
        } catch (Exception ex) {
            following.put("Error", "Server error");
            following.put("status code", "500 Internal server error");
        }

        return following;
    }

    @RequestMapping( "/api/{username}/follow" )
    @ResponseBody
    public static Hashtable<String,Object> followUser( @PathVariable String username , String toFollow , String token ) {
        Hashtable<String,Object> followStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            User userToFollow = UserAuthentication.getUserByUsername(toFollow);
            boolean success = Follow.addFollowing(String.valueOf(userToFollow.getUserId()),
                    String.valueOf(user.getUserId()));
            if( !success )
                throw new Exception();
            followStatus.put("success", "true");
            followStatus.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            followStatus.put("Error", "No such user exists");
            followStatus.put("status code", "401 User not found");
        } catch (Exception ex) {
            followStatus.put("Error", "Server error");
            followStatus.put("status code", "500 Internal server error");
        }

        return followStatus;
    }

    @RequestMapping( "/api/{username}/unfollow" )
    @ResponseBody
    public static Hashtable<String,Object> unFollowUser( @PathVariable String username , String toUnFollow , String token ) {
        Hashtable<String,Object> unfollowStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            User userToFollow = UserAuthentication.getUserByUsername(toUnFollow);
            boolean success = Follow.removeFollowing(String.valueOf(userToFollow.getUserId()),
                    String.valueOf(user.getUserId()));
            if( !success )
                throw new Exception();
            unfollowStatus.put("success", "true");
            unfollowStatus.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            unfollowStatus.put("Error", "No such user exists");
            unfollowStatus.put("status code", "401 User not found");
        } catch (Exception ex) {
            unfollowStatus.put("Error", "Server error");
            unfollowStatus.put("status code", "500 Internal server error");
        }

        return unfollowStatus;
    }

    @RequestMapping( "/api/{username}/favorite" )
    @ResponseBody
    public static Hashtable<String,Object> markFavorite( @PathVariable String username , String tweetId , String token ) {
        Hashtable<String,Object> favoriteStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            boolean success = UserTweetList.markFavorite(tweetId, String.valueOf(user.getUserId()));
            if( !success )
                throw new Exception();
            favoriteStatus.put("success", "true");
            favoriteStatus.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            favoriteStatus.put("Error", "No such user exists");
            favoriteStatus.put("status code", "401 User not found");
        } catch (Exception ex) {
            favoriteStatus.put("Error", "Server error");
            favoriteStatus.put("status code", "500 Internal server error");
        }

        return favoriteStatus;
    }

    @RequestMapping( "/api/{username}/unfavorite" )
    @ResponseBody
    public static Hashtable<String,Object> markUnfavorite( @PathVariable String username , String tweetId , String token ) {
        Hashtable<String,Object> unFavoriteStatus = new Hashtable<String, Object>();
        try {
            User user = UserAuthentication.getUserByUsername(username);
            boolean success = UserTweetList.deleteFavorite(tweetId, String.valueOf(user.getUserId()));
            if( !success )
                throw new Exception();
            unFavoriteStatus.put("success", "true");
            unFavoriteStatus.put("status code", "200 OK");
        } catch (NullPointerException ex) {
            unFavoriteStatus.put("Error", "No such user exists");
            unFavoriteStatus.put("status code", "401 User not found");
        } catch (Exception ex) {
            unFavoriteStatus.put("Error", "Server error");
            unFavoriteStatus.put("status code", "500 Internal server error");
        }

        return unFavoriteStatus;
    }
}


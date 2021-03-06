package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.User;
import twitter.services.Follow;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.multi.MultiViewportUI;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rahul.pl
 * Date: 7/1/11
 * Time: 2:44 AM
 * To change this template use File | Settings | File Templates.
 */

@Controller
public class FollowController {

    public FollowController() {
    }

    @RequestMapping("/all_users")
    public ModelAndView allUserGet(HttpSession session) {
        List<User> ret = Follow.allUsersList(session.getAttribute("userId").toString());
        ModelAndView mv = new ModelAndView();
        mv.addObject("userList", ret);
        return mv;
    }

    @RequestMapping(value = "/all_users/addFollowing", method = RequestMethod.POST)
    @ResponseBody
    public void addFollowing(HttpSession session, @RequestParam String userId) {
        Follow.addFollowing(userId, session.getAttribute("userId").toString());
    }

    @RequestMapping(value = "/all_users/removeFollowing", method = RequestMethod.POST)
    @ResponseBody
    public void removeFollowingPost(HttpSession session, @RequestParam String userId) {
        Follow.removeFollowing(userId, session.getAttribute("userId").toString());
    }

    @RequestMapping("/first_following")
    @ResponseBody
    public List<User> firstFollowing(HttpSession session, @RequestParam String user, @RequestParam String follower) {
        List<User> followings = Follow.nFollowedInLimits(user, follower, null, "10");
        System.out.println("size of following is " + followings.size());
        return followings;
    }

    @RequestMapping("/more_following")
    @ResponseBody
    public List<User> moreFollowing(HttpSession session, @RequestParam String user, @RequestParam String follower, @RequestParam String from) {
        List<User> followings = Follow.nFollowedInLimits(user, follower, from, "10");
        return followings;
    }

    @RequestMapping("/first_follower")
    @ResponseBody
    public List<User> firstFollowers(HttpSession session, @RequestParam String user, @RequestParam String follower) {
        List<User> followers = Follow.nFollowingInLimits(user, follower, null, "10");
        System.out.println("size of followers is " + followers.size());
        return followers;
    }

    @RequestMapping("/more_follower")
    @ResponseBody
    public List<User> moreFollowers(HttpSession session, @RequestParam String user, @RequestParam String follower, @RequestParam String from) {
        List<User> followings = Follow.nFollowingInLimits(user, follower, from, "10");
        return followings;
    }


    @RequestMapping("/first_search")
    @ResponseBody
    public List<User> firstSearch(HttpSession session, @RequestParam String user, @RequestParam String follower) {
        List<User> searches = Follow.nSearchedInLimits(user, follower, null, "10");
        return searches;
    }

    @RequestMapping("/more_search")
    @ResponseBody
    public List<User> moreSearch(HttpSession session, @RequestParam String user, @RequestParam String follower, @RequestParam String from) {
        List<User> searches = Follow.nSearchedInLimits(user, follower, from, "10");
        return searches;
    }
}
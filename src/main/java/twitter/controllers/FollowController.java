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

    public FollowController(){
    }

    @RequestMapping("/all_users")
    public ModelAndView allUserGet(HttpSession session) {
        List<User> ret = Follow.allUsersList( session.getAttribute("userId").toString() );
        ModelAndView mv = new ModelAndView();
        mv.addObject("userList",  ret);
        return mv;
    }

    @RequestMapping(value="/all_users/addFollowing", method= RequestMethod.POST) @ResponseBody // Ajax call
    public void addFollowing(HttpSession session, @RequestParam String userId) {
        Follow.addFollowing( userId , session.getAttribute("userId").toString() );
    }

    @RequestMapping(value="/all_users/removeFollowing", method= RequestMethod.POST) @ResponseBody // Ajax call
    public void removeFollowingPost(HttpSession session, @RequestParam String userId) {
        Follow.removeFollowing(userId, session.getAttribute("userId").toString());
    }

    @RequestMapping("/followed")
    public ModelAndView followedList(HttpSession session) {
        List<User> followedList = Follow.getFollowedList( session.getAttribute("userId").toString() );
        ModelAndView mv = new ModelAndView();
        mv.addObject("followedList", followedList );
        return mv;
    }

    @RequestMapping("/follower")
    public ModelAndView followerList(HttpSession session) {
        List<User> followerList = Follow.getFollowerList( session.getAttribute("userId").toString() );
        ModelAndView mv = new ModelAndView();
        mv.addObject("followerList", followerList );
        return mv;
    }

    @RequestMapping("/profile/follower")
    public ModelAndView getSpecificUsersFollowers(String userId){
        List<User> ret = Follow.getFollowerList( userId );
        ModelAndView mv = new ModelAndView("/follower");
        mv.addObject( "followerList" , ret );
        return mv;
    }

    @RequestMapping("/profile/followed")
    public ModelAndView getSpecificUsersFollowed(String userId){
        List<User> ret = Follow.getFollowedList( userId );
        ModelAndView mv = new ModelAndView("/followed");
        mv.addObject( "followedList" , ret );
        return mv;
    }

}
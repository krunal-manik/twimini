package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.User;
import twitter.services.Follow;

import javax.servlet.http.HttpSession;
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

    @RequestMapping(value="/all_users", method= RequestMethod.POST) // Ajax call
    public ModelAndView followedAddedPost(HttpSession session, @RequestParam String userId) {
        Follow.addFollowing( userId , session.getAttribute("userId").toString() );
        ModelAndView mv = new ModelAndView();
        return mv;
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



}
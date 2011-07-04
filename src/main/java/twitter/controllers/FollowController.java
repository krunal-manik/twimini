package twitter.controllers;

import org.hsqldb.rights.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

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

    public final SimpleJdbcTemplate db;

    @Autowired
    public FollowController(SimpleJdbcTemplate db) {
        this.db = db;
    }

    @RequestMapping("/all_users")
    public ModelAndView allUserGet(HttpSession session) {
        List<Map<String, Object>> l = db.queryForList("SELECT user_id, username, name from user " +
                                                      "where user_id not in (select followed from follower_followed " +
                                                      "where follower = ?) and user_id != ?",
                                                      session.getAttribute("userId"), session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView();
        mv.addObject("userList", l);
        return mv;
    }

    @RequestMapping(value="/all_users", method= RequestMethod.POST)
    public ModelAndView followedAddedPost(HttpSession session, @RequestParam final String userId) {
        System.out.println("USERID -> " + userId);
        db.update("INSERT INTO follower_followed (followed, follower) values (?, ?)", userId, session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView();
        return mv;
    }

    @RequestMapping("/followed")
    public ModelAndView FollowedGet(HttpSession session) {
        List<Map<String, Object>> l = db.queryForList("SELECT user_id, username, name from user " +
                                                      "where user_id in (SELECT followed from follower_followed " +
                                                      "where follower = ?)", session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView();
        mv.addObject("followedList", l);
        return mv;
    }

    @RequestMapping("/follower")
    public ModelAndView FollowerGet(HttpSession session) {
        List<Map<String, Object>> l = db.queryForList("SELECT user_id, username, name from user " +
                                                      "where user_id in (SELECT follower from follower_followed " +
                                                      "where followed = ?)", session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView();
        mv.addObject("followedList", l);
        return mv;
    }



}
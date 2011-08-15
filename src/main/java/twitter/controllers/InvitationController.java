package twitter.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.User;
import twitter.services.Invitation;
import twitter.services.UserAuthentication;

import javax.servlet.http.HttpSession;
import java.util.Hashtable;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/15/11
 * Time: 10:32 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class InvitationController {

    @RequestMapping( value = "/addInvite" , method = RequestMethod.POST )
    @ResponseBody
    public Hashtable<String,Object> makeInviteTemporary(@RequestParam String receiverName , @RequestParam String email,HttpSession session) {
        Hashtable<String,Object> response = new Hashtable<String, Object>();
        if( session.getAttribute("userId") == null ) {
            response.put( "success"  , "false" );
            response.put( "error" , "Session expired" );
            return response;
        }
        User loggedInUser = UserAuthentication.getUserByUsername( session.getAttribute("username").toString() );
        Invitation.insertTemporaryInvitation( receiverName , loggedInUser , email );

        response.put( "success" , "true" );
        return response;
    }

    @RequestMapping( value = "/deleteInvite" , method = RequestMethod.POST )
    @ResponseBody
    public Hashtable<String,Object> deleteTemporaryInvite(@RequestParam String receiverName , @RequestParam String email,HttpSession session) {
        Hashtable<String,Object> response = new Hashtable<String, Object>();
        if( session.getAttribute("userId") == null ) {
            response.put( "success"  , "false" );
            response.put( "error" , "Session expired" );
            return response;
        }
        User loggedInUser = UserAuthentication.getUserByUsername( session.getAttribute("username").toString() );
        Invitation.deleteTemporaryInvitation(receiverName, loggedInUser, email);

        response.put( "success" , "true" );
        return response;
    }

    @RequestMapping("/sendInvites")
    public ModelAndView sendInvites() {
        Invitation.makeInvitesPermanent();
        ModelAndView mv = new ModelAndView("/message");
        mv.addObject("message","Invitations to the marked people would be sent shortly.");
        return mv;
    }
}

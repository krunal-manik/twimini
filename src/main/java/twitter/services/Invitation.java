package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import twitter.models.User;

import javax.servlet.http.HttpSession;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/15/11
 * Time: 10:27 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class Invitation {

    public static SimpleJdbcTemplate db;

    @Autowired
    public Invitation(SimpleJdbcTemplate simpleJdbcTemplate) {
        db = simpleJdbcTemplate;
    }

    private static String getInvitationTemplate(String senderName,String senderUsername,String receiverName) {
        return String.format("Hi %s!\n%s has just invited you to join twitter.\nYou can join us at http://localhost:8080 to start tweeting!\n" +
                "You can view %s's profile at http://localhost:8080/%s.\nHappy Tweeting!\n" +
                "Regards,\nTeam Twitter" , receiverName, senderName, senderName ,senderUsername );
    }

    public static void insertTemporaryInvitation(String receiverName,User sender, String receiver) {
        try {
            db.update( "INSERT INTO temporary_invites(sender_name,sender_email,receiver_name,receiver_email,message,subject) VALUES (?,?,?,?,?,?)" ,
                    sender.getName() , sender.getEmail() , receiverName , receiver , getInvitationTemplate(sender.getName(), sender.getUsername(), receiverName) , "Invitation to Twitter" );
        } catch (Exception ex) {
            System.out.println( "Bug in temporary invitation :((" );
            ex.printStackTrace();
        }
    }

    public static void deleteTemporaryInvitation(String receiverName,User sender,String receiverEmail) {
        try{
            db.update( "DELETE FROM temporary_invites WHERE receiver_name = ? AND receiver_email = ? AND sender_email = ?" ,
                    receiverName , receiverEmail , sender.getEmail() );
        } catch (Exception ex) {
            System.out.println("Bug in deleting temporary invite :(");
            ex.printStackTrace();
        }
    }

    public static void makeInvitesPermanent() {
        try{
            List<Map<String,Object>> mails = db.queryForList( "SELECT * from temporary_invites" );
            for(int i=0;i<mails.size();i++) {
                Map<String,Object> map = mails.get(i);
                db.update( "INSERT INTO emails(sender,receiver,subject,body) VALUES (?,?,?,?)" , map.get("sender_email").toString() , map.get("receiver_email").toString() ,
                        map.get("subject").toString() , map.get("message").toString() );
            }
            db.update( "DELETE FROM temporary_invites" );
        } catch (Exception ex) {
            System.out.println("Bug in make invites permanent :(");
            ex.printStackTrace();
        }
    }
}

package twitter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import twitter.models.Mail;
import twitter.services.Email;
import twitter.services.EmailService;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/15/11
 * Time: 12:06 PM
 * To change this template use File | Settings | File Templates.
 */

public class EmailThread extends Thread{

    private static Email emailSender;

    public EmailThread(Email emailSender) {
        this.emailSender = emailSender;
    }


    public void run() {
        try {
            while( true ) {
                sleep( 60000 );
                List<Mail> mailList = EmailService.getEmails();
                for( Mail mail : mailList ) {
                    emailSender.sendMail( mail.getFrom() , mail.getTo() ,
                            mail.getSubject() , mail.getBody() );
                }
            }
        } catch (NullPointerException ex) {
            System.out.println( "here null pointer" );
        }
        catch (Exception ex) {
            System.out.println( emailSender );
            System.out.println( "Bug in email thread :((" );
        }
    }
}

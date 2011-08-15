package twitter.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/14/11
 * Time: 7:24 PM
 * To change this template use File | Settings | File Templates.
 */

public class Email {

    private MailSender mailSender;
    private SimpleMailMessage message;

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
        message = new SimpleMailMessage();
    }

    public MailSender getMailSender() {
        return this.mailSender;
    }

    public void sendMail(String from, String to, String subject, String body) {
        System.out.println( "Sending to " + to  );
        message.setFrom(from);
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}

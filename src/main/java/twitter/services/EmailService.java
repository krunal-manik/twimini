package twitter.services;

import org.hsqldb.lib.tar.DbBackup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.stereotype.Service;
import twitter.models.Mail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/15/11
 * Time: 12:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class EmailService {

    public static SimpleJdbcTemplate db;

    @Autowired
    public EmailService(SimpleJdbcTemplate db) {
        this.db = db;
    }

    public static final RowMapper<Mail> rowMapper = new RowMapper<Mail>() {
        @Override
        public Mail mapRow(ResultSet rs, int i) throws SQLException {
            return new Mail( "manikkrunal@gmail.com" , rs.getString("receiver") ,
                    rs.getString("subject") , rs.getString("body") );
        }
    };

    public static List<Mail> getEmails() {
        List<Mail> mailList = null;
        try {
            mailList = db.query( "SELECT * FROM emails" , rowMapper );
            db.update( "DELETE FROM emails" );
            if( mailList == null )
                mailList = new ArrayList<Mail>();

            List<Mail> inactiveUsersList = db.query( "SELECT * from inactive_users" , rowMapper );
            if( inactiveUsersList == null )
                inactiveUsersList = new ArrayList<Mail>();

            for( Mail mail : inactiveUsersList ) {
                mailList.add(mail);
            }

        } catch (EmptyResultDataAccessException ex) {

        } catch (Exception ex) {
            System.out.println( "Bug in get emails :(" );
            ex.printStackTrace();
        }

        return mailList;
    }

    public static void sendResetPasswordMail(String sender, String receiver, String subject, String body) {
        try{
            db.update( "INSERT INTO emails(sender,receiver,subject,body) VALUES(?,?,?,?)" ,
                    sender , receiver , subject , body );
        } catch (Exception ex) {
            System.out.println( "Bug in reset password :((" );
            ex.printStackTrace();
        }
    }
}

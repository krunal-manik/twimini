package twitter.models;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/15/11
 * Time: 12:10 PM
 * To change this template use File | Settings | File Templates.
 */
public class Mail {
    private String from;
    private String to;
    private String subject;
    private String body;

    public Mail(String from, String to, String subject, String body) {
        this.from = from;
        this.to = to;
        this.subject = subject;
        this.body = body;
    }

    public String getFrom() {

        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

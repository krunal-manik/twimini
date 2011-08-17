package twitter.controllers;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.Contact;
import twitter.models.User;
import twitter.services.Follow;
import twitter.services.UserAuthentication;

import javax.servlet.http.HttpSession;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/17/11
 * Time: 1:35 PM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class ContactImporterController {

    @RequestMapping(value = "/import_contacts")
    public ModelAndView contactImporterGet(HttpSession session) {
        if( session.getAttribute("userId") != null ) {
            return new ModelAndView("/contact-import");
        }
        return new ModelAndView(){{
            setViewName("redirect:/");
        }};
    }


    @RequestMapping(value = "/gmail")
    @ResponseBody
    public List<Contact> contactImporter(String access_token, String token_type, String expires_in, HttpSession session) throws Exception {
        if( session.getAttribute("userId") == null ) {
            return new ArrayList<Contact>();
        }

        System.out.println(access_token);
        if (access_token == null) {
            return new ArrayList<Contact>();
        }

        URL url = new URL(String.format("https://www.google.com/m8/feeds/contacts/default/full?oauth_token=%s&max-results=500&alt=json", access_token));
        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));
        StringBuffer json = new StringBuffer("");
        String s = "";
        while ((s = br.readLine()) != null) {
            json.append(s);
        }

        ArrayList<String> names = new ArrayList<String>();
        ArrayList<String> emails = new ArrayList<String>();

        Object obj = JSONValue.parse(json.toString());
        JSONObject jsonObject = (JSONObject) obj;
        obj = jsonObject.get("feed");
        obj = ((JSONObject) obj).get("entry");
        JSONArray array = (JSONArray) obj;
        for (int i = 0; i < array.size(); i++) {
            JSONObject entryObject = (JSONObject) array.get(i);
            entryObject = (JSONObject) entryObject.get("title");
            String contactName = entryObject.get("$t").toString();

            JSONObject emailObject = (JSONObject) array.get(i);
            JSONArray arr = (JSONArray) emailObject.get("gd$email");
            if (arr != null && arr.size() > 0) {
                JSONObject emailIdObject = (JSONObject) arr.get(0);
                String email = emailIdObject.get("address").toString();
                if (contactName.equals("")) contactName = email;

                names.add(contactName);
                emails.add(email);
            }
        }


        List<User> followingList = Follow.getFollowedList(session.getAttribute("userId").toString());

        ArrayList<Contact> contactList = new ArrayList<Contact>();
        for (int i = 0; i < emails.size(); i++) {
            User user = UserAuthentication.getUserByEmail(emails.get(i));
            Contact contact = new Contact();
            contact.setEmail(emails.get(i));
            contact.setName(names.get(i));

            if (user == null) {
                contact.setStatus("Invite");
                contact.setUsername("");
                contact.setBio("");
            } else {
                contact.setUsername(user.getUsername());
                contact.setBio(user.getAboutMe());
                contact.setUserId(user.getUserId());
                for (User u : followingList) {
                    if (emails.get(i).equals(u.getEmail())) {
                        contact.setStatus("Following");
                        break;
                    }
                }
            }
            contactList.add(contact);
        }

        return contactList;
    }
}

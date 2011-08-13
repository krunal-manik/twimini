package twitter.controllers;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;
import org.apache.commons.lang.StringEscapeUtils;
import org.hsqldb.Session;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.mail.MailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import twitter.models.Tweet;
import twitter.models.User;
import twitter.services.Email;
import twitter.services.Follow;
import twitter.services.UserAuthentication;
import twitter.services.UserTweetList;

import javax.servlet.http.HttpSession;
import javax.swing.plaf.multi.MultiViewportUI;
import javax.xml.transform.Source;
import java.io.*;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 7/1/11
 * Time: 11:16 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class UserController {

    @Autowired
    Email emailSender;

    public UserController() {
    }
	public static boolean isValidEmailAddress(String emailAddress)
	{
		String  expression=""+
			"(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t]"+
			")+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:"+
			"\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:("+
			"?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?["+
			"\\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\0"+
			"31]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\"+
			"](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+"+
			"(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:"+
			"(?:\\r\\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z"+
			"|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)"+
			"?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\"+
			"r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?["+
			"\\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)"+
			"?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t]"+
			")*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?["+
			"\\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*"+
			")(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t]"+
			")+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)"+
			"*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+"+
			"|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r"+
			"\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:"+
			"\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t"+
			"]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031"+
			"]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\]("+
			"?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?"+
			":(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?"+
			":\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)|(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?"+
			":(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?"+
			"[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*:(?:(?:\\r\\n)?[ \\t])*(?:(?:(?:[^()<>@,;:\\\\\".\\[\\]"+
			"\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|"+
			"\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>"+
			"@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\""+
			"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t]"+
			")*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\"+
			"\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?"+
			":[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\["+
			"\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\n)?[ \\t])*))*|(?:[^()<>@,;:\\\\\".\\[\\] \\000-"+
			"\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|("+
			"?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)?[ \\t])*(?:@(?:[^()<>@,;"+
			":\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[(["+
			"^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\""+
			".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\"+
			"]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\"+
			"[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\"+
			"r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\]"+
			"\\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]"+
			"|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?(?:[^()<>@,;:\\\\\".\\[\\] \\0"+
			"00-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\"+
			".|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\n)?[ \\t])*(?:[^()<>@,"+
			";:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?"+
			":[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*))*@(?:(?:\\r\\n)?[ \\t])*"+
			"(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\"."+
			"\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t])*(?:["+
			"^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]"+
			"]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:(?:\\r\\n)?[ \\t])*)(?:,\\s*("+
			"?:(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\"+
			"\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:("+
			"?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=["+
			"\\[\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t"+
			"])*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t"+
			"])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?"+
			":\\.(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|"+
			"\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*|(?:"+
			"[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\"+
			"]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)*\\<(?:(?:\\r\\n)"+
			"?[ \\t])*(?:@(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\""+
			"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)"+
			"?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>"+
			"@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*(?:,@(?:(?:\\r\\n)?["+
			 "\\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,"+
			";:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:\\r\\n)?[ \\t]"+
			")*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\"+
			"\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*)*:(?:(?:\\r\\n)?[ \\t])*)?"+
			"(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\[\"()<>@,;:\\\\\"."+
			"\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])*)(?:\\.(?:(?:"+
			"\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z|(?=[\\["+
			"\"()<>@,;:\\\\\".\\[\\]]))|\"(?:[^\\\"\\r\\\\]|\\\\.|(?:(?:\\r\\n)?[ \\t]))*\"(?:(?:\\r\\n)?[ \\t])"+
			"*))*@(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])"+
			"+|\\Z|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*)(?:\\"+
			".(?:(?:\\r\\n)?[ \\t])*(?:[^()<>@,;:\\\\\".\\[\\] \\000-\\031]+(?:(?:(?:\\r\\n)?[ \\t])+|\\Z"+
			"|(?=[\\[\"()<>@,;:\\\\\".\\[\\]]))|\\[([^\\[\\]\\r\\\\]|\\\\.)*\\](?:(?:\\r\\n)?[ \\t])*))*\\>(?:("+
			"?:\\r\\n)?[ \\t])*))*)?;\\s*)";

		CharSequence inputStr = emailAddress;

		Pattern pattern = Pattern.compile(expression,Pattern.CASE_INSENSITIVE);
		Matcher matcher = pattern.matcher(inputStr);

		return matcher.matches();
	}

    public static String validateName( String name ) {
        for(int i=0;i<name.length();i++)
            if( !(Character.isLetter(name.charAt(i)) || name.charAt(i) == ' ') )
                return "Name should only contain charcters a-z,' ',A-Z";
        return " ";
    }

    public static String validateUsername( String username ) {
        System.out.println( username );
        for(int i=0;i<username.length();i++)
            if( !(Character.isLowerCase(username.charAt(i)) ||
                    username.charAt(i) == '-' ||
                    Character.isDigit(username.charAt(i)) ) )
                return "Username should only contain characters a-z,'-',0-9";
        return " ";
    }

    public static String validatePassword( String password ) {
        if( password.length() < 6 )
            return "Password length should be minimum 6 characters";
        String specialCharacters = "\"/\\#%;:\'~`<>";
        for(int i=0;i<password.length();i++)
            if( specialCharacters.indexOf(password.charAt(i)) != -1 )
                return specialCharacters + " not allowed for password";
        return " ";
    }

    public static String validateEmailAddress( String email ) {
        if( isValidEmailAddress(email) )
            return " ";
        return "Invalid email address";
    }


    @RequestMapping(value = "/" )
    public ModelAndView landingPageGet(HttpSession session) {
        if (session.getAttribute("username") == null)
            return new ModelAndView("/login");
        return TweetController.tweetsList(session);
    }

    @RequestMapping( value = "/login" )
    public ModelAndView loginGet(HttpSession session) {
        ModelAndView mv = new ModelAndView("/login-new-attempt");
        if( session.getAttribute("username") != null )
            mv.setViewName("redirect:/");
        return mv;
    }

    @RequestMapping( value = "/login" , method = RequestMethod.POST )
    public ModelAndView login(@RequestParam String username , @RequestParam String password , HttpSession session) {
        if( session.getAttribute("username") != null ) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/login-new-attempt");
        String usernameError = validateUsername(username);
        User user = UserAuthentication.authenticateUser(username,password);
        if( user != null && usernameError.equals(" ") ) {
            session.setAttribute("userId" , user.getUserId());
            session.setAttribute("username",user.getUsername());
            mv.setViewName("redirect:/");
        } else {
            mv.addObject("usernameError",usernameError);
            mv.addObject("loginError","Invalid username/password combination");
        }
        return mv;


    }
    @RequestMapping(value ="/", method = RequestMethod.POST)
    public ModelAndView landingPage(@RequestParam("username") String username,
                              @RequestParam("password") String password,
                              HttpSession session) {

        if (session.getAttribute("username") != null) {
            return TweetController.tweetsList(session);
        }

        ModelAndView mv = new ModelAndView("/login");
        User currentUser = UserAuthentication.authenticateUser(username, password);
        if (currentUser == null) {
            mv.addObject("message", "Invalid password");
            return mv;
        }
        session.setAttribute("username", currentUser.getUsername());
        session.setAttribute("userId", currentUser.getUserId());
        mv.setViewName("redirect:/");
        return mv;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public ModelAndView signUpGet(HttpSession session) {
        ModelAndView mv = new ModelAndView("/register");
        if( session.getAttribute("username") != null )
            mv.setViewName("redirect:/");
        return mv;
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public ModelAndView signup(@RequestParam("username") String username,
                                 @RequestParam("password") String password,
                                 @RequestParam("confirm_password") String confirmPassword,
                                 @RequestParam("name") String name,
                                 @RequestParam("email") String email,
                                 HttpSession session) {
        if( session.getAttribute("username") != null ) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/");
            return mv;
        }
        String usernameError = validateUsername(username);
        String nameError = validateName(name);
        String passwordError = validatePassword(password);
        String emailError = validateEmailAddress(email);
        String passwordsError = password.equals(confirmPassword) ? " " : "Passwords don't match";
        boolean noErrors = usernameError.equals(" ") && nameError.equals(" ") && passwordError.equals(" ") && emailError.equals( " " ) && passwordsError.equals(" ");
        if( !noErrors ) {
            ModelAndView mv = new ModelAndView("/register");
            mv.addObject("usernameError" , usernameError);
            mv.addObject("emailError" , emailError);
            mv.addObject("passwordError" , passwordError);
            mv.addObject("nameError" , nameError);
            mv.addObject("passwordsError" , passwordsError);
            mv.addObject("name",name);
            mv.addObject("username",username);
            mv.addObject("email",email);
            return mv;
        }
        String registerToken = getRandomToken();
        UserAuthentication.registerTemporaryUser(username, password, name, email, registerToken);
        emailSender.sendMail("manikkrunal@gmail.com", email, "Activate your twitter account",
                String.format("Hi %s!\n" +
                        "To start tweeting, just click on the link below to activate your account: " +
                        "http://localhost:8080/activateAccount?token=%s\n" +
                        "Regards,\nTeam Twitter", name, registerToken));

        ModelAndView mv = new ModelAndView("/activate");
        return mv;
    }

    @RequestMapping("/activate")
    public ModelAndView getActivatePage() {
        return new ModelAndView("/activate");
    }

    @RequestMapping(value = "/activateAccount", method = RequestMethod.GET)
    public ModelAndView activateAccount(String token, HttpSession session) {
        User user = UserAuthentication.makeUserPermanent(token);
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getUserId());
        String session_userId = session.getAttribute("userId").toString();
        ModelAndView mv = new ModelAndView("/login");
        mv.setViewName("redirect:/");
        return mv;
        /*
        mv.addObject("followerList", 0);
        mv.addObject("followedList", 0);
        if (session.getAttribute("userId") != null) {
            mv = new ModelAndView("/tweet");
            session_userId = session.getAttribute("userId").toString();
            mv.addObject("allUserList", Follow.allUsersList(session_userId));
        }
        mv.setViewName("redirect:/");
        return mv;*/
    }

    @RequestMapping("/logout")
    public ModelAndView logout(HttpSession session) {
        session.invalidate();
        ModelAndView mv = new ModelAndView("/login");
        return mv;
    }

    @RequestMapping("/search_user")
    @ResponseBody
    public List<User> search_user(HttpSession session, @RequestParam String pattern) {
        System.out.println("session userId is null -> " + session.getAttribute("userId") == null);
        List<User> userList = Follow.allUsersListbyPattern(pattern, session.getAttribute("userId").toString());
        return userList;
    }

    @RequestMapping("/{username}")
    public ModelAndView getUserProfile(@PathVariable String username, HttpSession session) {

        User urlMappedUser = UserAuthentication.getUserByUsername(username);
        if (urlMappedUser == null) {
            ModelAndView mv = new ModelAndView("/error404");
            return mv;
        }
        String userId = String.valueOf(urlMappedUser.getUserId());
        ModelAndView mv = new ModelAndView("/profile");
        String session_userId = null;
        if (session.getAttribute("userId") != null) {
            session_userId = session.getAttribute("userId").toString();
            mv.addObject("allUserList", Follow.allUsersList(session_userId));
            if (Follow.ifFollow(userId, session_userId)) {
                mv.addObject("followStatus", "Follow");
            } else {
                mv.addObject("followStatus", "Unfollow");
            }
        }
        List<Tweet> userTweetList = UserTweetList.userTweetList(userId, session_userId);
        for (Tweet tweet : userTweetList)
            tweet.setTweet(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(tweet.getTweet())));
        List<Tweet> userMentionList = UserTweetList.mentionFeed(userId, session_userId);
        for (Tweet tweet : userMentionList)
            tweet.setTweet(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(tweet.getTweet())));
        List<Tweet> userFavoriteList = UserTweetList.favoritesFeed(userId, session_userId);
        for (Tweet tweet : userFavoriteList)
            tweet.setTweet(StringEscapeUtils.escapeJavaScript(StringEscapeUtils.escapeHtml(tweet.getTweet())));
        mv.addObject("userTweets", userTweetList);
        mv.addObject("userMentionList", userMentionList);
        mv.addObject("userFavoriteList", userFavoriteList);
        mv.addObject("followerList", Follow.getFollowerListLimited(userId));
        mv.addObject("followedList", Follow.getFollowedListLimited(userId));
        mv.addObject("followerCount", Follow.getFollowerList(userId).size());
        mv.addObject("followingCount", Follow.getFollowedList(userId).size());
        mv.addObject("currentUsername", username);
        mv.addObject("currentUserId", userId);
        mv.addObject("currentEmail", urlMappedUser.getEmail());
        mv.addObject("currentName", urlMappedUser.getName());
        return mv;
    }

    @RequestMapping("/{username}/followers")
    public ModelAndView getUserFollowers(@PathVariable String username, HttpSession session) {
        String loggedInUserId = session.getAttribute("username") != null ? session.getAttribute("username").toString() : null;
        User urlMappedUser = UserAuthentication.getUserByUsername(username);
        User loggedInUser = UserAuthentication.getUserByUsername(loggedInUserId);
        if (urlMappedUser == null) {
            ModelAndView mv = new ModelAndView("/error404");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/user-list");
        String userId = String.valueOf(urlMappedUser.getUserId());
        List<User> followerList = Follow.getFollowerList(userId);
        List<User> loggedInUsersFollowingList = new ArrayList<User>();
        if (loggedInUser != null)
            loggedInUsersFollowingList = Follow.getFollowedList("" + loggedInUser.getUserId());
        for (User user : followerList) {
            for (User loggedInUserIsFollowing : loggedInUsersFollowingList) {
                if (user.getUserId() == loggedInUserIsFollowing.getUserId()) {
                    user.setFollowStatus("Following");
                    break;
                }
            }
        }
        mv.addObject("userList", followerList);
        mv.addObject("username", username);
        mv.addObject("message", username + "\'s follower list");
        System.out.println("done");
        return mv;
    }

    @RequestMapping("/{username}/followings")
    public ModelAndView getUserFollowings(@PathVariable String username, HttpSession session) {
        String loggedInUserId = session.getAttribute("username") != null ? session.getAttribute("username").toString() : null;
        User urlMappedUser = UserAuthentication.getUserByUsername(username);
        User loggedInUser = UserAuthentication.getUserByUsername(loggedInUserId);
        if (urlMappedUser == null) {
            ModelAndView mv = new ModelAndView("/error404");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/user-list");

        String userId = String.valueOf(urlMappedUser.getUserId());
        List<User> followedList = Follow.getFollowedList(userId);
        List<User> loggedInUsersFollowingList = new ArrayList<User>();
        if (loggedInUser != null)
            loggedInUsersFollowingList = Follow.getFollowedList("" + loggedInUser.getUserId());
        for (User user : followedList) {
            for (User loggedInUserIsFollowing : loggedInUsersFollowingList) {
                if (user.getUserId() == loggedInUserIsFollowing.getUserId()) {
                    user.setFollowStatus("Following");
                    break;
                }
            }
        }

        mv.addObject("userList", followedList);
        mv.addObject("username", username);
        mv.addObject("message", username + "\'s following list");
        return mv;
    }

    @RequestMapping(value = "/account/reset_password")
    public ModelAndView resetPassword() {
        return new ModelAndView("/reset_password");
    }


    @RequestMapping(value = "/email", method = RequestMethod.POST)
    public ModelAndView test(@RequestParam String email,
                             @RequestParam("recaptcha_challenge_field") String challenge,
                             @RequestParam("recaptcha_response_field") String response,
                             HttpSession session) {
        String remoteAddr = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest().getRemoteAddr();
        System.out.println(remoteAddr);
        System.out.println(challenge);
        System.out.println(response);
        ReCaptchaImpl reCaptcha = new ReCaptchaImpl();

        // Probably don't want to hardcode your private key here but
        // just to get it working is OK...
        reCaptcha.setPrivateKey("6Lfwr8YSAAAAADODckf9OR9xR9CXHD3btgFe7mLN");

        ReCaptchaResponse reCaptchaResponse =
                reCaptcha.checkAnswer(remoteAddr, challenge, response);

        if (reCaptchaResponse.isValid()) {
            System.out.println("VALID !!!!!!!!!!");
            User user = UserAuthentication.getPassword(email);
            if (user == null) {
                return new ModelAndView("/error404");
            }
            String token = getRandomToken();
            UserAuthentication.insertToken(user, token);
            emailSender.sendMail("manikkrunal@gmail.com", email, "Password recovery",
                    String.format("Hi %s!\n" +
                            "It seems you have lost your password.But no worries, you can reset your password." +
                            "Just click the link below to activate." +
                            "http://localhost:8080/%s/changePassword?token=%s\n" +
                            "\nRegards,\n" +
                            "Team Twitter\n", user.getUsername(), user.getUsername(), token));
            ModelAndView mv = new ModelAndView("/login");
            return mv;
        } else {
            System.out.println("Invalid !!!!");
            ModelAndView mv = new ModelAndView("reset_password");
            return mv;
        }
    }

    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.GET)
    public ModelAndView tokenQueryString(@PathVariable String username, String token) {

        boolean tokenExists = UserAuthentication.tokenExists(username, token);
        if (!tokenExists) {
            return new ModelAndView("/error404");
        }
        ModelAndView mv = new ModelAndView("/confirm_passwords");
        mv.addObject("username", username);
        mv.addObject("token", token);
        return mv;
    }

    @RequestMapping(value = "/{username}/updatePassword")
    public ModelAndView updatePassword(@PathVariable String username, @RequestParam String password, @RequestParam String token) {

        UserAuthentication.deleteToken(username, token);
        UserAuthentication.updatePassword(username, password);
        ModelAndView mv = new ModelAndView("/login");
        mv.setViewName("redirect:/login");
        return mv;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search(String pattern, HttpSession session) {
        if (pattern == null || pattern.equals("")) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/");
            return mv;
        }
        String loggedInUserId = session.getAttribute("username") != null ? session.getAttribute("username").toString() : null;
        User loggedInUser = UserAuthentication.getUserByUsername(loggedInUserId);
        List<User> userList = Follow.allUsersListContainingSubstring(pattern, session.getAttribute("userId").toString());

        List<User> loggedInUsersFollowingList = new ArrayList<User>();
        if (loggedInUser != null)
            loggedInUsersFollowingList = Follow.getFollowedList("" + loggedInUser.getUserId());
        for (User user : userList) {
            for (User loggedInUserIsFollowing : loggedInUsersFollowingList) {
                if (user.getUserId() == loggedInUserIsFollowing.getUserId()) {
                    user.setFollowStatus("Following");
                    break;
                }
            }
        }

        ModelAndView mv = new ModelAndView("/user-list");
        mv.addObject("userList", userList);
        mv.addObject("message", "Search results for " + pattern);
        return mv;
    }

    public static String getRandomToken() {
        char a[] = new char[62];
        int c = 0;
        for (char i = 'a'; i <= 'z'; i++) a[c++] = i;
        for (char i = 'A'; i <= 'Z'; i++) a[c++] = i;
        for (char i = '0'; i <= '9'; i++) a[c++] = i;

        int left = a.length - 1;
        for (int i = 0; i < a.length; i++) {
            int num = ((int) (Math.random() * 150)) % (left + 1);
            swap(a, i, num);
            left--;
        }
        return new String(a);
    }

    public static void swap(char a[], int i, int j) {
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }
}
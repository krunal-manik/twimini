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
import twitter.EmailThread;
import twitter.models.Tweet;
import twitter.models.User;
import twitter.services.*;

import javax.annotation.PostConstruct;
import javax.mail.Message;
import javax.servlet.http.HttpServletRequest;
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


    public static Email emailSender;

    @Autowired
    public UserController(Email email) {
        emailSender = email;
    }

    @PostConstruct
    public void instantiateEmailThread() {
        EmailThread emailThread = new EmailThread(emailSender);
        emailThread.start();
    }

	private static boolean isValidEmailAddress(String emailAddress)
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

    private static String validateName( String name ) {
        if( name == null || name.equals("") )
            return "Name cannot be blank";
        for(int i=0;i<name.length();i++)
            if( !(Character.isLetter(name.charAt(i)) || name.charAt(i) == ' ') )
                return "Name should only contain charcters a-z,' ',A-Z";
        return " ";
    }

    private static String validateUsername( String username ) {
        if( username == null || username.equals("") )
            return "Username cannot be blank";
        if( !Character.isLowerCase(username.charAt(0)))
            return "Username should start with a lower case letter";
        for(int i=0;i<username.length();i++)
            if( !(Character.isLowerCase(username.charAt(i)) ||
                    username.charAt(i) == '-' ||
                    Character.isDigit(username.charAt(i)) ) )
                return "Username should only contain characters a-z,'-',0-9";
        return " ";
    }

    private static String validatePassword( String password ) {
        if( password.length() < 6 )
            return "Password length should be minimum 6 characters";
        String specialCharacters = "\"/\\#%;:\'~`<>";
        for(int i=0;i<password.length();i++)
            if( specialCharacters.indexOf(password.charAt(i)) != -1 )
                return specialCharacters + " not allowed for password";
        return " ";
    }

    private static String validateEmailAddress( String email ) {
        if( isValidEmailAddress(email) )
            return " ";
        return "Invalid email address";
    }


    @RequestMapping(value = "/" )
    public ModelAndView landingPageGet(HttpSession session) {
        if (session.getAttribute("username") == null)
            return new ModelAndView("/login");
        ModelAndView mv = new ModelAndView("/tweet");
        String username = session.getAttribute("username").toString();
        String userId = session.getAttribute("userId").toString();
        mv.addObject("tweetCount",UserTweetList.getUserTweetsCount(userId));
        mv.addObject("followerList", Follow.getFollowerListLimited(userId));
        mv.addObject("followedList", Follow.getFollowedListLimited(userId));
        mv.addObject("followerCount", Follow.getFollowerCount(userId));
        mv.addObject("followingCount", Follow.getFollowingCount(userId));
        mv.addObject("allUserList", Follow.allUsersList(userId));
        mv.addObject("currentUsername",username);
        mv.addObject("currentUserId",userId);
        User currentUser = UserAuthentication.getUserByUsername(username);
        mv.addObject("aboutCurrentUser",currentUser.getAboutMe());
        mv.addObject("name",currentUser.getName());
       return mv;
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
            return new ModelAndView(){{
                setViewName("redirect:/");
            }};
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
        boolean usernameAvailable = UserAuthentication.checkUsernameAvailabiltyForSignUp(username);
        boolean emailExists = UserAuthentication.getUserByEmail(email) != null ;
        System.out.println( "Username Available : " + usernameAvailable  );
        System.out.println( "Email exists : " + emailExists );

        if( !noErrors || !usernameAvailable || emailExists ) {
            ModelAndView mv = new ModelAndView("/register");
            if( !usernameError.equals(" ") )
                mv.addObject("usernameError" , usernameError);
            if( !emailError.equals(" ") )
                mv.addObject("emailError" , emailError);
            mv.addObject("passwordError" , passwordError);
            mv.addObject("nameError" , nameError);
            mv.addObject("passwordsError" , passwordsError);
            mv.addObject("name",name);
            mv.addObject("username",username);
            mv.addObject("email",email);
            if( !usernameAvailable && usernameError.equals(" ") )
                mv.addObject("usernameError","Username not available");
            if( emailExists && emailError.equals(" ") )
                mv.addObject("emailError","Email already exists");
            return mv;
        }

        String registerToken = getRandomToken();
        String message = String.format("Hi %s!\n" +
                        "To start tweeting, just click on the link below to activate your account: " +
                        "http://localhost:8080/activateAccount?token=%s\n" +
                        "Regards,\nTeam Twitter", name, registerToken);

        UserAuthentication.registerTemporaryUser(username, password, name, email, registerToken ,
                "Activate your twitter account" , message );


        ModelAndView mv = new ModelAndView("/message");
        mv.addObject("message","An activation mail has been sent to your account.Please click on the link in the mail to activate your account. Your account will be activated once you click that link.Please wait for a while if you don't receive the mail instantly.");
        return mv;
    }


    @RequestMapping(value = "/activateAccount", method = RequestMethod.GET)
    public ModelAndView activateAccount(String token, HttpSession session) {
        User user = UserAuthentication.makeUserPermanent(token);
        if( user == null ) {
            return new ModelAndView("/error404");
        }
        session.setAttribute("username", user.getUsername());
        session.setAttribute("userId", user.getUserId());
        String session_userId = session.getAttribute("userId").toString();
        ModelAndView mv = new ModelAndView("/message");
        mv.addObject("message","Your twitter account has been activated successfully.You can now login and start tweeting.");
        return mv;
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
        mv.addObject("tweetCount",UserTweetList.getUserTweetsCount(userId));
        mv.addObject("followerList", Follow.getFollowerListLimited(userId));
        mv.addObject("followedList", Follow.getFollowedListLimited(userId));
        mv.addObject("followerCount", Follow.getFollowerCount(userId));
        mv.addObject("followingCount", Follow.getFollowingCount(userId));
        mv.addObject("currentUsername", username);
        mv.addObject("currentUserId", userId);
        User currentUser = UserAuthentication.getUserByUsername(username);
        mv.addObject("aboutCurrentUser",currentUser.getAboutMe());
        mv.addObject("name",currentUser.getName());
        return mv;
    }

    @RequestMapping("/{username}/followers")
    public ModelAndView getUserFollowers(@PathVariable String username, HttpSession session) {
        String loggedInUserId = session.getAttribute("username") != null ? session.getAttribute("username").toString() : null;
        User urlMappedUser = UserAuthentication.getUserByUsername(username);
        User loggedInUser = UserAuthentication.getUserByUsername(loggedInUserId);
//        if (urlMappedUser == null) {
//            ModelAndView mv = new ModelAndView("/error404");
//            return mv;
//        }
//        ModelAndView mv = new ModelAndView("/user-list");
//        String userId = String.valueOf(urlMappedUser.getUserId());
//        List<User> followerList = Follow.getFollowerList(userId);
//        List<User> loggedInUsersFollowingList = new ArrayList<User>();
//        if (loggedInUser != null)
//            loggedInUsersFollowingList = Follow.getFollowedList("" + loggedInUser.getUserId());
//        for (User user : followerList) {
//            for (User loggedInUserIsFollowing : loggedInUsersFollowingList) {
//                if (user.getUserId() == loggedInUserIsFollowing.getUserId()) {
//                    user.setFollowStatus("Following");
//                    break;
//                }
//            }
//        }
//        mv.addObject("userList", followerList);
//        mv.addObject("username", username);
//        mv.addObject("message", username + "\'s follower list");
        ModelAndView mv = new ModelAndView("/user-list");
        mv.addObject("title", "follower");
        mv.addObject("currentUserId", urlMappedUser.getUserId());
        System.out.println("done");
        return mv;
    }

    @RequestMapping("/{username}/followings")
    public ModelAndView getUserFollowings(@PathVariable String username, HttpSession session) {
        String loggedInUserId = session.getAttribute("username") != null ? session.getAttribute("username").toString() : null;
        User urlMappedUser = UserAuthentication.getUserByUsername(username);
        User loggedInUser = UserAuthentication.getUserByUsername(loggedInUserId);
//        if (urlMappedUser == null) {
//            ModelAndView mv = new ModelAndView("/error404");
//            return mv;
//        }

        ModelAndView mv = new ModelAndView("/user-list");

//        String userId = String.valueOf(urlMappedUser.getUserId());
//        List<User> followedList = Follow.getFollowedList(userId);
//        List<User> loggedInUsersFollowingList = new ArrayList<User>();
//        if (loggedInUser != null)
//            loggedInUsersFollowingList = Follow.getFollowedList("" + loggedInUser.getUserId());
//        for (User user : followedList) {
//            for (User loggedInUserIsFollowing : loggedInUsersFollowingList) {
//                if (user.getUserId() == loggedInUserIsFollowing.getUserId()) {
//                    user.setFollowStatus("Following");
//                    break;
//                }
//            }
//        }
//
//        mv.addObject("userList", followedList);
//        mv.addObject("username", username);
//        mv.addObject("message", username + "\'s following list");

        mv.addObject("title", "following");
        mv.addObject("currentUserId", urlMappedUser.getUserId());
        return mv;
    }

    @RequestMapping(value = "/reset_password")
    public ModelAndView resetPassword(HttpSession session) {
        if( session.getAttribute("userId") == null )
            return new ModelAndView("/reset-password");
        return new ModelAndView(){{
            setViewName("redirect:/");
        }};
    }


    @RequestMapping(value = "/reset_password", method = RequestMethod.POST)
    public ModelAndView test(@RequestParam String email,
                             @RequestParam("recaptcha_challenge_field") String challenge,
                             @RequestParam("recaptcha_response_field") String response,
                             HttpSession session) {
        if( session.getAttribute("userId") != null ) {
            return new ModelAndView() {{
                setViewName("redirect:/");
            }};
        }
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
            User user = UserAuthentication.getUserByEmail(email);
            if (user == null) {
                return new ModelAndView("/reset-password"){{
                    addObject( "errorMessage" , "No user registered with this email id" );
                }};
            }
            String token = getRandomToken();
            String message = String.format("Hi %s!\n" +
                    "It seems you have lost your password.But no worries, you can reset your password." +
                    "Just click the link below to reset your password." +
                    "http://localhost:8080/%s/changePassword?token=%s\n" +
                    "\nRegards,\n" +
                    "Team Twitter\n", user.getUsername(), user.getUsername(), token);

            UserAuthentication.insertToken(user, token);
            EmailService.sendResetPasswordMail( "manikkrunal@gmail.com" , email , "Twitter Password recovery" , message );
            ModelAndView mv = new ModelAndView("/message");
            mv.addObject( "message", "Reset password instructions have been mailed to you.Please check your inbox for the mail.Please wait for a while if you don't receive the mail instantly." );
            return mv;
        } else {
            System.out.println("Invalid !!!!");
            ModelAndView mv = new ModelAndView("/reset-password");
            mv.addObject( "errorMessage" , "Captcha check failed" );
            return mv;
        }
    }

    @RequestMapping(value = "/{username}/changePassword", method = RequestMethod.GET)
    public ModelAndView tokenQueryString(@PathVariable String username, @RequestParam String token,String passwordError,String confirmPasswordError) {

        boolean tokenExists = UserAuthentication.tokenExists(username, token);
        if (!tokenExists) {
            return new ModelAndView("/error404");
        }

        ModelAndView mv = new ModelAndView("/confirm_passwords");
        mv.addObject("username", username);
        mv.addObject("token", token);
        mv.addObject("passwordError",passwordError);
        mv.addObject("confirmPasswordError",confirmPasswordError);
        return mv;
    }

    @RequestMapping(value = "/{username}/updatePassword" , method=RequestMethod.POST)
    public ModelAndView updatePassword(@PathVariable String username, @RequestParam String password, @RequestParam String token,@RequestParam String confirm_password) {

        boolean passwordsMatch = password != null && password.equals(confirm_password);
        boolean passwordLength = password.length() >= 6;
        boolean passwordNotEmpty = password != null && !password.equals("");

        boolean noError = passwordsMatch && passwordLength && passwordNotEmpty;

        if( !noError ) {
            ModelAndView mv = new ModelAndView("/confirm_passwords");
            if( !passwordsMatch ) {
                mv.addObject( "confirmPasswordError" , "Passwords don't match" );
            }
            if( !passwordLength ) {
                mv.addObject( "passwordError" , "Password has to be atleast 6 characters" );
            }
            mv.setViewName( String.format("redirect:/%s/changePassword?token=%s",username,token));
            return mv;
        }

        UserAuthentication.deleteToken(username, token);
        UserAuthentication.updatePassword(username, password);
        ModelAndView mv = new ModelAndView("/message");
        mv.addObject( "message", "Password changed successfully" );
        return mv;
    }

    @RequestMapping(value = "/{username}/updatePassword" , method=RequestMethod.GET)
    public ModelAndView updatePasswordGet() {
        return new ModelAndView("/error404");
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView search(@RequestParam String pattern) {
        if (pattern == null || pattern.equals("")) {
            ModelAndView mv = new ModelAndView();
            mv.setViewName("redirect:/");
            return mv;
        }

        ModelAndView mv = new ModelAndView("/user-list");
        mv.addObject("message", "Search results for " + pattern);
        mv.addObject("title", "search");
        mv.addObject("currentUserId", pattern);
        //mv.addObject("loggedInUser", follower);
        return mv;
    }

    private static String getRandomToken() {
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

    private static void swap(char a[], int i, int j) {
        char c = a[i];
        a[i] = a[j];
        a[j] = c;
    }
}
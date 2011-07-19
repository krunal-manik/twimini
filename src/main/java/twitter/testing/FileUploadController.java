package twitter.testing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.simple.SimpleJdbcTemplate;
import org.springframework.jdbc.core.support.AbstractLobCreatingPreparedStatementCallback;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.jdbc.support.lob.LobCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.w3c.dom.ranges.RangeException;
import javax.servlet.http.HttpSession;
import javax.tools.JavaCompiler;
import java.io.*;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: rahul.pl
 * Date: 7/5/11
 * Time: 5:43 AM
 * To change this template use File | Settings | File Templates.
 */
@Controller
public class FileUploadController {

    final SimpleJdbcTemplate db;
    public static final String prefixPath = "C:\\Users\\krunal.ma\\twimini\\src\\main\\webapp\\";
    public static final String tempPath = "C://photos/username.jpg";

    @Autowired
    public FileUploadController(SimpleJdbcTemplate db) {
        this.db = db;
    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST)
    public ModelAndView uploadPost(HttpSession session, @RequestParam MultipartFile file) throws Exception {

        try{
            InputStream in = file.getInputStream();
            System.out.println( file.getName() );
            System.out.println( file.getOriginalFilename() );
            OutputStream out = new BufferedOutputStream( new FileOutputStream( prefixPath + "\\static\\photo\\" + file.getOriginalFilename()  ) );

            int bytesRead = 0;
            byte[] buffer = new byte[8192];

            while ( (bytesRead = in.read(buffer, 0, 8192)) != -1 ) {
                    out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();
        }
        catch( Exception ex ) {
            System.out.println( "Bug in image upload :((" );
            ex.printStackTrace();
        }

        ModelAndView mv = new ModelAndView("/testing");
        mv.addObject( "image" , "/static/photo/" + file.getOriginalFilename() );
        return mv;
    }
}

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
public class testing_image_upload {

    final SimpleJdbcTemplate db;

    @Autowired
    public testing_image_upload(SimpleJdbcTemplate db) {
        this.db = db;
    }

    @RequestMapping("/login_signup")
    public ModelAndView login_test(HttpSession session) {
        ModelAndView mv = new ModelAndView();
        return mv;
    }


    @RequestMapping("/testing")
    public ModelAndView upload(HttpSession session) {
        Map<String, Object> a = db.queryForMap("select * from images where id = 1");
        OutputStream os= new ByteArrayOutputStream();
        try {
            os.write((byte[]) a.get("image"));
        } catch (IOException e) {
            System.out.println("HAHA");
        }
        ModelAndView mv = new ModelAndView();
        mv.addObject("image", (byte[]) a.get("image"));
        return mv;
    }

    @RequestMapping(value = "/testing", method = RequestMethod.POST)
    public ModelAndView uploadPost(HttpSession session, @RequestParam final File pic) throws Exception {
        InputStream is = new FileInputStream(pic);
        int length = (int) pic.length();

        /*DefaultLobHandler lobHandler = new DefaultLobHandler();
        System.out.println(pic.getAbsolutePath() + " " + pic.exists());
        final InputStream blobIs = new FileInputStream(pic);
        db.update(
          "INSERT INTO images (image) VALUES (?)",
          new AbstractLobCreatingPreparedStatementCallback(lobHandler) {
              protected void setValues(PreparedStatement ps, LobCreator lobCreator)
                  throws SQLException {
                ps.setLong(1, 1L);
                lobCreator.setBlobAsBinaryStream(ps, 1, blobIs, (int)pic.length());
              }
          }
        );*/
        byte[] bytes = new byte[length];
        int offset = 0;
    int numRead = 0;
    while (offset < bytes.length
           && (numRead=is.read(bytes, offset, bytes.length-offset)) >= 0) {
        offset += numRead;
    }

    // Ensure all the bytes have been read in
    if (offset < bytes.length) {
        throw new IOException("Could not completely read file "+pic.getName());
    }

    // Close the input stream and return bytes
    is.close();
        db.update(
                "INSERT INTO images (image) VALUES (?)", bytes);
        System.out.println("haha");
        ModelAndView mv = new ModelAndView();
        return mv;
    }


}

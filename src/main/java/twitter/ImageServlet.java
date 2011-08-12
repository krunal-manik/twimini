package twitter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: krunal.ma
 * Date: 8/2/11
 * Time: 10:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class ImageServlet extends HttpServlet {

    private String imagePath;
    private static final int DEFAULT_BUFFER_SIZE = 1024 * 5;

    public void init() throws ServletException {
        imagePath = "C:\\Users\\krunal.ma\\Desktop\\photos\\";
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestedImage = request.getPathInfo();
        if (requestedImage == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        File image = new File(imagePath, URLDecoder.decode(requestedImage, "UTF-8"));

        if (!image.exists()) {
            requestedImage = "default-user.jpg";
            image = new File(imagePath, URLDecoder.decode(requestedImage, "UTF-8"));
        }

        String contentType = getServletContext().getMimeType(image.getName());
        if (contentType == null || !contentType.startsWith("image")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        response.reset();
        response.setBufferSize(DEFAULT_BUFFER_SIZE);
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(image.length()));
        response.setHeader("Content-Disposition", "inline; filename=\"" + image.getName() + "\"");

        BufferedInputStream input = null;
        BufferedOutputStream output = null;

        try {
            input = new BufferedInputStream(new FileInputStream(image), DEFAULT_BUFFER_SIZE);
            output = new BufferedOutputStream(response.getOutputStream(), DEFAULT_BUFFER_SIZE);
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
        } catch (Exception ex) {
            System.out.println("Bug in imageServlet :(");
            ex.printStackTrace();
        } finally {
            if (input != null)
                input.close();
            if (output != null)
                output.close();
        }
    }
}

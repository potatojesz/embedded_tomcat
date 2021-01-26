package servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;

@WebServlet(
        name = "DummyServlet",
        urlPatterns = {"/execute"}
)
public class DummyServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain");
        try (Writer writer = response.getWriter()) {
            writer.write("Hello, its your dummy servlet!!!");
            writer.flush();
        }
    }
}
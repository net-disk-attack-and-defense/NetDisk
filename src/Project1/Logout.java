package Project1;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


public class Logout extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession() ;
        session.invalidate();//TODO:未真正销毁ID，因此存在漏洞，如果使用未被销毁的ID且使用post请求可以伪造登录
        response.sendRedirect("index.html");
    }
}

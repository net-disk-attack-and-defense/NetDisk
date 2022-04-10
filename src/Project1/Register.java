package Project1;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Register extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        int SpiderState = 0;
        try {
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "NetDisk/register.html");
            if (uAC.check()) {
                response.sendRedirect("403.html");
                SpiderState = 1;
            } else if (RC.check()) {
                response.sendRedirect("403.html");
                SpiderState = 1;
            }
        } catch (NullPointerException e) {
            response.sendRedirect("403.html");
            SpiderState = 1;
        }
        if (SpiderState==0){
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String email = request.getParameter("email");
            if (username.length() > 20 || username.length() <= 2 || password.length() < 6 || password.length() > 32 || email.length() > 32) {
                response.sendRedirect("register.html");
            } else {
                int register_state;
                try {
                    String path = this.getServletContext().getRealPath("/WEB-INF/classes/DB_Info.properties");
                    DB_Write try01 = new DB_Write(username, password, email, path);
                    register_state = try01.write();
                    switch (register_state) {
                        case 0 -> response.sendRedirect("ServerError.html");
                        case 1 -> response.sendRedirect("EmailExist.html");
                        case 2 -> response.sendRedirect("SignUpSuccess.html");
                        case 3 -> response.sendRedirect("SignUpFail.html");
                    }
                } catch (Exception e) {
                    PrintWriter out = response.getWriter();
                    out.print("<br><h1 style=\"text-align:center;font-size:2.5em;\">配置文件出错，请检查配置文件</h1>");
                }
            }
        }
    }
}








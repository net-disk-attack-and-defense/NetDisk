package Project1;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        int SpiderState = 0;
        try {
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/login.html");
            if (uAC.check()) {
                response.sendRedirect("403.html");//user_agent
                SpiderState = 1;
            } else if (RC.check()) {
                response.sendRedirect("403.html");//referer
                SpiderState = 1;
            }
        } catch (NullPointerException e) {
            response.sendRedirect("403.html");
            SpiderState = 1;
        }
        if (SpiderState==0) {
            String email = request.getParameter("email");
            String password = request.getParameter("password");
            //TODO 没有验证账户密码长度，尚不知有无问题
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String path = this.getServletContext().getRealPath("/WEB-INF/classes/DB_Info.properties");
                Email_Check check = new Email_Check(email, path);
                if (check.check()) {
                    DB_Connect connect = new DB_Connect();
                    conn = connect.connect(path);
                    String sql = "select*from mypan.users where email=? and passwd=?";//?是占位符
                    ps = conn.prepareStatement(sql);
                    //给占位符？传值，第一个问号下标是1，jdbc的下标从1开始
                    ps.setString(1, email);
                    ps.setString(2, password);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        HttpSession session = request.getSession();
                        if (!session.isNew()) {  //如果session不是新的，那么失效上一个session并再次创建
                            session.invalidate();//TODO 此方法只是删除ID属性值，未真正删除ID，尚不知会不会有问题
                            session = request.getSession();
                        }
                        session.setMaxInactiveInterval(60);//session时长设置为5分钟
                        session.setAttribute("username", rs.getString("name"));
                        response.sendRedirect("SFP");
                        //request.getRequestDispatcher("SUI").forward(request,response);
                    } else {
                        response.sendRedirect("wrongpassword.html"); //重定向到密码错误页面
                    }
                } else {
                    response.sendRedirect("wrongusername.html"); //重定向到账号错误页面
                }
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (rs != null) {
                        rs.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (ps != null) {
                        ps.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    if (conn != null) {
                        conn.close();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}

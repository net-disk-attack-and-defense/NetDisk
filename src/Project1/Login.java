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
        int SpiderState = 0;
        try {
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/login.html");
            if (uAC.check()) {
                response.sendRedirect("403.html");//user_agent
                SpiderState = 1;
                System.out.println("User Agent测试不通过");
            } else if (RC.check()) {
                response.sendRedirect("403.html");//referer
                SpiderState = 1;
                System.out.println("Referer测试不通过");
            }
        } catch (NullPointerException e) {
            response.sendRedirect("403.html");
            SpiderState = 1;
        }
        if (SpiderState==0 && request.getParameter("password")!=null && request.getParameter("email")!=null) {
            Connection conn = null;
            PreparedStatement ps = null;
            ResultSet rs = null;
            try {
                String email = request.getParameter("email");
                String password = request.getParameter("password");
                //TODO 没有验证账户密码长度，尚不知有无问题
                String path = this.getServletContext().getRealPath("/WEB-INF/classes/DB_Info.properties");//无用
                Email_Check check = new Email_Check(email, path);
                if (email.equals("ROOT@ROOT") && password.equals("QxbS2*F4y15J78=TrsB!3mY0-+nv.uZD")) { //如果是管理员用户登录,管理员密码暂且写死为一个强密码
                    //给予session
                    HttpSession session = request.getSession();
                    if (!session.isNew()) {  //如果session不是新的，那么失效上一个session并再次创建
                        session.invalidate();//TODO 此方法只是删除ID属性值，未真正删除ID，尚不知会不会有问题
                        session = request.getSession();
                    }
                    //管理员session有效时长设置为默认的关闭浏览器才销毁，不知是否安全
                    session.setAttribute("username", "ROOT");
                    session.setAttribute("email", "ROOT@ROOT");
                    response.sendRedirect("RootPage.html"); //重定向至管理员页
                } else {
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
                            session.setMaxInactiveInterval(300);//session时长设置为5分钟
                            session.setAttribute("username", rs.getString("name"));
                            session.setAttribute("email", email);
                            response.sendRedirect("SFP");
                            //request.getRequestDispatcher("SUI").forward(request,response);
                        } else response.sendRedirect("wrongpassword.html"); //重定向到密码错误页面，这个页面不归入ErrorPage1.html页面，防止为登录用户获得session
                    } else response.sendRedirect("wrongusername.html"); //重定向到账号错误页面，这个页面不归入ErrorPage1.html页面，防止为登录用户获得session
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
        } else response.sendError(403);
    }
}

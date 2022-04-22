package Project1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RootDeleteUser extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int SpiderState = 0;
        try {
            System.out.println("RDU:"+request.getHeader("referer"));
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/RSU");
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
        if (SpiderState == 0){
            HttpSession session = request.getSession(false);
            if (session != null) {
                if (session.getAttribute("username")!=null) {
                    if (session.getAttribute("username").equals("ROOT") && request.getParameter("bt_del")!=null) {
                        System.out.println("要删除的用户邮箱："+request.getParameter("bt_del"));
                        String del_email = request.getParameter("bt_del");
                        Connection conn = null;
                        PreparedStatement ps = null;
                        ResultSet rs = null;
                        String path = this.getServletContext().getRealPath("/WEB-INF/classes/DB_Info.properties");
                        DB_Connect connect = new DB_Connect();
                        try {
                            conn = connect.connect(path);
                            String sql1 = "select*from mypan.users where email=?;";
                            ps = conn.prepareStatement(sql1);
                            ps.setString(1, del_email);
                            rs = ps.executeQuery();
                            if(rs.next()){
                                String sql2 = "DELETE FROM mypan.users WHERE email=?;";
                                ps = conn.prepareStatement(sql2);
                                ps.setString(1, del_email);
                                int delstate = ps.executeUpdate();
                                if (delstate == 1){
                                    String realpath = request.getServletContext().getRealPath("/WEB-INF/");//获取项目真实地址
                                    File file0 = new File(realpath+"File/");
                                    file0.mkdir();
                                    File file1 = new File(realpath+"File/"+del_email+"/");
                                    if (file1.exists()) {
                                        File[] files = file1.listFiles();
                                        for (File file : files) {
                                            file.delete();
                                        }
                                        file1.delete();
                                        if (!file1.exists()) {
                                            System.out.println("删除成功");
                                            response.sendRedirect("RSU");
                                        } else {
                                            System.out.println("用户文件夹删除失败");
                                            response.sendRedirect("RSU");
                                        }
                                    } else {
                                        System.out.println("删除成功");
                                        response.sendRedirect("RSU");
                                    }
                                } else {
                                    System.out.println("删除失败");
                                    response.sendRedirect("RootDeleteFail.html");
                                }
                            }
                        } catch (SQLException | ClassNotFoundException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (ps !=null){
                                    ps.close();
                                }
                            }catch (SQLException e){
                                e.printStackTrace();
                            }
                            try {
                                if (rs !=null){
                                    rs.close();
                                }
                            }catch (SQLException e){
                                e.printStackTrace();
                            }
                            try {
                                if (conn !=null){
                                    conn.close();
                                }
                            }catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                    } else response.sendError(403);
                } else response.sendError(403);
            } else response.sendError(403);
        }
    }
}

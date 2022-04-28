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
import java.text.SimpleDateFormat;
import java.util.Calendar;


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
                        //先验证ip是否被封禁
                        String remoteIP = request.getRemoteAddr();
                        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar = Calendar.getInstance();
                        int hour = calendar.get(Calendar.HOUR_OF_DAY);
                        int minute = calendar.get(Calendar.MINUTE);
                        int permit = 0;
                        DB_Connect connect = new DB_Connect();
                        conn = connect.connect(path);
                        String sql0 = "select*from mypan.ip_forbidden where ip=?";
                        ps = conn.prepareStatement(sql0);
                        //给占位符？传值，第一个问号下标是1，jdbc的下标从1开始
                        ps.setString(1, remoteIP);
                        rs = ps.executeQuery();
                        if (rs.next()){ //如果确实有ip被封的记录,则比对被封时间
                            System.out.println("存在ip被封的记录");
                            if (!(rs.getString("YMD")).equals(sdf.format(System.currentTimeMillis()))) {
                                // 如果ip被封的与当前不是同一天，那么放行，但是不删除ip记录
                                permit = 1;
                                System.out.println("不是同一天");
                            } else {
                                // 如果ip被封时间是今天，那么继续比较小时与分钟
                                int forbidden_hour = Integer.parseInt(rs.getString("hour"));//获取ip被封时间
                                int forbidden_minute = Integer.parseInt(rs.getString("minute"));
                                int delta_minute = (hour - forbidden_hour)*60 + (minute - forbidden_minute);
                                if (delta_minute>5) {
                                    permit = 1; //如果已经被封禁了超过五分钟，那么放行，但是不删除ip
                                    System.out.println("时间已经超过五分钟");
                                }
                            }
                        } else permit = 1; //如果没有ip被封的记录，则放行
                        if (permit == 1){
                            //正常放行
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
                            } else {
                                System.out.println("用户IP地址: " + remoteIP);
                                String sql1 = "select*from mypan.passwd_wrong where ip=?";
                                ps = conn.prepareStatement(sql1);
                                ps.setString(1, remoteIP);
                                rs = ps.executeQuery();
                                if (rs.next()){ //如果数据库中有输错密码的记录，那么验证时间是否是5分钟之内
                                    if (!(rs.getString("YMD")).equals(sdf.format(System.currentTimeMillis()))) {
                                        //如果上次输错密码不是今天，那么放将记录设置为1
                                        SetPasswd_wrong.set(conn,remoteIP,"1",sdf.format(System.currentTimeMillis()),String.valueOf(hour),String.valueOf(minute));
                                    } else {
                                        // 如果果上次输错密码是今天，那么继续比较小时与分钟
                                        int wrong_hour = Integer.parseInt(rs.getString("hour"));//获取ip被封时间
                                        int wrong_minute = Integer.parseInt(rs.getString("minute"));
                                        int delta_minute = (hour - wrong_hour)*60 + (minute - wrong_minute);
                                        if (delta_minute>5) {
                                            //如果上次错误离现在已经超过5分钟，那么将记录重置为1
                                            SetPasswd_wrong.set(conn,remoteIP,"1",sdf.format(System.currentTimeMillis()),String.valueOf(hour),String.valueOf(minute));
                                        } else {
                                            //如果上次错误时间离现在未超过5分钟，那么继续验证当前记录次数
                                            if (Integer.parseInt(rs.getString("num"))==6){
                                                //如果错误次数为6，代表该ip已经从上一次封禁中释放，那么将其错误次数回归1
                                                SetPasswd_wrong.set(conn,remoteIP,"1",sdf.format(System.currentTimeMillis()),String.valueOf(hour),String.valueOf(minute));
                                            } else if (Integer.parseInt(rs.getString("num"))==5) {
                                                //如果错误次数达到5，那么需要将此ip封禁，并且将错误次数设置为6
                                                SetPasswd_wrong.set(conn,remoteIP,"6",sdf.format(System.currentTimeMillis()),String.valueOf(hour),String.valueOf(minute));
                                                //封禁ip
                                                String sql6 = "select*from mypan.ip_forbidden where ip=?";
                                                ps = conn.prepareStatement(sql6);
                                                ps.setString(1,remoteIP);
                                                rs = ps.executeQuery();
                                                if (rs.next()){
                                                    //如果已经存在此ip之前就被封禁的记录，那么更新时间
                                                    String sql4 = "UPDATE mypan.ip_forbidden SET YMD=?, hour=?, minute=? WHERE ip=?;";
                                                    ps = conn.prepareStatement(sql4);
                                                    ps.setString(1, sdf.format(System.currentTimeMillis()));//设置年月日
                                                    ps.setString(2, String.valueOf(hour));
                                                    ps.setString(3, String.valueOf(minute));
                                                    ps.setString(4, remoteIP);
                                                    ps.executeUpdate();
                                                } else {
                                                    //如果此ip之前未被封禁，则创建封禁记录
                                                    String sql4 = "insert into mypan.ip_forbidden values(?,?,?,?)";
                                                    ps = conn.prepareStatement(sql4);
                                                    ps.setString(1, remoteIP);
                                                    ps.setString(2, sdf.format(System.currentTimeMillis()));//设置年月日
                                                    ps.setString(3, String.valueOf(hour));
                                                    ps.setString(4, String.valueOf(minute));
                                                    ps.executeUpdate();
                                                    System.out.println("将此ip封禁："+remoteIP);
                                                }
                                            } else{
                                                //如果错误次数还没达到5，那么将错误次数增加1
                                                SetPasswd_wrong.set(conn,remoteIP, String.valueOf(Integer.parseInt(rs.getString("num"))+1),sdf.format(System.currentTimeMillis()),String.valueOf(hour),String.valueOf(minute));
                                            }
                                        }
                                    }
                                } else { //如果数据库中没有输错密码的记录，则创建一条新纪录
                                    String sql2 = "insert into mypan.passwd_wrong values(?,1,?,?,?)";
                                    ps = conn.prepareStatement(sql2);
                                    ps.setString(1, remoteIP);
                                    ps.setString(2, sdf.format(System.currentTimeMillis()));//设置年月日
                                    ps.setString(3, String.valueOf(hour));
                                    ps.setString(4, String.valueOf(minute));
                                    int insert = ps.executeUpdate();
                                }
                                response.sendRedirect("wrongpassword.html"); //重定向到密码错误页面，这个页面不归入ErrorPage1.html页面，防止为登录用户获得session
                            }
                        } else response.sendRedirect("IPForbidden.html");
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

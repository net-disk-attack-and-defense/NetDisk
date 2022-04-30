package Project1;

import javax.persistence.SecondaryTable;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class logs extends ViewBaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);//不创建新ID
        if (session != null) { //判断session是否存在
            if (!session.isNew() && request.getHeader("referer")!=null && session.getAttribute("username") != null && session.getAttribute("email") != null) {//判断session是否新的，但似乎无用
                System.out.println("RSU:"+request.getHeader("referer"));
                Referer_Check RC = new Referer_Check(request.getHeader("referer"), "NetDisk/RootSignUpFail.html","NetDisk/RootSignUpSuccess.html","NetDisk/RootEmailExist.html","NetDisk/ShowError","NetDisk/RSU","NetDisk/RootPage.html","NetDisk/RootShowUsers.html","NetDisk/RFD","NetDisk/RFU","NstDisk/RootShowUserFile.html");
                //检查来源链接以及校验是否管理员账号密码
                if (!RC.check() && session.getAttribute("username").equals("ROOT") && session.getAttribute("email").equals("ROOT@ROOT")) {  //验证来源链接
                    ServletContext con = this.getServletContext();
                    String path=con.getRealPath("/");
                    File file = new File(path);
                    //获取tomcat目录中的日志文件
                    System.out.println("path:"+file.getPath());
                    while (!file.getName().equals("Tomcat8")){
                        file=file.getParentFile();
                    }
                    SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");

                    file=new File(file.getPath()+"/logs/netDisk_log."+sdf.format(System.currentTimeMillis())+".txt");

                        System.out.println(file.getPath());
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
                        String lineTxt = null;
                        List<String[]> logs=new ArrayList<>();
                        while ((lineTxt=br.readLine()) != null){
                            String[] log = lineTxt.split(" ");
                            logs.add(log);
                        }
                        session.setAttribute("logs",logs);
                        super.processTemplate("logs", request, response);
                } else response.sendError(403);
            } else response.sendError(403);
        } else response.sendRedirect("login.html");//如果session过期
    }
}



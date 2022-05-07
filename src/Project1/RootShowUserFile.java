package Project1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public class RootShowUserFile extends ViewBaseServlet{
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println("RSUF:"+request.getHeader("referer"));
        int SpiderState = 0;
        try {
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/RSUF","/NetDisk/RSU");
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
                if (session.getAttribute("email")!=null) {
                    if (session.getAttribute("username").equals("ROOT") && request.getParameter("bt_ShowFile")!=null) {
                        System.out.println("文件所属用户的邮箱："+request.getParameter("bt_ShowFile"));
                        String SF_email = request.getParameter("bt_ShowFile");
                        String SF_name = request.getParameter("bt_ShowFileName");
                        if (SF_email != null && SF_name != null) {
                            if (SpecialCharCheck.check(SF_email) || SpecialCharCheck.check(SF_name)){
                                //如果包含违禁字符，销毁session并返回首页
                                session.invalidate();
                                response.sendError(403);
                            } else {
                                session.setAttribute("R_username",SF_name);
                                session.setAttribute("R_email",SF_email);
                                String realpath = request.getServletContext().getRealPath("/");//获取项目真实地址
                                File file0 = new File(realpath+"upload/");
                                file0.mkdir();
                                File file1 = new File(realpath+"upload/"+SF_email+"/");
                                file1.mkdir();
                                File[] files = file1.listFiles();
                                session.setAttribute("R_allfiles", files);
                                super.processTemplate("RootShowUserFile", request, response);
                            }
                        } else {
                            //如果传入的SF_name或者SF_email为空，销毁session并返回首页
                            session.invalidate();
                            response.sendError(403);
                        }
                    } else response.sendError(403);
                } else response.sendError(403);
            } else response.sendError(403);
        }
    }
}

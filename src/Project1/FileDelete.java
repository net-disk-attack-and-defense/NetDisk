package Project1;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

@MultipartConfig
public class FileDelete extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session!=null){
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/filepage.html","/NetDisk/SFP","/NetDisk/RSUF");
            if (session.getAttribute("email")!=null && !uAC.check() && !RC.check()){
                String realpath = request.getServletContext().getRealPath("/");//获取项目真实地址

                String[] filenames = request.getParameterValues("filename");
                if (filenames != null){
                    for (String filename : filenames){
                        File file2 = new File(realpath+"WEB-INF/File/"+session.getAttribute("email")+"/"+filename);

                        if(!file2.delete()){
                            session.setAttribute("Error","文件删除失败");
                            response.sendRedirect("ShowError");
                        };
                    }
                }
                response.sendRedirect("SFP");
            }
        }
        else response.sendRedirect("login.html");
    }
}

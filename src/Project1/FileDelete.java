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
        if (session!=null && request.getHeader("referer")!=null && request.getHeader("user-agent")!=null){
            System.out.println("FD:"+request.getHeader("referer"));
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/filepage.html","/NetDisk/SFP","/NetDisk/RSUF");
            if (session.getAttribute("email")!=null && !uAC.check() && !RC.check()){
                String[] filenames = request.getParameterValues("filename");
                if (filenames != null){
                    String realpath = request.getServletContext().getRealPath("/WEB-INF/");//获取项目真实地址
                    File file0 = new File(realpath+"File/");
                    file0.mkdir();
                    File file1 = new File(realpath+"File/"+session.getAttribute("email")+"/");
                    file1.mkdir(); //创建用户文件夹
                    if (file1.exists()) { //如果用户文件夹存在
                        for (String filename : filenames){
                            System.out.println("del_file:"+filename);
                            File file2 = new File(realpath+"File/"+session.getAttribute("email")+"/"+filename);
                            if (file2.exists()) { //如果欲删除的文件存在
                                if (!file2.delete()) { //如果文件未删除成功
                                    session.setAttribute("Error","文件删除失败");
                                    response.sendRedirect("ShowError");
                                }
                            }
                        }
                    } else {
                        session.setAttribute("Error","用户文件夹不存在");
                        response.sendRedirect("ShowError");
                    }
                }
                response.sendRedirect("SFP");
            } else response.sendError(403);
        } else response.sendRedirect("login.html");
    }
}

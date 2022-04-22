package Project1;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public class RootFileDelete extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session!=null &&request.getHeader("user-agent")!=null &&request.getHeader("referer")!=null){
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            System.out.println("RFD:"+request.getHeader("referer"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/RSUF");
            if (session.getAttribute("email")!=null) {
                if (session.getAttribute("email").equals("ROOT@ROOT") && !uAC.check() && !RC.check() && session.getAttribute("R_email") != null){
                    String[] filenames = request.getParameterValues("filename");
                    if (filenames != null){ //如果要删除的文件不为空
                        String realpath = request.getServletContext().getRealPath("/WEB-INF/");//获取项目真实地址
                        File file0 = new File(realpath+"File/");
                        file0.mkdir();
                        File file1 = new File(realpath+"File/"+session.getAttribute("R_email")+"/");
                        file1.mkdir(); //创建用户文件夹
                        if (file1.exists()) { //如果用户文件夹存在
                            for (String filename : filenames){
                                System.out.println("Rdel_file:"+filename);
                                File file2 = new File(realpath+"File/"+session.getAttribute("R_email")+"/"+filename);//每个欲删除的用户文件的地址
                                if (file2.exists()) { //如果欲删除的文件存在
                                    if (!file2.delete()) { //如果文件未删除成功
                                        session.setAttribute("Error","文件删除失败");
                                        response.sendRedirect("ShowError");
                                    }
                                }
                            }
                        }else {
                            session.setAttribute("Error","用户文件夹不存在");
                            response.sendRedirect("ShowError");
                        }
                    }
                    response.sendRedirect("RSU");
                } else response.sendError(403);
            }else response.sendError(403);
        } else response.sendRedirect("login.html");
    }
}

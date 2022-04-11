package Project1;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;

@MultipartConfig
public class FileUpload extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);
        if (session!=null){
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/filepage.html");
            if (session.getAttribute("username")!=null && !uAC.check()){
                String realpath = request.getServletContext().getRealPath("/");//获取项目真实地址
                File file0 = new File(realpath+"File/");
                file0.mkdir();
                File file1 = new File(realpath+"File/"+session.getAttribute("username")+"/");
                file1.mkdir();
                if (file1.exists()){
                    String resetfilename =request.getParameter("resetfilename");//用户输入的名字
                    Part part = request.getPart("uploadfile");
                    String realfilename =  part.getSubmittedFileName();//文件真实的名字
                    //判断上传的文件是否已存在
                    File file2 = new File(realpath+"File/"+session.getAttribute("username")+"/"+realfilename);
                    if (file2.exists()){
                        response.sendRedirect("FileExist.html");
                    }
                    else {
                        System.out.println(resetfilename);
                        if (resetfilename.length()!=0){
                            part.write(realpath+"File/"+session.getAttribute("username")+"/"+resetfilename);
                            File file3 = new File(realpath+"File/"+session.getAttribute("username")+"/"+resetfilename);
                            if(file3.exists()) response.sendRedirect("SFP");
                            else response.sendRedirect("FileUploadFail.html");
                        } else {
                            part.write(realpath+"File/"+session.getAttribute("username")+"/"+realfilename);
                            if(file2.exists()) response.sendRedirect("SFP");
                            else response.sendRedirect("FileUploadFail.html");
                        }
                    }
                }
                else response.sendRedirect("MkdirFail.html");
            }
        }
        else response.sendRedirect("login.html");
    }
}

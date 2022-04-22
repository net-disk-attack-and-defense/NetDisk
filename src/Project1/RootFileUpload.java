package Project1;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;

@MultipartConfig
public class RootFileUpload extends HttpServlet {
    @Override
    public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session!=null){
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/RSUF");
            System.out.println("RFU:"+request.getHeader("referer"));
            if (session.getAttribute("email")!=null) {
                if (session.getAttribute("email").equals("ROOT@ROOT") && !uAC.check() && !RC.check() && session.getAttribute("R_email") != null){
                    String realpath = request.getServletContext().getRealPath("/WEB-INF/");//获取项目真实地址
                    File file0 = new File(realpath+"File/");
                    file0.mkdir();
                    File file1 = new File(realpath+"File/"+session.getAttribute("R_email")+"/");
                    file1.mkdir();
                    if (file1.exists()){
                        String resetfilename =request.getParameter("resetfilename");//用户输入的文件名
                        Part part = request.getPart("uploadfile");
                        String realfilename =  part.getSubmittedFileName();//文件真实的名字
                        if (resetfilename.length()!=0){
                            File file2 = new File(realpath+"File/"+session.getAttribute("R_email")+"/"+resetfilename);
                            if (file2.exists()){ //判断上传的文件是否已存在
                                session.setAttribute("Error","已存在同名文件");
                                session.setAttribute("Errormsg","如果需要替换文件请先删除原同名文件后再进行提交");
                                response.sendRedirect("ShowError");
                            }else{
                                part.write(realpath+"File/"+session.getAttribute("R_email")+"/"+resetfilename);
                                //上传完后验证是否上传成功
                                File file3 = new File(realpath+"File/"+session.getAttribute("R_email")+"/"+resetfilename);
                                if(file3.exists()) response.sendRedirect("RSU");
                                else {
                                    session.setAttribute("Error","文件上传失败");
                                    response.sendRedirect("ShowError");
                                }
                            }

                        } else {
                            File file2 = new File(realpath+"File/"+session.getAttribute("R_email")+"/"+realfilename);
                            System.out.println("file2"+file2);
                            System.out.println("exist:"+file2.exists());
                            if (file2.exists()){ //判断上传的文件是否已存在
                                session.setAttribute("Error","已存在同名文件");
                                session.setAttribute("Errormsg","如果需要替换文件请先删除原同名文件后再进行提交");
                                response.sendRedirect("ShowError");
                            }else {
                                part.write(realpath+"File/"+session.getAttribute("R_email")+"/"+realfilename);
                                if(file2.exists()) response.sendRedirect("RSU");
                                else {
                                    session.setAttribute("Error","文件上传失败");
                                    response.sendRedirect("ShowError");
                                }
                            }
                        }
                    } else {
                        session.setAttribute("Error","用户文件夹创建失败");
                        response.sendRedirect("ShowError");
                    }
                } else response.sendError(403);
            }else response.sendError(403);
        } else response.sendRedirect("login.html");
    }
}

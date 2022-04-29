package Project1;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


@MultipartConfig
public class FileDownload extends HttpServlet {
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session!=null && request.getHeader("referer")!=null && request.getHeader("user-agent")!=null) {
            System.out.println("FD:" + request.getHeader("referer"));
            User_Agent_Check uAC = new User_Agent_Check(request.getHeader("user-agent"));
            Referer_Check RC = new Referer_Check(request.getHeader("referer"), "/NetDisk/filepage.html", "/NetDisk/SFP", "/NetDisk/RSUF");
            if (session.getAttribute("email") != null && !uAC.check() && !RC.check()) {
                String[] filenames = request.getParameterValues("filename");
                if (filenames!=null){
                    if (filenames.length==1 && !SpecialCharCheck.check((String) session.getAttribute("email"))) {
                        String realpath = request.getServletContext().getRealPath("/WEB-INF/");//获取项目真实地址
                        File file0 = new File(realpath + "File/");
                        file0.mkdir();
                        File file1 = new File(realpath + "File/" + session.getAttribute("email") + "/");
                        file1.mkdir(); //创建用户文件夹
                        if (file1.exists()) {
                            for (String filename : filenames) {  //实际只有一个文件
                                filename = filename.replace("\\","");
                                filename = filename.replace("/","");
                                System.out.println("下载的文件:" + filename);
                                File file2 = new File(realpath + "File/" + session.getAttribute("email") + "/" + filename);
                                if (file2.exists() && file2.isFile()) { //如果欲下载的文件存在
                                    response.setContentType("application/x-msdownload");
                                    response.setHeader("Content-Disposition", "attachment;filename=" + file2.getName()); //下载框内容
                                    FileInputStream is = new FileInputStream(file2);
                                    ServletOutputStream os = response.getOutputStream();//创建输入流和输出流
                                    byte[] temp = new byte[1024];
                                    int len;
                                    while ((len = is.read(temp)) != -1) {
                                        os.write(temp, 0, len);
                                    }//下载文件
                                    os.close();
                                    is.close();
                                } else {
                                    session.setAttribute("Redirect","SFP");
                                    session.setAttribute("Error", "要下载的文件不存在");
                                    response.sendRedirect("ShowError");
                                }
                            }
                        } else {
                            session.setAttribute("Redirect","SFP");
                            session.setAttribute("Error", "用户文件夹不存在");
                            response.sendRedirect("ShowError");
                        }
                    } else {
                        session.setAttribute("Redirect","SFP");
                        session.setAttribute("Error", "一次下一个文件");
                        response.sendRedirect("ShowError");
                    }
                } else response.sendRedirect("SFP");
            } else response.sendError(403);
        } else response.sendRedirect("login.html");
    }
}

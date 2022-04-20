package Project1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;

public class Showfilepage extends ViewBaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //TODO 后端需增添校验防止xss脚本攻击
        //TODO 需对文件内容和后缀进行校验，防止文件上传漏洞，顺便完成后缀自动补全功能
        //TODO 限制用户操作频率和最大操作数
        HttpSession session = request.getSession(false);//不创建新ID
        if (session != null) { //判断session是否存在
            if (!session.isNew()) {//判断session是否新的，但似乎无用
                if (request.getHeader("referer")!=null){
                    System.out.println("SFP:"+request.getHeader("referer"));
                    Referer_Check RC = new Referer_Check(request.getHeader("referer"), "NetDisk/ShowError","NetDisk/SFP","NetDisk/filepage.html","NetDisk/login.html","/NetDisk/RSUF");
                    if (!RC.check() && session.getAttribute("email") != null) {  //验证来源链接
                        String realpath = request.getServletContext().getRealPath("/WEB-INF/");//获取项目真实地址
                        File file0 = new File(realpath+"File/");
                        file0.mkdir();
                        File file1 = new File(realpath+"File/"+session.getAttribute("email")+"/");
                        file1.mkdir();
                        File[] files = file1.listFiles();
                        session.setAttribute("allfiles", files);
                        super.processTemplate("filepage", request, response);
                    } else response.sendRedirect("403.html");
                } else response.sendRedirect("403.html");
            } else response.sendRedirect("403.html");
        } else response.sendRedirect("login.html");//如果session过期
    }
}

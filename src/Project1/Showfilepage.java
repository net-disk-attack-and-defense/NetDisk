package Project1;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class Showfilepage extends ViewBaseServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");
        HttpSession session = request.getSession(false);//不创建新ID
        if (session != null) { //判断session是否存在
            if (!session.isNew()) {//判断session是否新的，但似乎无用
                Referer_Check RC = new Referer_Check(request.getHeader("referer"), "NetDisk/login.html");
                Referer_Check RC1 = new Referer_Check(request.getHeader("referer"), "NetDisk/filepage.html");
                Referer_Check RC2 = new Referer_Check(request.getHeader("referer"), "NetDisk/SFP");
                if (!RC.check() || !RC1.check() || !RC2.check()) {  //验证来源链接
                    if (session.getAttribute("username") != null) {
                        super.processTemplate("filepage", request, response);
                    }
                }
            } else response.sendError(403, "禁止访问");
        } else response.sendRedirect("login.html");//如果session过期
    }
}

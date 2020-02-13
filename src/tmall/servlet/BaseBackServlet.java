package tmall.servlet;

import tmall.dao.CategoryDAO;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseBackServlet extends HttpServlet
{
    protected CategoryDAO categoryDAO = new CategoryDAO();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException
    {
        //分页默认从0开始，每页显示5项
        int start = 0;
        int count = 5;
        if(req.getParameter("page.start") != null)
            start = Integer.parseInt(req.getParameter("page.start"));
        if(req.getParameter("page.count") != null)
            count = Integer.parseInt(req.getParameter("page.count"));
        Page page = new Page(start,count);

        //通过反射调用了CategoryServlet中对应的方法
        String method = (String) req.getAttribute("method");
        String redirect = "";
        try
        {
            Method m = this.getClass().getDeclaredMethod(method,HttpServletRequest.class,
                    HttpServletResponse.class,Page.class);
            redirect = (String) m.invoke(this,req,resp,page);
        } catch (Exception e)
        {
            e.printStackTrace();
        }

        //根据方法返回的字符串进行跳转
        if(redirect.startsWith("@"))
            resp.sendRedirect(redirect.substring(1));
        else if(redirect.startsWith("%"))
            resp.getWriter().print(redirect.substring(1));
        else
            req.getRequestDispatcher(redirect).forward(req,resp);

    }
}

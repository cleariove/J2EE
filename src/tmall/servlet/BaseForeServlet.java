package tmall.servlet;

import tmall.dao.*;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

public class BaseForeServlet extends HttpServlet {

    protected CategoryDAO categoryDAO = new CategoryDAO();

    protected OrderDAO orderDAO = new OrderDAO();

    protected OrderItemDAO orderItemDAO = new OrderItemDAO();

    protected ProductDAO productDAO = new ProductDAO();

    protected ProductImageDAO productImageDAO = new ProductImageDAO();

    protected PropertyDAO propertyDAO = new PropertyDAO();

    protected PropertyValueDAO propertyValueDAO = new PropertyValueDAO();

    protected ReviewDAO reviewDAO = new ReviewDAO();

    protected UserDAO userDAO = new UserDAO();

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        int start = 0;
        int count = 10;
        if(req.getParameter("page.start") != null)
            start = Integer.parseInt(req.getParameter("page.start"));
        if(req.getParameter("page.count") != null)
            count = Integer.parseInt(req.getParameter("page.count"));
        Page page = new Page(start,count);

        String method = (String) req.getAttribute("method");
        String redirect = "";
        try {
            Method m = this.getClass().getDeclaredMethod(method,HttpServletRequest.class,
                    HttpServletResponse.class,Page.class);
            redirect = (String) m.invoke(this,req,resp,page);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(redirect.startsWith("@"))
            resp.sendRedirect(redirect.substring(1));
        else if(redirect.startsWith("%"))
            resp.getWriter().print(redirect.substring(1));
        else
            req.getRequestDispatcher(redirect).forward(req,resp);
    }

}

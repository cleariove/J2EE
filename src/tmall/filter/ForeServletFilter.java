package tmall.filter;

import org.apache.commons.lang.StringUtils;
import tmall.bean.Category;
import tmall.bean.OrderItem;
import tmall.bean.User;
import tmall.dao.CategoryDAO;
import tmall.dao.OrderItemDAO;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ForeServletFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        User user = (User) request.getSession().getAttribute("user");
        int cartTotalNumber = 0;
        if(user != null)
        {
            List<OrderItem> orderItems = new OrderItemDAO().listByUser(user.getId());
            for(OrderItem o:orderItems)
            {
                cartTotalNumber += o.getNumber();
            }
        }
        request.setAttribute("cartTotalNumber",cartTotalNumber);

        //比如直接访问/tmall/forehome
        //现在这里设置一次categories
        //进入foreservlet中设置了一个新的categories，这个categories设置了产品对象并保存在request中
        //foreservlet返回了一个jsp页面，该页面也被拦截，如果不判断是否已经设置了categories则直接创建了一个新的
        List<Category> categories = (List<Category>) request.getAttribute("categories");
        if(categories == null)
        {
            categories = new CategoryDAO().list();
            request.setAttribute("categories",categories);
        }

        String context = request.getContextPath();
        request.setAttribute("contextPath",context);
        String uri = request.getRequestURI();
        String s = StringUtils.remove(uri,context);
        //不能拦截foreServlet开头的网址，不然就出不去了
        if(s.startsWith("/fore") && !s.startsWith("/foreServlet"))
        {
            String method = StringUtils.substringAfterLast(s,"/fore");
            request.setAttribute("method",method);
            request.getRequestDispatcher("/foreServlet").forward(request,response);
            return;
        }
        filterChain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}

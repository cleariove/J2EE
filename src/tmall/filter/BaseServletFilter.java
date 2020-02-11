package tmall.filter;

import org.apache.commons.lang.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class BaseServletFilter implements Filter
{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException
    {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException
    {
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String uri = request.getRequestURI();//注意是uri，相对于完整url去掉了主机名端口
        String contentPath = request.getContextPath();//获取该项目的根路径名
        String s = StringUtils.remove(uri,contentPath);
        //一个例如"/tmall/admin_category_add"表示增加一条category
        //uri = /tmall/admin_category_add
        //contentPath = /admin_category_add
        if(s.startsWith("/admin_"))
        {
            //获取url中需要跳转到的servlet的名称
            String s1 = StringUtils.substringBetween(s,"_","_").concat("Servlet");
            //获取跳转至的servlet需要使用的方法
            String s2 = StringUtils.substringAfterLast(s,"_");
            request.setAttribute("method",s2);
            request.getRequestDispatcher("/"+s1).forward(request,response);
            return;
        }
        filterChain.doFilter(request,response);//过滤器放行，继续运行下一个过滤器或者访问最终的servlet，jsp，html
    }

    @Override
    public void destroy()
    {

    }
}

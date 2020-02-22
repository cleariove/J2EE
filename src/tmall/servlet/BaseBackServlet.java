package tmall.servlet;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import tmall.dao.CategoryDAO;
import tmall.dao.PropertyDAO;
import tmall.util.Page;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class BaseBackServlet extends HttpServlet
{
    protected CategoryDAO categoryDAO = new CategoryDAO();

    protected PropertyDAO propertyDAO = new PropertyDAO();

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

        //通过反射调用了对应Servlet中对应的方法
        String method = (String) req.getAttribute("method");
        String redirect = "";
        try
        {
            //这个this是指子类的对象，因为是子类调用的父类方法
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

    public InputStream parseUpload(HttpServletRequest request, Map<String,String> param)
    {
        InputStream inputStream = null;
        try
        {
            DiskFileItemFactory diskFileItemFactory = new DiskFileItemFactory();
            diskFileItemFactory.setSizeThreshold(1024*10240);
            ServletFileUpload upload = new ServletFileUpload(diskFileItemFactory);
            upload.setHeaderEncoding("UTF-8");
            List items = upload.parseRequest(request);//这里写成List<FileItem>也可以
            Iterator iterator = items.iterator();
            while(iterator.hasNext())
            {
                FileItem fileItem = (FileItem) iterator.next();
                //判断该项如果是文件
                if(!fileItem.isFormField())
                {
                    inputStream = fileItem.getInputStream();
                }
                else
                {
                    String name = fileItem.getFieldName();
                    String value = fileItem.getString("UTF-8");
//                    value =  new String(value.getBytes("ISO-8859-1"),"UTF-8");
                    param.put(name,value);
                }
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return inputStream;
    }

}

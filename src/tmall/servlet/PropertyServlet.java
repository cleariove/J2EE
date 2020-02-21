package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Property;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class PropertyServlet extends BaseBackServlet {

    public String list(HttpServletRequest request,HttpServletResponse response,Page page)
    {
        int cid = Integer.parseInt(request.getParameter("cid"));
        List<Property> properties = propertyDAO.list(cid,page.getStart(),page.getCount());
        request.setAttribute("properties",properties);
        return "admin/listProperty.jsp";
    }

    public String add(HttpServletRequest request,HttpServletResponse response,Page page)
    {
        String name = request.getParameter("name");
        int cid = Integer.parseInt(request.getParameter("cid"));
        Property property = new Property();
        Category category = categoryDAO.get(cid);
        property.setName(name);
        property.setCategory(category);
        propertyDAO.add(property);
        return "@admin_property_list?cid="+cid;
    }

    public String delete(HttpServletRequest request,HttpServletResponse response,Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int cid = propertyDAO.get(pid).getCategory().getId();
        propertyDAO.delete(pid);
        return "@admin_property_list?cid="+cid;
    }

    public String edit(HttpServletRequest request,HttpServletResponse response,Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Property property = propertyDAO.get(pid);
        request.setAttribute("p",property);
        return "admin/editProperty.jsp";
    }

    public String update(HttpServletRequest request,HttpServletResponse response,Page page)
    {
        int cid = Integer.parseInt(request.getParameter("cid"));
        int pid = Integer.parseInt(request.getParameter("pid"));
        String name = request.getParameter("name");
        Property property = propertyDAO.get(pid);
        Category category = categoryDAO.get(cid);
        property.setName(name);
        property.setCategory(category);
        return "@admin_property_list?cid="+cid;
    }

}

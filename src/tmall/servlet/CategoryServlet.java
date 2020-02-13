package tmall.servlet;


import tmall.bean.Category;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

public class CategoryServlet extends BaseBackServlet
{

    public String list(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        List<Category> categories = categoryDAO.list(page.getStart(),page.getCount());
        int total = categoryDAO.getTotal();
        page.setTotal(total);
        request.setAttribute("categories",categories);
        request.setAttribute("page",page);
        return "admin/listCategory.jsp";
    }

    public String add(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        return null;
    }

    public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int id = Integer.parseInt(request.getParameter("id"));
        categoryDAO.delete(id);
        return "@admin/listCategory.jsp";
    }

    public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int id = Integer.parseInt(request.getParameter("id"));
        Category c = categoryDAO.get(id);
        request.setAttribute("c",c);
        return "admin/editCategory.jsp";
    }

    public String update(HttpServletRequest request, HttpServletResponse response, Page page)
    {

    }


}

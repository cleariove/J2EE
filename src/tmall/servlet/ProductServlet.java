package tmall.servlet;

import tmall.bean.Category;
import tmall.bean.Product;
import tmall.bean.PropertyValue;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class ProductServlet extends BaseBackServlet
{
    public String list(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int cid = Integer.parseInt(request.getParameter("cid"));
        List<Product> products = productDAO.list(cid,page.getStart(),page.getCount());
        Category c = categoryDAO.get(cid);
        page.setTotal(productDAO.getTotal(cid));
        page.setParam("&cid="+cid);
        request.setAttribute("c",c);
        request.setAttribute("products",products);
        request.setAttribute("page",page);
        return "admin/listProduct.jsp";
    }

    public String add(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category category = categoryDAO.get(cid);
        Product product = new Product();
        product.setSubTitle(subTitle);
        product.setCategory(category);
        product.setName(name);
        product.setStock(stock);
        product.setOriginalPrice(originalPrice);
        product.setPromotePrice(promotePrice);
        product.setCreateDate(new Date());
        productDAO.add(product);
        return "@admin_product_list?cid="+cid;
    }

    public String delete(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int cid = productDAO.get(pid).getCategory().getId();
        productDAO.delete(pid);
        return "@admin_product_list?cid="+cid;
    }

    public String edit(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        request.setAttribute("p",product);
        return "admin/editProduct.jsp";
    }

    public String update(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        String name = request.getParameter("name");
        String subTitle = request.getParameter("subTitle");
        float originalPrice = Float.parseFloat(request.getParameter("originalPrice"));
        float promotePrice = Float.parseFloat(request.getParameter("promotePrice"));
        int stock = Integer.parseInt(request.getParameter("stock"));
        int cid = Integer.parseInt(request.getParameter("cid"));
        int pid = Integer.parseInt(request.getParameter("pid"));
        Category category = categoryDAO.get(cid);
        Product product = new Product();
        product.setId(pid);
        product.setSubTitle(subTitle);
        product.setCategory(category);
        product.setName(name);
        product.setStock(stock);
        product.setOriginalPrice(originalPrice);
        product.setPromotePrice(promotePrice);
        productDAO.update(product);
        return "@admin_product_list?cid="+cid;
    }

    public String editPropertyValue(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product p = productDAO.get(pid);
        //这行代码完成向PropertyValue表插入的操作
        propertyValueDAO.init(p);
        List<PropertyValue> propertyValues = propertyValueDAO.list(p.getId());
        request.setAttribute("pv",propertyValues);
        request.setAttribute("p",p);
        return "admin/editPropertyValue.jsp";
    }

    public String updatePropertyValue(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pvid = Integer.parseInt(request.getParameter("pvid"));
        String value = request.getParameter("value");
        PropertyValue pv = propertyValueDAO.get(pvid);
        pv.setValue(value);
        propertyValueDAO.update(pv);
        return "%success";
    }
}

package tmall.servlet;

import org.springframework.web.util.HtmlUtils;
import tmall.bean.*;
import tmall.dao.ProductImageDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class ForeServlet extends BaseForeServlet {
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> categories = categoryDAO.list();
        productDAO.fill(categories);
        productDAO.fillByRow(categories);
        request.setAttribute("categories", categories);
        return "home.jsp";
    }

    public String register(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        name = HtmlUtils.htmlEscape(name);
        if (userDAO.isExist(name)) {
            request.setAttribute("msg", "用户名已经存在");
            return "register.jsp";
        }
        User user = new User();
        user.setName(name);
        user.setPassword(password);
        userDAO.add(user);
        return "@registerSuccess.jsp";
    }

    public String login(HttpServletRequest request, HttpServletResponse response, Page page) {
        String name = request.getParameter("name");
        name = HtmlUtils.htmlEscape(name);
        String password = request.getParameter("password");
        User user = userDAO.get(name, password);
        if (user == null) {
            request.setAttribute("msg", "账号密码错误");
            return "login.jsp";
        }
        request.getSession().setAttribute("user", user);
        return "@forehome";
    }

    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    }

    public String product(HttpServletRequest request, HttpServletResponse response, Page page) {
        int pid = Integer.parseInt(request.getParameter("pid"));
        Product product = productDAO.get(pid);
        List<ProductImage> productSingleImages = productImageDAO.list(product, ProductImageDAO.type_single);
        List<ProductImage> productDetailImages = productImageDAO.list(product, ProductImageDAO.type_detail);
        product.setProductSingleImages(productSingleImages);
        product.setProductDetailImages(productDetailImages);
        List<PropertyValue> propertyValues = propertyValueDAO.list(pid);
        List<Review> reviews = reviewDAO.list(pid);
        productDAO.setSaleAndReviewNumber(product);
        request.setAttribute("p", product);
        request.setAttribute("reviews", reviews);
        request.setAttribute("pvs", propertyValues);
        return "product.jsp";
    }

    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page) {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return "%error";
        return "%success";
    }

    public String loginAjax(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        String name = request.getParameter("name");
        String password = request.getParameter("password");
        User user = userDAO.get(name,password);
        if(user == null)
            return "%error";
        request.getSession().setAttribute("user",user);
        return "%success";
    }
}

package tmall.servlet;

import org.springframework.web.util.HtmlUtils;
import tmall.bean.*;
import tmall.dao.ProductImageDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Comparator;
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

    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page)
    {
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

    public String category(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int cid = Integer.parseInt(request.getParameter("cid"));
        Category c = categoryDAO.get(cid);
        productDAO.fill(c);
        productDAO.setSaleAndReviewNumber(c.getProducts());
        String sort = request.getParameter("sort");
        if(sort != null)
            switch (sort)
            {
                case "review":
                    c.getProducts().sort((o1,o2)-> o2.getReviewCount() - o1.getReviewCount());
                    break;
                case "date":
                    c.getProducts().sort(Comparator.comparing(Product::getCreateDate));
                    break;
                case "saleCount":
                    c.getProducts().sort((p1,p2)->p2.getSaleCount() - p1.getSaleCount());
                    break;
                case "price":
                    c.getProducts().sort((p1,p2)-> (int) (p1.getPromotePrice() - p2.getPromotePrice()));
                    break;
                case "all":
                    c.getProducts().sort((p1,p2)-> p2.getReviewCount() * p2.getSaleCount() -
                            p1.getReviewCount() * p1.getSaleCount());
                    break;
            }
        request.setAttribute("c",c);
        return "category.jsp";
    }

    public String search(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        String keyword = request.getParameter("keyword");
        List<Product> products = productDAO.search(keyword);
        productDAO.setSaleAndReviewNumber(products);
        request.setAttribute("ps",products);
        return "searchResult.jsp";
    }

    public String buyone(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("num"));

        User user= (User) request.getSession().getAttribute("user");
        Product product = productDAO.get(pid);

        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        int oiid = 0;
        boolean flag = false;
        for(OrderItem o:orderItems)
        {
            if(o.getProduct().getId() == pid)
            {
                o.setNumber(o.getNumber() + number);
                orderItemDAO.update(o);
                oiid = o.getId();
                flag = true;
                break;
            }
        }
        if(!flag)
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(number);
            orderItem.setProduct(product);
            orderItem.setUser(user);
            orderItemDAO.add(orderItem);
            oiid = orderItem.getId();
        }
        return "@forebuy?oiid="+oiid;
    }

    public String buy(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        //因为结算时可以同时选中多个商品一起结算
        String[] oiids = request.getParameterValues("oiid");
        List<OrderItem> orderItems = new ArrayList<>();
        float totalPrice = 0;
        for(String oiid:oiids)
        {
            int id = Integer.parseInt(oiid);
            OrderItem orderItem = orderItemDAO.get(id);
            totalPrice += orderItem.getNumber() * orderItem.getProduct().getPromotePrice();
            orderItems.add(orderItem);
        }
        request.getSession().setAttribute("ois",orderItems);
        request.setAttribute("total",totalPrice);
        return "buy.jsp";
    }

    public String addCart(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("num"));

        User user= (User) request.getSession().getAttribute("user");
        Product product = productDAO.get(pid);

        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        boolean flag = false;
        for(OrderItem o:orderItems)
        {
            if(o.getProduct().getId() == pid)
            {
                o.setNumber(o.getNumber() + number);
                orderItemDAO.update(o);
                flag = true;
                break;
            }
        }
        if(!flag)
        {
            OrderItem orderItem = new OrderItem();
            orderItem.setNumber(number);
            orderItem.setProduct(product);
            orderItem.setUser(user);
            orderItemDAO.add(orderItem);
        }
        return "%success";
    }
}

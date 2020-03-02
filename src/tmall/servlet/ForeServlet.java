package tmall.servlet;

import org.apache.commons.lang.math.RandomUtils;
import org.springframework.web.util.HtmlUtils;
import tmall.bean.*;
import tmall.dao.OrderDAO;
import tmall.dao.OrderItemDAO;
import tmall.dao.ProductImageDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class ForeServlet extends BaseForeServlet {
    //主页
    public String home(HttpServletRequest request, HttpServletResponse response, Page page) {
        List<Category> categories = categoryDAO.list();
        productDAO.fill(categories);
        productDAO.fillByRow(categories);
        request.setAttribute("categories", categories);
        return "home.jsp";
    }

    //注册
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

    //登录
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

    //登出
    public String logout(HttpServletRequest request, HttpServletResponse response, Page page) {
        request.getSession().removeAttribute("user");
        return "@forehome";
    }

    //查看某个商品
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

    //判断是否登录了
    public String checkLogin(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        User user = (User) request.getSession().getAttribute("user");
        if (user == null)
            return "%error";
        return "%success";
    }

    //通过模态框进行的登录
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

    //查看某分类下所有商品
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

    //搜索
    public String search(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        String keyword = request.getParameter("keyword");
        List<Product> products = productDAO.search(keyword);
        productDAO.setSaleAndReviewNumber(products);
        request.setAttribute("ps",products);
        return "searchResult.jsp";
    }

    //直接从商品链接处进行购买
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

    //从购物车页面进行购买
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
        //不仅在付款页面需要该订单的所有项目，后续页面也需要
        request.getSession().setAttribute("ois",orderItems);
        request.setAttribute("total",totalPrice);
        return "buy.jsp";
    }

    //商品添加购物车
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

    //查看购物车
    public String cart(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        User user = (User) request.getSession().getAttribute("user");
        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        request.setAttribute("ois",orderItems);
        return "cart.jsp";
    }

    //修改购物车中商品的数量
    public String changeOrderItem(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null)
            return "%error";
        int pid = Integer.parseInt(request.getParameter("pid"));
        int number = Integer.parseInt(request.getParameter("number"));
        List<OrderItem> orderItems = orderItemDAO.listByUser(user.getId());
        for(OrderItem oi:orderItems)
        {
            if(oi.getProduct().getId() == pid)
            {
                oi.setNumber(number);
                orderItemDAO.update(oi);
                break;
            }
        }
        return "%success";
    }

    //删除购物车中某商品
    public String deleteOrderItem(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null)
            return "%error";
        int oiid = Integer.parseInt(request.getParameter("oiid"));
        orderItemDAO.delete(oiid);
        return "%success";
    }

    //生成订单
    public String createOrder(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        User user = (User) request.getSession().getAttribute("user");
        if(user == null)
            return "@login.jsp";
        List<OrderItem> ois = (List<OrderItem>) request.getSession().getAttribute("ois");
        if(ois == null)
            return "@login.jsp";
        String address = request.getParameter("address");
        String post = request.getParameter("post");
        String receiver = request.getParameter("receiver");
        String mobile = request.getParameter("mobile");
        String userMessage = request.getParameter("userMessage");

        Order order = new Order();
        String orderCode = new SimpleDateFormat("yyyyMMddHHmmssSSS")
                .format(new Date()) + RandomUtils.nextInt(10000);
        order.setOrderCode(orderCode);
        order.setAddress(address);
        order.setPost(post);
        order.setReceiver(receiver);
        order.setMobile(mobile);
        order.setUserMessage(userMessage);
        order.setUser(user);
        order.setStatus(OrderDAO.waitPay);
        order.setCreateDate(new Date());

        orderDAO.add(order);
        float total = 0;
        for(OrderItem oi:ois)
        {
            oi.setOrder(order);
            orderItemDAO.update(oi);
            Product p = oi.getProduct();
            p.setStock(p.getStock() - oi.getNumber());
            productDAO.update(p);
            total += oi.getNumber() * oi.getProduct().getPromotePrice();
        }
        return "@forealipay?oid="+order.getId()+"&total="+total;
    }

    //支付页面
    public String alipay(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        return "alipay.jsp";
    }

    //支付成功页面
    public String payed(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitDelivery);
        order.setPayDate(new Date());
        new OrderDAO().update(order);
        request.setAttribute("o", order);
        return "payed.jsp";
    }

    //查看订单页面
    public String bought(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        User user = (User) request.getSession().getAttribute("user");
        List<Order> orders = orderDAO.list(user.getId(),OrderDAO.delete);
        orderItemDAO.fill(orders);
        request.setAttribute("orders",orders);
        return "bought.jsp";
    }

    //确认收货
    public String confirmPay(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        request.setAttribute("o",order);
        return "confirmPay.jsp";
    }
}

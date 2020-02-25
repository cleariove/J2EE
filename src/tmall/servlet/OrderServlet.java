package tmall.servlet;

import tmall.bean.Order;
import tmall.dao.OrderDAO;
import tmall.util.Page;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

public class OrderServlet extends BaseBackServlet
{
    public String list(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        List<Order> orders = orderDAO.list(page.getStart(),page.getCount());
        orderItemDAO.fill(orders);
        page.setTotal(orderDAO.getTotal());
        request.setAttribute("orders",orders);
        request.setAttribute("page",page);
        return "admin/listOrder.jsp";
    }

    public String add(HttpServletRequest request, HttpServletResponse response, Page page) {

        return null;
    }

    public String delete(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    public String edit(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    public String update(HttpServletRequest request, HttpServletResponse response, Page page) {
        return null;
    }

    public String delivery(HttpServletRequest request, HttpServletResponse response, Page page)
    {
        int oid = Integer.parseInt(request.getParameter("oid"));
        Order order = orderDAO.get(oid);
        order.setStatus(OrderDAO.waitConfirm);
        order.setDeliveryDate(new Date());
        orderDAO.update(order);
        return "@admin_order_list";
    }
}

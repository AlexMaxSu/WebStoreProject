package com.alex.store.controller.admin;

import com.alex.store.model.admin.*;
import com.alex.store.service.admin.OrderService;
import com.alex.store.service.admin.UserService;
import com.alex.store.service.admin.impl.OrderServiceImpl;
import com.alex.store.service.admin.impl.UserServiceImpl;
import com.alex.store.util.BaseServlet;
import com.alex.store.util.PageHelper;
import com.alex.store.util.UuidUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

@WebServlet(name = "OrderServlet",urlPatterns = "/OrderServlet")
public class OrderServlet extends BaseServlet {
    OrderService orderService = new OrderServiceImpl();


    public void findAllOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //当前用户传入的分页的第几页
        String num = request.getParameter("num");
        if (num == null || num.isEmpty()) {

            num = "1";
        }
        PageHelper<Order> pageHelper = null;
        try {
            pageHelper = orderService.findOrderListByPagenumber(num);

            request.setAttribute("page", pageHelper);
            request.getRequestDispatcher("/admin/order/orderList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void deleteOrderItems(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {
        try {
            int itemid = Integer.parseInt(request.getParameter("itemid"));
            orderService.deleteOrderItems(itemid);
            request.getRequestDispatcher("/OrderServlet?op=findAllOrder").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void delOrder(HttpServletRequest request,
                                 HttpServletResponse response) throws ServletException, IOException {
        try {
            String oid = request.getParameter("oid");
            orderService.delOrder(oid);
            request.getRequestDispatcher("/OrderServlet?op=findAllOrder").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    public void deleteMulti(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {

        String[] itemids = request.getParameterValues("checkbox");
        //System.out.println("CategoryServlet.deleteMulti()" + Arrays.toString(cids));
        if (itemids == null || itemids.length == 0) {
            response.sendRedirect(request.getContextPath() + "/OrderServlet?op=findAllOrder");
            return;
        }

        try {
            orderService.deleteMulti(itemids);
            response.getWriter().println("批量删除成功!<br>");

            response.setHeader("refresh", "1;url=" + request.getServletContext().getContextPath() + "/OrderServlet?op=findAllOrder");
        } catch (Exception e) {

            response.getWriter().println("批量删除失败!<br>" + e.getMessage());

            response.setHeader("refresh", "3;url=" + request.getServletContext().getContextPath() + "/OrderServlet?op=findAllOrder");
        }
    }


    public void orderDetail(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {

        try {
            String oid = request.getParameter("oid");
            List<OrderItem> orderitems = null;
            orderitems = orderService.orderDetail(oid);
            request.setAttribute("orderitems",orderitems);
            request.getRequestDispatcher("/admin/order/orderDetails.jsp").forward(request,response);
        } catch (SQLException e) {
            e.printStackTrace();
        }



    }





    public String placeOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取页面的参数
            Map<String, String[]> map = request.getParameterMap();
            // 封装Order对象
            Order order = new Order();
            // 设置订单号
            order.setOid(UuidUtil.getUuid());
            // 设置订单总金额
            order.setMoney(Double.parseDouble(map.get("money")[0]));
            // 设置订单的接收人
            order.setRecipients(map.get("recipients")[0]);
            // 设置订单接收人的电话
            order.setTel(map.get("tel")[0]);
            // 设置订单接收人的地址
            order.setAddress(map.get("address")[0]);
            // 设置订单的状态
            order.setState(new Random().nextInt(2));
            // 设置订单的创建时间
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            order.setOrdertime(date);
            // 设置订单所属的用户
            UserService userService = new UserServiceImpl();
            User user = userService.findByUid(Integer.parseInt(map.get("uid")[0]));
            order.setUser(user);
            // 设置订单的订单项
            HttpSession session = request.getSession();
            ShoppingCar shoppingCar = (ShoppingCar) session.getAttribute("shoppingCar");
            //把购物项转换成订单项
            Map<String, ShoppingItem> shoppingItem = shoppingCar.getShoppingItem();
            List<OrderItem> list = order.getList();
            String[] ids = map.get("ids");
            for (String id : ids) {
                ShoppingItem item = shoppingItem.get(id);
                //循环创建一个订单对象
                OrderItem orderItem = new OrderItem();
                //将购物项的数据转换给订单项
                orderItem.setBuynum(item.getSubcount());
                orderItem.setProduct(item.getProduct());
                orderItem.setItemid(item.getItemid());
                orderItem.setOrder(order);
                list.add(orderItem);
            }
            // 调用service的方法
            orderService.placeOrder(order);
            // 下单后清空购物车
            shoppingCar.clearShoppingCar();
            session.setAttribute("shoppingCar", shoppingCar);
            // 重定向至订单列表页面
            response.sendRedirect(request.getContextPath() + "/OrderServlet?op=myoid");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }



    public String myoid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");

            if (user != null) {
                List<Order> orders = orderService.myoid(user.getUid());
                request.setAttribute("orders", orders);
                request.getRequestDispatcher("/myOrders.jsp").forward(request,response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String cancelOrder(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String oid = request.getParameter("oid");
            orderService.cancelOrder(oid);
            response.sendRedirect(request.getContextPath()+"/OrderServlet?op=myoid");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

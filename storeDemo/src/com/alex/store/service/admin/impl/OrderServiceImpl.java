package com.alex.store.service.admin.impl;

import com.alex.store.dao.admin.OrderDao;
import com.alex.store.dao.admin.impl.OrderDaoImpl;
import com.alex.store.model.admin.*;
import com.alex.store.service.admin.OrderService;
import com.alex.store.util.MyC3PODataSouce;
import com.alex.store.util.PageHelper;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderServiceImpl implements OrderService {

    OrderDao orderDao = new OrderDaoImpl();
    private static  final int PAGE_COUNT=3;

    @Override
    public PageHelper<Order> findOrderListByPagenumber(String num) throws SQLException {

        int totalNumber = orderDao.findTotalNumber();
        int currentPageNumber = Integer.parseInt(num);

        PageHelper<Order> pageHelper=new PageHelper<> (currentPageNumber, totalNumber,PAGE_COUNT);;

        //分页也可以内聚到PageHelper里面
        //总的页码数，
        //每一页需要显示的记录数
        int limit=PAGE_COUNT;
        int offset= (currentPageNumber-1)*PAGE_COUNT;
        List<Order> orders =orderDao.findPartOrder(limit,offset);
        pageHelper.setRecords(orders);

        return pageHelper;
    }






    @Override
    public void placeOrder(Order order) throws SQLException {

        Connection con = null;

        try {
            // 开启事务
            con = MyC3PODataSouce.getConnection();
            con.setAutoCommit(false);

            // 保存订单
            orderDao.placeOrder(order,con);
            // 保存订单项
            List<OrderItem> orderItems = order.getList();
            for (OrderItem orderItem : orderItems) {
                orderDao.saveOrderItem(orderItem,con);
            }
            // 提交事务
            con.commit();
        } catch (Exception e) {
            // 回滚事务
                con.rollback();
        }

    }

    @Override
    public List<Order> myoid(int uid) throws SQLException {
        return orderDao.myoid(uid);
    }

    @Override
    public void cancelOrder(String oid) throws SQLException {
        orderDao.cancelOrder(oid);
    }



    @Override
    public List<OrderItem> orderDetail(String oid) throws SQLException {
        return orderDao.orderDetail(oid);
    }

    @Override
    public void deleteOrderItems(int itemid) throws SQLException {
        orderDao.deleteOrderItems(itemid);
    }

    @Override
    public void delOrder(String oid) throws SQLException {
        orderDao.delOrder(oid);
    }

    @Override
    public void deleteMulti(String[] itemids) {
        orderDao.deleteMulti(itemids);
    }
}

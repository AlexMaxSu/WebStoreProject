package com.alex.store.service.admin;

import com.alex.store.model.admin.Order;
import com.alex.store.model.admin.OrderItem;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;
import java.util.List;

public interface OrderService {
    PageHelper<Order> findOrderListByPagenumber(String num) throws SQLException;

    
    

    void placeOrder(Order order) throws SQLException;

    List<Order> myoid(int uid) throws SQLException;

    void cancelOrder(String oid) throws SQLException;



    List<OrderItem> orderDetail(String oid) throws SQLException;

    void deleteOrderItems(int itemid) throws SQLException;

    void delOrder(String oid) throws SQLException;

    void deleteMulti(String[] itemids);
}

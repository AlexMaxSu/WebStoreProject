package com.alex.store.dao.admin;

import com.alex.store.model.admin.Order;
import com.alex.store.model.admin.OrderItem;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface OrderDao {
    int findTotalNumber() throws SQLException;

    List<Order> findPartOrder(int limit, int offset) throws SQLException;

    


    void placeOrder(Order order, Connection con) throws SQLException;

    void saveOrderItem(OrderItem orderItem, Connection con) throws SQLException;

    List<Order> myoid(int uid) throws SQLException;

    void cancelOrder(String oid) throws SQLException;



    List<OrderItem> orderDetail(String oid) throws SQLException;

    void deleteOrderItems(int itemid) throws SQLException;

    void delOrder(String oid) throws SQLException;

    void deleteMulti(String[] itemids);
}

package com.alex.store.dao.admin.impl;

import com.alex.store.dao.admin.OrderDao;
import com.alex.store.model.admin.Order;
import com.alex.store.model.admin.OrderItem;
import com.alex.store.model.admin.User;
import com.alex.store.util.MyC3PODataSouce;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    @Override
    public int findTotalNumber() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        Long query = (Long) qr.query("select count(*) from `order`", new ScalarHandler());

        return query.intValue();
    }

    @Override
    public List<Order> findPartOrder(int limit, int offset) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        List<Order> query = qr.query("select * from `order` limit ? offset ?",
                new BeanListHandler<Order>(Order.class),
                limit,
                offset);

        return query;
    }


    @Override
    public void placeOrder(Order order, Connection con) throws SQLException {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into `order` values(?, ?, ?, ?, ?, ?, ?, ?)";
        qr.update(con,sql,order.getOid(), order.getMoney(), order.getRecipients(), order.getTel(),
                order.getAddress(), order.getState(), order.getOrdertime(), order.getUser().getUid());

    }

    @Override
    public void saveOrderItem(OrderItem orderItem, Connection con) throws SQLException {
        QueryRunner qr = new QueryRunner();
        String sql = "insert into orderitem values(?, ?, ?, ?)";
        qr.update(con, sql, orderItem.getItemid(), orderItem.getOrder().getOid(),
                orderItem.getProduct().getPid(), orderItem.getBuynum());

    }

    @Override
    public List<Order> myoid(int uid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from `order` where uid=?";
        return qr.query(sql,new BeanListHandler<Order>(Order.class),uid);
    }

    @Override
    public void cancelOrder(String oid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "update `order` set state=? where oid=?";
        qr.update(sql,0,oid);

    }

    @Override
    public List<OrderItem> orderDetail(String oid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from orderitem where oid=?";

        return qr.query(sql,new BeanListHandler<OrderItem>(OrderItem.class),oid);
    }

    @Override
    public void deleteOrderItems(int itemid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "delete from orderitem where itemid=?";
        qr.update(sql,itemid);
    }

    @Override
    public void delOrder(String oid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "delete from `order` where oid=?";
        qr.update(sql,oid);
    }

    @Override
    public void deleteMulti(String[] itemids) {
        QueryRunner qRunner = new QueryRunner();
        Connection connection =null;
        try {

            connection = MyC3PODataSouce.getConnection();

            Object[][] params = new Object[itemids.length][];

            for (int i =0;i<itemids.length;i++) {

                Object[] o= { itemids[i]};
                params[i]=o;

            }

            //开启事务
            connection.setAutoCommit(false);

            int[] batch = qRunner.batch(connection,"delete from orderitem where itemid =? ;", params);

            //提交
            connection.commit();
            for (int i : batch) {

                System.out.println("OrderDaoImpl.deleteorderitemsByIds()"+i);
            }
        } catch (SQLException e) {

            e.printStackTrace();

            if(connection!=null){
                try {
                    connection.rollback();
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            }

            throw new RuntimeException(e.getMessage());
        }
    }


}

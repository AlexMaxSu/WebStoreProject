package com.alex.store.dao.admin.impl;

import com.alex.store.dao.admin.ProductDao;
import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.Product;
import com.alex.store.util.MyC3PODataSouce;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    @Override
    public int findTotalNumber() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        Long query = (Long) qr.query("select count(*) from product", new ScalarHandler());

        return query.intValue();
    }

    @Override
    public List<Product> findPartProduct(int limit, int offset) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        List<Product> query = qr.query("select * from product limit ? offset ?",
                new BeanListHandler<Product>(Product.class),
                limit,
                offset);

        return query;
    }

   /* @Override
    public boolean deleteOneById(int pid) throws SQLException {
        boolean ret =false;


        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        try {
            int update = qr.update("delete from product where pid =? ", pid);

            System.out.println("ProductDaoImpl.deleteProductById()"+update);
            if (update==1) {
                ret =true;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw  new RuntimeException("detete product error! " + e.getMessage());
        }



        return ret;
    }*/

    @Override
    public void deleteMultiByIds(String[] pids) {
        QueryRunner qRunner = new QueryRunner();
        Connection connection = null;
        try {

            connection = MyC3PODataSouce.getConnection();

            Object[][] params = new Object[pids.length][];

            for (int i = 0; i < pids.length; i++) {

                Object[] o = {pids[i]};
                params[i] = o;

            }

            //开启事务
            connection.setAutoCommit(false);

            int[] batch = qRunner.batch(connection, "delete from product where pid =? ", params);

            //提交
            connection.commit();
            for (int i : batch) {

                System.out.println("ProductDaoImpl.deleteMultiByIds()" + i);
            }
        } catch (SQLException e) {

            e.printStackTrace();

            if (connection != null) {
                try {
                    connection.rollback();
                } catch (SQLException e1) {

                    e1.printStackTrace();
                }
            }

            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public boolean deleteOneById(String pid) throws SQLException {
        boolean ret = false;


        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        try {
            int update = qr.update("delete from product where pid =? ", pid);

            System.out.println("ProductDaoImpl.deleteProductById()" + update);
            if (update == 1) {
                ret = true;
            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw new RuntimeException("detete product error! " + e.getMessage());
        }


        return ret;
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        // 编写sql
        String sql = "update product set pname = ?, estoreprice = ?, markprice= ?, " +
                "pnum = ?, cid = ?, imgurl = ?, description = ? where pid like?";
        // 执行sql
        int update = qr.update(sql, product.getPname(), product.getEstoreprice(), product.getMarkprice(), product.getPnum(),
                product.getCid(), product.getImgurl(), product.getDescription(), product.getPid());
        System.out.println(update);
    }

    @Override
    public Category findPageCategory(int cid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        // 编写sql
        String sql = "select * from category where cid = ?";
        // 执行sql
        return qr.query(sql, new BeanHandler<>(Category.class), cid);
    }

    @Override
    public Product findProductInfo(String pid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        // 编写sql
        String sql = "select * from product where pid = ?";
        // 执行sql
        return qr.query(sql, new BeanHandler<>(Product.class), pid);
    }

    @Override
    public int findTotalNumber(String pid, String cid, String pname, String minprice, String maxprice) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select count(*) from product where 1=1 ";
        List<Object> list = new ArrayList<>();
        if (pid != "") {
            sql += " and pid = ? ";
            list.add(pid);
        }
        if (cid != "") {
            sql += " and cid = ? ";
            list.add(Integer.parseInt(cid));
        }
        if (pname != "") {
            sql += " and pname = ? ";
            list.add(pname);
        }
        if (minprice != "") {
            sql += " and markprice >= ? ";
            list.add(Double.parseDouble(minprice));
        }
        if (maxprice != "") {
            sql += " and markprice <= ? ";
            list.add(Double.parseDouble(maxprice));
        }
        Object[] params = list.toArray();
        Long query = (Long) qr.query(sql, new ScalarHandler(), params);

        return query.intValue();
    }

    @Override
    public List<Product> findPartProduct(String pid, String cid, String pname, String minprice, String maxprice, int limit, int offset) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from product where 1=1 ";
        List<Object> list = new ArrayList<>();
        if (pid != "") {
            sql += " and pid = ? ";
            list.add(pid);
        }
        if (cid != "") {
            sql += " and cid = ? ";
            list.add(Integer.parseInt(cid));
        }
        if (pname != "") {
            sql += " and pname = ? ";
            list.add(pname);
        }
        if (minprice != "") {
            sql += " and markprice >= ? ";
            list.add(Double.parseDouble(minprice));
        }
        if (maxprice != "") {
            sql += " and markprice <= ? ";
            list.add(Double.parseDouble(maxprice));
        }
        list.add(offset);
        list.add(limit);
        Object[] params = list.toArray();
        sql += " limit ?, ? ";
        System.out.println(sql);

        List<Product> query = qr.query(sql, new BeanListHandler<Product>(Product.class), params);

        return query;
    }


    //按分类查找商品
    @Override
    public List<Product> byCid(String cid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select pid,pname,estoreprice,imgurl from product where cid=?";
        return qr.query(sql,new BeanListHandler<Product>(Product.class),cid);
    }


    //查找商品详细信息
    @Override
    public Product findProductById(String pid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from product where pid=?";
        return qr.query(sql,new BeanHandler<Product>(Product.class),pid);
    }

    @Override
    public List<Product> findProduct() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from product";
        return qr.query(sql,new BeanListHandler<Product>(Product.class));
    }

    @Override
    public List<Product> findProByName(String pname) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        //模糊查询like,参数前后加"%"
        String sql ="select * from product where pname like ?";
        return qr.query(sql,new BeanListHandler<Product>(Product.class),"%"+pname+"%");
    }


}

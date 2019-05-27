package com.alex.store.dao.admin.impl;

import com.alex.store.dao.admin.CategoryDao;
import com.alex.store.model.admin.Category;
import com.alex.store.util.MyC3PODataSouce;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;


public class CategoryDaoImpl implements CategoryDao {
    @Override
    public void addCategory(Category category) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "insert into category values (?,?)";
        qr.update(sql,category.getCid(),category.getCname());
    }

    @Override
    public List<Category> findAllCategory() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        List<Category> query = qr.query("select * from category", new BeanListHandler<Category>(Category.class));

        return query;
    }

    @Override
    public int findTotalNumber() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        Long query = (Long) qr.query("select count(*) from category", new ScalarHandler());

        return query.intValue();
    }

    @Override
    public List<Category> findPartCategory(int limit, int offset) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        List<Category> query = qr.query("select * from category limit ? offset ?",
                new BeanListHandler<Category>(Category.class),
                limit,
                offset);

        return query;
    }

    @Override
    public boolean deleteCategoryById(int cid) throws SQLException {
        boolean ret =false;


        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        try {
            int update = qr.update("delete from category where cid =? ;", cid);

            System.out.println("CategoryDaoImpl.deleteCategoryById()"+update);
            if (update==1) {
                ret =true;
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw  new RuntimeException("detete category error! " + e.getMessage());
        }



        return ret;
    }

    @Override
    public void deleteCategoriesByIds(String[] cids) {
        QueryRunner qRunner = new QueryRunner();
        Connection connection =null;
        try {

            connection = MyC3PODataSouce.getConnection();

            Object[][] params = new Object[cids.length][];

            for (int i =0;i<cids.length;i++) {

                Object[] o= { cids[i]};
                params[i]=o;

            }

            //开启事务
            connection.setAutoCommit(false);

            int[] batch = qRunner.batch(connection,"delete from category where cid =? ;", params);

            //提交
            connection.commit();
            for (int i : batch) {

                System.out.println("CategoryDaoImpl.deleteCategoriesByIds()"+i);
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
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

    @Override
    public Category getCategoryByCid(int intcid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        Category query = qr.query("select * from category where cid =?", new BeanHandler<Category>(Category.class),intcid);

        return query;
    }


    @Override
    public boolean updateCategory(Category category) throws SQLException {
        boolean ret =false;


        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        try {
            int update = qr.update("update category set cname =? where cid =? ;", category.getCname(),category.getCid());

            System.out.println("CategoryDaoImpl.addCategoryToDB()"+update);
            if (update==1) {
                ret =true;

            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            throw  new RuntimeException("update category error! ");
        }

        return ret;
    }

    @Override
    public List<Category> findCategory() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        List<Category> query = qr.query("select * from category", new BeanListHandler<Category>(Category.class));

        return query;
    }

}

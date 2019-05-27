package com.alex.store.dao.admin.impl;

import com.alex.store.dao.admin.AdminDao;
import com.alex.store.model.admin.Admin;
import com.alex.store.util.MyC3PODataSouce;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class AdminDaoImpl implements AdminDao {
    @Override
    public void addAdmin(Admin admin) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "insert into `admin` values (?,?,?)";
        qr.update(sql,admin.getAid(),admin.getUsername(),admin.getPassword());
    }

    @Override
    public int findTotalNumber() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        Long query = (Long) qr.query("select count(*) from `admin`", new ScalarHandler());

        return query.intValue();
    }

    @Override
    public List<Admin> findPartAdmin(int limit, int offset) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        List<Admin> query = qr.query("select * from `admin` limit ? offset ?",
                new BeanListHandler<Admin>(Admin.class),
                limit,
                offset);

        return query;
    }

    @Override
    public boolean deleteOneById(int aid) throws SQLException {
        boolean ret =false;
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        try {
            int update = qr.update("delete from `admin` where aid =? ;", aid);

            System.out.println("AdminDaoImpl.deleteOneById()"+update);
            if (update==1) {
                ret =true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw  new RuntimeException("detete admin error! " + e.getMessage());
        }
        return ret;
    }

    @Override
    public boolean updateAdmin(Admin admin) throws SQLException {
        boolean ret =false;

        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        try {
            int update = qr.update("update `admin` set password =? where username =? ;", admin.getPassword(),admin.getUsername());

            System.out.println("AdminDaoImpl.addAdminToDB()"+update);
            if (update==1) {
                ret =true;

            }
        } catch (SQLException e) {

            e.printStackTrace();
            throw  new RuntimeException("update admin error! ");
        }

        return ret;
    }



    //admin是数据库关键字，一定要记着`admin`
    @Override
    public Admin login(String username, String password) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from `admin` where username=? and password=?";
        return qr.query(sql,new BeanHandler<Admin>(Admin.class),username,password);
    }

}

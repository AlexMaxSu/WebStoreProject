package com.alex.store.dao.admin.impl;

import com.alex.store.dao.admin.UserDao;
import com.alex.store.model.admin.User;
import com.alex.store.util.MyC3PODataSouce;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    @Override
    public void addUser(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?)";
        qr.update(sql,user.getUid(), user.getUsername(), user.getNickname(), user.getPassword(),
                user.getEmail(), user.getBirthday(), user.getUpdatetime());
    }

    @Override
    public int findTotalNumber() throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        Long query = (Long) qr.query("select count(*) from user", new ScalarHandler());

        return query.intValue();
    }

    @Override
    public List<User> findPartUser(int limit, int offset) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());

        List<User> query = qr.query("select * from user limit ? offset ?",
                new BeanListHandler<User>(User.class),
                limit,
                offset);

        return query;
    }

    @Override
    public boolean regist(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "insert into user values(?,?,?,?,?,?,?,?,?)";
        int update = qr.update(sql,user.getUid(), user.getUsername(), user.getNickname(), user.getPassword(),
                user.getEmail(), user.getBirthday(), user.getUpdatetime(),user.getState(),user.getCode());
        return update ==1?true:false;
    }

    @Override
    public User login(String username, String password) throws SQLException {

        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from user where username=? and password=?";
        return qr.query(sql,new BeanHandler<User>(User.class),username,password);
    }

    @Override
    public User findByUid(int uid) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from user where uid=?";
        User user = qr.query(sql,new BeanHandler<User>(User.class),uid);
        return user;
    }

    @Override
    public User findByCode(String code) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "select * from user where code=?";
        User user = qr.query(sql,new BeanHandler<User>(User.class),code);
        return user;
    }


    //Y记着加引号
    @Override
    public void updateState(User user) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "update user set state = 'Y' where uid=?";
        qr.update(sql,user.getUid());

    }

    @Override
    public int judgeName(String name) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        Long result = (Long) qr.query("select count(*) from user where username=?", new ScalarHandler(),name);
        return result.intValue();
    }


}

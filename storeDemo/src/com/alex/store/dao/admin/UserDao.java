package com.alex.store.dao.admin;

import com.alex.store.model.admin.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    void addUser(User user) throws SQLException;

    int findTotalNumber() throws SQLException;

    List<User> findPartUser(int limit, int offset) throws SQLException;

    boolean regist(User user) throws SQLException;

    User login(String username, String password) throws SQLException;

    User findByUid(int uid) throws SQLException;

    User findByCode(String code) throws SQLException;

    void updateState(User user) throws SQLException;

    int judgeName(String name) throws SQLException;

    /*User judgeName(String name) throws SQLException;*/
}

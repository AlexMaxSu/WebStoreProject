package com.alex.store.service.admin;

import com.alex.store.model.admin.User;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;

public interface UserService {
    void addUser(User user) throws SQLException;

    PageHelper<User> findUserListByPagenumber(String num) throws SQLException;

    boolean regist(User user) throws SQLException;

    User login(String username, String password) throws SQLException;

    User findByUid(int uid) throws SQLException;

    boolean active(String code) throws SQLException;

    boolean judgeName(String name) throws SQLException;
}

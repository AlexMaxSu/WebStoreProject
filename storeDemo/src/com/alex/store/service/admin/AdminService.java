package com.alex.store.service.admin;

import com.alex.store.model.admin.Admin;
import com.alex.store.model.admin.User;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;

public interface AdminService {
    void addAdmin(Admin admin) throws SQLException;

    PageHelper<Admin> findAdminListByPagenumber(String num) throws SQLException;

    boolean deleteOne(int aid) throws SQLException;

    boolean updateAdmin(Admin admin) throws SQLException;

    Admin login(String username, String password) throws SQLException;
}

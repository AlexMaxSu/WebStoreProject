package com.alex.store.dao.admin;

import com.alex.store.model.admin.Admin;

import java.sql.SQLException;
import java.util.List;

public interface AdminDao {
    void addAdmin(Admin admin) throws SQLException;


    int findTotalNumber() throws SQLException;

    List<Admin> findPartAdmin(int limit, int offset) throws SQLException;

    boolean deleteOneById(int aid) throws SQLException;

    boolean updateAdmin(Admin admin) throws SQLException;

    Admin login(String username, String password) throws SQLException;
}




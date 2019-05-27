package com.alex.store.dao.admin;

import com.alex.store.model.admin.Product;

import java.sql.SQLException;

public interface AddProductDao {
    void addProduct(Product product) throws SQLException;
}

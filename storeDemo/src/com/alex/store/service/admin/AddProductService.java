package com.alex.store.service.admin;

import com.alex.store.model.admin.Product;

import java.sql.SQLException;

public interface AddProductService {
    void addProduct(Product product) throws SQLException;
}

package com.alex.store.service.admin.impl;

import com.alex.store.dao.admin.AddProductDao;
import com.alex.store.dao.admin.impl.AddProductDaoImpl;
import com.alex.store.model.admin.Product;
import com.alex.store.service.admin.AddProductService;

import java.sql.SQLException;

public class AddProductServiceImpl implements AddProductService {
    AddProductDao addProductDao = new AddProductDaoImpl();
    @Override
    public void addProduct(Product product) throws SQLException {
        addProductDao.addProduct(product);

    }
}

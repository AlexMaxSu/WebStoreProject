package com.alex.store.service.admin;

import com.alex.store.model.admin.Product;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;
import java.util.List;

public interface ProductService {
    PageHelper<Product> findProductListByPagenumber(String num) throws SQLException;

    //boolean deleteOne(int pid) throws SQLException;

    void deleteMulti(String[] pids);

    boolean deleteOne(String pid) throws SQLException;

    void updateProduct(Product product) throws SQLException;

    Product findProductInfo(String pid) throws SQLException;

    PageHelper<Product> searchProduct(String pid, String cid, String pname, String minprice, String maxprice, int pageNumber) throws SQLException;

    List<Product> byCid(String cid) throws SQLException;


    Product findProductById(String pid) throws SQLException;

    List<Product> findProduct() throws SQLException;

    List<Product> findProByName(String pname) throws SQLException;
}

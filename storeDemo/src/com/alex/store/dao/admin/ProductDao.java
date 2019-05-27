package com.alex.store.dao.admin;

import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.Product;

import java.sql.SQLException;
import java.util.List;

public interface ProductDao {
    int findTotalNumber() throws SQLException;

    List<Product> findPartProduct(int limit, int offset) throws SQLException;

    //boolean deleteOneById(int pid) throws SQLException;

    void deleteMultiByIds(String[] pids);

    boolean deleteOneById(String pid) throws SQLException;

    void updateProduct(Product product) throws SQLException;


    Category findPageCategory(int cid) throws SQLException;

    Product findProductInfo(String pid) throws SQLException;

    int findTotalNumber(String pid, String cid, String pname, String minprice, String maxprice) throws SQLException;

    List<Product> findPartProduct(String pid, String cid, String pname, String minprice, String maxprice, int limit, int offset) throws SQLException;

    List<Product> byCid(String cid) throws SQLException;

    Product findProductById(String pid) throws SQLException;

    List<Product> findProduct() throws SQLException;

    List<Product> findProByName(String pname) throws SQLException;






    /*List<Product> findProductInfo() throws SQLException;*/
}

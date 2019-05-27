package com.alex.store.dao.admin.impl;

import com.alex.store.dao.admin.AddProductDao;
import com.alex.store.model.admin.Product;
import com.alex.store.util.MyC3PODataSouce;
import org.apache.commons.dbutils.QueryRunner;

import java.sql.SQLException;

public class AddProductDaoImpl implements AddProductDao {
    @Override
    public void addProduct(Product product) throws SQLException {
        QueryRunner qr = new QueryRunner(MyC3PODataSouce.getDataSource());
        String sql = "insert into product values(?, ?, ?, ?, ?, ?, ?, ?)";
        qr.update(sql, product.getPid(), product.getPname(), product.getEstoreprice(), product.getMarkprice(),
                product.getPnum(), product.getCid(), product.getImgurl(), product.getDescription());
    }
}

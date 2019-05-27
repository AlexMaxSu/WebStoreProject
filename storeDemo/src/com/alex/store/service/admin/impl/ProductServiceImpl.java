package com.alex.store.service.admin.impl;

import com.alex.store.dao.admin.ProductDao;
import com.alex.store.dao.admin.impl.ProductDaoImpl;
import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.Product;
import com.alex.store.service.admin.ProductService;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;
import java.util.List;

public class ProductServiceImpl implements ProductService {
    ProductDao productDao = new ProductDaoImpl();
    private static  final int PAGE_COUNT=3;

    @Override
    public PageHelper<Product> findProductListByPagenumber(String num) throws SQLException {

        int totalNumber = productDao.findTotalNumber();
        int currentPageNumber = Integer.parseInt(num);

        PageHelper<Product> pageHelper=new PageHelper<> (currentPageNumber, totalNumber,PAGE_COUNT);


        int limit=PAGE_COUNT;
        int offset= (currentPageNumber-1)*PAGE_COUNT;
        List<Product> products =productDao.findPartProduct(limit,offset);
        for (Product product : products) {
            Category category = productDao.findPageCategory(product.getCid());
            product.setCategory(category);
        }

        pageHelper.setRecords(products);

        return pageHelper;
    }

   /* @Override
    public boolean deleteOne(int pid) throws SQLException {
        return productDao.deleteOneById(pid);
    }*/

    @Override
    public void deleteMulti(String[] pids) {
        productDao.deleteMultiByIds(pids);
    }

    @Override
    public boolean deleteOne(String pid) throws SQLException {
        return productDao.deleteOneById(pid);
    }

    @Override
    public void updateProduct(Product product) throws SQLException {
        productDao.updateProduct(product);
    }

    @Override
    public Product findProductInfo(String pid) throws SQLException {
        return productDao.findProductInfo(pid);
    }

    @Override
    public PageHelper<Product> searchProduct(String pid, String cid, String pname, String minprice, String maxprice, int pageNumber) throws SQLException {
        int totalNumber = productDao.findTotalNumber(pid, cid, pname, minprice, maxprice);
        int currentPageNumber = pageNumber;

        PageHelper<Product> pageHelper=new PageHelper<> (currentPageNumber, totalNumber,PAGE_COUNT);


        int limit=PAGE_COUNT;
        int offset= (currentPageNumber-1)*PAGE_COUNT;
        List<Product> products =productDao.findPartProduct(pid, cid, pname, minprice, maxprice,limit,offset);
        pageHelper.setRecords(products);

        return pageHelper;
    }

    @Override
    public List<Product> byCid(String cid) throws SQLException {
        return productDao.byCid(cid);
    }

    @Override
    public Product findProductById(String pid) throws SQLException {
        return productDao.findProductById(pid);
    }

    @Override
    public List<Product> findProduct() throws SQLException {
        return productDao.findProduct();
    }

    @Override
    public List<Product> findProByName(String pname) throws SQLException {
        return productDao.findProByName(pname);
    }


}

package com.alex.store.service.admin.impl;

import com.alex.store.dao.admin.CategoryDao;
import com.alex.store.dao.admin.impl.CategoryDaoImpl;
import com.alex.store.model.admin.Category;
import com.alex.store.service.admin.CategoryService;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    CategoryDao categoryDao = new CategoryDaoImpl();
    private static  final int PAGE_COUNT=3;
    @Override
    public void addCategory(Category category) throws SQLException {

        categoryDao.addCategory(category);
    }

    @Override
    public List<Category> findAllCategory() throws SQLException {

        return categoryDao.findAllCategory();

    }

    @Override
    public PageHelper<Category> findCategoryListByPagenumber(String num) throws SQLException {
        int totalNumber = categoryDao.findTotalNumber();
        int currentPageNumber = Integer.parseInt(num);

        PageHelper<Category> pageHelper=new PageHelper<> (currentPageNumber, totalNumber,PAGE_COUNT);


        int limit=PAGE_COUNT;
        int offset= (currentPageNumber-1)*PAGE_COUNT;
        List<Category> categories =categoryDao.findPartCategory(limit,offset);
        pageHelper.setRecords(categories);

        return pageHelper;
    }

    @Override
    public boolean deleteCategory(int cid) throws SQLException {
        return categoryDao.deleteCategoryById(cid);
    }

    @Override
    public void deleteCategories(String[] cids) {
        categoryDao.deleteCategoriesByIds(cids);

    }

    @Override
    public boolean updateCategory(Category category) throws SQLException {
        return categoryDao.updateCategory(category);
    }

    @Override
    public Category getCategoryByCid(String cid) throws SQLException {
        int intcid = Integer.parseInt(cid);

        return categoryDao.getCategoryByCid(intcid);
    }

    @Override
    public List<Category> findCategory() throws SQLException {
        return categoryDao.findCategory();
    }

}

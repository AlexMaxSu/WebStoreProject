package com.alex.store.service.admin;

import com.alex.store.model.admin.Admin;
import com.alex.store.model.admin.Category;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;
import java.util.List;

public interface CategoryService {
    void addCategory(Category category) throws SQLException;
    List<Category> findAllCategory() throws SQLException;



    PageHelper<Category> findCategoryListByPagenumber(String num) throws SQLException;

    boolean deleteCategory(int cid) throws SQLException;

    void deleteCategories(String[] cids);

    boolean updateCategory(Category category) throws SQLException;

    Category getCategoryByCid(String cid) throws SQLException;

    List<Category> findCategory() throws SQLException;
}

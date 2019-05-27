package com.alex.store.dao.admin;

import com.alex.store.model.admin.Category;

import java.sql.SQLException;
import java.util.List;

public interface CategoryDao {
    void addCategory(Category category) throws SQLException;
    List<Category> findAllCategory() throws SQLException;

    int findTotalNumber() throws SQLException;

    List<Category> findPartCategory(int limit, int offset) throws SQLException;

    boolean deleteCategoryById(int cid) throws SQLException;

    void deleteCategoriesByIds(String[] cids);

    Category getCategoryByCid(int intcid) throws SQLException;

    boolean updateCategory(Category category) throws SQLException;

    List<Category> findCategory() throws SQLException;
}

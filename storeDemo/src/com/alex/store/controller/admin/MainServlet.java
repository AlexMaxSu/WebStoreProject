package com.alex.store.controller.admin;

import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.Product;
import com.alex.store.service.admin.CategoryService;
import com.alex.store.service.admin.ProductService;
import com.alex.store.service.admin.impl.CategoryServiceImpl;
import com.alex.store.service.admin.impl.ProductServiceImpl;
import com.alex.store.util.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@WebServlet(name = "MainServlet",urlPatterns = "/MainServlet")
public class MainServlet extends BaseServlet {

    public void loading(HttpServletRequest request, HttpServletResponse response) {

        try {
            CategoryService categoryService = new CategoryServiceImpl();
            List<Category> categories = categoryService.findCategory();
            ProductService productService = new ProductServiceImpl();
            List<Product> products = productService.findProduct();
            List<Product> topproducts = new ArrayList<>();
            List<Product> hotproducts = new ArrayList<>();
            Random random = new Random();
            //随机显示轮播商品
            for (int i = 0; i < 5; i++) {
                topproducts.add(products.get(random.nextInt(products.size())));
            }
            //随机显示热门商品
            for (int i = 0; i < 6; i++) {
                hotproducts.add(products.get(random.nextInt(products.size())));
            }
            HttpSession session = request.getSession();
            session.setAttribute("categories",categories);
            session.setAttribute("topproducts",topproducts);
            session.setAttribute("hotproducts",hotproducts);
            response.sendRedirect(request.getContextPath()+"/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

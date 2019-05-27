package com.alex.store.controller.admin;

import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.Product;
import com.alex.store.service.admin.CategoryService;
import com.alex.store.service.admin.impl.CategoryServiceImpl;
import com.alex.store.service.admin.impl.ProductServiceImpl;
import com.alex.store.util.BaseServlet;
import com.alex.store.util.PageHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@WebServlet(name = "CategoryServlet", urlPatterns = "/CategoryServlet")
public class CategoryServlet extends BaseServlet {
    CategoryService categoryService = new CategoryServiceImpl();

    public String addCategory(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            String cname = request.getParameter("cname");
            Category category = new Category();
            System.out.println(cname);
            category.setCname(cname);
            categoryService.addCategory(category);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    public void toUpdateCategory(HttpServletRequest request, HttpServletResponse response) {

        String cid = request.getParameter("cid");

        try {
            Category category =categoryService.getCategoryByCid(cid);

            request.setAttribute("category", category);

            request.getRequestDispatcher("/admin/category/updateCategory.jsp").forward(request, response);


        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void findAllCategory(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {

        //当前用户传入的分页的第几页
        String num = request.getParameter("num");
        if (num==null||num.isEmpty()){

            num="1";
        }

        PageHelper<Category>  pageHelper= null;
        try {
            pageHelper = categoryService.findCategoryListByPagenumber(num);
            request.setAttribute("page",pageHelper);
            request.getRequestDispatcher("/admin/category/categoryList.jsp").forward(request, response);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void deleteCategory(HttpServletRequest request,
                                HttpServletResponse response) throws ServletException, IOException {

        String cid_string = request.getParameter("cid");

        try {
            int cid = Integer.parseInt(cid_string);

            boolean ret = categoryService.deleteCategory(cid);
            System.out.println("CategoryServlet.updateCategory()" + ret);
            if (ret) {
                request.getRequestDispatcher(
                        "/CategoryServlet?op=findAllCategory")
                        .forward(request, response);
                updateCategoryListInCache();

            }

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().print(e.getMessage());
        }

    }

    public void updateCategory(HttpServletRequest request,
                                HttpServletResponse response)
            throws IOException, ServletException {

        String cid = request.getParameter("cid");
        String cname = request.getParameter("cname");

        try {
            Category category = new Category();
            category.setCid(Integer.parseInt(cid));
            category.setCname(cname);

            boolean ret = categoryService.updateCategory(category);
            System.out.println("CategoryServlet.updateCategory()" + ret);
            if (ret) {
                request.getRequestDispatcher("/CategoryServlet?op=findAllCategory&num=1").forward(request, response);
                updateCategoryListInCache();
            }

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().print(e.getMessage());
        }

    }
    private void  updateCategoryListInCache() {

    }

    public void deleteMulti(HttpServletRequest request,
                             HttpServletResponse response) throws IOException {

        String[] cids = request.getParameterValues("checkbox");
        System.out.println("CategoryServlet.deleteMulti()" + Arrays.toString(cids));
        if (cids == null || cids.length == 0) {
            response.sendRedirect(request.getContextPath() + "/CategoryServlet?op=findAllCategory&pageNum=1");
            return;
        }

        try {
            categoryService.deleteCategories(cids);
            response.getWriter().println("批量删除成功!<br>");

            response.setHeader("refresh", "1;url=" + request.getServletContext().getContextPath() + "/CategoryServlet?op=findAllCategory&pageNum=1");
        } catch (Exception e) {

            response.getWriter().println("批量删除失败!<br>" + e.getMessage());

            response.setHeader("refresh", "3;url=" + request.getServletContext().getContextPath() + "/CategoryServlet?op=findAllCategory&pageNum=1");
        }
    }




    //查询分类，转到商品增加页面
    public void findCategory(HttpServletRequest request, HttpServletResponse response) {
        List<Category> categories = null;
        try {
            categories = categoryService.findCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //将categoryList放到request域

        try {
            request.setAttribute("categories",categories);
            request.getRequestDispatcher("/admin/product/addProduct.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    //回显商品
    public void findCategoryByUpdate(HttpServletRequest request, HttpServletResponse response) {

        List<Category> categories = null;
        Product product = null;
        try {

            categories = categoryService.findCategory();


            String pid = request.getParameter("pid");
            ProductServiceImpl productService = new ProductServiceImpl();
            product = productService.findProductInfo(pid);

        } catch (Exception e) {
            e.printStackTrace();
        }


        //jsp页面修改：图片路径位应用名+数据库存储路径
       /* <img width="200px" height="180px" src="${pageContext.request.contextPath}/files/${product.imgurl}"/>

           改为 ：
            <img width="200px" height="180px" src="${pageContext.request.contextPath}${product.imgurl}"/>
*/
        try {
            request.setAttribute("categories",categories);
            request.setAttribute("product",product);
            request.getRequestDispatcher("/admin/product/updateProduct.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



   /* public void findIndexCategory(HttpServletRequest request, HttpServletResponse response) {
        List<Category> categories = null;
        try {
            categories = categoryService.findCategory();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //将categoryList放到request域

        try {
            request.setAttribute("categories",categories);
            request.getRequestDispatcher("/index.jsp").forward(request,response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

}

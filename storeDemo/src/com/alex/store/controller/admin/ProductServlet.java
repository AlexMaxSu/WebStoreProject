package com.alex.store.controller.admin;

import com.alex.store.model.admin.Admin;
import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.Product;
import com.alex.store.service.admin.CategoryService;
import com.alex.store.service.admin.ProductService;
import com.alex.store.service.admin.impl.CategoryServiceImpl;
import com.alex.store.service.admin.impl.ProductServiceImpl;
import com.alex.store.util.BaseServlet;
import com.alex.store.util.PageHelper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@WebServlet(name = "ProductServlet",urlPatterns = "/ProductServlet")
public class ProductServlet extends BaseServlet {
    ProductService productService = new ProductServiceImpl();



    public void deleteOne(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {

        String pid = request.getParameter("pid");

        try {
            //int pid = Integer.parseInt(pid_string);

            boolean ret = productService.deleteOne(pid);

            if (ret) {
                request.getRequestDispatcher(
                        "/ProductServlet?op=findAllProduct")
                        .forward(request, response);
                updateProductListInCache();

            }

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().print(e.getMessage());
        }

    }

    public void deleteMulti(HttpServletRequest request,
                            HttpServletResponse response) throws IOException {

        String[] pids = request.getParameterValues("checkbox");
        System.out.println("ProductServlet.deleteMulti()" + Arrays.toString(pids));
        if (pids == null || pids.length == 0) {
            response.sendRedirect(request.getContextPath() + "/ProductServlet?op=findAllProduct&pageNum=1");
            return;
        }

        try {
            productService.deleteMulti(pids);
            response.getWriter().println("批量删除成功!<br>");

            response.setHeader("refresh", "1;url=" + request.getServletContext().getContextPath() + "/ProductServlet?op=findAllProduct&pageNum=1");
        } catch (Exception e) {

            response.getWriter().println("批量删除失败!<br>" + e.getMessage());

            response.setHeader("refresh", "3;url=" + request.getServletContext().getContextPath() + "/ProductServlet?op=findAllProduct&pageNum=1");
        }
    }

    private void updateProductListInCache() {
    }





    public String updateProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Map<String, String[]> map = request.getParameterMap();
            // 将map中的数据拷贝到bean中
            Product product = new Product();
            BeanUtils.populate(product, map);
            // 调用service的方法
            productService.updateProduct(product);
            return "/ProductServlet?op=findAllProduct&pageNum=1";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }









    public void findAllProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //当前用户传入的分页的第几页

        String num = request.getParameter("num");
        if (num == null || num.isEmpty()) {

            num = "1";
        }
        PageHelper<Product> pageHelper = null;
        try {
            pageHelper = productService.findProductListByPagenumber(num);




            request.setAttribute("page", pageHelper);
            request.getRequestDispatcher("/admin/product/productList.jsp").forward(request, response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }




    public String searchProductUI(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            CategoryService categoryService = new CategoryServiceImpl();
            List<Category> categories = categoryService.findCategory();
            // 放入域中请求转发到修改页面
            request.setAttribute("categories", categories);
            return "/admin/product/searchProduct.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }




    public String searchProduct(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取前端的参数
            String pid = request.getParameter("pid");
            String cid = request.getParameter("cid");
            String pname = request.getParameter("pname");
            String minprice = request.getParameter("minprice");
            String maxprice = request.getParameter("maxprice");
            int pageNumber = Integer.parseInt(request.getParameter("pageNumber"));
            // 调用service的方法
            PageHelper<Product> pageHelper = productService.searchProduct(pid, cid, pname, minprice, maxprice, pageNumber);
            // 将list放入域对象中
            request.setAttribute("page", pageHelper);
            return "/admin/product/productList.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //商城首页按分类搜索,！！！注意查找商品pid用来查找商品详细信息！！！
    public String byCid(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String cid = request.getParameter("cid");
            List<Product> products = null;
            products = productService.byCid(cid);
            request.setAttribute("products",products);
            return "/products.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }



    //查看商品详细信息，根据按类查找获取商品的pid查找
    public String findProductById(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String pid = request.getParameter("pid");
            //查找商品详细信息是一个商品，所以不能放在list里，
            // jsp页面也不需要<c:forEach>遍历
            Product product = productService.findProductById(pid);
            request.setAttribute("product",product);
            return "/productdetail.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //根据名字搜索商品
    public String findProByName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            String pname = request.getParameter("pname");

            List<Product> products = productService.findProByName(pname);
            request.setAttribute("products",products);
            return "/products.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }


}

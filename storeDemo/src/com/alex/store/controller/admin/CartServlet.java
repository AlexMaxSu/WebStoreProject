package com.alex.store.controller.admin;

import com.alex.store.model.admin.Product;
import com.alex.store.model.admin.ShoppingCar;
import com.alex.store.model.admin.ShoppingItem;
import com.alex.store.model.admin.User;
import com.alex.store.service.admin.CartService;
import com.alex.store.service.admin.ProductService;
import com.alex.store.service.admin.UserService;
import com.alex.store.service.admin.impl.CartServiceImpl;
import com.alex.store.service.admin.impl.ProductServiceImpl;
import com.alex.store.service.admin.impl.UserServiceImpl;
import com.alex.store.util.BaseServlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CartServlet",urlPatterns = "/CartServlet")
public class CartServlet extends BaseServlet {

    CartService cartService = new CartServiceImpl();


    public String addCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // 获取前台的参数
            String pid = request.getParameter("pid");
            int uid = Integer.parseInt(request.getParameter("uid"));
            int snum = Integer.parseInt(request.getParameter("snum"));
            // 调用service获取指定的商品
            ProductService productService = new ProductServiceImpl();
            Product product = productService.findProductById(pid);
            // 调用service获取指定的用户
            UserService userService = new UserServiceImpl();
            User user = userService.findByUid(uid);
            // 封装ShoppingItem
            ShoppingItem shoppingItem = new ShoppingItem();
            shoppingItem.setProduct(product);
            shoppingItem.setSubcount(snum);
            // 获取购物车
            ShoppingCar shoppingCar = getCart(request);
            shoppingCar.addShoppingCar(shoppingItem);
            shoppingCar.setUser(user);
            // 放入request域对象中
            request.setAttribute("shoppingCar", shoppingCar);
            return "shoppingcart.jsp";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public ShoppingCar getCart(HttpServletRequest request) {
        HttpSession session = request.getSession();
        ShoppingCar  shoppingCar = (ShoppingCar) session.getAttribute("shoppingCar");
        if (shoppingCar == null) {
            shoppingCar = new ShoppingCar();
            session.setAttribute("shoppingCar", shoppingCar);
        }
        return shoppingCar;
    }


    //删除商品
    public String delItem(HttpServletRequest request, HttpServletResponse response){

        try {
            int uid = Integer.parseInt(request.getParameter("uid"));
            String pid = request.getParameter("pid");
            HttpSession session = request.getSession();
            User user = (User) session.getAttribute("user");
            if (uid == user.getUid()){
                ShoppingCar shoppingCar = getCart(request);
                shoppingCar.removeShoppingCar(pid);
                session.setAttribute("shoppingCar",shoppingCar);
            }
            response.sendRedirect(request.getContextPath()+"/shoppingcart.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;


    }



    public void findCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ShoppingCar shoppingCar = (ShoppingCar) session.getAttribute("shoppingCar");

        session.setAttribute("shoppingCar", shoppingCar);

        request.getRequestDispatcher("/shoppingcart.jsp").forward(request, response);

    }

}

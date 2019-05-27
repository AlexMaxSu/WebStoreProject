package com.alex.store.controller.admin;

import com.alex.store.service.admin.UserService;
import com.alex.store.service.admin.impl.UserServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet(name = "ActiveUserServlet",urlPatterns = "/ActiveUserServlet")
public class ActiveUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");
        if (code != null){
            UserService userService = new UserServiceImpl();
            boolean flag = false;
            try {
                flag = userService.active(code);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            String msg = null;
            if (flag){
                msg = "激活成功，请<a href='/user/login.jsp'>登录</a>";
            }else {
                msg = "激活失败！";
            }
            response.setContentType("text/html;charset=utf-8");
            response.getWriter().write(msg);
        }


    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}

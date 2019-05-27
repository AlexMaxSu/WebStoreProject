package com.alex.store.util;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;

@WebServlet(name = "BaseServlet")
public class BaseServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");

        try {
            Class clazz = this.getClass();
            Method op = clazz.getMethod(request.getParameter("op"), HttpServletRequest.class, HttpServletResponse.class);
            String value = (String) op.invoke(clazz.newInstance(), request, response);
            if (value != null){
                request.getRequestDispatcher(value).forward(request,response);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}

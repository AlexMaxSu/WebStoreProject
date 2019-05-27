package com.alex.store.controller.admin;

import com.alex.store.model.admin.Admin;
import com.alex.store.model.admin.Category;
import com.alex.store.model.admin.User;
import com.alex.store.service.admin.AdminService;
import com.alex.store.service.admin.CategoryService;
import com.alex.store.service.admin.impl.AdminServiceImpl;
import com.alex.store.service.admin.impl.CategoryServiceImpl;
import com.alex.store.util.BaseServlet;
import com.alex.store.util.PageHelper;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

@WebServlet(name = "AdminServlet",urlPatterns = "/AdminServlet")
public class AdminServlet extends BaseServlet {
    AdminService adminService = new AdminServiceImpl();

    public String addAdmin(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            //String password1 = request.getParameter("password1");
            Admin admin = new Admin();
            admin.setUsername(username);
            admin.setPassword(password);
            //admin.setPassword1(password1);
            System.out.println(username + "," + password);
            admin.setUsername(username);
            admin.setPassword(password);
            //admin.setPassword1(password1);
            adminService.addAdmin(admin);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteOne(HttpServletRequest request,
                               HttpServletResponse response) throws ServletException, IOException {

        String aid_string = request.getParameter("aid");

        try {
            int aid = Integer.parseInt(aid_string);

            boolean ret = adminService.deleteOne(aid);
            
            if (ret) {
                request.getRequestDispatcher(
                        "/AdminServlet?op=findAllAdmin")
                        .forward(request, response);
                updateAdminListInCache();

            }

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().print(e.getMessage());
        }

    }



    public String updateAdmin(HttpServletRequest request,
                               HttpServletResponse response)
            throws IOException, ServletException {

        //String aid = request.getParameter("aid");
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            Admin admin = new Admin();
            //admin.setAid(Integer.parseInt(aid));
            admin.setUsername(username);
            admin.setPassword(password);

            boolean ret = adminService.updateAdmin(admin);

            if (ret) {
                request.getRequestDispatcher("/AdminServlet?op=findAllAdmin&num=1").forward(request, response);
                updateAdminListInCache();
            }

        } catch (Exception e) {

            e.printStackTrace();
            response.getWriter().print(e.getMessage());
        }
        return null;

    }

    private void updateAdminListInCache() {
    }




    public void findAllAdmin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //当前用户传入的分页的第几页
        String num = request.getParameter("num");
        if (num == null || num.isEmpty()) {

            num = "1";
        }
        PageHelper<Admin> pageHelper = null;
        try {
            pageHelper = adminService.findAdminListByPagenumber(num);
            request.setAttribute("page", pageHelper);
            request.getRequestDispatcher("/admin/admin/adminList.jsp").forward(request, response);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //登录后台管理系统
    public String login(HttpServletRequest request,HttpServletResponse response) {

        try {
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            Admin admin = adminService.login(username,password);
            if (admin == null){
                //request.getRequestDispatcher("admin/index.jsp").forward(request,response);
                response.sendRedirect(request.getContextPath()+"/admin/index.jsp");
            } else {
                //放在session中到首页显示用户信息
                HttpSession session = request.getSession();
                session.setAttribute("admin", admin);
                response.sendRedirect(request.getContextPath()+"/admin/main.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //退出后台
    public String logout(HttpServletRequest request,HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.removeAttribute("admin");
        try {
            response.sendRedirect(request.getContextPath()+"/admin/index.jsp");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }




}

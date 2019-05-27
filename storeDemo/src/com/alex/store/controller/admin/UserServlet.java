package com.alex.store.controller.admin;

import com.alex.store.model.admin.User;
import com.alex.store.service.admin.UserService;
import com.alex.store.service.admin.impl.UserServiceImpl;
import com.alex.store.util.BaseServlet;
import com.alex.store.util.PageHelper;
import org.apache.commons.beanutils.BeanUtils;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;


@WebServlet(name = "UserServlet", urlPatterns = "/UserServlet")
public class UserServlet extends BaseServlet {
    UserService userService = new UserServiceImpl();


    //后台添加注册用户
    public String addUser(HttpServletRequest request, HttpServletResponse response) {

        try {

            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user, map);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            user.setUpdatetime(date);
            userService.addUser(user);
            return "/UserServlet?op=findAllUser";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //后台查找注册用户
    public void findAllUser(HttpServletRequest request, HttpServletResponse response) {
        String num = request.getParameter("num");
        if (num == null || num.isEmpty()) {

            num = "1";
        }
        PageHelper<User> pageHelper = null;
        try {
            pageHelper = userService.findUserListByPagenumber(num);
            request.setAttribute("page", pageHelper);
            request.getRequestDispatcher("/admin/user/userList.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }






    public void judgeForm(HttpServletRequest request, HttpServletResponse response) throws IOException, SQLException {
        String name = request.getParameter("username");
        boolean flag = userService.judgeName(name);
        if(flag) {
            response.getWriter().write("1");
        }else {
            response.getWriter().write("0");
        }
    }



    //用户注册
    public String regist(HttpServletRequest request, HttpServletResponse response) {

        try {
            Map<String, String[]> map = request.getParameterMap();
            User user = new User();
            BeanUtils.populate(user, map);
            String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            user.setUpdatetime(date);


            boolean ret = userService.regist(user);
            if (ret) {
                //注册成功！
                response.getWriter().println("<h1>注册成功！请到邮箱激活！</h1>");
                String contextPath = request.getContextPath();
                response.setHeader("refresh", "2;url='" + contextPath + "/user/login.jsp'");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    //登录
    public String login(HttpServletRequest request, HttpServletResponse response) {

        try {
            String verifyCode = request.getParameter("verifyCode");
            HttpSession session = request.getSession();
            String checkcode_session = (String) session.getAttribute("checkcode_session");
            //为了保证验证码只能使用一次
            session.removeAttribute("checkcode_session");
            if (verifyCode == null || !verifyCode.equals(checkcode_session)){
                request.setAttribute("msg", "验证码错误！");
                request.getRequestDispatcher("user/login.jsp").forward(request, response);
            }
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            User user = userService.login(username, password);

            if (user == null) {
                request.setAttribute("msg", "用户名或密码错误！");
                request.getRequestDispatcher("user/login.jsp").forward(request, response);
            } else {
                //放在session中到首页显示用户信息

                session.setAttribute("user", user);
                response.sendRedirect(request.getContextPath() + "/index.jsp");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //退出
    public String logout(HttpServletRequest request, HttpServletResponse response) {

        try {
            //获取session
            HttpSession session = request.getSession();
            //删除session的user的key
            session.removeAttribute("user");
            response.sendRedirect(request.getContextPath() + "/index.jsp");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

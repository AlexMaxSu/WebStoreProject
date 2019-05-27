package com.alex.store.service.admin.impl;

import com.alex.store.dao.admin.UserDao;
import com.alex.store.dao.admin.impl.UserDaoImpl;
import com.alex.store.model.admin.User;
import com.alex.store.service.admin.UserService;
import com.alex.store.util.MailUtils;
import com.alex.store.util.PageHelper;
import com.alex.store.util.UuidUtil;

import java.sql.SQLException;
import java.util.List;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();
    private static  final int PAGE_COUNT=3;
    @Override
    public void addUser(User user) throws SQLException {
        userDao.addUser(user);

    }

    @Override
    public PageHelper<User> findUserListByPagenumber(String num) throws SQLException {
        //前置信息
        int totalNumber = userDao.findTotalNumber();
        int currentPageNumber = Integer.parseInt(num);

        PageHelper<User> pageHelper=new PageHelper<> (currentPageNumber, totalNumber,PAGE_COUNT);;

        //分页也可以内聚到PageHelper里面
        //总的页码数，
        //每一页需要显示的记录数
        int limit=PAGE_COUNT;
        int offset= (currentPageNumber-1)*PAGE_COUNT;
        List<User> users =userDao.findPartUser(limit,offset);
        pageHelper.setRecords(users);

        return pageHelper;
    }

    //发送激活邮件
    @Override
    public boolean regist(User user) throws SQLException {
        user.setCode(UuidUtil.getUuid());
        user.setState("N");
        userDao.regist(user);
        String content = "<a href='http://localhost/ActiveUserServlet?code="+user.getCode()+"'>点击激活</a>";
        MailUtils.sendMail(user.getEmail(),content);
        return true;
    }

    @Override
    public User login(String username, String password) throws SQLException {
        return userDao.login(username,password);
    }

    @Override
    public User findByUid(int uid) throws SQLException {
        return userDao.findByUid(uid);
    }

    @Override
    public boolean active(String code) throws SQLException {
        User user = userDao.findByCode(code);
        if (user != null){
            userDao.updateState(user);
            return true;
        }
        return false;
    }

    @Override
    public boolean judgeName(String name) throws SQLException {
        int count = userDao.judgeName(name);
        if (count == 0) {
            return false;
        } else {
            return true;
        }
    }
}

package com.alex.store.service.admin.impl;

import com.alex.store.dao.admin.AdminDao;
import com.alex.store.dao.admin.impl.AdminDaoImpl;
import com.alex.store.model.admin.Admin;

import com.alex.store.model.admin.User;
import com.alex.store.service.admin.AdminService;
import com.alex.store.util.PageHelper;

import java.sql.SQLException;
import java.util.List;

public class AdminServiceImpl implements AdminService {
    AdminDao adminDao = new AdminDaoImpl();
    private static  final int PAGE_COUNT=3;
    @Override
    public void addAdmin(Admin admin) throws SQLException {
        adminDao.addAdmin(admin);

    }


    @Override
    public PageHelper<Admin> findAdminListByPagenumber(String num) throws SQLException {
        //前置信息
        int totalNumber = adminDao.findTotalNumber();
        int currentPageNumber = Integer.parseInt(num);

        PageHelper<Admin> pageHelper=new PageHelper<> (currentPageNumber, totalNumber,PAGE_COUNT);;

        //分页也可以内聚到PageHelper里面
        //总的页码数，
        //每一页需要显示的记录数
        int limit=PAGE_COUNT;
        int offset= (currentPageNumber-1)*PAGE_COUNT;
        List<Admin> admins =adminDao.findPartAdmin(limit,offset);
        pageHelper.setRecords(admins);

        return pageHelper;
    }

    @Override
    public boolean deleteOne(int aid) throws SQLException {
        return adminDao.deleteOneById(aid);
    }

    @Override
    public boolean updateAdmin(Admin admin) throws SQLException {
        return adminDao.updateAdmin(admin);
    }

    @Override
    public Admin login(String username, String password) throws SQLException {
        return adminDao.login(username,password);
    }
}




package com.alex.store.util;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class MyDBUtils {

    public static Connection getConnection() throws SQLException {
        ComboPooledDataSource dataSource = new ComboPooledDataSource();
        return dataSource.getConnection();
    }
    public static DataSource getDataSource(){
        return new ComboPooledDataSource();
    }
}

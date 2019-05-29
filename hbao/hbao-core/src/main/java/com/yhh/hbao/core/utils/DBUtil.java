package com.yhh.hbao.core.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.sql.*;

/**
 * author: youhh
 * date: 2018/5/30 下午3:21
 * description:
 */

//@Component
public class DBUtil {

    @Value("${denghui.jdbc.driver}")
    private String driver;

    @Value("${denghui.jdbc.url}")
    private String url;

    @Value("${denghui.jdbc.username}")
    private String username;

    @Value("${denghui.jdbc.password}")
    private String password;


    public Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName(driver);
            conn = DriverManager.getConnection(url, username,password);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }


    public void close(Connection conn, Statement stat, ResultSet rs) {
        if (null != rs) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != stat) {
            try {
                stat.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (null != conn) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }





}

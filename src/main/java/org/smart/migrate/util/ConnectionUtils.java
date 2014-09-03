/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.sql.DataSource;
import org.apache.commons.dbcp.BasicDataSource;
import org.smart.migrate.setting.DBSetting;

/**
 * Database connection tools
 * 数据库连接工具类
 * @author Sandy Duan
 */
public class ConnectionUtils {
    
    /**
     * connect to database
     * 根据数据库配置连接数据库
     * @param dbs
     * @return
     */
    public static Connection connect(DBSetting dbs){
        Connection conn = null;
        try {
            Class.forName(dbs.getdBType().getDriver());
	} catch (ClassNotFoundException e) {
            // 没有驱动
            e.printStackTrace();
        }
        DriverManager.setLoginTimeout(10);
        try {
            java.util.Properties prop = new java.util.Properties();
            prop.put("user", dbs.getUsername());
            prop.put("password", dbs.getPassword()==null?"":dbs.getPassword());
            conn = DriverManager.getConnection(dbs.getConnectUrl(), prop);
        } catch (SQLException e) {
            // 未知数据库错误
            e.printStackTrace();
        }
        return conn;
    }
    
    /**
     * disconnect to database
     * 断开数据库连接
     * @param connection
     */
    public static void disconnect(Connection connection){
        if(connection==null){
            return;
        }    
        try 
        {
            if (!connection.isClosed()) {
                    connection.close();
            }
        } catch (SQLException e) {
                    e.printStackTrace();
        }
    }
    
    /**
     * Create datasource by dbsettign
     * 根据数据库配置创建数据源
     * @param dBSetting
     * @return
     */
    public static DataSource createDataSource(DBSetting dBSetting){
       BasicDataSource dataSource = new BasicDataSource();
       dataSource.setDriverClassName(dBSetting.getdBType().getDriver());
       dataSource.setUrl(dBSetting.getConnectUrl());
       dataSource.setUsername(dBSetting.getUsername());
       dataSource.setPassword(dBSetting.getPassword());
       return dataSource;
    }
    
    
}

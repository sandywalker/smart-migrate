/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate;

import java.io.Serializable;

/**
 * DataBase Type Enum
 * 数据库类型枚举类
 * @author Sandy Duan
 */
public enum DBType implements Serializable{
    MySQL("com.mysql.jdbc.Driver","3306","jdbc:mysql://$host:$port/$database?useUnicode=true&characterEncoding=utf-8"),
    Oracle("oracle.jdbc.driver.OracleDriver","1521","jdbc:oracle:thin:@$host:$port/$database"),
    SQLServer("net.sourceforge.jtds.jdbc.Driver","1433","jdbc:jtds:sqlserver://$host:$port/$database"),
    DB2("com.ibm.db2.jcc.DB2Driver","50000","jdbc:db2://$host:$port/$database"),
    Access("sun.jdbc.odbc.JdbcOdbcDriver","","jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=$database"),
    Excel("com.googlecode.sqlsheet.Driver","","jdbc:xls:file:$database");
    
    private final String driver;
    private final String defaultPort;
    private final String urlPattern;
    
    
    private DBType(String driver,String defaultPort,String urlPattern){
        this.driver = driver;
        this.defaultPort = defaultPort;
        this.urlPattern = urlPattern;
    }

    /**
     * @return the driver
     */
    public String getDriver() {
        return driver;
    }

    /**
     * @return the defaultPort
     */
    public String getDefaultPort() {
        return defaultPort;
    }

    /**
     * @return the pattern
     */
    public String getUrlPattern() {
        return urlPattern;
    }
    
    
    
    
}

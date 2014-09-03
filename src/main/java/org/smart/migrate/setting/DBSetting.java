/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import org.smart.migrate.DBType;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.util.VelocityUtils;

/**
 * DataBase Settings Info,contains dbType,host,port,database name,username,password...
 * 数据库映射信息
 * @author Sandy Duan
 */
public class DBSetting implements  Serializable{
    
    private DBType dBType;
    
    private String host;
    private String port;
    private String database;
    private String username;
    private String password;

    public DBSetting(DBType dBType, String host, String port, String database, String username, String password) {
        
        this.dBType = dBType;
        this.host = StringUtils.isBlank(host)?"127.0.0.1":host;
        this.port = StringUtils.isBlank(port)?dBType.getDefaultPort():port;
        this.database = database;
        this.username = username;
        this.password = password;
    }

    public DBSetting() {
    }

    
    
    
    /**
     * @return the dBType
     */
    public DBType getdBType() {
        return dBType;
    }

    /**
     * @param dBType the dBType to set
     */
    public void setdBType(DBType dBType) {
        this.dBType = dBType;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * @param host the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * @param port the port to set
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * @param database the database to set
     */
    public void setDatabase(String database) {
        this.database = database;
    }

    /**
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * @param username the username to set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getConnectUrl(){
        Map<String,Object> context = new HashMap<String,Object>();
        context.put("host", host);
        context.put("port", port);
        context.put("database", database);
                
        return VelocityUtils.StringEvaluate(dBType.getUrlPattern(), context);
    }

    @Override
    public String toString() {
        return "DBSetting{" + "dBType=" + dBType + ", host=" + host + ", port=" + port + ", database=" + database + ", username=" + username + ", password=" + password + '}';
    }

   
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.smart.migrate.model.Field;

/**
 * Template of db actions
 * @author Sandy Duan
 */
public class MetaDaoTemplate {

    /**
     * find all user databases by sql
     * 根据查询语句获取用户所有的数据库
     * @param connection
     * @param sql
     * @return
     */
    public List<String> getDatabasesBySql(Connection connection,String sql){
          List<String> databases = new ArrayList<String>();
          try {
              PreparedStatement pstmt = connection.prepareStatement(sql); 
              ResultSet rs = pstmt.executeQuery();
              while (rs.next()) {
                databases.add(rs.getString(1));
              }
         }catch (SQLException e) {
             e.printStackTrace();
         }
         
          return databases;             
     }
     
    /**
     * get database  by DatabaseMetaData
     * 根据元数据获取所有用户数据库
     * @param connection
     * @return
     */
    public List<String> getDatabasesByDBMD(Connection connection){
         return null;
     }
     
    /**
     * get tables of database by sql
     * 根据查询语句获取用户表
     * @param connection
     * @param sql
     * @return
     */
    public List<String> getTablesBySql(Connection connection,String sql){
         List<String> tables = new ArrayList<String>();

        try {
            PreparedStatement pstmt = connection.prepareStatement(sql);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(tables);
        return tables;
     }
     
    /**
     * get user tables by DatabaseMetaData
     * 根据数据库元数据信息获取用户表
     * @param connection
     * @return
     */
    public List<String> getTablesByDBMD(Connection connection){
         List<String> databases = new ArrayList<String>();
         try {
            DatabaseMetaData dbmd = connection.getMetaData();
            ResultSet rs = dbmd.getTables(null, null, "%", new String[] { "TABLE" });
            while (rs.next()) {
                    databases.add(rs.getString(3));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return databases;
     }
    
    /**
     * get fields of table by sql
     * 根据查询语句获取用户表中的字段
     * @param connection 数据库连接
     * @param sql 查询语句
     * @param tableName 表名
     * @param mapper 字段映射接口
     * @param upcase 表名是否需要转大写
     * @param quoted
     * @return
     */
    public List<Field> getFieldsBySql(Connection connection,String sql,String tableName,MetaFieldMapper mapper,boolean upcase,boolean quoted){
         List<Field> fields = new ArrayList<Field>();

        try {
            
            String table = upcase?tableName.toUpperCase():tableName;
            if (quoted){
                table="'"+table+"'";
            }
            sql = sql.replace("?", table);
            PreparedStatement pstmt = connection.prepareStatement(sql);
            //pstmt.setString(1, table);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Field field = mapper.mappMetaField(rs);
                fields.add(field);
            }
            rs.close();
            pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Collections.sort(fields);
        return fields;
    }
    
    public List<Field> getFieldsBySql(Connection connection,String sql,String tableName,MetaFieldMapper mapper){
        return getFieldsBySql(connection, sql, tableName, mapper,false,true);
    }
    
    
     
    /**
     * get fields of table by ResultSetMetaData
     * 根据数据库元数据信息获取用户表中的字段
     * @param connection
     * @param tableName
     * @param sql
     * @return
     */
    public List<Field> getFieldsByRSMD(Connection connection,String tableName,String sql){
        List<Field> fields = new ArrayList<Field>();
		try {
                    // 使用参数，SQL语句无法执行
                    PreparedStatement pstmt = connection.prepareStatement(sql.replace("?", tableName));
                    // pstmt.setString(1, table);
                    ResultSet rs = pstmt.executeQuery();
                    ResultSetMetaData rsmd = rs.getMetaData();
                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                            Field field = new Field();
                            field.setName(rsmd.getColumnName(i + 1));
                            fields.add(field);
                    }
                    rs.close();
                    pstmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return fields;
     }
     
     
    
}

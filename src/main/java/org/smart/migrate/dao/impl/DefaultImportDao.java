/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao.impl;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.dao.ImportDao;
import org.smart.migrate.log.ImportLogger;
import org.smart.migrate.log.LogLevel;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableRelation;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.SettingUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

/**
 *
 * @author Sandy Duan
 */
public class DefaultImportDao implements ImportDao{

    
    protected JdbcTemplate sourceJdbcTemplate;
    protected  JdbcTemplate targetJdbcTemplate;
    
    private  ImportLogger importLogger;
    
    public DefaultImportDao(MigratePlan migratePlan,DataSource targetDataSource,ImportLogger importLogger){
        sourceJdbcTemplate = null;
        targetJdbcTemplate = null;
        if (targetDataSource!=null){
            targetJdbcTemplate = new JdbcTemplate(targetDataSource);
        }
        this.importLogger = importLogger;
    }
    
    public DefaultImportDao(DataSource sourceDataSource,DataSource targetDataSource,ImportLogger importLogger){
        sourceJdbcTemplate = null;
        targetJdbcTemplate = null;
        if (sourceDataSource!=null){
            sourceJdbcTemplate = new JdbcTemplate(sourceDataSource);
        }
        if (targetDataSource!=null){
            targetJdbcTemplate = new JdbcTemplate(targetDataSource);
        }
        this.importLogger = importLogger;
    }

    @Override
    public List<String> findAllSourcePrimaryKeys(TableSetting tableSetting) {
        String sql = "SELECT " + tableSetting.getSourcePK() + " FROM "+ tableSetting.getSourceTable();
        if (StringUtils.isNotBlank(tableSetting.getWhere())){
            sql+=" WHERE " + tableSetting.getWhere();
        }
        sql+=" ORDER BY " + tableSetting.getSourcePK();
        return sourceJdbcTemplate.queryForList(sql, String.class);
    }

    @Override
    public List<Map<String,Object>> findSourceByPrimaryKeys(TableSetting tableSetting, List<String> primaryKeys) {
        
        String sql = "SELECT " + SettingUtils.getSourceFields(tableSetting) + " FROM " + tableSetting.getSourceTable();
        sql+=" WHERE " + tableSetting.getSourcePK() + " in (:ids) ";
        
        NamedParameterJdbcTemplate namedParameterJdbcTemplate =   new NamedParameterJdbcTemplate(sourceJdbcTemplate);  
        MapSqlParameterSource parameters = new MapSqlParameterSource();  
        parameters.addValue("ids", primaryKeys);  
        return namedParameterJdbcTemplate.queryForList(sql, parameters);
        
    }

    @Override
    public void deleteTargetData(String table, String where) {
        String sql = "DELETE FROM  " + table;
        if (StringUtils.isNotBlank(where)){
            sql+=" WHERE " + where;
        }
        System.out.println(sql);
       targetJdbcTemplate.update(sql);
    }
    
     @Override
    public void deleteTargetDataByPrimaryKeys(TableSetting tableSetting, List<String> primaryKeys) {
        String sql = "DELETE FROM " + tableSetting.getTargetTable() + " WHERE " + tableSetting.getTargetPK() + " in (:ids)";
        NamedParameterJdbcTemplate namedParameterJdbcTemplate =   new NamedParameterJdbcTemplate(targetJdbcTemplate);  
        MapSqlParameterSource parameters = new MapSqlParameterSource();  
        parameters.addValue("ids", primaryKeys);  
        namedParameterJdbcTemplate.update(sql,parameters);
    }
    
    

    @Override
    public String getMaxTargetPrimaryKey(TableSetting tableSetting) {
       String sql = "SELECT MAX("+tableSetting.getTargetPK()+") FROM " + tableSetting.getTargetTable();
       String value = targetJdbcTemplate.queryForObject(sql, String.class);
       if (value==null){
           value = "0";
       }
       return value;
    }

    @Override
    public void toggleTargetSqlServerIdentity(String table, boolean enabled) {
       String toggle = enabled?"ON":"OFF";
       String sql =  "SET IDENTITY_INSERT " + table +" "+toggle;
       targetJdbcTemplate.update(sql);
    }

    @Override
    public void saveTargetData(TableSetting tableSetting, List<Map<String, Object>> targetDataList) {
        
        String sql = "INSERT INTO " + tableSetting.getTargetTable()  + " (" + SettingUtils.getTargetFields(tableSetting) + ")";
        sql+=" VALUES (" + SettingUtils.getTargetPreparedFields(tableSetting) + ")";
        
        NamedParameterJdbcTemplate namedParameterJdbcTemplate =   new NamedParameterJdbcTemplate(targetJdbcTemplate);  
        List<MapSqlParameterSource> parameterSources = new ArrayList<MapSqlParameterSource>();
        for(Map<String,Object> targetData:targetDataList){
             MapSqlParameterSource psource = new MapSqlParameterSource(); 
             psource.addValues(targetData);
             parameterSources.add(psource);
        }

        namedParameterJdbcTemplate.batchUpdate(sql, parameterSources.toArray(new MapSqlParameterSource[0]));
    }

    @Override
    public void updateTargetFKByPK(TableRelation relation, TableSetting tableSetting,String primaryKey,String foreignKey) {
        String sql = "UPDATE " + relation.getForeignTable() + " SET " + relation.getForeignKey() + " = ? WHERE " + tableSetting.getTargetPK() + " = ?";
        targetJdbcTemplate.update(sql, foreignKey,primaryKey);
    }

    @Override
    public void updateTargetRelatedFK(String foreignTable, String logicFK, String FK, String primaryTable, String logicPK, String PK) {
        String sql = "UPDATE " + foreignTable + " a SET " + FK + " = ( SELECT " + PK + " FROM " + primaryTable + " b WHERE a." + logicFK +" = b." + logicPK + " ) ";
        targetJdbcTemplate.update(sql);
    }

    
    
   

    
    
    
}

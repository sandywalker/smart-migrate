/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Migrate Plan 
 * 数据迁移方案
 * @author Sandy Duan
 */
public class MigratePlan  implements  Serializable{
    
    private String name;
    private DBSetting sourceDB = new DBSetting();
    private DBSetting targetDB = new DBSetting();

    private List<TableSetting> tableSettings = new ArrayList<TableSetting>();
    
    private List<TableRelation> sourceRelations = new ArrayList<TableRelation>();
    
    /**
     * create default new plan
     * 创建默认的新迁移方案
     * @return
     */
    public static MigratePlan createNewPlan(){
        MigratePlan migratePlan = new MigratePlan();
        migratePlan.setName("NewPlan");
        return migratePlan;
    }
    
    /**
     * find  source tableSetting by table name
     * 根据源表名找到对应的表设置信息
     * @param sourceTable
     * @return
     */
    public  TableSetting  getTableSettingBySourceTable(String sourceTable){
        if (sourceTable!=null){
            for(TableSetting tableSetting:tableSettings){
                if (sourceTable.equals(tableSetting.getSourceTable())) {
                     return tableSetting;     
                }
            }    
        }
        return null;
    }

    /**
     * find  target tableSetting by table name
     * 根据目标表名找到对应的表设置信息
     * @param targetTable
     * @return
     */
    public  TableSetting getTableSettingByTargetTable(String targetTable){
        if (targetTable!=null){
            for(TableSetting tableSetting:tableSettings){
                if (targetTable.equals(tableSetting.getTargetTable())) {
                     return tableSetting;     
                }
            }    
        }
        return null;
    }
    
    /**
     * caculate target  relations by source relations
     * 根据源表的关联关系推导出目标表的关联关系
     * @return
     */
    public List<TableRelation> getTargetRelations(){
        List<TableRelation> targetRelations = new ArrayList<TableRelation>();
        for(TableRelation relation:sourceRelations){
            
            String sourcePrimaryTable = relation.getPrimaryTable();
            String sourceForeignTable = relation.getForeignTable();
            //get table setting of the primary table
            //获取主键表相关的配置映射信息
            TableSetting primaryTableSetting = getTableSettingBySourceTable(sourcePrimaryTable);
            //get table setting of the foreign table
            //获取外键表相关的配置映射信息
            TableSetting foreignTableSetting = getTableSettingBySourceTable(sourceForeignTable);
            if (primaryTableSetting!=null&&foreignTableSetting!=null){
                //get foreign field setting
                //获取外键字段的配置信息
                FieldSetting foreignFieldSetting = foreignTableSetting.getFieldSettingBySourceField(relation.getForeignKey());
                if (foreignFieldSetting!=null){
                    TableRelation targetRelation = new TableRelation();
                    targetRelation.setPrimaryTable(primaryTableSetting.getTargetTable());
                    targetRelation.setPrimaryKey(primaryTableSetting.getTargetPK());
                    targetRelation.setForeignTable(foreignTableSetting.getTargetTable());
                    targetRelation.setForeignKey(foreignFieldSetting.getTargetField());
                    targetRelations.add(targetRelation);
                }
            }
            
        }
        
        return targetRelations;
    }
    
    /**
     * @return the sourceDB
     */
    public DBSetting getSourceDB() {
        return sourceDB;
    }

    /**
     * @param sourceDB the sourceDB to set
     */
    public void setSourceDB(DBSetting sourceDB) {
        this.sourceDB = sourceDB;
    }

    /**
     * @return the targetDB
     */
    public DBSetting getTargetDB() {
        return targetDB;
    }

    /**
     * @param targetDB the targetDB to set
     */
    public void setTargetDB(DBSetting targetDB) {
        this.targetDB = targetDB;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    

    /**
     * @return the tableSettings
     */
    public List<TableSetting> getTableSettings() {
        return tableSettings;
    }

    /**
     * @param tableSettings the tableSettings to set
     */
    public void setTableSettings(List<TableSetting> tableSettings) {
        this.tableSettings = tableSettings;
    }
   
    

    /**
     * @return the sourceRelations
     */
    public List<TableRelation> getSourceRelations() {
        return sourceRelations;
    }

    /**
     * @param sourceRelations the sourceRelations to set
     */
    public void setSourceRelations(List<TableRelation> sourceRelations) {
        this.sourceRelations = sourceRelations;
    }
   
        
    
}

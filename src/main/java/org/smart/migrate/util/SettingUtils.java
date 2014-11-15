/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.setting.FieldSetting;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableRelation;
import org.smart.migrate.setting.TableSetting;

/**
 * 字段及表设置工具类
 * @author Sandy Duan
 */
public class SettingUtils {
    
    /**
     * 判断表映射是否可以添加到现有的迁移方案中
     * @param tableSetting
     * @param migratePlan
     * @return
     */
    public static boolean isTableSettingCanAddToPlan(TableSetting tableSetting,MigratePlan migratePlan){
        for (TableSetting setting : migratePlan.getTableSettings()) {
            if (setting.getSourceTable().equals(tableSetting.getSourceTable())
                    ||setting.getTargetTable().equals(tableSetting.getTargetTable())){
                return false;
            }
        }

        
        return true;
    }
    
    /**
     * 根据迁移方案的表映射信息获取以外键字段为key的，关联关系为value的散列表
     * @param migratePlan
     * @param tableSetting
     * @return
     */
    public static Map<String,TableRelation> getSourceRelationsBySetting(MigratePlan migratePlan, TableSetting tableSetting){
        Map<String,TableRelation> relations = new HashMap<String,TableRelation>();
        for(TableRelation relation:migratePlan.getSourceRelations()){
            if (tableSetting.getSourceTable().equals(relation.getForeignTable())){
                for(FieldSetting fieldSetting:tableSetting.getFieldSettings()){
                    if (fieldSetting.getSourceField().equals(relation.getForeignKey())){
                        relations.put(fieldSetting.getSourceField(), relation);
                    }
                }
            }
        }
        return relations;
    }
    
    public static TableRelation getTargetRelationBySourceRelation(MigratePlan migratePlan,TableRelation tableRelation){
            String sourcePrimaryTable = tableRelation.getPrimaryTable();
            String sourceForeignTable = tableRelation.getForeignTable();
            //获取主键表相关的配置映射信息
            TableSetting primaryTableSetting = migratePlan.getTableSettingBySourceTable(sourcePrimaryTable);
            //获取外键表相关的配置映射信息
            TableSetting foreignTableSetting = migratePlan.getTableSettingBySourceTable(sourceForeignTable);
            if (primaryTableSetting!=null&&foreignTableSetting!=null){
                //获取外键字段的配置信息
                FieldSetting foreignFieldSetting = foreignTableSetting.getFieldSettingBySourceField(tableRelation.getForeignKey());
                if (foreignFieldSetting!=null){
                    TableRelation targetRelation = new TableRelation();
                    targetRelation.setPrimaryTable(primaryTableSetting.getTargetTable());
                    targetRelation.setPrimaryKey(primaryTableSetting.getTargetPK());
                    targetRelation.setForeignTable(foreignTableSetting.getTargetTable());
                    targetRelation.setForeignKey(foreignFieldSetting.getTargetField());
                    return targetRelation;
                }
            }
            return null;    
    }
    
    /**
     * 根据表关联关系重新排序表设置，表被引用次数越多，排越后
     * @param migratePlan
     */
    public static void sortTableSettingsByRelations(MigratePlan migratePlan){
        for(TableSetting tableSetting:migratePlan.getTableSettings()){
            tableSetting.setReferenceCount(0);
        }
        for(TableRelation tableRelation:migratePlan.getSourceRelations()){
             TableSetting tableSetting = migratePlan.getTableSettingBySourceTable(tableRelation.getPrimaryTable());
             if (tableSetting!=null){
                 tableSetting.setReferenceCount(tableSetting.getReferenceCount()+1);
             }
             
        }
        Collections.sort(migratePlan.getTableSettings(), new TableSettingComparator());
    }
    
    
    
    
    /**
     * 获取源数据表的字段 如 field1,field2,field3
     * @param tableSetting
     * @return
     */
    public static String getSourceFields(TableSetting tableSetting){
        String fields="";
        for(FieldSetting fieldSetting:tableSetting.getFieldSettings()){
            fields+=","+fieldSetting.getSourceField();
        }
        
        return fields.replaceFirst(",", "");
    }
    
    /**
     * 获取目标数据表的字段 如 field1,field2,field3
     * @param tableSetting
     * @return
     */
    public static String getTargetFields(TableSetting tableSetting){
        String fields="";
        for(FieldSetting fieldSetting:tableSetting.getFieldSettings()){
            fields+=","+fieldSetting.getTargetField();
        }
        return fields.replaceFirst(",", "");
    }
    
    /**
     * 获取目标数据表的字段 如 field1,field2,field3
     * @param tableSetting
     * @return
     */
    public static String getTargetPreparedFields(TableSetting tableSetting){
        String fields="";
        for(FieldSetting fieldSetting:tableSetting.getFieldSettings()){
            fields+=",:"+fieldSetting.getTargetField();
        }
        return fields.replaceFirst(",", "");
    }
    
    
    public static TableRelation getRelationByPKTableAndFKTable(MigratePlan migratePlan, String pkTable,String fkTable){
        if (StringUtils.isNotBlank(pkTable)&&StringUtils.isNotBlank(fkTable)){
            for(TableRelation relation:migratePlan.getSourceRelations()){
                if (pkTable.equals(relation.getPrimaryTable())&&fkTable.equals(relation.getForeignTable())){
                    return relation;
                }
            }
        }
        return null;
    }


}

class TableSettingComparator implements  Comparator<TableSetting>{

    @Override
    public int compare(TableSetting o1, TableSetting o2) {
        return o1.getReferenceCount()-o2.getReferenceCount();
    }

 
 
    
}

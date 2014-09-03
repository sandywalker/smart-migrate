/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.context;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableRelation;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.SettingUtils;

/**
 * Import Data Context,Use to update table relations and data rollback 
 * 信息导入上下文环境,主要用于更新表关联关系，数据回滚等操作
 * @author Sandy Duan
 */
public class ImportContext {
    
    //已映射主键信息
    private final Map<TableSetting,Map<String,String>> mappedKeys = new HashMap<TableSetting,Map<String,String>>();
    
    //源表主键->源表外键信息
    private final Map<TableRelation,Map<String,String>> sourceRelationKeys = new HashMap<TableRelation,Map<String,String>>();
    
    //目标表主键 ->目标表外键信息
    private final Map<TableRelation,Map<String,String>> targetRelationKeys = new HashMap<TableRelation,Map<String,String>>();
    
    
    
    /**
     * 添加主键映射
     * @param tableSetting
     * @param src
     * @param target
     */
    public void addMappedKey(TableSetting tableSetting,String src,String target){
        Map<String,String> mkeys = getMappedKeys().get(tableSetting);
        if (mkeys==null){
            mkeys = new HashMap<String,String>();
            getMappedKeys().put(tableSetting, mkeys);
        }
        if (StringUtils.isNotBlank(src)&&StringUtils.isNotBlank(target)){
            mkeys.put(src, target);
        }
    }
    
    /**
     * 清除某表设置对应的主键映射信息
     * @param tableSetting
     */
    public void removeMappedKeys(TableSetting tableSetting){
         if (getMappedKeys().containsKey(tableSetting)){
             getMappedKeys().remove(tableSetting);
         }
    }
    
    /**
     * 添加源表的关联信息
     * @param tableRelation
     * @param pk
     * @param fk
     */
    public void addSourceRelationKey(TableRelation tableRelation,String pk,String fk){
        Map<String,String> keys = getSourceRelationKeys().get(tableRelation);
        if (keys==null){
            keys = new HashMap<String,String>();
            getSourceRelationKeys().put(tableRelation, keys);
        }
        if (StringUtils.isNotBlank(pk)&&StringUtils.isNotBlank(fk)){
            keys.put(pk, fk);
        }
    }
    
    /**
     * 清除表关联关系包含的映射字段信息
     * @param tableRelation
     */
    public void removeSourceRelationKeys(TableRelation tableRelation){
        if (getSourceRelationKeys().containsKey(tableRelation)){
            getSourceRelationKeys().remove(tableRelation);
        }
    }
    /**
     * 初始化目标表的主外键信息
     * @param migratePlan
     */
    public void initTargetRelationsKeys(MigratePlan migratePlan){
        getTargetRelationKeys().clear();
        for (Map.Entry<TableRelation, Map<String, String>> entry : getSourceRelationKeys().entrySet()) {
            TableRelation relation = entry.getKey();
            Map<String, String> srcKeys = entry.getValue();
            
            //获取源主键表的配置
            TableSetting primaryTableSetting = migratePlan.getTableSettingBySourceTable(relation.getPrimaryTable());
            //获取源外键表的配置
            TableSetting foreignTableSetting = migratePlan.getTableSettingBySourceTable(relation.getForeignTable());
            if (primaryTableSetting!=null&&foreignTableSetting!=null){
                //获取主键表的字段映射
                Map<String,String> pmkeys =  getMappedKeys().get(primaryTableSetting);
                //获取外键表的字段映射
                Map<String,String> fmkeys =  getMappedKeys().get(foreignTableSetting);
                
                if (pmkeys!=null&&!pmkeys.isEmpty()&&fmkeys!=null&&!fmkeys.isEmpty()){
                    Map<String,String> trKeys = new HashMap<String,String> ();
                    for (Map.Entry<String, String> srcKey : srcKeys.entrySet()) {
                        String srcPK = srcKey.getKey();
                        String srcFK = srcKey.getValue();
                        //获取目标表的主键
                        String tgtPK = fmkeys.get(srcPK);
                        //获取目标表的外键，这个外键是要获取的关键信息
                        String tgtFK = pmkeys.get(srcFK);
                        if (StringUtils.isNotBlank(tgtPK)&&StringUtils.isNotBlank(tgtFK)){
                            trKeys.put(tgtPK, tgtFK);
                        }
                    }
                    TableRelation targetRelation = SettingUtils.getTargetRelationBySourceRelation(migratePlan, relation);
                    if (targetRelation!=null&&trKeys.size()>0){
                        getTargetRelationKeys().put(targetRelation, trKeys);
                    }
                }
            }
        }
    }
    
    public void printTargetRelations(){
        for (Map.Entry<TableRelation, Map<String, String>> entry : targetRelationKeys.entrySet()) {
            TableRelation tableRelation = entry.getKey();
            Map<String, String> map = entry.getValue();
            System.out.println(tableRelation.getForeignTable());
            for (Map.Entry<String, String> k : map.entrySet()) {
                String pk = k.getKey();
                String fk = k.getValue();
                System.out.println(pk+","+fk);
            }
            
        }
    }

    /**
     * @return the mappedKeys
     */
    public Map<TableSetting,Map<String,String>> getMappedKeys() {
        return mappedKeys;
    }

    /**
     * @return the sourceRelationKeys
     */
    public Map<TableRelation,Map<String,String>> getSourceRelationKeys() {
        return sourceRelationKeys;
    }

    /**
     * @return the targetRelationKeys
     */
    public Map<TableRelation,Map<String,String>> getTargetRelationKeys() {
        return targetRelationKeys;
    }
    
    
    
    
    
    
    
    
    
    
    
            
}

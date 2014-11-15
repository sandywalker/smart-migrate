/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao;

import java.util.List;
import java.util.Map;
import org.smart.migrate.setting.TableRelation;
import org.smart.migrate.setting.TableSetting;

/**
 * ImportDao acitons
 * @author Sandy Duan
 */
public interface ImportDao {
    
    
    public List<String> findAllSourcePrimaryKeys(TableSetting tableSetting);
    
    public List<Map<String,Object>> findSourceByPrimaryKeys(TableSetting tableSetting, List<String> primaryKeys);
    
    public void deleteTargetData(String table,String where);
    
    public void deleteTargetDataByPrimaryKeys(TableSetting tableSetting,List<String> primaryKeys);
    
    public void saveTargetData(TableSetting tableSetting, List<Map<String,Object>> targetDataList);
    
    public String getMaxTargetPrimaryKey(TableSetting tableSetting);
    
    public void updateTargetFKByPK(TableRelation relation,TableSetting tableSetting,String primaryKey,String foreignKey);
    
    
    

    public void toggleTargetSqlServerIdentity(String table, boolean enabled);
    
    public void updateTargetRelatedFK(String foreignTable,String logicFK,String FK,String primaryTable,String logicPK,String PK);
            
}

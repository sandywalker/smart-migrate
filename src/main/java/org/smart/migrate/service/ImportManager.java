/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.JOptionPane;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.DBType;
import org.smart.migrate.PKStrategy;
import org.smart.migrate.context.ImportContext;
import org.smart.migrate.dao.ImportDao;
import org.smart.migrate.dao.impl.DefaultImportDao;
import org.smart.migrate.log.ImportLogger;
import org.smart.migrate.log.LogLevel;
import org.smart.migrate.setting.FieldSetting;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableRelation;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.ConnectionUtils;
import org.smart.migrate.util.SettingUtils;

/**
 * Core Class To Import
 * @author Sandy Duan
 */
 public class ImportManager {

    private static final int DEFAULT_TIMEOUT = 10000;
    private static final int DEFAULT_BATCH_SIZE = 50;
    
    private DataSource sourceDataSource;
    private DataSource targetDataSource;
    private MigratePlan migratePlan;
    private ImportLogger importLogger;
    
    private ImportDao importDao;
    
    private final ImportContext importContext;
    
    private int batchSize = DEFAULT_BATCH_SIZE;
    
    public ImportManager(){
        super();
        this.importContext = new ImportContext();
    }
    
    public boolean isConnected(){
        try {
            return sourceDataSource!=null&&!sourceDataSource.getConnection().isClosed()
                    &&targetDataSource!=null&&!targetDataSource.getConnection().isClosed();
        } catch (SQLException ex) {
            Logger.getLogger(ImportManager.class.getName()).log(Level.SEVERE, null, ex);
            importLogger.log(LogLevel.ERROR, ex.getMessage());
        }
        return false;
    }
    
    public void closeConnection(){
        if (sourceDataSource!=null){
            try {
                ConnectionUtils.disconnect(sourceDataSource.getConnection());
            } catch (SQLException ex) {
                Logger.getLogger(ImportManager.class.getName()).log(Level.SEVERE, null, ex);
                importLogger.log(LogLevel.ERROR, ex.getMessage());
            }
        }
        if (targetDataSource!=null){
            try {
                ConnectionUtils.disconnect(targetDataSource.getConnection());
            } catch (SQLException ex) {
                Logger.getLogger(ImportManager.class.getName()).log(Level.SEVERE, null, ex);
                importLogger.log(LogLevel.ERROR, ex.getMessage());
            }
        }
    }
    
    public boolean connectToDataBase(boolean source,boolean target){
        sourceDataSource = null;
        targetDataSource = null;
        if (source){
            sourceDataSource = ConnectionUtils.createDataSource(getMigratePlan().getSourceDB());
        }
        if (target){
            targetDataSource = ConnectionUtils.createDataSource(getMigratePlan().getTargetDB());
        }
        importDao = new DefaultImportDao(sourceDataSource, targetDataSource,importLogger);
        return isConnected();
    }
    
    public boolean connectToDataBase(){
       return connectToDataBase(true, true);
    }
    
    /**
     * Delete target table's data
     * 删除目标表的数据
     * @param table
     * @param where
     */
    public void deleteTargetData(String table,String where){
        if (importDao==null){
            throw  new IllegalArgumentException("importDao is null!");
        }
        importDao.deleteTargetData(table, where);
    }
    
    /**
     * Find all table's primary keys 
     * 查找所有源数据表的主键
     * @param tableSetting
     * @return
     */
    public List<String> findAllSourcePrimaryKeys(TableSetting tableSetting){
        return importDao.findAllSourcePrimaryKeys(tableSetting);
    }
    
    /**
     * Find source Data By PrimaryKey
     * 根据主键列表查找对应的数据
     * @param tableSetting
     * @param primaryKeys
     * @return
     */
    public List<Map<String,Object>> findSourceByPrimaryKeys(TableSetting tableSetting, List<String> primaryKeys){
        return importDao.findSourceByPrimaryKeys(tableSetting, primaryKeys);
    }
    
    /**
     * Convert source data to Target
     * 将源表的数据列表转换到目标数据列表
     * @param tableSetting
     * @param sourceList
     * @return
     */
    public List<Map<String,Object>> convertSourctToTarget(TableSetting tableSetting,List<Map<String,Object>> sourceList){
        
        Long newPrimaryKey = 0l;
        if (tableSetting.getpKStrategy()==PKStrategy.NEW){
            String maxPK = importDao.getMaxTargetPrimaryKey(tableSetting);
            try {
               newPrimaryKey = Long.parseLong(maxPK); 
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
                return null;
            }
        }
        
        List<Map<String,Object>> targetList = new ArrayList<Map<String,Object>>();
        //Acquire source relations
        //获取源表的关系
        Map<String,TableRelation> sourceRelations = SettingUtils.getSourceRelationsBySetting(migratePlan, tableSetting);
        
        for (Map<String, Object> sourceData : sourceList) {
            String sourcePrimaryKey = String.valueOf(sourceData.get(tableSetting.getSourcePK()));
            String targetPrimaryKey;
            
            if (tableSetting.getpKStrategy()==PKStrategy.NEW){
                newPrimaryKey++;
                targetPrimaryKey = String.valueOf(newPrimaryKey);
            }else{
                targetPrimaryKey = sourcePrimaryKey;
            }
            if (StringUtils.isBlank(targetPrimaryKey)){
                JOptionPane.showMessageDialog(null,"无法确定目标表的主键！");
                importLogger.log(LogLevel.ERROR, targetPrimaryKey);
                return null;
            }
            Map<String,Object> targetData = new HashMap<String,Object>();
            for(FieldSetting fieldSetting:tableSetting.getFieldSettings()){
                Object sourceValue = sourceData.get(fieldSetting.getSourceField());
                
                if (fieldSetting.isPrimaryKey()){
                    //If is PK,add PKMappings to Context
                    //如果是主键，则添加主键映射
                    importContext.addMappedKey(tableSetting, sourcePrimaryKey, targetPrimaryKey);
                    targetData.put(fieldSetting.getTargetField(), targetPrimaryKey);
                }else{
                    TableRelation relation = sourceRelations.get(fieldSetting.getSourceField());
                    if (relation!=null){
                        //if source has relation ,add relation to Context
                        //如果源有关联，则添加关联关系
                        importContext.addSourceRelationKey(relation,sourcePrimaryKey, String.valueOf(sourceValue));
                        targetData.put(fieldSetting.getTargetField(), null);
                    }else{
                        //normal fields,copy/convert to target data
                       //其它字段，复制转换到目标数据
                       if (sourceValue==null||StringUtils.isBlank(sourceValue.toString().trim())){
                           targetData.put(fieldSetting.getTargetField(), fieldSetting.getDefaultValue());
                       }else if (StringUtils.isNotBlank(fieldSetting.getDictText())){
                           String dictValue = fieldSetting.getDict().get(sourceValue.toString());
                           targetData.put(fieldSetting.getTargetField(), dictValue);
                       }else{
                           targetData.put(fieldSetting.getTargetField(),sourceValue);
                       }
                    }
                }
                
            }
            targetList.add(targetData);
        }
        
        return targetList;
    }
    
    /**
     * split source pk list to sublist with batch size
     * 将主键列表按批次大小打包成子列表
     * @param primaryKeys
     * @return
     */
    public List<List<String>> splitKeysByBatchSize(List<String> primaryKeys){
        List<List<String>> spList = new ArrayList<List<String>>();
        
        int count = primaryKeys.size()/batchSize;
        if (primaryKeys.size()%batchSize>0){
            count +=1;
        }
        int index = 0;
        for (int i = 0; i < count; i++) {
            List<String> list = new ArrayList<String>();
            for (int j = 0; j < batchSize; j++) {
                if (index<primaryKeys.size()){
                list.add(primaryKeys.get(index));
                index++;
            }    
            }
            spList.add(list);
        }
        return spList;
    }
    
    /**
     * batch import source data
     * 批量导入源表的数据
     * @param tableSetting
     * @param sourceDataList
     */
    public void batchImportSourceData(TableSetting tableSetting,List<Map<String,Object>> sourceDataList){
        List<Map<String,Object>> targetDataList = convertSourctToTarget(tableSetting, sourceDataList);
        importDao.saveTargetData(tableSetting, targetDataList);
    }
    
    /**
     * update table relations
     * 更新目标表关联关系
     */
    public void updateReferences(){
        importContext.initTargetRelationsKeys(migratePlan);
        Map<TableRelation,Map<String,String>> relations = importContext.getTargetRelationKeys();
        for (Map.Entry<TableRelation, Map<String, String>> relationKeys : relations.entrySet()) {
            TableRelation tableRelation = relationKeys.getKey();
            TableSetting tableSetting = migratePlan.getTableSettingByTargetTable(tableRelation.getForeignTable());
            Map<String, String> keys = relationKeys.getValue();
            for (Map.Entry<String, String> k : keys.entrySet()) {
                String pk = k.getKey();
                String fk = k.getValue();
                importDao.updateTargetFKByPK(tableRelation, tableSetting, pk, fk);
            }
        }
        
    }
    
    /**
     * delete imported data
     * 删除已导入的数据
     * @param tableSetting
     */
    public void deleteImportedData(TableSetting tableSetting){
            Map<String,String> primaryKeys = importContext.getMappedKeys().get(tableSetting);
            if (primaryKeys!=null){
                List<String> pkList = new ArrayList<String>();
                for (Map.Entry<String, String> k : primaryKeys.entrySet()) {
                    String sk = k.getKey();
                    String tk = k.getValue();
                    pkList.add(tk);
                }
                int index = 0;
                List<List<String>> keys = splitKeysByBatchSize(pkList);
                for(List<String> pks:keys){
                    importDao.deleteTargetDataByPrimaryKeys(tableSetting, pks);
                }
            }
            
    }
    
    public boolean isTableImported(TableSetting tableSetting){
        return importContext.getMappedKeys().containsKey(tableSetting);
    }
    
    public void resetContext(){
        importContext.getMappedKeys().clear();
        importContext.getSourceRelationKeys().clear();
        importContext.getTargetRelationKeys().clear();
        
    }
    
    /**
     * toggle Identity (for microsoft sql server)
     * 开关数据库的自动生成ID
     * @param table
     * @param enabled
     */
    public void toggleIdentity(String table, boolean enabled){
         if (migratePlan.getTargetDB().getdBType()==DBType.SQLServer){
            importDao.toggleTargetSqlServerIdentity(table, enabled);
         }
    }

    /**
     * @return the batchSize
     */
    public int getBatchSize() {
        return batchSize;
    }

    /**
     * @param batchSize the batchSize to set
     */
    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }

    /**
     * @return the migratePlan
     */
    public MigratePlan getMigratePlan() {
        return migratePlan;
    }

    /**
     * @param migratePlan the migratePlan to set
     */
    public void setMigratePlan(MigratePlan migratePlan) {
        this.migratePlan = migratePlan;
        closeConnection();
        this.importDao = null;
    }

    /**
     * @return the importLogger
     */
    public ImportLogger getImportLogger() {
        return importLogger;
    }

    /**
     * @param importLogger the importLogger to set
     */
    public void setImportLogger(ImportLogger importLogger) {
        this.importLogger = importLogger;
    }
    
    
    
}

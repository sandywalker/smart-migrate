/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.sql.DataSource;
import org.junit.Before;
import org.junit.Test;
import org.smart.migrate.DBType;
import org.smart.migrate.dao.ImportDao;
import org.smart.migrate.dao.impl.DefaultImportDao;
import org.smart.migrate.util.ConnectionUtils;

/**
 *
 * @author Sandy Duan
 */
public class ImportDaoTest {
    
    private TableSetting tableSetting;
    private ImportDao importDao;
    
    @Before
    public void init(){
        DBSetting srcdbs = new DBSetting(DBType.MySQL, "localhost", "3306", "smartData", "root", null);
        DBSetting tgtdbs = new DBSetting(DBType.MySQL, "localhost", "3306", "testDB", "root", null);
        DataSource srcDS = ConnectionUtils.createDataSource(srcdbs);
        DataSource tgtDS = ConnectionUtils.createDataSource(tgtdbs);
        importDao = new DefaultImportDao(srcDS, tgtDS,null);
        tableSetting = new TableSetting();
        tableSetting.setSourceTable("gen_fawen");
        tableSetting.setTargetTable("mytable");
       
        FieldSetting fieldSetting = new FieldSetting();
        fieldSetting.setSourceField("id");
        fieldSetting.setPrimaryKey(true);
        fieldSetting.setTargetField("id");
        
        FieldSetting fieldSetting2 = new FieldSetting();
        fieldSetting2.setSourceField("title");
        fieldSetting2.setTargetField("title");
        
        tableSetting.getFieldSettings().add(fieldSetting);
        tableSetting.getFieldSettings().add(fieldSetting2);
        
       
    }
    
    @Test
    public void testFindSourcePrimaryKeys(){
//         List<String> keys =  importDao.findAllSourcePrimaryKeys(tableSetting);
//        for(String key:keys){
//            System.out.println(key);
//        }
    }
    
    @Test
    public void testFindByPrimaryKeys(){
       List<String> keys = new ArrayList<String>();
       keys.add("52");
       keys.add("59");
       List<Map<String,Object>> data = importDao.findSourceByPrimaryKeys(tableSetting, keys);
        System.out.println(data.size());
        for (Map<String, Object> map : data) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String string = entry.getKey();
                Object object = entry.getValue();
                System.out.println(string+","+object);
            }
        }
    }
    
    @Test
    public void testGetMaxTargetPrimaryKey(){
        System.out.println(importDao.getMaxTargetPrimaryKey(tableSetting)); 
    }
    
    @Test
    public void testSaveTargetData(){
        List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String,Object> data1 = new HashMap<String, Object>();
        data1.put("id", "1");
        data1.put("title", "test title");
        
        Map<String,Object> data2 = new HashMap<String, Object>();
        data2.put("id", "2");
        data2.put("title", "test title2");
        
        dataList.add(data1);
        dataList.add(data2);
        
        importDao.saveTargetData(tableSetting, dataList);
       
    }
}

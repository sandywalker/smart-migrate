/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.smart.migrate.DBType;

/**
 * 测试迁移方案的序列号与反序列化
 * @author Sandy Duan
 */

public class MigratePlanSerializeTest {
    
    private MigratePlan migratePlan;
    
    @Before
    public void init(){
        migratePlan = new MigratePlan();
        migratePlan.setName("testMigraPlan");
        DBSetting sourceDB = new DBSetting();
        sourceDB.setDatabase("smartData");
        sourceDB.setHost("127.0.0.1");
        sourceDB.setPort(DBType.MySQL.getDefaultPort());
        sourceDB.setdBType(DBType.MySQL);
        sourceDB.setUsername("root");
        sourceDB.setPassword("");
        
        DBSetting targetDB = new DBSetting();
        targetDB.setDatabase("testDB");
        targetDB.setHost("127.0.0.1");
        targetDB.setPort(DBType.MySQL.getDefaultPort());
        targetDB.setdBType(DBType.MySQL);
        targetDB.setUsername("root");
        targetDB.setPassword("");
        
        TableSetting table1 = new TableSetting();
        table1.setSourceTable("example_books");
        table1.setTargetTable("example_books");
        table1.setWhere("id>100");
        
        
        TableSetting table2 = new TableSetting();
        table2.setSourceTable("file");
        table2.setTargetTable("targetFile");
        
        List<TableSetting> tableSettings = new ArrayList<TableSetting>();
        tableSettings.add(table1);
        tableSettings.add(table2);
        
        FieldSetting fs1 = new FieldSetting();
        fs1.setSourceField("name");
        fs1.setTargetField("title");
        fs1.setDictText("001=a,002=b");
        
        
        List<FieldSetting> fieldSettings = new ArrayList<FieldSetting>();
        
        fieldSettings.add(fs1);
        
        table1.setFieldSettings(fieldSettings);
        
        
        migratePlan.setTableSettings(tableSettings);
        migratePlan.setSourceDB(sourceDB);
        migratePlan.setTargetDB(targetDB);
        
        
    }
    
//    @Test
//    public void testSerializeOrigin() throws FileNotFoundException, IOException{
//        OutputStream fos = new FileOutputStream("migratePlan.dat");
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        oos.writeObject(migratePlan);
//        
//    } 
//    
//    @Test
//    public void testDeSerializeOrigin() throws FileNotFoundException, IOException, ClassNotFoundException{
//        InputStream fis = new FileInputStream("migratePlan.dat");
//        ObjectInputStream ois = new ObjectInputStream(fis);
//        Object o = ois.readObject();
//        System.out.println(o);
//        
//    } 
    
     @Test
    public void testSerialize() throws FileNotFoundException, IOException{
         MigratePlanIO.serialize(migratePlan);
    } 
    
    @Test
    public void testDeSerialize() throws FileNotFoundException, IOException, ClassNotFoundException{
       
    } 
    
    @Test
    public void testScanPlans(){
      
        
    } 
}

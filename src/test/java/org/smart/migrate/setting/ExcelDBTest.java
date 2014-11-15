/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.smart.migrate.util.ExcelUtils;

/**
 *
 * @author sindtom
 */
public class ExcelDBTest {
    
    public static void testConnection() throws Exception{
        Class.forName("com.googlecode.sqlsheet.Driver");

        
        
        Connection connection = DriverManager.getConnection("jdbc:xls:file:/Users/sindtom/tmp/2010年.xls");
        
       
        
//        java.sql.Statement writeStatement = connection.createStatement();
//
//        writeStatement.executeUpdate("CREATE TABLE TEST_INSERT(COL1 INT, COL2 VARCHAR(255), COL3 DATE)");
;

        
//        
//        PreparedStatement writeStatement2 = 
//         writeConnection.prepareStatement("INSERT INTO TEST_INSERT(COL1, COL2, COL3) VALUES(?,?,?)");
//
//        for(int i = 0; i<3;i++){
//          writeStatement2.setDouble(1, i);
//          writeStatement2.setString(2, "Row" + i);
//          writeStatement2.setDate(3, new java.sql.Date(new Date().getTime()));
//          writeStatement2.execute();
//        }
        PreparedStatement statement = connection.prepareStatement("select *  from 案卷目录");
        ResultSet rs = statement.executeQuery();
        while(rs.next()){ 
            System.out.println(rs.getString(1));
        }
        
        
        
        //writeStatement.close();
        statement.close();
        connection.close();
    }
    
    
    
    public static void main(String[] args) throws  Exception{
        //ExcelUtils.addIndendityColumnData("/Users/sindtom/tmp/报销清单.xlsx", "Sheet1");
        testConnection();
    }


}

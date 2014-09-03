/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.util.List;
import java.util.Map;
import org.apache.commons.dbcp.BasicDataSource;
import org.junit.Test;
import org.smart.migrate.DBType;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 *
 * @author Sandy Duan
 */
public class DataSourceTest {
    
    @Test
    public void testDataSource(){
        DBSetting dbs = new DBSetting(DBType.MySQL, "localhost", "3306", "smartData", "root", null);
       BasicDataSource dataSource = new BasicDataSource();
       dataSource.setDriverClassName(dbs.getdBType().getDriver());
       dataSource.setUrl(dbs.getConnectUrl());
       dataSource.setUsername(dbs.getUsername());
       dataSource.setPassword(dbs.getPassword());
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        List<Map<String,Object>> dataList= jdbcTemplate.queryForList("select * from gen_fawen");
        for (Map<String, Object> map : dataList) {
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String string = entry.getKey();
                Object object = entry.getValue();
                System.out.println(string+","+object);
            }
        }
        
    }
}

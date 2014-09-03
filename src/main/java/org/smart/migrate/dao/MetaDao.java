/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao;

import java.sql.Connection;
import java.util.List;
import org.smart.migrate.model.Field;

/**
 * Meta Data Interface ,use to fetch database,table,field infos
 * 元数据数据处理接口，主要用来获取数据库，表，字段等元数据信息
 * @author Sandy Duan
 */
public interface MetaDao {
    
    public List<String> getDatabases(Connection connection);
    
    public List<String> getTables(Connection connection);
    
    public List<Field> getFieldsOfTable(Connection connection,String table);
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao.impl;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.smart.migrate.dao.MetaAbstractDao;
import org.smart.migrate.dao.MetaDao;
import org.smart.migrate.dao.MetaDaoTemplate;
import org.smart.migrate.dao.MetaFieldMapper;
import org.smart.migrate.model.Field;

/**
 *
 * @author Sandy Duan
 */
public class MetaMySqlDao extends MetaAbstractDao implements MetaDao,MetaFieldMapper{

    private static final String SHOW_DATABASE = "show databases";
    private static final String SHOW_TABLE = "show tables";
    private static final String SHOW_FEILD = "show full fields from ?";
    
    public MetaMySqlDao(MetaDaoTemplate metaDaoTemplate) {
        super(metaDaoTemplate);
    }

    @Override
    public List<String> getDatabases(Connection connection) {
        return this.metaDaoTemplate.getDatabasesBySql(connection,SHOW_DATABASE);
    }

    @Override
    public List<String> getTables(Connection connection) {
        return this.metaDaoTemplate.getTablesBySql(connection, SHOW_TABLE);
    }

    @Override
    public List<Field> getFieldsOfTable(Connection connection, String table) {
        return this.metaDaoTemplate.getFieldsBySql(connection, SHOW_FEILD, table, this,false,false);
    }

    @Override
    public Field mappMetaField(ResultSet resultSet) throws SQLException{
        Field field = new Field();
            field.setName(resultSet.getString("field"));
            field.setType(resultSet.getString("type"));
            field.setNullable(resultSet.getBoolean("null"));
            field.setDefaultValue(resultSet.getString("default"));
        return field;
    }

    
}

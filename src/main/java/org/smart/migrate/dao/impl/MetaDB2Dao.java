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
public class MetaDB2Dao extends MetaAbstractDao implements MetaDao,MetaFieldMapper{

    private static final String SHOW_FEILD = "select NAME from SYSIBM.SYSCOLUMNS where TBNAME = ?";

    
    public MetaDB2Dao(MetaDaoTemplate metaDaoTemplate) {
        super(metaDaoTemplate);
    }

    @Override
    public List<String> getDatabases(Connection connection) {
        return this.metaDaoTemplate.getDatabasesByDBMD(connection);
    }

    @Override
    public List<String> getTables(Connection connection) {
        return this.metaDaoTemplate.getTablesByDBMD(connection);
    }

    @Override
    public List<Field> getFieldsOfTable(Connection connection, String table) {
        return this.metaDaoTemplate.getFieldsBySql(connection, SHOW_FEILD, table, this);
    }

    @Override
    public Field mappMetaField(ResultSet resultSet) throws SQLException{
        Field field = new Field();
        field.setName(resultSet.getString("NAME"));
        return field;
    }

    
}

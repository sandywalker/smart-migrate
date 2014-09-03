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
public class MetaOracleDao extends MetaAbstractDao implements MetaDao,MetaFieldMapper{

    private static final String SHOW_DATABASE = "select instance_name from v$instance";
    private static final String SHOW_TABLE = "select table_name from user_all_tables";
    private static final String SHOW_FEILD = "select column_name,data_type,data_length,nullable,data_default from sys.user_tab_columns t where table_name = ?";

    
    public MetaOracleDao(MetaDaoTemplate metaDaoTemplate) {
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
        return this.metaDaoTemplate.getFieldsBySql(connection, SHOW_FEILD, table, this,true,true);
    }

    @Override
    public Field mappMetaField(ResultSet resultSet) throws SQLException{
        Field field = new Field();
        field.setName(resultSet.getString("column_name").toLowerCase());
        field.setType(resultSet.getString("data_type").toLowerCase() + "(" + resultSet.getInt("data_length") + ")");
        String nullable = resultSet.getString("nullable");
        field.setNullable("Y".equals(nullable));
        field.setDefaultValue(resultSet.getString("data_default"));
        return field;
    }

    
}

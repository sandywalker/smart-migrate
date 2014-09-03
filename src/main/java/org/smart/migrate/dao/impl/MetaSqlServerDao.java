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
public class MetaSqlServerDao extends MetaAbstractDao implements MetaDao,MetaFieldMapper{

    private static final String SHOW_DATABASE = "select [name] from [sysdatabases]";
    private static final String SHOW_TABLE = "select [name] from [sysobjects] where [type] = 'u'";
    private static final String SHOW_FEILD = "select c.[name],c.[isnullable] from [syscolumns] c join [sysobjects] t on c.id = t.id where t.[name] = ?";

    
    public MetaSqlServerDao(MetaDaoTemplate metaDaoTemplate) {
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
        return this.metaDaoTemplate.getFieldsBySql(connection, SHOW_FEILD, table, this);
    }

    @Override
    public Field mappMetaField(ResultSet resultSet) throws SQLException{
        Field field = new Field();
        field.setName(resultSet.getString("name").toLowerCase());
        String nullable = resultSet.getString("isnullable");
        field.setNullable("1".equals(nullable));
        return field;
    }

    
}

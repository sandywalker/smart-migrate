/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.smart.migrate.model.Field;

/**
 *
 * @author Sandy Duan
 */
public interface MetaFieldMapper {
    
    public Field mappMetaField(ResultSet resultSet) throws  SQLException;
}

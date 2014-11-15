/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao;

import org.smart.migrate.dao.impl.MetaSqlServerDao;
import org.smart.migrate.dao.impl.MetaMySqlDao;
import org.smart.migrate.dao.impl.MetaOracleDao;
import org.smart.migrate.dao.impl.MetaDB2Dao;
import org.smart.migrate.dao.impl.MetaAccessDao;
import org.smart.migrate.DBType;
import org.smart.migrate.dao.impl.MetaExcelDao;

/**
 * Factory Class to create MetaDao
 * 元数据信息获取工厂类
 * @author Sandy Duan
 */
public class MetaDaoFactory {
    
    /**
     * create metao by dbtype
     * @param dbType
     * @return
     */
    public static MetaDao createMetaDao(DBType dbType,String dbname){
        MetaDaoTemplate metaDaoTemplate = new MetaDaoTemplate();
        switch (dbType){
            case MySQL:
                return new MetaMySqlDao(metaDaoTemplate);
            case Oracle:
                return new MetaOracleDao(metaDaoTemplate);
            case SQLServer:
                return new MetaSqlServerDao(metaDaoTemplate);
            case DB2:
                return new MetaDB2Dao(metaDaoTemplate);
            case Access:
                return new MetaAccessDao(metaDaoTemplate);
            case Excel:
                return new MetaExcelDao(metaDaoTemplate,dbname);
            default:
                throw new AssertionError(dbType.name());
            
        }
    }
}

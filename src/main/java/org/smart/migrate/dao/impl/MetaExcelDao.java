/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.smart.migrate.dao.MetaAbstractDao;
import org.smart.migrate.dao.MetaDao;
import org.smart.migrate.dao.MetaDaoTemplate;
import org.smart.migrate.model.Field;

/**
 *
 * @author Sandy Duan
 */
public class MetaExcelDao extends MetaAbstractDao implements MetaDao{

    private String dbfile;
    
    private static final String SHOW_FEILD = "select NAME from SYSIBM.SYSCOLUMNS where TBNAME = ?";
    
    public MetaExcelDao(MetaDaoTemplate metaDaoTemplate) {
        super(metaDaoTemplate);
    }
    
    public MetaExcelDao(MetaDaoTemplate metaDaoTemplate,String dbfile){
        super(metaDaoTemplate);
        this.dbfile = dbfile;
    }

    @Override
    public List<String> getDatabases(Connection connection) {
        return this.metaDaoTemplate.getDatabasesByDBMD(connection);
    }

    @Override
    public List<String> getTables(Connection connection) {
        List<String> tables = new ArrayList<String>();
        try {
            InputStream inp = new FileInputStream(dbfile);
            Workbook wb = WorkbookFactory.create(inp); 
            for(int i=0;i<wb.getNumberOfSheets();i++){
                Sheet sheet = wb.getSheetAt(i);
                if (sheet.getPhysicalNumberOfRows()>0){
                    tables.add(sheet.getSheetName());
                }
            } 
        } catch (IOException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return tables;
    }

    @Override
    public List<Field> getFieldsOfTable(Connection connection, String table) {
        List<Field> fields = new ArrayList<Field>();
        try {
            InputStream inp  = new FileInputStream(dbfile);
            Workbook wb = WorkbookFactory.create(inp);
            Sheet sheet = wb.getSheet(table);
            if (sheet==null){
                inp.close();
                return fields;
            }
            Row row = sheet.getRow(sheet.getFirstRowNum());
            if (row!=null){
                for (Cell cell : row) { 
                     Field field = new Field();
                     field.setDefaultValue(null);
                     field.setName(cell.getStringCellValue());
                     field.setNullable(true);
                     field.setType("varchar");
                     fields.add(field);
                 }
            }
        } catch (IOException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return fields;
    }

    

    
}

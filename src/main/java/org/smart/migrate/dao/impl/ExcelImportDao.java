/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao.impl;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.format.CellFormatType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.smart.migrate.log.ImportLogger;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.ExcelUtils;

/**
 *
 * @author sindtom
 */
public class ExcelImportDao extends DefaultImportDao{

    private Workbook sourceWorkbook;
    
    private final Map<String,Map<String,Object>> sourceData = new HashMap<String,Map<String, Object>>();
    
    private Workbook readWorkBook(String filename){
        Workbook wb = null;
        try {
            
            InputStream inp = new FileInputStream(filename);
            wb = WorkbookFactory.create(inp); 
            inp.close();
        } catch (IOException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return wb;
    }
    
    public ExcelImportDao(MigratePlan migratePlan,DataSource targetDataSource,ImportLogger importLogger){
       super(migratePlan, targetDataSource, importLogger);
       sourceWorkbook= readWorkBook(migratePlan.getSourceDB().getDatabase());
       
    }
   
    
    public ExcelImportDao(DataSource sourceDataSource, DataSource targetDataSource, ImportLogger importLogger) {
        super(sourceDataSource, targetDataSource, importLogger);
    }
    
    

    @Override
    public List<Map<String, Object>> findSourceByPrimaryKeys(TableSetting tableSetting, List<String> primaryKeys) {
        List<Map<String,Object>> dataList = new ArrayList<Map<String, Object>>();
        for(String key:primaryKeys){
            Map<String,Object> data = sourceData.get(key);
            if (data!=null){
                dataList.add(data);
            }
        }
        return dataList;
    }

    @Override
    public List<String> findAllSourcePrimaryKeys(TableSetting tableSetting) {
        
        sourceData.clear();
        
        List<String> pks = new ArrayList<String>();
        Sheet sheet = sourceWorkbook.getSheet(tableSetting.getSourceTable());
        if (sheet==null){
            throw  new RuntimeException("can not get sheet from " + tableSetting.getSourceTable());
        }
        Row headRow = sheet.getRow(sheet.getFirstRowNum());
        if (headRow!=null){
            int idColumn = ExcelUtils.cellIndexInRow("id", headRow);
            if (idColumn==-1){
                throw new RuntimeException("sheet must have id column!");
            }
            
            //initialize header map (key: column index,value: fieldname)
            Map<Integer,String> header = new HashMap<Integer, String>();
            for(Cell cell: headRow){
                header.put(cell.getColumnIndex(), cell.getStringCellValue());
            }
            
            for (int i = 1; i < sheet.getPhysicalNumberOfRows(); i++) {
                Row row = sheet.getRow(i);
                String pk;
                if (row.getCell(idColumn).getCellType()==Cell.CELL_TYPE_NUMERIC){
                    Double did = row.getCell(idColumn).getNumericCellValue();
                    pk =  String.valueOf(did.intValue());
                }else{
                    pk =  row.getCell(idColumn).getStringCellValue();
                }
                pks.add(pk);
                Map<String,Object> data = new HashMap<String, Object>();
                for(Cell cell: row){
                    String fieldname = header.get(cell.getColumnIndex());
                    
                    if (cell.getCellType() == Cell.CELL_TYPE_NUMERIC){
                        Double dvalue = cell.getNumericCellValue();
                        String s = String.valueOf(dvalue);
                        if (s.endsWith(".0")){
                            data.put(fieldname, dvalue.intValue());
                        }else{
                            data.put(fieldname, dvalue);
                        }
                    }else if (cell.getCellType() == Cell.CELL_TYPE_BOOLEAN){
                        data.put(fieldname, cell.getBooleanCellValue());
                    }else{
                        data.put(fieldname, cell.getStringCellValue());
                    }
                    
                }
                sourceData.put(pk, data);
            }
        }
        return pks;
    }

    
    
   
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.smart.migrate.dao.impl.MetaExcelDao;

/**
 * utils for excel update
 * @author sindtom
 */
public class ExcelUtils {
    
    public static int cellIndexInRow(String cellContent,Row row){
        for(Cell cell:row){
            if (cell.getStringCellValue()!=null&&cell.getStringCellValue().equals(cellContent)){
                return cell.getColumnIndex();
            }
        }
        return -1;
    }
    
    /**
     * Add indendity column data for excel sheet
     * @param filename Excel name
     * @param sheetName Sheet name
     */
    public static void addIndendityColumnData(String filename,String sheetName){
        try {
            InputStream inp = new FileInputStream(filename);
            Workbook wb = WorkbookFactory.create(inp); 
            Sheet sheet = wb.getSheet(sheetName);
            if (sheet!=null&&sheet.getPhysicalNumberOfRows()>0){
                Row row = sheet.getRow(sheet.getFirstRowNum());
                
                if (row!=null){
                    int idColumn = cellIndexInRow("id",row);
                    if (idColumn==-1){
                        idColumn = row.getLastCellNum();
                        row.createCell(idColumn).setCellValue("id");
                        for(int i = 1;i<sheet.getPhysicalNumberOfRows();i++){
                            Row r = sheet.getRow(i);
                            Cell cell = r.getCell(idColumn);
                            if (cell==null){
                                cell = r.createCell(idColumn);
                            }
                            cell.setCellValue(i);
                        }
                     FileOutputStream fileOut = new FileOutputStream(filename); 
                     wb.write(fileOut); 
                     fileOut.close();
                    }
                    inp.close();
                }
            }
            
        } catch (IOException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidFormatException ex) {
            Logger.getLogger(MetaExcelDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.ui;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JProgressBar;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.log.ImportLogger;
import org.smart.migrate.log.LogLevel;
import org.smart.migrate.service.ImportManager;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.SettingUtils;

/**
 * Main Thread of Data Migration
 * 数据导入主线程
 * @author Sandy Duan
 */
public class ImportThread  extends Thread implements ImportLogger{
    
    private final ImportManager  importManager;
    private final JTextPane logger;
    private final JProgressBar progressBar;
    private final MigratePlan migratePlan;
    private final UIView uIView;
    
    public boolean running;
    public boolean terminate;
    private boolean paused;
    
    private java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/smart/migrate/ui/Bundle"); // NOI18N;

    public ImportThread(ImportManager importManager,UIView uIView, JTextPane logger,JProgressBar progressBar,MigratePlan migratePlan){
        this.importManager = importManager;
        this.logger = logger;
        this.progressBar = progressBar;
        
        this.migratePlan = migratePlan;
        this.uIView = uIView;
        importManager.setImportLogger(this);
        addStylesToDocument(logger.getStyledDocument());
    }
    
    private void addStylesToDocument(StyledDocument doc) {
        //Initialize some styles.
        Style def = StyleContext.getDefaultStyleContext().
                        getStyle(StyleContext.DEFAULT_STYLE);
 
        Style regular = doc.addStyle("regular", def);
        StyleConstants.setFontFamily(def, "SansSerif");
 
        Style s = doc.addStyle("italic", regular);
        StyleConstants.setItalic(s, true);
 
        s = doc.addStyle("bold", regular);
        StyleConstants.setBold(s, true);
 
        s = doc.addStyle("small", regular);
        StyleConstants.setFontSize(s, 10);
 
        s = doc.addStyle("large", regular);
        StyleConstants.setFontSize(s, 16);
 
        s = doc.addStyle("icon", regular);
        StyleConstants.setAlignment(s, StyleConstants.ALIGN_CENTER);
        
        s = doc.addStyle("info", regular);
        StyleConstants.setForeground(s, Color.blue);
        
        s = doc.addStyle("error", regular);
        StyleConstants.setForeground(s, Color.red);
        
        s = doc.addStyle("warning", regular);
        StyleConstants.setForeground(s, Color.orange);
        
         s = doc.addStyle("gray", regular);
        StyleConstants.setForeground(s, Color.gray);
        
         s = doc.addStyle("success", regular);
        StyleConstants.setForeground(s, Color.green);
    }
    
    @Override
    public void log(String level, String msg) {
       if (logger!=null){
           StyledDocument doc = logger.getStyledDocument();
           try {
               doc.insertString(doc.getLength(), new SimpleDateFormat("MM-dd hh:mm:ss ").format(new Date()), doc.getStyle("gray"));
               doc.insertString(doc.getLength(), msg+"\n", doc.getStyle(level.toLowerCase()));
               logger.setCaretPosition(logger.getDocument().getLength());
               //logger.append(   + ", level:" + level+", message: " + msg+"\n");
           } catch (BadLocationException ex) {
               Logger.getLogger(ImportThread.class.getName()).log(Level.SEVERE, null, ex);
           }
       }
    }

    @Override
    public void log(String msg) {
        log(LogLevel.INFO, msg);
    }
    
    public void resetUI(){
        logger.setText(null);
        progressBar.setValue(0);
    }
    
    public void terminate(){
        running = false;
    }
    
    
    
    public int importTable(TableSetting tableSetting){
        progressBar.setValue(0);
        
        //disable indentity if needed
        //将自动生成主键策略关闭
        importManager.toggleIdentity(tableSetting.getTargetTable(), false);
        List<String> allPrimaryKeys = importManager.findAllSourcePrimaryKeys(tableSetting,migratePlan);
        List<List<String>> primaryKeys = importManager.splitKeysByBatchSize(allPrimaryKeys);
        progressBar.setMaximum(primaryKeys.size());
        int index = 0;
        int importedCount = 0;        
        log( bundle.getString("ImportThread.log.totalPrefix") + allPrimaryKeys.size() + bundle.getString("ImportThread.log.totalSuffix"));
        for (List<String> keyList : primaryKeys) {
            if (running) {
                log(bundle.getString("ImportThread.log.import.running")+ "(" + (importedCount+1) + "-" + (importedCount+keyList.size()) +") "+bundle.getString("ImportThread.log.import.PK")+"： ["+ StringUtils.join(keyList,",")+"]"); 
                List<Map<String,Object>> sourceDatalList = importManager.findSourceByPrimaryKeys(tableSetting, keyList,migratePlan);
                importManager.batchImportSourceData(tableSetting,sourceDatalList);
                importedCount+=keyList.size();
                index++;
                progressBar.setValue(index);
                log(bundle.getString("ImportThread.log.import.complete")); 
                //仅调试用，实际环境下应删除
                try {
                    sleep(100);
                } catch (InterruptedException ex) {
                    Logger.getLogger(ImportThread.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            uIView.refreshUI();
        }
        importManager.toggleIdentity(tableSetting.getTargetTable(), true);
        return importedCount;
    }
    
    /**
     * 删除已导入的数据
     */
    public void deleteImportedData(){
        try {
            SettingUtils.sortTableSettingsByRelations(migratePlan);
            for(TableSetting tableSetting:migratePlan.getTableSettings()){
                if (importManager.isTableImported(tableSetting)){
                    log(LogLevel.WARNING, bundle.getString("ImportThread.log.rollback.runing") + tableSetting.getTargetTable());
                    importManager.deleteImportedData(tableSetting);
                    log(tableSetting.getTargetTable()+ bundle.getString("ImportThread.log.rollback.complete") );
                }
            }
        } catch (Exception e) {
            log(LogLevel.ERROR, bundle.getString("ImportThread.log.rollback.error"));
            importManager.resetContext();
        }
        importManager.resetContext();
    }

    @Override
    public void run() {
        resetUI();
        running = true;
        uIView.refreshUI();
        importManager.setMigratePlan(migratePlan);
        importManager.connectToDataBase();
        if (importManager.isConnected()){
            try {
                importManager.resetContext();
                for (TableSetting tableSetting : migratePlan.getTableSettings()) {
                    if (running){   
                        if (tableSetting.isEnabled()){
                            log(LogLevel.WARNING, bundle.getString("ImportThread.log.import.table") + tableSetting.getSourceTable() + "->" + tableSetting.getTargetTable());
                            int importedCount = importTable(tableSetting);
                            log(LogLevel.INFO, bundle.getString("ImportThread.log.import.table.complete1") + importedCount + bundle.getString("ImportThread.log.import.table.complete2"));
                            sleep(500);
                        }
                    }
                    uIView.refreshUI();
                }
                if (running){
                    log(LogLevel.WARNING,bundle.getString("ImportThread.log.relation.running"));
                    importManager.updateReferences();
                    log(bundle.getString("ImportThread.log.relation.complete"));
                }
                running = false;
                uIView.refreshUI();
            } catch (Exception e) {
                running = false;
                log(LogLevel.ERROR, e.getMessage());
                deleteImportedData();
                uIView.refreshUI();
                e.printStackTrace();
            }finally{
                running = false;
                importManager.closeConnection();
            }

            
        }
    }
    
    
    
}

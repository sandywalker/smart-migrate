/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.ui;

import java.awt.event.ItemEvent;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultCellEditor;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.DBType;
import org.smart.migrate.PKStrategy;
import org.smart.migrate.dao.MetaDao;
import org.smart.migrate.dao.MetaDaoFactory;
import org.smart.migrate.model.Field;
import org.smart.migrate.model.MigrateOptions;
import org.smart.migrate.setting.FieldSetting;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.ConnectionUtils;
import org.smart.migrate.util.ExcelUtils;

/**
 *
 * @author Sandy Duan
 */
public class MappingDialog extends javax.swing.JDialog {

    
    private TableSetting tableSetting = new TableSetting();
    private MigratePlan migratePlan;
    private Connection sourceConnection;
    private Connection targetConnection;
    private MetaDao sourceMetaDao;
    private MetaDao targetMetaDao;
    
    private int modalResult;
    
    
    private java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/smart/migrate/ui/Bundle"); // NOI18N;
    
    
    
    
    /**
     * Creates new form MappingDialog
     * @param parent
     * @param modal
     */
    public MappingDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        initCustomComponents();
    }
    
    private void initCustomComponents(){
        cbxPKStrategy.removeAllItems();
        for (PKStrategy strategy : PKStrategy.values()) {
            cbxPKStrategy.addItem(strategy.getDescription());
        }
        cbxPKStrategy.setSelectedItem(PKStrategy.COPY.getDescription());
    }
    
    //数据模型转换到视图
    private void modelToView(){
           cbxSourceTables.setSelectedItem(tableSetting.getSourceTable());
           cbxTargetTables.setSelectedItem(tableSetting.getTargetTable());
           edtWhere.setText(tableSetting.getWhere());
           cbxPKStrategy.setSelectedItem(tableSetting.getpKStrategy().getDescription());
    }
    
    //视图数据转换到数据模型
    private void viewToModel(){
        tableSetting.setSourceTable(cbxSourceTables.getSelectedItem().toString());
        tableSetting.setTargetTable(cbxTargetTables.getSelectedItem().toString());
        tableSetting.setWhere(edtWhere.getText());
        String pkStrategy = (String)cbxPKStrategy.getSelectedItem();
        tableSetting.setpKStrategy(PKStrategy.getStrategyByDescription(pkStrategy));
        tableSetting.getFieldSettings().clear();
        
        
         for (int i = 0; i < tblFieldMapping.getRowCount(); i++) {
                String srcField = (String)tblFieldMapping.getValueAt(i, 0);
                String tgtField = (String)tblFieldMapping.getValueAt(i, 1);
                String defaultValue = (String)tblFieldMapping.getValueAt(i, 3);
                
                if (StringUtils.isNotBlank(srcField)||StringUtils.isNotBlank(defaultValue)){
                      FieldSetting fieldSetting = new FieldSetting();
                      fieldSetting.setSourceField(srcField);
                      fieldSetting.setTargetField(tgtField);
                      fieldSetting.setPrimaryKey((Boolean)tblFieldMapping.getValueAt(i, 2));
                      fieldSetting.setDefaultValue(defaultValue);
                      fieldSetting.setDictText((String)tblFieldMapping.getValueAt(i, 4));
                      tableSetting.getFieldSettings().add(fieldSetting);
                }
                      
        }
        
        
    }
    
    private void autoMappingField(){
        List<Field> srcFields = sourceMetaDao.getFieldsOfTable(sourceConnection, cbxSourceTables.getSelectedItem().toString());
        for (int i = 0; i < tblFieldMapping.getRowCount(); i++) {
            String tgtField = (String)tblFieldMapping.getValueAt(i, 1);
            for(Field field:srcFields){
                if (field.getName().equalsIgnoreCase(tgtField))
                tblFieldMapping.setValueAt(field.getName(), i, 0);
            }
        }
    }
    
    
    //初始化表
    private void initTables(JComboBox comboBox,MetaDao metaDao,Connection connection){
        comboBox.removeAllItems();
        List<String> tables = metaDao.getTables(connection);
        for(String table:tables){
            comboBox.addItem(table);
        }
    }
    
    //初始化字段映射
    private void initFieldMappings(){
        if (StringUtils.isBlank((String)cbxSourceTables.getSelectedItem())
                ||StringUtils.isBlank((String)cbxTargetTables.getSelectedItem())){
            return;
        }
        DefaultTableModel model = (DefaultTableModel)tblFieldMapping.getModel();
        model.setRowCount(0);
        TableColumn srcColumn = tblFieldMapping.getColumnModel().getColumn(0);
        TableColumn tgtColumn = tblFieldMapping.getColumnModel().getColumn(1);
        JComboBox srcComboBox = new JComboBox();
        srcComboBox.setMaximumRowCount(20);
        
        List<Field> srcFields =  sourceMetaDao.getFieldsOfTable(sourceConnection, cbxSourceTables.getSelectedItem().toString());
        List<Field> tgtFields = targetMetaDao.getFieldsOfTable(targetConnection, cbxTargetTables.getSelectedItem().toString());
        
        srcComboBox.addItem("");
        for(Field field:srcFields){
            srcComboBox.addItem(field.getName());
        }
        
        srcColumn.setCellEditor(new DefaultCellEditor(srcComboBox));
        
        int k = 0;
        for(Field tgtField:tgtFields){
            k++;
            model.addRow(new Object[]{null, tgtField.getName(), false, null, null,k});
        }
        
        
        
        if (tableSetting!=null&&!CollectionUtils.isEmpty(tableSetting.getFieldSettings()) 
                &&tableSetting.getSourceTable().equals(cbxSourceTables.getSelectedItem())
                &&tableSetting.getTargetTable().equals(cbxTargetTables.getSelectedItem())){
            
            for (int i = 0; i < tblFieldMapping.getRowCount(); i++) {
                String tgtField = (String)tblFieldMapping.getValueAt(i, 1);
                FieldSetting fieldSetting = tableSetting.getFieldSettingByTargetField(tgtField);
                if (fieldSetting!=null){
                    tblFieldMapping.setValueAt(fieldSetting.getSourceField(), i, 0);
                    tblFieldMapping.setValueAt(fieldSetting.isPrimaryKey(), i, 2);
                    tblFieldMapping.setValueAt(fieldSetting.getDefaultValue(), i, 3);
                    tblFieldMapping.setValueAt(fieldSetting.getDictText(), i, 4);
                }     
            }
        }
    }
    /**
     根据目标字段得到映射表的行号
     */
    private int getRowIndexByTargetField(String targetField){
        for (int i = 0; i < tblFieldMapping.getRowCount(); i++) {
            if (targetField.equals(tblFieldMapping.getValueAt(i, 1))){
                return i;
            } 
        }
        return -1;
    }
        
    
    private void autoMatchTargetTable(String sourceTable){
        for (int i = 0; i < cbxTargetTables.getItemCount(); i++) {
            if (((String)cbxTargetTables.getItemAt(i)).equalsIgnoreCase(sourceTable)){
                cbxTargetTables.setSelectedIndex(i);
                return;
            }
        }
 
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cbxTargetTables = new javax.swing.JComboBox();
        jLabel11 = new javax.swing.JLabel();
        cbxSourceTables = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        edtWhere = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblFieldMapping = new javax.swing.JTable();
        btnOK = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        btnAutoMapping = new javax.swing.JButton();
        cbxPKStrategy = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        btnBatchDefaultValues = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosed(java.awt.event.WindowEvent evt) {
                formWindowClosed(evt);
            }
        });

        cbxTargetTables.setMaximumRowCount(20);
        cbxTargetTables.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTargetTablesItemStateChanged(evt);
            }
        });

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/smart/migrate/ui/Bundle"); // NOI18N
        jLabel11.setText(bundle.getString("MappingDialog.jLabel11.text")); // NOI18N

        cbxSourceTables.setMaximumRowCount(20);
        cbxSourceTables.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxSourceTablesItemStateChanged(evt);
            }
        });
        cbxSourceTables.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxSourceTablesActionPerformed(evt);
            }
        });

        jLabel1.setText(bundle.getString("MappingDialog.jLabel1.text")); // NOI18N

        jLabel2.setText(bundle.getString("MappingDialog.jLabel2.text")); // NOI18N

        jLabel3.setText(bundle.getString("MappingDialog.jLabel3.text")); // NOI18N

        tblFieldMapping.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Source Field", "Target Field", "是否主键", "默认值", "数据字典", "Row Num"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                true, false, true, true, true, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblFieldMapping.setDropMode(javax.swing.DropMode.ON);
        tblFieldMapping.setGridColor(new java.awt.Color(204, 204, 204));
        tblFieldMapping.setShowGrid(true);
        tblFieldMapping.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblFieldMappingMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblFieldMapping);
        if (tblFieldMapping.getColumnModel().getColumnCount() > 0) {
            tblFieldMapping.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("MappingDialog.tblFieldMapping.columnModel.title0")); // NOI18N
            tblFieldMapping.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("MappingDialog.tblFieldMapping.columnModel.title1")); // NOI18N
            tblFieldMapping.getColumnModel().getColumn(2).setResizable(false);
            tblFieldMapping.getColumnModel().getColumn(2).setPreferredWidth(5);
            tblFieldMapping.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("MappingDialog.tblFieldMapping.columnModel.title2")); // NOI18N
            tblFieldMapping.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("MappingDialog.tblFieldMapping.columnModel.title3")); // NOI18N
            tblFieldMapping.getColumnModel().getColumn(4).setHeaderValue(bundle.getString("MappingDialog.tblFieldMapping.columnModel.title4")); // NOI18N
            tblFieldMapping.getColumnModel().getColumn(5).setResizable(false);
            tblFieldMapping.getColumnModel().getColumn(5).setPreferredWidth(30);
            tblFieldMapping.getColumnModel().getColumn(5).setHeaderValue(bundle.getString("MappingDialog.tblFieldMapping.columnModel.title5")); // NOI18N
        }

        btnOK.setText(bundle.getString("MappingDialog.btnOK.text")); // NOI18N
        btnOK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOKActionPerformed(evt);
            }
        });

        btnCancel.setText(bundle.getString("MappingDialog.btnCancel.text")); // NOI18N
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        btnAutoMapping.setText(bundle.getString("MappingDialog.btnAutoMapping.text")); // NOI18N
        btnAutoMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAutoMappingActionPerformed(evt);
            }
        });

        cbxPKStrategy.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        jLabel4.setText(bundle.getString("MappingDialog.jLabel4.text")); // NOI18N

        btnBatchDefaultValues.setText(bundle.getString("MappingDialog.btnBatchDefaultValues.text")); // NOI18N
        btnBatchDefaultValues.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchDefaultValuesActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(btnAutoMapping)
                        .addGap(27, 27, 27)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cbxPKStrategy, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBatchDefaultValues, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(btnCancel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnOK))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jScrollPane1)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel11)
                                        .addGap(0, 74, Short.MAX_VALUE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel1)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(cbxSourceTables, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jLabel2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(cbxTargetTables, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtWhere, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addContainerGap())))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cbxTargetTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbxSourceTables, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(edtWhere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(12, 12, 12)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnOK)
                    .addComponent(btnCancel)
                    .addComponent(btnAutoMapping)
                    .addComponent(cbxPKStrategy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnBatchDefaultValues))
                .addContainerGap())
        );

        cbxTargetTables.getAccessibleContext().setAccessibleName(bundle.getString("MappingDialog.cbxTargetTables.AccessibleContext.accessibleName")); // NOI18N

        setBounds(0, 0, 862, 519);
    }// </editor-fold>//GEN-END:initComponents

    private void btnOKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOKActionPerformed
        viewToModel();
        if (tableSetting.getFieldSettings().isEmpty()){
            JOptionPane.showMessageDialog(rootPane,  bundle.getString("MappingDialog.error.noField"));
            this.modalResult = 1;
            return ;
        }
        
        if (tableSetting.getSourcePK()==null||tableSetting.getTargetPK()==null){
           JOptionPane.showMessageDialog(rootPane, bundle.getString("MappingDialog.error.noPK"));
           this.modalResult = 1;
           return;
        }    
        this.modalResult = 0;
        
        
        this.dispose();
    }//GEN-LAST:event_btnOKActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        this.modalResult = 1;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed

    private void formWindowClosed(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosed
        if (sourceConnection!=null){
            ConnectionUtils.disconnect(sourceConnection);
        }
        if (targetConnection!=null){
            ConnectionUtils.disconnect(targetConnection);
        }
    }//GEN-LAST:event_formWindowClosed

    private void cbxSourceTablesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxSourceTablesItemStateChanged
        if (evt.getStateChange()==ItemEvent.SELECTED){
            String tableName = (String)cbxSourceTables.getSelectedItem();
            autoMatchTargetTable(tableName);
            if (migratePlan.getSourceDB().getdBType()==DBType.Excel){
                ExcelUtils.addIndendityColumnData(migratePlan.getSourceDB().getDatabase(), tableName);
            }
            initFieldMappings();
            cbxSourceTables.setToolTipText(tableName);
        }
    }//GEN-LAST:event_cbxSourceTablesItemStateChanged

    private void cbxTargetTablesItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTargetTablesItemStateChanged
        if (evt.getStateChange()==ItemEvent.SELECTED){
            String tableName = (String)cbxTargetTables.getSelectedItem();
            if (migratePlan.getTargetDB().getdBType()==DBType.Excel){
                ExcelUtils.addIndendityColumnData(migratePlan.getTargetDB().getDatabase(), tableName);
            }
            initFieldMappings();
            cbxTargetTables.setToolTipText(tableName);
        }
    }//GEN-LAST:event_cbxTargetTablesItemStateChanged

    private void btnAutoMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAutoMappingActionPerformed
        // TODO add your handling code here:
        autoMappingField();
    }//GEN-LAST:event_btnAutoMappingActionPerformed

    private void tblFieldMappingMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblFieldMappingMouseClicked
        int row = tblFieldMapping.rowAtPoint(evt.getPoint());
        int col = tblFieldMapping.columnAtPoint(evt.getPoint());
        if (row >= 0 && col == 2) {
            for(int i=0;i<tblFieldMapping.getRowCount();i++){
                if (i!=row){
                    tblFieldMapping.setValueAt(false, i, 2);
                }
            }
        }
    }//GEN-LAST:event_tblFieldMappingMouseClicked

    private void cbxSourceTablesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxSourceTablesActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cbxSourceTablesActionPerformed

    private void btnBatchDefaultValuesActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchDefaultValuesActionPerformed
        Map<String,Object> defaultValues = MigrateOptions.getInstance().getDefaultValues();
        for (int i = 0; i < tblFieldMapping.getRowCount(); i++) {
            String tgtField = (String)tblFieldMapping.getValueAt(i, 1);
            Object value = defaultValues.get(tgtField);
            if (value!=null){
               tblFieldMapping.setValueAt(value, i, 3);
            }
        }
    }//GEN-LAST:event_btnBatchDefaultValuesActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MappingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MappingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MappingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MappingDialog.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MappingDialog dialog = new MappingDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }
    
    

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAutoMapping;
    private javax.swing.JButton btnBatchDefaultValues;
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnOK;
    private javax.swing.JComboBox cbxPKStrategy;
    private javax.swing.JComboBox cbxSourceTables;
    private javax.swing.JComboBox cbxTargetTables;
    private javax.swing.JTextField edtWhere;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tblFieldMapping;
    // End of variables declaration//GEN-END:variables

    /**
     * @return the tableSetting
     */
    public TableSetting getTableSetting() {
        return tableSetting;
    }

    /**
     * @param tableSetting the tableSetting to set
     */
    public void setTableSetting(TableSetting tableSetting) {
        this.tableSetting = tableSetting;
        modelToView();
        initFieldMappings();
    }

    /**
     * @return the migratePlan
     */
    public MigratePlan getMigratePlan() {
        return migratePlan;
    }

    /**
     * @param migratePlan the migratePlan to set
     */
    public void setMigratePlan(MigratePlan migratePlan) {
        this.migratePlan = migratePlan;
        sourceMetaDao = MetaDaoFactory.createMetaDao(migratePlan.getSourceDB().getdBType(),migratePlan.getSourceDB().getDatabase());
        targetMetaDao = MetaDaoFactory.createMetaDao(migratePlan.getTargetDB().getdBType(),migratePlan.getTargetDB().getDatabase());
        sourceConnection = ConnectionUtils.connect(migratePlan.getSourceDB());
        targetConnection = ConnectionUtils.connect(migratePlan.getTargetDB());
        initTables(cbxSourceTables,sourceMetaDao,sourceConnection);
        initTables(cbxTargetTables,targetMetaDao,targetConnection);
    }

    /**
     * @return the sourceConnection
     */
    public Connection getSourceConnection() {
        return sourceConnection;
    }

    /**
     * @param sourceConnection the sourceConnection to set
     */
    public void setSourceConnection(Connection sourceConnection) {
        this.sourceConnection = sourceConnection;
    }

    /**
     * @return the targetConnection
     */
    public Connection getTargetConnection() {
        return targetConnection;
    }

    /**
     * @param targetConnection the targetConnection to set
     */
    public void setTargetConnection(Connection targetConnection) {
        this.targetConnection = targetConnection;
    }

    /**
     * @return the sourceMetaDao
     */
    public MetaDao getSourceMetaDao() {
        return sourceMetaDao;
    }

    /**
     * @param sourceMetaDao the sourceMetaDao to set
     */
    public void setSourceMetaDao(MetaDao sourceMetaDao) {
        this.sourceMetaDao = sourceMetaDao;
    }

    /**
     * @return the targetMetaDao
     */
    public MetaDao getTargetMetaDao() {
        return targetMetaDao;
    }

    /**
     * @param targetMetaDao the targetMetaDao to set
     */
    public void setTargetMetaDao(MetaDao targetMetaDao) {
        this.targetMetaDao = targetMetaDao;
    }

    /**
     * @return the modalResult
     */
    public int getModalResult() {
        return modalResult;
    }
}

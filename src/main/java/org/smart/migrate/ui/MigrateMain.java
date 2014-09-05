/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.ui;

import java.awt.event.ItemEvent;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.DBType;
import org.smart.migrate.service.ImportManager;
import org.smart.migrate.setting.DBSetting;
import org.smart.migrate.setting.MigratePlan;
import org.smart.migrate.setting.MigratePlanIO;
import org.smart.migrate.setting.TableRelation;
import org.smart.migrate.setting.TableSetting;
import org.smart.migrate.util.ConnectionUtils;
import org.smart.migrate.util.SettingUtils;

/**
 *
 * @author Sandy Duan
 */
public class MigrateMain extends javax.swing.JFrame implements UIView {

    private MigratePlan migratePlan = MigratePlan.createNewPlan();
    
    private final GuideController guideController;
    private final Map<Integer,String> validationMessages = new HashMap<Integer,String>();
    
    private  boolean createNewPlan;
    
    private DefaultListModel migratePlans  = new DefaultListModel();
    
    private final ImportManager importManager = new ImportManager();
    
    private ImportThread currentThread;
    
    private java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/smart/migrate/ui/Bundle"); // NOI18N;
    
    /**
     * Creates new form MigrateMain
     */
    public MigrateMain() {
        this.createNewPlan = false;
        initComponents();
        initModel();
        initCustomComponents();
        guideController = new GuideController(panelGuides, panelSteps);
        guideController.initEvents();
        refreshUI();
    }
    
    
    private void initCustomComponents(){
         initDBTypes(cbxSrcDBType);
         initDBTypes(cbxTgtDBType);
    }       
    
    
    @Override
    public void refreshUI(){
        boolean running = currentThread!=null&&currentThread.running;
        btnRunImport.setEnabled(!running&&isMigratePlanValid());
        btnStop.setEnabled(running);
        btnExportLogs.setEnabled(!running&&StringUtils.isNotBlank(mmoLogs.getText()));
        if (!running){
            pbMain.setValue(0);
        }
        btnRollBack.setEnabled(!running&&StringUtils.isNotBlank(mmoLogs.getText()));
        buttonNext.setEnabled(!running);
        buttonPrev.setEnabled(!running);
        guideController.setEnabled(!running);
    }
    
    
    
    private void initModel(){
        migratePlans.clear();
        List<String> plans = MigratePlanIO.scanPlans();
        for(String plan:plans){
            migratePlans.addElement(plan);
        }
        listPlan.setModel(migratePlans);
    }
    
    private void initDBTypes(JComboBox comboBox){
        comboBox.removeAllItems();
        for(DBType dBType:DBType.values()){
            comboBox.addItem(dBType);
        }
    }
    
    
    public boolean isMigratePlanValid(){
        
        return migratePlan!=null&&StringUtils.isNotBlank(migratePlan.getName())
                        &&StringUtils.isNotBlank(migratePlan.getSourceDB().getDatabase())
                        &&StringUtils.isNotBlank(migratePlan.getTargetDB().getDatabase())
                        &&migratePlan.getTableSettings().size()>0;
    }
    
    public void validateInputs(){
		int step = guideController.getIndex();
		if (step>=0){
			validationMessages.remove(step);
			if (step==0){
                                
				if (tabPlan.getSelectedIndex()==0){
					if (StringUtils.isBlank(textNewPlan.getText())){
						validationMessages.put(step, bundle.getString("MigrateMain.validation.newPlan"));
					}
				}else{
					if (listPlan.getSelectedIndex()==-1){
						validationMessages.put(step, bundle.getString("MigrateMain.validation.selectPlan"));
					}
				}
			}else if (step==1){
                            if (StringUtils.isBlank(edtSrcDBName.getText())){
                                validationMessages.put(step, bundle.getString("MigrateMain.validation.sourceDB"));
                            }
                        }else if (step==2){
                            if (StringUtils.isBlank(edtTgtDBName.getText())){
                                validationMessages.put(step, bundle.getString("MigrateMain.validation.targetDB"));
                            }
                        }
		}
	}
    
    
	
	public String validateCurrentStep(){
			validateInputs();
			int step = guideController.getIndex();
			if (step>=0){
				return  validationMessages.get(step);
			}
			return null;
	}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        bindingGroup = new org.jdesktop.beansbinding.BindingGroup();

        buttonGroup1 = new javax.swing.ButtonGroup();
        popPlan = new javax.swing.JPopupMenu();
        popDeletePlan = new javax.swing.JMenuItem();
        popExportPlan = new javax.swing.JMenuItem();
        popImportPlan = new javax.swing.JMenuItem();
        panelGuides = new javax.swing.JPanel();
        panelGuide1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        panelGuide2 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        panelGuide3 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        panelGuide4 = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        panelGuide5 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        panelGuide6 = new javax.swing.JPanel();
        jLabel30 = new javax.swing.JLabel();
        jLabel31 = new javax.swing.JLabel();
        panelSteps = new javax.swing.JPanel();
        panelStep1 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        tabPlan = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        textNewPlan = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        listPlan = new javax.swing.JList();
        jLabel36 = new javax.swing.JLabel();
        panelStep2 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        jLabel21 = new javax.swing.JLabel();
        cbxSrcDBType = new javax.swing.JComboBox();
        edtSrcDBHost = new javax.swing.JTextField();
        edtSrcDBName = new javax.swing.JTextField();
        edtSrcDBUser = new javax.swing.JTextField();
        edtSrcDBPassword = new javax.swing.JPasswordField();
        jLabel22 = new javax.swing.JLabel();
        edtSrcDBPort = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        btnTestConnectSrc = new javax.swing.JButton();
        panelStep3 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        edtTgtDBPort = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        edtTgtDBPassword = new javax.swing.JPasswordField();
        edtTgtDBUser = new javax.swing.JTextField();
        edtTgtDBName = new javax.swing.JTextField();
        edtTgtDBHost = new javax.swing.JTextField();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jLabel28 = new javax.swing.JLabel();
        cbxTgtDBType = new javax.swing.JComboBox();
        jLabel29 = new javax.swing.JLabel();
        btnTestConnectTarget = new javax.swing.JButton();
        panelStep4 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblTableMapping = new javax.swing.JTable();
        btnAddMapping = new javax.swing.JButton();
        btnDeleteMapping = new javax.swing.JButton();
        btnEditMapping = new javax.swing.JButton();
        cbxCheckAll = new javax.swing.JCheckBox();
        btnDeleteTargetData = new javax.swing.JButton();
        edtDeleteWhere = new javax.swing.JTextField();
        jLabel35 = new javax.swing.JLabel();
        panelStep5 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblTableRelations = new javax.swing.JTable();
        btnEditRelation = new javax.swing.JButton();
        btnDeleteMapping1 = new javax.swing.JButton();
        btnAddRelation = new javax.swing.JButton();
        panelStep6 = new javax.swing.JPanel();
        jLabel32 = new javax.swing.JLabel();
        btnRunImport = new javax.swing.JButton();
        pbMain = new javax.swing.JProgressBar();
        btnStop = new javax.swing.JButton();
        btnExportLogs = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        mmoLogs = new javax.swing.JTextPane();
        btnRollBack = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jLabel38 = new javax.swing.JLabel();
        edtBatchSize = new javax.swing.JFormattedTextField();
        jLabel39 = new javax.swing.JLabel();
        jLabel37 = new javax.swing.JLabel();
        buttonPrev = new javax.swing.JButton();
        buttonNext = new javax.swing.JButton();
        btnSavePlan = new javax.swing.JButton();
        jLabel33 = new javax.swing.JLabel();
        jLabel34 = new javax.swing.JLabel();

        java.util.ResourceBundle bundle = java.util.ResourceBundle.getBundle("org/smart/migrate/ui/Bundle"); // NOI18N
        popDeletePlan.setText(bundle.getString("MigrateMain.popDeletePlan.text")); // NOI18N
        popDeletePlan.setToolTipText(bundle.getString("MigrateMain.popDeletePlan.toolTipText")); // NOI18N
        popDeletePlan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                popDeletePlanMouseClicked(evt);
            }
        });
        popDeletePlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popDeletePlanActionPerformed(evt);
            }
        });
        popPlan.add(popDeletePlan);

        popExportPlan.setText(bundle.getString("MigrateMain.popExportPlan.text")); // NOI18N
        popExportPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popExportPlanActionPerformed(evt);
            }
        });
        popPlan.add(popExportPlan);

        popImportPlan.setText(bundle.getString("MigrateMain.popImportPlan.text")); // NOI18N
        popImportPlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                popImportPlanActionPerformed(evt);
            }
        });
        popPlan.add(popImportPlan);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle(bundle.getString("MigrateMain.title")); // NOI18N

        panelGuides.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));

        panelGuide1.setBackground(new java.awt.Color(255, 255, 255));
        panelGuide1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(51, 153, 255)));
        panelGuide1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelGuide1.setName("panelGuide1"); // NOI18N

        jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel1.setText(bundle.getString("MigrateMain.jLabel1.text")); // NOI18N

        jLabel2.setForeground(new java.awt.Color(153, 153, 153));
        jLabel2.setText(bundle.getString("MigrateMain.jLabel2.text")); // NOI18N

        javax.swing.GroupLayout panelGuide1Layout = new javax.swing.GroupLayout(panelGuide1);
        panelGuide1.setLayout(panelGuide1Layout);
        panelGuide1Layout.setHorizontalGroup(
            panelGuide1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuide1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGuide1Layout.setVerticalGroup(
            panelGuide1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGuide2.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
        panelGuide2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelGuide2.setName("panelGuide2"); // NOI18N

        jLabel5.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel5.setText(bundle.getString("MigrateMain.jLabel5.text")); // NOI18N

        jLabel6.setForeground(new java.awt.Color(153, 153, 153));
        jLabel6.setText(bundle.getString("MigrateMain.jLabel6.text")); // NOI18N

        javax.swing.GroupLayout panelGuide2Layout = new javax.swing.GroupLayout(panelGuide2);
        panelGuide2.setLayout(panelGuide2Layout);
        panelGuide2Layout.setHorizontalGroup(
            panelGuide2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuide2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGuide2Layout.setVerticalGroup(
            panelGuide2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel6)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        panelGuide3.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
        panelGuide3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelGuide3.setName("panelGuide3"); // NOI18N

        jLabel7.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel7.setText(bundle.getString("MigrateMain.jLabel7.text")); // NOI18N

        jLabel8.setForeground(new java.awt.Color(153, 153, 153));
        jLabel8.setText(bundle.getString("MigrateMain.jLabel8.text")); // NOI18N

        javax.swing.GroupLayout panelGuide3Layout = new javax.swing.GroupLayout(panelGuide3);
        panelGuide3.setLayout(panelGuide3Layout);
        panelGuide3Layout.setHorizontalGroup(
            panelGuide3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuide3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGuide3Layout.setVerticalGroup(
            panelGuide3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel8)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        panelGuide4.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
        panelGuide4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelGuide4.setName("panelGuide4"); // NOI18N

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel9.setText(bundle.getString("MigrateMain.jLabel9.text")); // NOI18N

        jLabel10.setForeground(new java.awt.Color(153, 153, 153));
        jLabel10.setText(bundle.getString("MigrateMain.jLabel10.text")); // NOI18N

        javax.swing.GroupLayout panelGuide4Layout = new javax.swing.GroupLayout(panelGuide4);
        panelGuide4.setLayout(panelGuide4Layout);
        panelGuide4Layout.setHorizontalGroup(
            panelGuide4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuide4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel9)
                    .addComponent(jLabel10))
                .addContainerGap(85, Short.MAX_VALUE))
        );
        panelGuide4Layout.setVerticalGroup(
            panelGuide4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        panelGuide5.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
        panelGuide5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelGuide5.setName("panelGuide5"); // NOI18N

        jLabel3.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel3.setText(bundle.getString("MigrateMain.jLabel3.text")); // NOI18N

        jLabel4.setForeground(new java.awt.Color(153, 153, 153));
        jLabel4.setText(bundle.getString("MigrateMain.jLabel4.text")); // NOI18N

        javax.swing.GroupLayout panelGuide5Layout = new javax.swing.GroupLayout(panelGuide5);
        panelGuide5.setLayout(panelGuide5Layout);
        panelGuide5Layout.setHorizontalGroup(
            panelGuide5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuide5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGuide5Layout.setVerticalGroup(
            panelGuide5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGuide6.setBorder(javax.swing.BorderFactory.createLineBorder(javax.swing.UIManager.getDefaults().getColor("Button.background")));
        panelGuide6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        panelGuide6.setName("panelGuide6"); // NOI18N

        jLabel30.setFont(new java.awt.Font("Lucida Grande", 1, 13)); // NOI18N
        jLabel30.setText(bundle.getString("MigrateMain.jLabel30.text")); // NOI18N

        jLabel31.setForeground(new java.awt.Color(153, 153, 153));
        jLabel31.setText(bundle.getString("MigrateMain.jLabel31.text")); // NOI18N

        javax.swing.GroupLayout panelGuide6Layout = new javax.swing.GroupLayout(panelGuide6);
        panelGuide6.setLayout(panelGuide6Layout);
        panelGuide6Layout.setHorizontalGroup(
            panelGuide6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuide6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel30)
                    .addComponent(jLabel31))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelGuide6Layout.setVerticalGroup(
            panelGuide6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuide6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel30)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel31)
                .addContainerGap(9, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelGuidesLayout = new javax.swing.GroupLayout(panelGuides);
        panelGuides.setLayout(panelGuidesLayout);
        panelGuidesLayout.setHorizontalGroup(
            panelGuidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuidesLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelGuidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGuide1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGuide2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGuide3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGuide4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(panelGuide5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelGuide6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        panelGuidesLayout.setVerticalGroup(
            panelGuidesLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelGuidesLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(panelGuide1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(9, 9, 9)
                .addComponent(panelGuide2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelGuide3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(panelGuide4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGuide5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelGuide6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        panelGuide1.getAccessibleContext().setAccessibleName(bundle.getString("MigrateMain.panelGuide1.AccessibleContext.accessibleName")); // NOI18N

        panelSteps.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(204, 204, 204)));
        panelSteps.setLayout(new java.awt.CardLayout());

        jLabel11.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel11.setText(bundle.getString("MigrateMain.jLabel11.text")); // NOI18N

        tabPlan.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabPlanStateChanged(evt);
            }
        });

        jLabel15.setText(bundle.getString("MigrateMain.jLabel15.text")); // NOI18N

        textNewPlan.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                textNewPlanPropertyChange(evt);
            }
        });
        textNewPlan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                textNewPlanKeyReleased(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(textNewPlan, javax.swing.GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(86, 86, 86)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(textNewPlan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15))
                .addContainerGap(204, Short.MAX_VALUE))
        );

        tabPlan.addTab(bundle.getString("MigrateMain.jPanel1.TabConstraints.tabTitle"), jPanel1); // NOI18N

        jLabel17.setText(bundle.getString("MigrateMain.jLabel17.text")); // NOI18N

        listPlan.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_INTERVAL_SELECTION);
        listPlan.setToolTipText(bundle.getString("MigrateMain.listPlan.toolTipText")); // NOI18N
        listPlan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                listPlanMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                listPlanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(listPlan);

        jLabel36.setForeground(new java.awt.Color(153, 153, 153));
        jLabel36.setText(bundle.getString("MigrateMain.jLabel36.text")); // NOI18N

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(jLabel17)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel36)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 551, Short.MAX_VALUE))
                .addGap(16, 16, 16))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel17)
                    .addComponent(jLabel36))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)
                .addGap(27, 27, 27))
        );

        tabPlan.addTab(bundle.getString("MigrateMain.jPanel2.TabConstraints.tabTitle"), jPanel2); // NOI18N

        javax.swing.GroupLayout panelStep1Layout = new javax.swing.GroupLayout(panelStep1);
        panelStep1.setLayout(panelStep1Layout);
        panelStep1Layout.setHorizontalGroup(
            panelStep1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep1Layout.createSequentialGroup()
                .addGroup(panelStep1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelStep1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel11))
                    .addGroup(panelStep1Layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(tabPlan)))
                .addGap(19, 19, 19))
        );
        panelStep1Layout.setVerticalGroup(
            panelStep1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(tabPlan, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(93, Short.MAX_VALUE))
        );

        panelSteps.add(panelStep1, "card1");

        jLabel13.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel13.setText(bundle.getString("MigrateMain.jLabel13.text")); // NOI18N

        jLabel18.setText(bundle.getString("MigrateMain.jLabel18.text")); // NOI18N

        jLabel19.setText(bundle.getString("MigrateMain.jLabel19.text")); // NOI18N

        jLabel20.setText(bundle.getString("MigrateMain.jLabel20.text")); // NOI18N

        jLabel21.setText(bundle.getString("MigrateMain.jLabel21.text")); // NOI18N

        cbxSrcDBType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxSrcDBType.setName("cbxSrcDBType"); // NOI18N

        org.jdesktop.beansbinding.Binding binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceDB.dBType}"), cbxSrcDBType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxSrcDBType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxSrcDBTypeItemStateChanged(evt);
            }
        });

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceDB.host}"), edtSrcDBHost, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceDB.database}"), edtSrcDBName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceDB.username}"), edtSrcDBUser, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceDB.password}"), edtSrcDBPassword, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel22.setText(bundle.getString("MigrateMain.jLabel22.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceDB.port}"), edtSrcDBPort, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel23.setText(bundle.getString("MigrateMain.jLabel23.text")); // NOI18N

        btnTestConnectSrc.setText(bundle.getString("MigrateMain.btnTestConnectSrc.text")); // NOI18N
        btnTestConnectSrc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestConnectSrcActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelStep2Layout = new javax.swing.GroupLayout(panelStep2);
        panelStep2.setLayout(panelStep2Layout);
        panelStep2Layout.setHorizontalGroup(
            panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep2Layout.createSequentialGroup()
                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelStep2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel13))
                    .addGroup(panelStep2Layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelStep2Layout.createSequentialGroup()
                                .addComponent(jLabel20)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtSrcDBUser, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelStep2Layout.createSequentialGroup()
                                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel18)
                                    .addComponent(jLabel19))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(edtSrcDBHost)
                                    .addComponent(cbxSrcDBType, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelStep2Layout.createSequentialGroup()
                                .addComponent(jLabel23)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtSrcDBName, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelStep2Layout.createSequentialGroup()
                                .addComponent(jLabel21)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTestConnectSrc)
                                    .addComponent(edtSrcDBPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel22, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtSrcDBPort, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(247, Short.MAX_VALUE))
        );
        panelStep2Layout.setVerticalGroup(
            panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel13)
                .addGap(79, 79, 79)
                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel19)
                    .addComponent(cbxSrcDBType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(edtSrcDBHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel22)
                    .addComponent(edtSrcDBPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(edtSrcDBName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel20)
                    .addComponent(edtSrcDBUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStep2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtSrcDBPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnTestConnectSrc)
                .addContainerGap(170, Short.MAX_VALUE))
        );

        panelSteps.add(panelStep2, "card2");

        jLabel14.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel14.setText(bundle.getString("MigrateMain.jLabel14.text")); // NOI18N

        jLabel24.setText(bundle.getString("MigrateMain.jLabel24.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.targetDB.port}"), edtTgtDBPort, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel25.setText(bundle.getString("MigrateMain.jLabel25.text")); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.targetDB.password}"), edtTgtDBPassword, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.targetDB.username}"), edtTgtDBUser, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.targetDB.database}"), edtTgtDBName, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.targetDB.host}"), edtTgtDBHost, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        jLabel26.setText(bundle.getString("MigrateMain.jLabel26.text")); // NOI18N

        jLabel27.setText(bundle.getString("MigrateMain.jLabel27.text")); // NOI18N

        jLabel28.setText(bundle.getString("MigrateMain.jLabel28.text")); // NOI18N

        cbxTgtDBType.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cbxTgtDBType.setName("cbxSrcDBType"); // NOI18N

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.targetDB.dBType}"), cbxTgtDBType, org.jdesktop.beansbinding.BeanProperty.create("selectedItem"));
        bindingGroup.addBinding(binding);

        cbxTgtDBType.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                cbxTgtDBTypeItemStateChanged(evt);
            }
        });

        jLabel29.setText(bundle.getString("MigrateMain.jLabel29.text")); // NOI18N

        btnTestConnectTarget.setText(bundle.getString("MigrateMain.btnTestConnectTarget.text")); // NOI18N
        btnTestConnectTarget.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTestConnectTargetActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelStep3Layout = new javax.swing.GroupLayout(panelStep3);
        panelStep3.setLayout(panelStep3Layout);
        panelStep3Layout.setHorizontalGroup(
            panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep3Layout.createSequentialGroup()
                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelStep3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel14))
                    .addGroup(panelStep3Layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelStep3Layout.createSequentialGroup()
                                .addComponent(jLabel27)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtTgtDBUser, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelStep3Layout.createSequentialGroup()
                                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(jLabel29)
                                    .addComponent(jLabel26))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(edtTgtDBHost)
                                    .addComponent(cbxTgtDBType, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelStep3Layout.createSequentialGroup()
                                .addComponent(jLabel24)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(edtTgtDBName, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(panelStep3Layout.createSequentialGroup()
                                .addComponent(jLabel28)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnTestConnectTarget)
                                    .addComponent(edtTgtDBPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel25, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(edtTgtDBPort, javax.swing.GroupLayout.PREFERRED_SIZE, 59, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(248, Short.MAX_VALUE))
        );
        panelStep3Layout.setVerticalGroup(
            panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel14)
                .addGap(72, 72, 72)
                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(cbxTgtDBType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(edtTgtDBHost, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel25)
                    .addComponent(edtTgtDBPort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(edtTgtDBName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(11, 11, 11)
                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(edtTgtDBUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelStep3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(edtTgtDBPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel28))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnTestConnectTarget)
                .addContainerGap(183, Short.MAX_VALUE))
        );

        panelSteps.add(panelStep3, "card3");

        jLabel16.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel16.setText(bundle.getString("MigrateMain.jLabel16.text")); // NOI18N

        tblTableMapping.setGridColor(new java.awt.Color(204, 204, 204));
        tblTableMapping.setShowGrid(true);

        org.jdesktop.beansbinding.ELProperty eLProperty = org.jdesktop.beansbinding.ELProperty.create("${migratePlan.tableSettings}");
        org.jdesktop.swingbinding.JTableBinding jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tblTableMapping);
        org.jdesktop.swingbinding.JTableBinding.ColumnBinding columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${sourceTable}"));
        columnBinding.setColumnName("Source Table");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${targetTable}"));
        columnBinding.setColumnName("Target Table");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${enabled}"));
        columnBinding.setColumnName("Enabled");
        columnBinding.setColumnClass(Boolean.class);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane2.setViewportView(tblTableMapping);
        if (tblTableMapping.getColumnModel().getColumnCount() > 0) {
            tblTableMapping.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("MigrateMain.tblTableMapping.columnModel.title0")); // NOI18N
            tblTableMapping.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("MigrateMain.tblTableMapping.columnModel.title1")); // NOI18N
            tblTableMapping.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("MigrateMain.tblTableMapping.columnModel.title2")); // NOI18N
        }

        btnAddMapping.setText(bundle.getString("MigrateMain.btnAddMapping.text")); // NOI18N
        btnAddMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddMappingActionPerformed(evt);
            }
        });

        btnDeleteMapping.setText(bundle.getString("MigrateMain.btnDeleteMapping.text")); // NOI18N
        btnDeleteMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteMappingActionPerformed(evt);
            }
        });

        btnEditMapping.setText(bundle.getString("MigrateMain.btnEditMapping.text")); // NOI18N
        btnEditMapping.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditMappingActionPerformed(evt);
            }
        });

        cbxCheckAll.setSelected(true);
        cbxCheckAll.setText(bundle.getString("MigrateMain.cbxCheckAll.text")); // NOI18N
        cbxCheckAll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbxCheckAllActionPerformed(evt);
            }
        });

        btnDeleteTargetData.setText(bundle.getString("MigrateMain.btnDeleteTargetData.text")); // NOI18N
        btnDeleteTargetData.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTargetDataActionPerformed(evt);
            }
        });

        jLabel35.setText(bundle.getString("MigrateMain.jLabel35.text")); // NOI18N

        javax.swing.GroupLayout panelStep4Layout = new javax.swing.GroupLayout(panelStep4);
        panelStep4.setLayout(panelStep4Layout);
        panelStep4Layout.setHorizontalGroup(
            panelStep4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep4Layout.createSequentialGroup()
                .addGroup(panelStep4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelStep4Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelStep4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                            .addGroup(panelStep4Layout.createSequentialGroup()
                                .addComponent(jLabel16)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(panelStep4Layout.createSequentialGroup()
                        .addComponent(btnAddMapping)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnEditMapping)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDeleteMapping)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cbxCheckAll))
                    .addGroup(panelStep4Layout.createSequentialGroup()
                        .addComponent(btnDeleteTargetData)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel35)
                        .addGap(1, 1, 1)
                        .addComponent(edtDeleteWhere)))
                .addContainerGap())
        );
        panelStep4Layout.setVerticalGroup(
            panelStep4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStep4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddMapping)
                    .addComponent(btnDeleteMapping)
                    .addComponent(btnEditMapping)
                    .addComponent(cbxCheckAll))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 358, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStep4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDeleteTargetData)
                    .addComponent(edtDeleteWhere, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel35))
                .addContainerGap(41, Short.MAX_VALUE))
        );

        panelSteps.add(panelStep4, "card4");

        panelStep5.setVerifyInputWhenFocusTarget(false);

        jLabel12.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel12.setText(bundle.getString("MigrateMain.jLabel12.text")); // NOI18N

        tblTableRelations.setGridColor(new java.awt.Color(204, 204, 204));
        tblTableRelations.setShowGrid(true);

        eLProperty = org.jdesktop.beansbinding.ELProperty.create("${migratePlan.sourceRelations}");
        jTableBinding = org.jdesktop.swingbinding.SwingBindings.createJTableBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, eLProperty, tblTableRelations);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${foreignTable}"));
        columnBinding.setColumnName("Foreign Table");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${foreignKey}"));
        columnBinding.setColumnName("Foreign Key");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${primaryTable}"));
        columnBinding.setColumnName("Primary Table");
        columnBinding.setColumnClass(String.class);
        columnBinding = jTableBinding.addColumnBinding(org.jdesktop.beansbinding.ELProperty.create("${primaryKey}"));
        columnBinding.setColumnName("Primary Key");
        columnBinding.setColumnClass(String.class);
        columnBinding.setEditable(false);
        bindingGroup.addBinding(jTableBinding);
        jTableBinding.bind();
        jScrollPane3.setViewportView(tblTableRelations);
        if (tblTableRelations.getColumnModel().getColumnCount() > 0) {
            tblTableRelations.getColumnModel().getColumn(0).setHeaderValue(bundle.getString("MigrateMain.tblTableRelations.columnModel.title0")); // NOI18N
            tblTableRelations.getColumnModel().getColumn(1).setHeaderValue(bundle.getString("MigrateMain.tblTableRelations.columnModel.title1")); // NOI18N
            tblTableRelations.getColumnModel().getColumn(2).setHeaderValue(bundle.getString("MigrateMain.tblTableRelations.columnModel.title2")); // NOI18N
            tblTableRelations.getColumnModel().getColumn(3).setHeaderValue(bundle.getString("MigrateMain.tblTableRelations.columnModel.title3")); // NOI18N
        }

        btnEditRelation.setText(bundle.getString("MigrateMain.btnEditRelation.text")); // NOI18N
        btnEditRelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditRelationActionPerformed(evt);
            }
        });

        btnDeleteMapping1.setText(bundle.getString("MigrateMain.btnDeleteMapping1.text")); // NOI18N

        btnAddRelation.setText(bundle.getString("MigrateMain.btnAddRelation.text")); // NOI18N
        btnAddRelation.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddRelationActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelStep5Layout = new javax.swing.GroupLayout(panelStep5);
        panelStep5.setLayout(panelStep5Layout);
        panelStep5Layout.setHorizontalGroup(
            panelStep5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelStep5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 623, Short.MAX_VALUE)
                    .addGroup(panelStep5Layout.createSequentialGroup()
                        .addGroup(panelStep5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(panelStep5Layout.createSequentialGroup()
                                .addComponent(btnAddRelation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnEditRelation)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnDeleteMapping1))
                            .addComponent(jLabel12))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelStep5Layout.setVerticalGroup(
            panelStep5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStep5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAddRelation)
                    .addComponent(btnDeleteMapping1)
                    .addComponent(btnEditRelation))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 409, Short.MAX_VALUE)
                .addGap(26, 26, 26))
        );

        panelSteps.add(panelStep5, "card5");

        panelStep6.setVerifyInputWhenFocusTarget(false);
        panelStep6.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                panelStep6ComponentShown(evt);
            }
        });

        jLabel32.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel32.setText(bundle.getString("MigrateMain.jLabel32.text")); // NOI18N

        btnRunImport.setText(bundle.getString("MigrateMain.btnRunImport.text")); // NOI18N
        btnRunImport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRunImportActionPerformed(evt);
            }
        });

        btnStop.setText(bundle.getString("MigrateMain.btnStop.text")); // NOI18N
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        btnExportLogs.setText(bundle.getString("MigrateMain.btnExportLogs.text")); // NOI18N
        btnExportLogs.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnExportLogs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportLogsActionPerformed(evt);
            }
        });

        mmoLogs.setEditable(false);
        jScrollPane4.setViewportView(mmoLogs);

        btnRollBack.setText(bundle.getString("MigrateMain.btnRollBack.text")); // NOI18N
        btnRollBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRollBackActionPerformed(evt);
            }
        });

        jPanel3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(153, 153, 153)));

        jLabel38.setText(bundle.getString("MigrateMain.jLabel38.text")); // NOI18N

        edtBatchSize.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter()));
        edtBatchSize.setValue(50);

        jLabel39.setForeground(new java.awt.Color(204, 204, 204));
        jLabel39.setText(bundle.getString("MigrateMain.jLabel39.text")); // NOI18N

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel38)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(edtBatchSize, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel39)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel38)
                    .addComponent(edtBatchSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel39))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel37.setText(bundle.getString("MigrateMain.jLabel37.text")); // NOI18N

        javax.swing.GroupLayout panelStep6Layout = new javax.swing.GroupLayout(panelStep6);
        panelStep6.setLayout(panelStep6Layout);
        panelStep6Layout.setHorizontalGroup(
            panelStep6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep6Layout.createSequentialGroup()
                .addComponent(btnRunImport)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnExportLogs)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnRollBack)
                .addGap(0, 165, Short.MAX_VALUE))
            .addGroup(panelStep6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelStep6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pbMain, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(panelStep6Layout.createSequentialGroup()
                        .addGroup(panelStep6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel32)
                            .addComponent(jLabel37))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        panelStep6Layout.setVerticalGroup(
            panelStep6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelStep6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel32)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel37)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelStep6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRunImport)
                    .addComponent(btnStop)
                    .addComponent(btnExportLogs)
                    .addComponent(btnRollBack))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 333, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(pbMain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        panelSteps.add(panelStep6, "card6");

        buttonPrev.setText(bundle.getString("MigrateMain.buttonPrev.text")); // NOI18N
        buttonPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonPrevActionPerformed(evt);
            }
        });

        buttonNext.setText(bundle.getString("MigrateMain.buttonNext.text")); // NOI18N
        buttonNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonNextActionPerformed(evt);
            }
        });

        btnSavePlan.setText(bundle.getString("MigrateMain.btnSavePlan.text")); // NOI18N
        btnSavePlan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSavePlanActionPerformed(evt);
            }
        });

        jLabel33.setText(bundle.getString("MigrateMain.jLabel33.text")); // NOI18N

        jLabel34.setForeground(new java.awt.Color(51, 102, 255));

        binding = org.jdesktop.beansbinding.Bindings.createAutoBinding(org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE, this, org.jdesktop.beansbinding.ELProperty.create("${migratePlan.name}"), jLabel34, org.jdesktop.beansbinding.BeanProperty.create("text"));
        bindingGroup.addBinding(binding);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelGuides, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel34, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSavePlan)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(buttonPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(buttonNext))
                    .addComponent(panelSteps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(panelGuides, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelSteps, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(buttonPrev)
                        .addComponent(buttonNext)
                        .addComponent(btnSavePlan))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel33)
                        .addComponent(jLabel34, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(10, 10, 10))
        );

        bindingGroup.bind();

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void buttonPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonPrevActionPerformed
        // TODO add your handling code here:
        if (!guideController.isFirst()){
            guideController.prev();
        }
        
    }//GEN-LAST:event_buttonPrevActionPerformed

    private void buttonNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonNextActionPerformed
       
        if (!guideController.isLast()){
                String validation = validateCurrentStep(); 
                if (StringUtils.isNotBlank(validation)){
                        JOptionPane.showMessageDialog(getRootPane(),validation);
                }else{
                    guideController.next();
                }

        }
        
    }//GEN-LAST:event_buttonNextActionPerformed

    private void listPlanMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listPlanMouseReleased
        // TODO add your handling code here:
        if (evt.getButton()==3&&listPlan.getSelectedIndex()>=0){
            popPlan.show(evt.getComponent(), evt.getX(), evt.getY());
        }
    }//GEN-LAST:event_listPlanMouseReleased

    private void cbxSrcDBTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxSrcDBTypeItemStateChanged
        if (ItemEvent.SELECTED == evt.getStateChange()){
            DBType dBType = (DBType)evt.getItem();
            edtSrcDBHost.setText("127.0.0.1");
            edtSrcDBPort.setText(dBType.getDefaultPort());
            
        }
        
    }//GEN-LAST:event_cbxSrcDBTypeItemStateChanged

    private void btnTestConnectSrcActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestConnectSrcActionPerformed
        //
        String validationMsg =validateCurrentStep();
        if (StringUtils.isNotBlank(validationMsg)){
            JOptionPane.showMessageDialog(rootPane, validationMsg);
            return;
        }
        DBType dBType = (DBType)cbxSrcDBType.getSelectedItem();
        DBSetting dbs = new DBSetting(dBType,edtSrcDBHost.getText(),edtSrcDBPort.getText(),
                        edtSrcDBName.getText(),edtSrcDBUser.getText(),String.valueOf(edtSrcDBPassword.getPassword()));
        Connection connection = ConnectionUtils.connect(dbs);
        if (connection!=null){
            JOptionPane.showMessageDialog(rootPane,bundle.getString("MigrateMain.db.success"));
            ConnectionUtils.disconnect(connection);
        }else{
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.db.error"));
        }
        
    }//GEN-LAST:event_btnTestConnectSrcActionPerformed

    private void btnTestConnectTargetActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTestConnectTargetActionPerformed
        String validationMsg =validateCurrentStep();
        if (StringUtils.isNotBlank(validationMsg)){
            JOptionPane.showMessageDialog(rootPane, validationMsg);
            return;
        }
        DBType dBType = (DBType)cbxTgtDBType.getSelectedItem();
        DBSetting dbs = new DBSetting(dBType,edtTgtDBHost.getText(),edtTgtDBPort.getText(),
                        edtTgtDBName.getText(),edtTgtDBUser.getText(),String.valueOf(edtTgtDBPassword.getPassword()));
        Connection connection = ConnectionUtils.connect(dbs);
        if (connection!=null){
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.db.success"));
            ConnectionUtils.disconnect(connection);
        }else{
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.db.error"));
        }        // TODO add your handling code here:
        
    }//GEN-LAST:event_btnTestConnectTargetActionPerformed

    private void tabPlanStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabPlanStateChanged
        // TODO add your handling code here:
        createNewPlan = tabPlan.getSelectedIndex()==0;
        if (tabPlan.getSelectedIndex()==0){
               MigratePlan newPlan =  MigratePlan.createNewPlan();
               if (StringUtils.isNotBlank(textNewPlan.getText())){
                   newPlan.setName(textNewPlan.getText());
               }else{
                   newPlan.setName(bundle.getString("MigrateMain.plan.defaultName")+(listPlan.getModel().getSize()+1));
                   textNewPlan.setText(newPlan.getName());
               }
              setMigratePlan(newPlan);
        }else if (tabPlan.getSelectedIndex()==1){
            setSelectedMigratePlan();
        }
    }//GEN-LAST:event_tabPlanStateChanged

    private void setSelectedMigratePlan(){
        String planName =  (String)listPlan.getSelectedValue();
        if (StringUtils.isNotBlank(planName)){
            try {
                setMigratePlan(MigratePlanIO.deSerialize(planName));
            } catch (IOException ex) {
                Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void listPlanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_listPlanMouseClicked
        // TODO add your handling code here:
        setSelectedMigratePlan();
    }//GEN-LAST:event_listPlanMouseClicked

    private void btnAddMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddMappingActionPerformed
        // TODO add your handling code here:
        MappingDialog mappingDialog = new MappingDialog(this,true);
        mappingDialog.setMigratePlan(migratePlan);
        mappingDialog.setLocationRelativeTo(null);
        mappingDialog.setVisible(true);
        
        if (mappingDialog.getModalResult()==0){
            if (SettingUtils.isTableSettingCanAddToPlan(mappingDialog.getTableSetting(), migratePlan)){
                List<TableSetting> ls = new ArrayList<TableSetting>();
                ls.addAll(migratePlan.getTableSettings());
                migratePlan.setTableSettings(ls);
                migratePlan.getTableSettings().add(mappingDialog.getTableSetting());
                firePropertyChange(null,null,null);
            }else{
               JOptionPane.showMessageDialog(rootPane,bundle.getString("MigrateMain.error.mappingName"));
            }
        }
        
        
    }//GEN-LAST:event_btnAddMappingActionPerformed

    private void btnEditMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditMappingActionPerformed
        // TODO add your handling code here:
        if (tblTableMapping.getSelectedRowCount()==0){
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.alert.selectMapping"));
        }else{
            String sourceTable = (String)tblTableMapping.getValueAt(tblTableMapping.getSelectedRow(),0);
            TableSetting tableSetting = migratePlan.getTableSettingBySourceTable(sourceTable);
            if (tableSetting!=null){
                MappingDialog mappingDialog = new MappingDialog(this,true);
                mappingDialog.setMigratePlan(migratePlan);
                mappingDialog.setTableSetting(tableSetting);
                mappingDialog.setLocationRelativeTo(null);
                mappingDialog.setVisible(true);
                if (mappingDialog.getModalResult()==0){
                    tblTableMapping.setValueAt(mappingDialog.getTableSetting().getSourceTable(), tblTableMapping.getSelectedRow(),0);
                    tblTableMapping.setValueAt(mappingDialog.getTableSetting().getTargetTable(), tblTableMapping.getSelectedRow(),1);
                }
            }
        }
    }//GEN-LAST:event_btnEditMappingActionPerformed

    private void btnEditRelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditRelationActionPerformed
         if (tblTableRelations.getSelectedRowCount()==0){
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.alert.selectRelation"));
        }else{
            RelationDialog relationDialog = new RelationDialog(this, true);
            relationDialog.setMigratePlan(migratePlan);
            relationDialog.setTableRelation(migratePlan.getSourceRelations().get(tblTableRelations.getSelectedRow()));
            relationDialog.setLocationRelativeTo(null);
            relationDialog.setVisible(true);
            if (relationDialog.getModalResult()==0){
                TableRelation relation = relationDialog.getTableRelation();
                int index = tblTableRelations.getSelectedRow();
                tblTableRelations.setValueAt(relation.getForeignTable(), index, 0);
                tblTableRelations.setValueAt(relation.getForeignKey(), index, 1);
                tblTableRelations.setValueAt(relation.getPrimaryTable(), index, 2);
                tblTableRelations.setValueAt(relation.getPrimaryKey(), index, 3);
                
            }  
        }
        
    }//GEN-LAST:event_btnEditRelationActionPerformed

    private void btnAddRelationActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddRelationActionPerformed
        RelationDialog relationDialog = new RelationDialog(this, true);
        relationDialog.setMigratePlan(migratePlan);
        relationDialog.setLocationRelativeTo(null);
        relationDialog.setVisible(true);
        if (relationDialog.getModalResult()==0){
            List<TableRelation> ls = new ArrayList<TableRelation>();
            ls.addAll(migratePlan.getSourceRelations());
            migratePlan.setSourceRelations(ls);
            migratePlan.getSourceRelations().add(relationDialog.getTableRelation());
            firePropertyChange(null, null, null);
        }
        
    }//GEN-LAST:event_btnAddRelationActionPerformed

    private void btnSavePlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSavePlanActionPerformed
        try {
            // TODO add your handling code here:
            MigratePlanIO.serialize(migratePlan);
            if (tabPlan.getSelectedIndex()==0){
                initModel();
                textNewPlan.setText(null);
            }
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.message.savePlanPrefix") + migratePlan.getName()+  bundle.getString("MigrateMain.message.savePlanSuffix"));
        } catch (IOException ex) {
            Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_btnSavePlanActionPerformed

    private void btnDeleteMappingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteMappingActionPerformed
        if (tblTableMapping.getSelectedRowCount()==0){
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.alert.deleteMapping"));
        }else{
            if (JOptionPane.showConfirmDialog(rootPane, bundle.getString("MigrateMain.alert.confirmDeleteMapping"),
                        UIManager.getString("OptionPane.titleText"),JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                
                String sourceTable = (String)tblTableMapping.getValueAt(tblTableMapping.getSelectedRow(),0);
                TableSetting tableSetting = migratePlan.getTableSettingBySourceTable(sourceTable);
                if (tableSetting!=null){
                    migratePlan.getTableSettings().remove(tableSetting);
                    tblTableMapping.remove(tblTableMapping.getSelectedRow());
                }
            }
            
        }
    }//GEN-LAST:event_btnDeleteMappingActionPerformed

    private void cbxCheckAllActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbxCheckAllActionPerformed
        // TODO add your handling code here:
        for (int i = 0; i < tblTableMapping.getRowCount(); i++) {
            tblTableMapping.setValueAt(cbxCheckAll.isSelected(), i, 2);
        }
    }//GEN-LAST:event_cbxCheckAllActionPerformed

    private void popDeletePlanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_popDeletePlanMouseClicked
        //
    }//GEN-LAST:event_popDeletePlanMouseClicked

    private void popDeletePlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popDeletePlanActionPerformed
        //
        if (listPlan.getSelectedIndex()>=0){
            if (JOptionPane.OK_OPTION==JOptionPane.showConfirmDialog(rootPane, bundle.getString("MigrateMain.alert.confirmDeletePlan"),"Confirm",JOptionPane.OK_CANCEL_OPTION)){
                MigratePlanIO.deletePlan((String)listPlan.getSelectedValue());
                initModel();
                if (listPlan.getModel().getSize()>0){
                    listPlan.setSelectedIndex(listPlan.getModel().getSize()-1);
                    setSelectedMigratePlan();
                }
            }
        }
        
    }//GEN-LAST:event_popDeletePlanActionPerformed

    private void textNewPlanPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_textNewPlanPropertyChange
        // TODO add your handling code here:
        
    }//GEN-LAST:event_textNewPlanPropertyChange

    private void textNewPlanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_textNewPlanKeyReleased
        migratePlan.setName(textNewPlan.getText());
        firePropertyChange(null, null, null);
    }//GEN-LAST:event_textNewPlanKeyReleased

    private void cbxTgtDBTypeItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_cbxTgtDBTypeItemStateChanged
        // TODO add your handling code here:
        if (ItemEvent.SELECTED == evt.getStateChange()){
            DBType dBType = (DBType)evt.getItem();
            if (StringUtils.isBlank(edtTgtDBHost.getText())){
                edtTgtDBHost.setText("127.0.0.1");
                edtTgtDBPort.setText(dBType.getDefaultPort());
            }
            
        }
    }//GEN-LAST:event_cbxTgtDBTypeItemStateChanged

    private void btnExportLogsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportLogsActionPerformed
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("log.txt"));
        int returnVal = chooser.showSaveDialog(getRootPane());
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            String filepath = chooser.getSelectedFile().getAbsolutePath();
            filepath = FilenameUtils.removeExtension(filepath)+".txt";
            try {
                FileUtils.write(new File(filepath), mmoLogs.getText());
            } catch (IOException ex) {
                Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
    }//GEN-LAST:event_btnExportLogsActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        if (currentThread!=null&&currentThread.isAlive()){
            currentThread.running=false;
        }
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnDeleteTargetDataActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTargetDataActionPerformed
        
        
        if (tblTableMapping.getSelectedRowCount()==0){
            JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.alert.deleteTargetData"));
        }else{
            if (JOptionPane.showConfirmDialog(rootPane, bundle.getString("MigrateMain.alert.confirmDeleteTargetData"),
                        UIManager.getString("OptionPane.titleText"),JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
                
                 importManager.setMigratePlan(migratePlan);
                 importManager.connectToDataBase(false,true);
                 
                 importManager.deleteTargetData((String)tblTableMapping.getValueAt(tblTableMapping.getSelectedRow(), 1), edtDeleteWhere.getText());
        
                 importManager.closeConnection();
                 JOptionPane.showMessageDialog(rootPane,bundle.getString("MigrateMain.message.deleteTarget"));
            }
            
        }
                
    }//GEN-LAST:event_btnDeleteTargetDataActionPerformed

    private void btnRunImportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRunImportActionPerformed
        importManager.setBatchSize(Integer.parseInt(edtBatchSize.getText()));
        currentThread = new ImportThread(importManager, this, mmoLogs, pbMain, migratePlan);
        currentThread.start();
    }//GEN-LAST:event_btnRunImportActionPerformed

    private void btnRollBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRollBackActionPerformed
        if (JOptionPane.showConfirmDialog(rootPane, bundle.getString("MigrateMain.alert.confirmRollback"),
                        UIManager.getString("OptionPane.titleText"),JOptionPane.OK_CANCEL_OPTION)==JOptionPane.OK_OPTION){
            currentThread.deleteImportedData();
        }
        
    }//GEN-LAST:event_btnRollBackActionPerformed

    private void panelStep6ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_panelStep6ComponentShown
        refreshUI();
    }//GEN-LAST:event_panelStep6ComponentShown

    private void popExportPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popExportPlanActionPerformed
         if (listPlan.getSelectedIndex()>=0){
             String planName = (String)listPlan.getSelectedValue();
             String planPath = MigratePlanIO.getPlanRoot()+"/"+ planName;
             JFileChooser fc = new JFileChooser();
             int returnVal = fc.showSaveDialog(this);
             if (returnVal == JFileChooser.APPROVE_OPTION) {
                 File file = fc.getSelectedFile();
                 try {
                     FileUtils.copyFile(new File(planPath),file);
                     JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.message.export")+file.getAbsolutePath());
                 } catch (IOException ex) {
                     Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }        
         }
        
    }//GEN-LAST:event_popExportPlanActionPerformed

    private void popImportPlanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_popImportPlanActionPerformed
        JFileChooser fc = new JFileChooser();
             int returnVal = fc.showSaveDialog(this);
             if (returnVal == JFileChooser.APPROVE_OPTION) {
                 File file = fc.getSelectedFile();
                 String planName = FilenameUtils.getBaseName(file.getName());
                 String planPath = MigratePlanIO.getPlanRoot()+"/"+ planName;
                 try {
                     FileUtils.copyFile(file,new File(planPath));
                     initModel();
                     JOptionPane.showMessageDialog(rootPane, bundle.getString("MigrateMain.message.import")+planName);
                 } catch (IOException ex) {
                     Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
                 }
             }   
    }//GEN-LAST:event_popImportPlanActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            /* Set the Nimbus look and feel */
            //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
            /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
            * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
            */
            
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MigrateMain.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MigrateMain migrateMain = new MigrateMain();
                migrateMain.setLocationRelativeTo(null);
                migrateMain.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAddMapping;
    private javax.swing.JButton btnAddRelation;
    private javax.swing.JButton btnDeleteMapping;
    private javax.swing.JButton btnDeleteMapping1;
    private javax.swing.JButton btnDeleteTargetData;
    private javax.swing.JButton btnEditMapping;
    private javax.swing.JButton btnEditRelation;
    private javax.swing.JButton btnExportLogs;
    private javax.swing.JButton btnRollBack;
    private javax.swing.JButton btnRunImport;
    private javax.swing.JButton btnSavePlan;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnTestConnectSrc;
    private javax.swing.JButton btnTestConnectTarget;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton buttonNext;
    private javax.swing.JButton buttonPrev;
    private javax.swing.JCheckBox cbxCheckAll;
    private javax.swing.JComboBox cbxSrcDBType;
    private javax.swing.JComboBox cbxTgtDBType;
    private javax.swing.JFormattedTextField edtBatchSize;
    private javax.swing.JTextField edtDeleteWhere;
    private javax.swing.JTextField edtSrcDBHost;
    private javax.swing.JTextField edtSrcDBName;
    private javax.swing.JPasswordField edtSrcDBPassword;
    private javax.swing.JTextField edtSrcDBPort;
    private javax.swing.JTextField edtSrcDBUser;
    private javax.swing.JTextField edtTgtDBHost;
    private javax.swing.JTextField edtTgtDBName;
    private javax.swing.JPasswordField edtTgtDBPassword;
    private javax.swing.JTextField edtTgtDBPort;
    private javax.swing.JTextField edtTgtDBUser;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JList listPlan;
    private javax.swing.JTextPane mmoLogs;
    private javax.swing.JPanel panelGuide1;
    private javax.swing.JPanel panelGuide2;
    private javax.swing.JPanel panelGuide3;
    private javax.swing.JPanel panelGuide4;
    private javax.swing.JPanel panelGuide5;
    private javax.swing.JPanel panelGuide6;
    private javax.swing.JPanel panelGuides;
    private javax.swing.JPanel panelStep1;
    private javax.swing.JPanel panelStep2;
    private javax.swing.JPanel panelStep3;
    private javax.swing.JPanel panelStep4;
    private javax.swing.JPanel panelStep5;
    private javax.swing.JPanel panelStep6;
    private javax.swing.JPanel panelSteps;
    private javax.swing.JProgressBar pbMain;
    private javax.swing.JMenuItem popDeletePlan;
    private javax.swing.JMenuItem popExportPlan;
    private javax.swing.JMenuItem popImportPlan;
    private javax.swing.JPopupMenu popPlan;
    private javax.swing.JTabbedPane tabPlan;
    private javax.swing.JTable tblTableMapping;
    private javax.swing.JTable tblTableRelations;
    private javax.swing.JTextField textNewPlan;
    private org.jdesktop.beansbinding.BindingGroup bindingGroup;
    // End of variables declaration//GEN-END:variables

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
        MigratePlan oldPlan = this.migratePlan;
        this.migratePlan = migratePlan;
        firePropertyChange("migratePlan", oldPlan, migratePlan);
    }

    /**
     * @return the migratePlans
     */
    public DefaultListModel getMigratePlans() {
        return migratePlans;
    }

    /**
     * @param migratePlans the migratePlans to set
     */
    public void setMigratePlans(DefaultListModel migratePlans) {
        this.migratePlans = migratePlans;
    }

   

    

}

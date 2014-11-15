/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.smart.migrate.setting;

import com.google.common.base.Objects;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.PKStrategy;

/**
 * Table Settings Info
 * 表映射信息
 *
 * @author Sandy Duan
 */
public class TableSetting implements Serializable, Comparable<TableSetting> {

    private String sourceTable;
    private String targetTable;
    private String where;
    private boolean enabled = true;

    private PKStrategy pKStrategy = PKStrategy.NEW;

    private List<FieldSetting> fieldSettings = new ArrayList<FieldSetting>();

    private transient int referenceCount;

    private static final long serialVersionUID = 5817738063403061042l;
    /**
     * Get Source Table Primary Key
     * 获取源表的主键
     *
     * @return
     */
    public String getSourcePK() {
        for (FieldSetting fieldSetting : fieldSettings) {
            if (fieldSetting.isPrimaryKey()) {
                return fieldSetting.getSourceField();
            }
        }
        return null;
    }

    /**
     * Get target table Primary key
     * 获取目标表的主键
     *
     * @return
     */
    public String getTargetPK() {
        for (FieldSetting fieldSetting : fieldSettings) {
            if (fieldSetting.isPrimaryKey()) {
                return fieldSetting.getTargetField();
            }
        }
        return null;
    }

    /**
     * Get Source Field Setting by field name
     * 根据源表字段获取字段配置信息
     *
     * @param sourceField
     * @return
     */
    public FieldSetting getFieldSettingBySourceField(String sourceField) {
        
        for (FieldSetting fieldSetting : fieldSettings) {           
            if (StringUtils.isNotBlank(fieldSetting.getSourceField()) && fieldSetting.getSourceField().equals(sourceField)) {
                return fieldSetting;
            }
        }
        return null;
    }

    /**
     * Get Target Field Setting by field name
     * 根据目标表字段获取字段配置信息
     *
     * @param targetField
     * @return
     */
    public FieldSetting getFieldSettingByTargetField(String targetField) {
        for (FieldSetting fieldSetting : fieldSettings) {
            if (fieldSetting.getTargetField().equals(targetField)) {
                return fieldSetting;
            }
        }
        return null;
    }

    /**
     * @return the sourceTable
     */
    public String getSourceTable() {
        return sourceTable;
    }

    /**
     * @param sourceTable the sourceTable to set
     */
    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    /**
     * @return the targetTable
     */
    public String getTargetTable() {
        return targetTable;
    }

    /**
     * @param targetTable the targetTable to set
     */
    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    /**
     * @return the where
     */
    public String getWhere() {
        return where;
    }

    /**
     * @param where the where to set
     */
    public void setWhere(String where) {
        this.where = where;
    }

    /**
     * @return the fieldSettings
     */
    public List<FieldSetting> getFieldSettings() {
        return fieldSettings;
    }

    /**
     * @param fieldSettings the fieldSettings to set
     */
    public void setFieldSettings(List<FieldSetting> fieldSettings) {
        this.fieldSettings = fieldSettings;
    }

    @Override
    public String toString() {
        return "TableSetting{" + "sourceTable=" + sourceTable + ", targetTable=" + targetTable + ", where=" + where + ", fieldSettings=" + fieldSettings + '}';
    }

    @Override
    public int compareTo(TableSetting o) {
        return (this.getSourceTable() + this.getTargetTable()).compareTo(o.getSourceTable() + o.getTargetTable());
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 73 * hash + Objects.hashCode(this.sourceTable);
        hash = 73 * hash + Objects.hashCode(this.targetTable);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TableSetting other = (TableSetting) obj;
        if ((this.sourceTable == null) ? (other.sourceTable != null) : !this.sourceTable.equals(other.sourceTable)) {
            return false;
        }
        if ((this.targetTable == null) ? (other.targetTable != null) : !this.targetTable.equals(other.targetTable)) {
            return false;
        }
        return true;
    }

    /**
     * @return the enabled
     */
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * @param enabled the enabled to set
     */
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * @return the pKStrategy
     */
    public PKStrategy getpKStrategy() {
        return pKStrategy;
    }

    /**
     * @param pKStrategy the pKStrategy to set
     */
    public void setpKStrategy(PKStrategy pKStrategy) {
        this.pKStrategy = pKStrategy;
    }

    /**
     * @return the referenceCount
     */
    public int getReferenceCount() {
        return referenceCount;
    }

    /**
     * @param referenceCount the referenceCount to set
     */
    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

}

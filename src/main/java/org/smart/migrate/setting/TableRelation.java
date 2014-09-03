/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import com.google.common.base.Objects;
import java.io.Serializable;

/**
 *
 * @author Sandy Duan
 */
public class TableRelation  implements  Serializable{
    
    //外键表
    private String foreignTable;
    //外键
    private String foreignKey;
    
    //外键表关联的主键表
    private String primaryTable;
    //外键表关联的主键
    private String primaryKey;

    /**
     * @return the foreignTable
     */
    public String getForeignTable() {
        return foreignTable;
    }

    /**
     * @param foreignTable the foreignTable to set
     */
    public void setForeignTable(String foreignTable) {
        this.foreignTable = foreignTable;
    }

    /**
     * @return the foreignKey
     */
    public String getForeignKey() {
        return foreignKey;
    }

    /**
     * @param foreignKey the foreignKey to set
     */
    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    /**
     * @return the primaryTable
     */
    public String getPrimaryTable() {
        return primaryTable;
    }

    /**
     * @param primaryTable the primaryTable to set
     */
    public void setPrimaryTable(String primaryTable) {
        this.primaryTable = primaryTable;
    }

    /**
     * @return the primaryKey
     */
    public String getPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.foreignTable);
        hash = 11 * hash + Objects.hashCode(this.foreignKey);
        hash = 11 * hash + Objects.hashCode(this.primaryTable);
        hash = 11 * hash + Objects.hashCode(this.primaryKey);
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
        final TableRelation other = (TableRelation) obj;
        if ((this.foreignTable == null) ? (other.foreignTable != null) : !this.foreignTable.equals(other.foreignTable)) {
            return false;
        }
        if ((this.foreignKey == null) ? (other.foreignKey != null) : !this.foreignKey.equals(other.foreignKey)) {
            return false;
        }
        if ((this.primaryTable == null) ? (other.primaryTable != null) : !this.primaryTable.equals(other.primaryTable)) {
            return false;
        }
        if ((this.primaryKey == null) ? (other.primaryKey != null) : !this.primaryKey.equals(other.primaryKey)) {
            return false;
        }
        return true;
    }

   
    
    
    
    
}

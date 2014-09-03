/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

/**
 * Field Info Settings contains sourceField,targetField,defaultValue,primaryKey,dicts
 * 字段映射设置
 * @author Sandy Duan
 */
public class FieldSetting implements Serializable{
    
    private String sourceField;
    private String targetField;
    private String defaultValue;
    private boolean primaryKey;
    
    private String dictText;
    
    /**
     * 解析数据字典字符串并返回散列表形式的字典
     * @return
     */
    public Map<String,String> getDict(){
        if (StringUtils.isNotBlank(getDictText())){
            Map<String,String> dictMap = new HashMap<String,String>();
           String[] dicts = getDictText().split(",");
           for(String d:dicts){
               String[] kv = d.split("=");
               dictMap.put(kv[0],kv[1]);
           }    
           return dictMap;
        }    
        return null;
    }

    /**
     * @return the sourceField
     */
    public String getSourceField() {
        return sourceField;
    }

    /**
     * @param sourceField the sourceField to set
     */
    public void setSourceField(String sourceField) {
        this.sourceField = sourceField;
    }

    /**
     * @return the targetField
     */
    public String getTargetField() {
        return targetField;
    }

    /**
     * @param targetField the targetField to set
     */
    public void setTargetField(String targetField) {
        this.targetField = targetField;
    }

    /**
     * @return the defaultValue
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * @param defaultValue the defaultValue to set
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * @return the dictText
     */
    public String getDictText() {
        return dictText;
    }

    /**
     * @param dictText the dictText to set
     */
    public void setDictText(String dictText) {
        this.dictText = dictText;
    }

    @Override
    public String toString() {
        return "FieldSetting{" + "sourceField=" + sourceField + ", targetField=" + targetField + ", defaultValue=" + defaultValue + ", dictText=" + dictText + '}';
    }

    /**
     * @return the primaryKey
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * @param primaryKey the primaryKey to set
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }
        
    
}

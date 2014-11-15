/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.model;

import java.util.HashMap;
import java.util.Map;
import java.util.prefs.Preferences;
import org.apache.commons.lang3.StringUtils;
import org.smart.migrate.ui.MigrateMain;

/**
 *
 * @author sindtom
 */
public class MigrateOptions {
    
    private  final static MigrateOptions instance = new MigrateOptions();
    
    private Map<String,Object> defaultValues = new HashMap<String, Object>();
    
    private final Preferences preferences = Preferences.userRoot().node(this.getClass().getName());
    
    private String defaultValueText = "";
    
    private MigrateOptions (){
        
    }
    
    
    public static MigrateOptions getInstance(){
        instance.loadOptions();
        return instance;
    }

    /**
     * @return the defaultValues
     */
    public Map<String,Object> getDefaultValues() {
        return defaultValues;
    }
    
    public void loadOptions(){
        defaultValueText = preferences.get("options.default.values", null);
        defaultValues = textToMap(defaultValueText);
    }
    
    public void saveOptions(){
        if (StringUtils.isNotBlank(defaultValueText)){
            preferences.put("options.default.values", defaultValueText);
        }
    }
    
    private static String mapToText(Map<String,Object> map){
        String result = "";
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            String field = entry.getKey();
            Object value = entry.getValue();
            String s = field+"="+value;
            result+=s+"\n";
        }
        return result;
    }
    
    private static Map<String,Object> textToMap(String text){
        Map<String,Object> map = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(text)){
            String[] fields = text.split("\\n");
             for (String string : fields) {
                    int idx = string.indexOf('=');
                    if (idx>=0){
                        map.put(string.substring(0,idx), string.substring(idx+1));
                    }
             }
        }
         return map;
    }



    /**
     * @return the defaultValueText
     */
    public String getDefaultValueText() {
        return defaultValueText;
    }

    /**
     * @param defaultValueText the defaultValueText to set
     */
    public void setDefaultValueText(String defaultValueText) {
        this.defaultValueText = defaultValueText;
    }
    
    
}

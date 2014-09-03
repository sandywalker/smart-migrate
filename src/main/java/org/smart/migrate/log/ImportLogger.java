/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.log;

/**
 * Interface for log import info
 * @author Sandy Duan
 */
public interface ImportLogger {
    
    public void log(String level,String msg);
    public void log(String msg);
    
}

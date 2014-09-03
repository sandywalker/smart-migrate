/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate;

/**
 * Primary Key Strategy
 * 主键生成策略
 * @author Sandy Duan
 */
public enum PKStrategy {
    COPY("Copy"),NEW("New");
    
    private final String description;
    
    private PKStrategy(String description){
        this.description = description;
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }
    
    public static PKStrategy getStrategyByDescription(String description){
        for(PKStrategy strategy:PKStrategy.values()){
            if (strategy.getDescription().equals(description)){
                return strategy;
            }
        }
        
        return PKStrategy.NEW;
    }
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.dao;

/**
 *
 * @author Sandy Duan
 */
public abstract class MetaAbstractDao implements MetaDao{
    
   protected  MetaDaoTemplate metaDaoTemplate;

   public MetaAbstractDao(MetaDaoTemplate metaDaoTemplate){
       this.metaDaoTemplate = metaDaoTemplate;
   }
    
}

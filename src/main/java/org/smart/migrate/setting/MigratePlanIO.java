/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.smart.migrate.setting;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.FilenameUtils;

/**
 * MigratePlan Input And Output
 * @author Sandy Duan
 */
public class MigratePlanIO {
    
    /**
     * Scan all plans in project root
     * 扫描项目根目录下的所有迁移方案
     * @return
     */
    public static List<String> scanPlans(){
        String planRoot = getPlanRoot();
        File file = new File(planRoot);
        return Arrays.asList(file.list());
    }
    
    /**
     * Delete Plan by plan name
     * 根据方案名称删除迁移方案
     * @param planName
     */
    public static void deletePlan(String planName){
        File file = new File(getPlanRoot()+"/"+planName);
        if (file.exists()){
            file.delete();
        }
    }
    
    
    /**
     * Get Plan Root in which all plans stored
     * 获取方案根目录
     * @return
     */
    public static String getPlanRoot(){
          String rootFile = MigratePlanIO.class.getProtectionDomain().getCodeSource().getLocation().getFile();
          System.out.println(rootFile);
//        String path = MigratePlanSerializer.class.getResource("/").getPath();
        String root = FilenameUtils.getFullPath(rootFile) +"plans";
        File file = new File(root);
        if (!file.exists()){
            file.mkdir();
        }
        return root;
    }
    
    /**
     * Serialize the plan
     * 序列化方案
     * @param migratePlan
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     */
    public static void serialize(MigratePlan migratePlan) throws IOException,FileNotFoundException{
        OutputStream fos = new FileOutputStream(getPlanRoot()+"/"+ migratePlan.getName());
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(migratePlan);
    }
    
    /**
     * Serialize the plan to File
     * 将方案序列化到文件
     * @param migratePlan
     * @param file
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static void serializeToFile(MigratePlan migratePlan,File file) throws IOException,FileNotFoundException{
        OutputStream fos = new FileOutputStream(file);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(migratePlan);
    }
    
    /**
     * DeSerialize plan by name
     * 反序列化方案
     * @param planName
     * @return
     * @throws java.io.IOException
     * @throws java.io.FileNotFoundException
     * @throws java.lang.ClassNotFoundException
     */
    public static MigratePlan deSerialize(String planName) throws IOException,FileNotFoundException,ClassNotFoundException{
        File file = new File(getPlanRoot()+"/"+planName);
        if (file.exists()){
            InputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object o = ois.readObject();
            return (MigratePlan)o;
        }
        return null;
    }
    
}

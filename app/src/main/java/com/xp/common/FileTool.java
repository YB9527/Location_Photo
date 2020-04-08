package com.xp.common;


import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * 文件处理工具类
 */
public class FileTool {
    /**
     * @param path
     * @return 获取文件的名字 包含文件后缀名
     */
    public static String getFileName(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf("/");
        if (index != -1 && index != path.length() - 1) {
            return path.substring(index + 1);
        }
        return null;
       /* Path.GetFileNameWithoutExtension
        Path.GetExtension
        Path.GetFileName*/
    }

    /***
     *
     * @param path
     * @return 返回不具有后缀名的 文件名
     */
    public static String getFileNameWithoutExtension(String path) {
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int index = fileName.indexOf(".");
        if (index != -1) {
            return fileName.substring(0, index);
        }
        return null;
    }

    /**
     *
     * @param path
     * @return 返回后缀名
     */
    public static String getExtension(String path) {
        String fileName = getFileName(path);
        if (fileName == null) {
            return null;
        }
        int index = fileName.indexOf(".");
        if (index != -1 && index != fileName.length() - 1) {
            return fileName.substring(index + 1);
        }
        return null;
    }

    /**
     *
     * @param path
     * @return 获取文件路径
     */
    public static String getDir(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf("/");
        if (index >0 ) {
            return path.substring(0,index);
        }
        return null;
    }
    /**
     * 文件复制
     * @param source
     * @param dest
     * @return
     */
    public static boolean copyFile(String source, String dest) {
        FileChannel input = null;
        FileChannel output = null;
        try {
            File dir = new File(getDir(dest));
            if(!dir.exists()){
                dir.mkdirs();
            }
            input = new FileInputStream(new File(source)).getChannel();
            output = new FileOutputStream(new File(dest)).getChannel();
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            return false;
        }
        return  true;
    }

    /**
     * 检查文件是否存在
     * @param filePath
     * @return
     */
    public static boolean exitFile(String filePath) {
boolean bl = (filePath == null|| filePath.trim() == "")?false:new File(filePath).exists();
        return  bl;

    }

    /**
     * 删除文件
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if(file.exists()){
           return file.delete();
        }
        return  false;
    }
    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @param isCreated
     * @return
     */
    public static boolean exitsDir(File dir, boolean isCreated) {
        if (dir.exists()) {
            return true;
        } else {
            if (isCreated) {
                return dir.mkdirs();
            }
        }
        return false;
    }
    /**
     * 检查文件夹是否存在
     *
     * @param dir
     * @param isCreated
     * @return
     */
    public static boolean exitsDir(String dir, boolean isCreated) {
        File  f = new File(dir);
        return  exitsDir(f,isCreated);
    }
}

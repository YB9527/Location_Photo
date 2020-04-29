package com.xp.common.tools;


import android.annotation.TargetApi;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.Date;

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
     * @param path
     * @return 获取文件路径
     */
    public static String getDir(String path) {
        if (path == null) {
            return null;
        }
        int index = path.lastIndexOf("/");
        if (index > 0) {
            return path.substring(0, index);
        }
        return null;
    }

    /**
     * 文件复制
     *
     * @param source
     * @param dest
     * @return
     */
    public static boolean copyFile(String source, String dest) {
        FileChannel input = null;
        FileChannel output = null;
        try {
            File dir = new File(getDir(dest));
            if (!dir.exists()) {
                dir.mkdirs();
            }
            input = new FileInputStream(new File(source)).getChannel();
            output = new FileOutputStream(new File(dest)).getChannel();
            output.transferFrom(input, 0, input.size());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 检查文件是否存在
     *
     * @param filePath
     * @return
     */
    public static boolean exitFile(String filePath) {
        boolean bl = (filePath == null || filePath.trim() == "") ? false : new File(filePath).exists();
        return bl;

    }

    /**
     * 删除文件
     *
     * @param path
     */
    public static boolean deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            return file.delete();
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
        File f = new File(dir);
        return exitsDir(f, isCreated);
    }

    /**
     * 保存文件
     *
     * @param is       数据流
     * @param savePath 保存的位置
     */
    public static void saveFile(InputStream is, String savePath) {

        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        // 储存下载文件的目录
        try {
            fos = new FileOutputStream(new File(savePath));
            long sum = 0;
            while ((len = is.read(buf)) != -1) {
                fos.write(buf, 0, len);
                sum += len;
            }
            fos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (is != null)
                    is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 获取文件的创建日期  "yyyy-MM-dd HH-mm-ss"
     *
     * @param file
     * @return
     */
    @TargetApi(Build.VERSION_CODES.O)
    public static Date getCreateDate(File file) {
        return getCreateDate(file.getAbsolutePath());
    }

    /**
     * 获取文件的创建日期  "yyyy-MM-dd HH-mm-ss"
     * @TargetApi(Build.VERSION_CODES.O)
     * @param path
     * @return
     */


    @RequiresApi(api = Build.VERSION_CODES.O)
    private static  Date getCreateDate(String path) {
        try {
            FileTime t = Files.readAttributes(Paths.get(path), BasicFileAttributes.class).creationTime();
            long millis = t.toMillis();
            return new Date(millis);


        } catch (Exception e) {
            AndroidTool.showAnsyTost(e.getMessage());
            //e.printStackTrace();
        }
        return null;
    }
}

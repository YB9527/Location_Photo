package com.xp.cbd.photo;

import com.xp.cbd.po.DK;
import com.xp.common.AndroidTool;
import com.xp.common.FileTool;
import com.xp.common.Photo;
import com.xp.common.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.IllformedLocaleException;
import java.util.List;

public class PhotoService {
    private static final String imageBootUrl = Tool.getHostAddress() + "cbd/";
    public static final String dirRoot = AndroidTool.getMainActivity().getExternalCacheDir().getAbsolutePath() + "/cbd/";

    public static String getDKPhotoDir() {
        return dirRoot + "dkphoto/";
    }

    public static String getDKPhotoDir(String dkbm) {
        return getDKPhotoDir() + dkbm + "/";
    }

    public static String getDKDownLoadUrlPath(DK dk, Photo photo) {
        String urlPath = imageBootUrl + "/dkphotodowload?dkbm=" + dk.getmDKBM() + "&photoname=" + photo.getName();
        return urlPath;
    }
    public static String getDKUpLoadUrlPath(DK dk, Photo photo) {
        String urlPath = imageBootUrl + "/dkphotoupload?dkbm=" + dk.getmDKBM() + "&photoname=" + photo.getName();
        return urlPath;
    }

    public static String getNativDKPhoto(DK dk, Photo photo) {
        String path = dirRoot + "dkphoto/" + dk.getmDKBM() + "/" + FileTool.getFileName(photo.getPath());
        return path;
    }

    public static String getSaveDKPhotoPath(String srcPath, String fileName, DK dk) {
        String desc = FileTool.getDir(srcPath) + "/dkphoto/" + dk.getmDKBM() + "/" + fileName + "." + FileTool.getExtension(srcPath);
        return desc;
    }

    /**
     * 检查是否又此照片了
     *
     * @param dk
     * @param photoPath 要检查的文件路径
     * @return
     */
    public static boolean exitsDKPhoto(DK dk, String photoPath) {
        String fileName = FileTool.getFileName(photoPath);
        for (Photo p : dk.getPhotos()
        ) {
            if (p.getName().equals(fileName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 添加本地照片
     *
     * @param dk
     */
    public static void addNativePhoto(DK dk) {
        dk.setSelectNative(true);
        File[] files = new File(getDKPhotoDir(dk.getmDKBM())).listFiles();
        List<Photo> photos = dk.getPhotos();
        if(files != null){
            for (File file : files
            ) {
                if (!checkLoad(photos, file.getAbsolutePath())) {
                    photos.add(new Photo(file.getAbsolutePath(),false));
                }
            }
        }

    }

    /**
     * 检查文件是否已经上传
     *
     * @param photos
     * @param path
     * @return
     */
    private static boolean checkLoad(List<Photo> photos, String path) {

        for (Photo photo :
                photos) {
            if (photo.getPath().equals(path)) {
                return true;
            }
        }
        return false;
    }

    /**
     *得到上传  或者 未上传 的照片
     * @param dk
     * @param isUpload 是否已经上传
     * @return
     */
    public static List<Photo> getPhotoState(DK dk, boolean isUpload) {
        List<Photo> photos = new ArrayList<>();
        for (Photo photo: dk.getPhotos()
             ) {
            if(photo.getUpload() == isUpload){
                photos.add(photo);
            }
        }
        return  photos;
    }
}

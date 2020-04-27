package com.xp.zjd.service;

import android.location.Location;

import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.tools.MyLocation;
import com.xp.zjd.po.ZJD;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.FileTool;
import com.xp.zjd.po.Photo;
import com.xp.common.tools.Tool;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class PhotoService {
    private static final String imageBootUrl = Tool.getHostAddress() + "zjdphoto/";
    public static final String dirRoot = AndroidTool.getMainActivity().getFilesDir().getAbsolutePath() + "/zjd/";

    public static String getDKPhotoDir() {
        return dirRoot + "zjdphoto/";
    }

    public static String getDKPhotoDir(String dkbm) {
        return getDKPhotoDir() + dkbm + "/";
    }

    public static String getDKDownLoadUrlPath(ZJD zjd, Photo photo) {
        String urlPath = imageBootUrl + "/zjdphotodowload?ZDNUM=" + zjd.getZDNUM() + "&photoname=" + photo.getName();
        return urlPath;
    }

    public static String getDKUpLoadUrlPath(ZJD zjd, Photo photo) {
        String urlPath = imageBootUrl + "/zjdphotoupload?ZDNUM=" + zjd.getZDNUM() + "&photoname=" + photo.getName();
        return urlPath;
    }

    public static String getNativDKPhoto(ZJD zjd, Photo photo) {
        String path = dirRoot + "zjdphoto/" + zjd.getZDNUM() + "/" + FileTool.getFileName(photo.getPath());
        return path;
    }

    public static String getSaveDKPhotoPath(String srcPath, String fileName, ZJD zjd) {
        String desc = getDKPhotoDir() + "" + zjd.getZDNUM() + "/" + fileName + "." + FileTool.getExtension(srcPath);
        return desc;
    }

    /**
     * 返回 宅基地照片访问web时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "zjdphoto/";
    }

    /**
     * 检查是否又此照片了
     *
     * @param zjd
     * @param photoPath 要检查的文件路径
     * @return
     */
    public static boolean exitsDKPhoto(ZJD zjd, String photoPath) {
        String fileName = FileTool.getFileName(photoPath);
        for (Photo p : zjd.getPhotos()
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
     * @param zjd
     */
    public static void addNativePhoto(ZJD zjd) {
        List<Photo> unUploadPhotos = findUnUpLoadPhotos(zjd);
        if (unUploadPhotos.size() > 0) {
            zjd.getPhotos().addAll(unUploadPhotos);
        }
    }

    public static void addNativePhoto(List<ZJD> zjds) {
        for (ZJD zjd : zjds
        ) {
            addNativePhoto(zjd);
        }
    }

    /**
     * 得到没有上传的文件
     *
     * @param zjd
     * @return
     */
    public static List<Photo> findUnUpLoadPhotos(ZJD zjd) {
        List<Photo> unUploadPhotos = new ArrayList<>();
        zjd.setSelectNative(true);
        File[] files = new File(getDKPhotoDir(zjd.getZDNUM())).listFiles();
        List<Photo> photos = zjd.getPhotos();
        if (files != null) {
            for (File file : files
            ) {
                if (!checkLoad(photos, file.getAbsolutePath())) {
                    Photo p = new Photo(file.getAbsolutePath(), false);
                    p.setZjd(zjd);
                    unUploadPhotos.add(p);
                }
            }
        }
        return unUploadPhotos;
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
     * 得到上传  或者 未上传 的照片
     *
     * @param zjd
     * @param isUpload 是否已经上传
     * @return
     */
    public static List<Photo> getPhotoState(ZJD zjd, boolean isUpload) {
        List<Photo> photos = new ArrayList<>();
        for (Photo photo : zjd.getPhotos()
        ) {
            if (photo.getUpload() == isUpload) {
                photos.add(photo);
            }
        }
        return photos;
    }

    /**
     * 删除本地照片
     * @param zjd
     * @return
     */
    public static int deleteLocalAllByZJD(ZJD zjd) {
        int count = 0;
        if (zjd == null) {
            return count;
        }
        String zdnum = zjd.getZDNUM();
        if (Tool.isEmpty(zdnum)) {
            return count;
        } else {
            List<Photo> list = zjd.getPhotos();
            for (Photo photo : list) {

            }
            File dir = new File(getDKPhotoDir(zdnum));
            if (dir.exists()) {
                count = dir.listFiles().length;
                for (File file : dir.listFiles()) {
                    file.delete();
                }
                dir.delete();
            }
        }
        return count;
    }

    /**
     * 照片添加定位
     * @param photo
     */
    public static void setPotoLocation(final Photo photo) {
        AndroidTool.showProgressBar();
        MyLocation myLocation = new MyLocation(new MyCallback() {
            @Override
            public void call(ResultData resultData) {
                Location location = ( Location)resultData.getObject();
                if(location != null){
                    photo.setLatitude(location.getLatitude());
                    photo.setLongitude(location.getLongitude());
                }
                AndroidTool.closeProgressBar();
            }
        });
        myLocation.start();
    }
}

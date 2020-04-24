package com.xp.zjd.service;

import com.xp.common.YBException.ZJDException;
import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.FileTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.Photo;
import com.xp.common.tools.RedisTool;
import com.xp.common.tools.Tool;
import com.xp.zjd.photo.PhotoService;
import com.xp.zjd.po.ZJD;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ZJDService {

    /**
     * 宅基地的 根路径
     */
    public static String zjdDirRoot = AndroidTool.getMainActivity().getFilesDir() + "/zjd/";
    /**
     * 宅基地 的离线数据库名字
     */
    public static String zjdGeodatabaseName = "zjd.geodatabase";

    /**
     * 返回 宅基地访问web时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "zjd/";
    }


    /**
     * @return 储存离线数据库的文件夹
     */
    public static String getTPKsDir() {
        return zjdDirRoot + "tpks/";
    }

    /**
     * 上传所有照片
     *
     * @param photos 需要上传的照片
     */
    public static void UploadAllPhoto(List<Photo> photos, Callback callback) {
        OkHttpClient client = OkHttpClientUtils.getClient();
        MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");

        MultipartBody.Builder body = new MultipartBody.Builder();
        for (Photo photo :
                photos) {
            String path = photo.getPath();
            body.addFormDataPart(path, photo.getZjd().getZDNUM(), RequestBody.create(MEDIA_TYPE_MARKDOWN, new File(path)));
        }
        RequestBody requestBody = body.build();

        Request request = new Request.Builder()
                .url(PhotoService.getURLBasic() + "uploads")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    /**
     * 得到宅基地的 离线数据库  文件夹
     *
     * @return
     */
    public static String getGeodatabaseDir() {
        String path = zjdDirRoot + "gdbs/";
        return path;
    }

    /**
     * 得到宅基地的 离线数据库
     *
     * @return
     */
    public static String getGeodatabasePath() {
        String path = getGeodatabaseDir() + zjdGeodatabaseName;
        return path;
    }

    /**
     * @param isdownload true 不管有无都重新下载geodatabase ，false 是 有才下载
     */
    public static void downloadGeodatase(boolean isdownload) {
        final File file = new File(ZJDService.getGeodatabasePath());
        if (!file.exists() || isdownload) {
            //如果没有 下载服务器的 离线数据库
            file.getParentFile().mkdirs();
            OkHttpClientUtils.httpGet(ZJDService.getURLBasic() + "downloadGeodatabase?geodatabaseName=" + zjdGeodatabaseName, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    FileTool.saveFile(response.body().byteStream(), ZJDService.getGeodatabasePath());
                }
            });
        }
    }

    /**
     * 保存地块，有如下情况
     * 1、存在于服务器时，更新服务器，用 isupload 鉴别
     * 2、存在于本地时，
     * 如果 == null，保存地块，
     * 如果 != null 更新本地地块
     *
     * @param zjd
     * @param myCallback
     */
    public static void updateDK(ZJD zjd, final MyCallback myCallback) throws ZJDException {
        if (zjd.getUpload()) {
            //更新服务器
            updateDKInServer(zjd, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    myCallback.call(new ResultData(Status.Error, "超时"));
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ResultData result = new ResultData(Status.Success, "1", response);
                    myCallback.call(result);
                }
            });
        } else {
            //保存 //更新本地地块
            RedisTool.saveRedis("zjd_" + zjd.getZDNUM(), zjd);
            myCallback.call(new ResultData(Status.Success, "1"));

        }
    }

    /**
     * 更新服务器中的地块
     *
     * @param zjd
     * @param callback
     */
    private static void updateDKInServer(ZJD zjd, Callback callback) {
        OkHttpClientUtils.httpPost(getURLBasic() + "updatezjd", zjd, callback);
    }

    /**
     * 从本地数据库中找到地块
     *
     * @return
     */
    public static List<ZJD> findLoaclZJDs() {
        List<ZJD> zjds = RedisTool.findListRedis("'zjd_%'", ZJD.class);
        return zjds;
    }

    /**
     * 找web 端的 地块
     * @return
     */
    public static void findWebZJDs(MyCallback callback) {
        OkHttpClientUtils.httpPostContainsDJZQDM_User(getURLBasic() + "findzjdsbyxzdmanduser",callback);
    }

    /**
     * 删除宅基地
     * @param zjd
     * @param callback
     */
    public static void deleteZJD(ZJD zjd, MyCallback callback) {

        if(Tool.isTrue( zjd.getUpload())){
            //服务器地块

        }else{
            //本地地块
            deleteLocalZJD(zjd);
        }

    }

    /**
     * 删除本地 地块
     * @param zjd
     */
    private static void deleteLocalZJD(ZJD zjd) {
        RedisTool.deleteRedisByMark("zjd_"+zjd.getZDNUM());
        //删除图片
    }

}

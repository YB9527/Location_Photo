package com.xp.zjd.service;

import android.os.FileUtils;

import com.xp.common.AndroidTool;
import com.xp.common.FileTool;
import com.xp.common.OkHttpClientUtils;
import com.xp.common.Photo;
import com.xp.common.Tool;
import com.xp.zjd.photo.PhotoService;
import com.xp.zjd.po.ZJD;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

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
            body.addFormDataPart(path, photo.getZjd().getmDKBM(), RequestBody.create(MEDIA_TYPE_MARKDOWN, new File(path)));
        }
        RequestBody requestBody = body.build();

        Request request = new Request.Builder()
                .url(PhotoService.getURLBasic() + "uploads")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(callback);

    }

    /**
     * 得到宅基地的 离线数据库
     *
     * @return
     */
    public static String getGeodatabasePath() {
        String path = zjdDirRoot + "gdbs/" + zjdGeodatabaseName;
        return path;
    }

    /**
     *
     * @param isdownload  true 不管有无都重新下载geodatabase ，false 是 有才下载
     */
    public static void downloadGeodatase(boolean isdownload) {
        final File file = new File(ZJDService.getGeodatabasePath());
        if (!file.exists() || isdownload) {
            //如果没有 下载服务器的 离线数据库
            file.getParentFile().mkdirs();
            OkHttpClientUtils.httpGet(ZJDService.getURLBasic() + "downloadGeodatabase?geodatabaseName="+zjdGeodatabaseName, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    FileTool.saveFile(response.body().byteStream(),ZJDService.getGeodatabasePath());
                }

            });

        }

    }


}

package com.xp.zjd.service;

import com.xp.common.OkHttpClientUtils;
import com.xp.common.Photo;
import com.xp.common.Tool;
import com.xp.zjd.photo.PhotoService;

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
    public static void UploadAllPhoto(List<Photo> photos,Callback callback) {
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
}

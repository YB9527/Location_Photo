package com.xp.common;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.xp.zjd.service.ZJDService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class OkHttpClientUtils {
    private static OkHttpClient client = null;

    public static OkHttpClient getClient() {
        if (client == null) {
            client = new OkHttpClient();
        }
        return client;
    }

    /**
     * Post请求 异步
     * 使用 Callback 回调可返回子线程中获得的网络数据
     *
     * @param url
     * @param t   对象
     */
    public static <T> void httpPost(final String url, final T t, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = getClient();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add( t.getClass().getSimpleName().toLowerCase(), Tool.getGson().toJson(t));
                FormBody formBody = formBodyBuilder.build();
                Request request = new Request
                        .Builder()
                        .post(formBody)
                        .url(url)
                        .build();
                //Response response = null;
                okHttpClient.newCall(request).enqueue(callback);


            }
        }).start();
    }

    /**
     * 发送多个对象到后台
     *
     * @param url
     * @param params
     * @param callback
     */
    public static void httpPostObjects(final String url, final Map<String, Object> params, final Callback callback) {
        Gson gson = Tool.getGson();
        Map<String, String> map = new HashMap<>();
        for (String key : params.keySet()
        ) {
            Object obj = params.get(key);
            map.put(key, gson.toJson(obj));
        }
        httpPost(url, map, callback);
    }

    /**
     * Post请求 异步
     * 使用 Callback 回调可返回子线程中获得的网络数据
     *
     * @param url
     * @param params 参数
     */
    public static void httpPost(final String url, final Map<String, String> params, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient okHttpClient = getClient();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                Set<String> keySet = params.keySet();
                for (String key : keySet) {
                    String value = params.get(key);
                    formBodyBuilder.add(key, value);
                }
                FormBody formBody = formBodyBuilder.build();
                Request request = new Request
                        .Builder()
                        .post(formBody)
                        .url(url)
                        .build();
                //Response response = null;
                okHttpClient.newCall(request).enqueue(callback);
            }
        }).start();
    }

    /**
     * 发送get请求
     * @param url
     * @param callback
     */
    public static void httpGet(final String url,final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = OkHttpClientUtils.getClient();
                Request request = new Request.Builder().get().url(url).build();
                Call call = client.newCall(request);
                call.enqueue(callback);
            }
        }).start();
    }

    /**
     *get 异步请求
     * @param basicUrl
     * @param params 发送的地址参数
     * @param callback
     */
    public static void httpGet(final String basicUrl,final Map<String,String> params, final Callback callback) {
        StringBuilder sb = new StringBuilder();
        for (String key:params.keySet()
        ) {
            sb.append("/"+key+"="+params.get(key));
        }
        httpGet(basicUrl+sb.toString(),callback);
    }
}


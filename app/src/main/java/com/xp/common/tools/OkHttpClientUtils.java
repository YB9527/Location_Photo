package com.xp.common.tools;

import com.google.gson.Gson;
import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.usermanager.service.UserService;
import com.xp.xzqy.service.XZDMService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
     * 使用 ICallback 回调可返回子线程中获得的网络数据
     *
     * @param url
     * @param t   对象  如果发送 list 集合， 请用带 mark 的参数 的重载方法
     */
    public static <T> void httpPost(final String url, final T t, final Callback callback) {
        httpPost(url, t.getClass().getSimpleName().toLowerCase(), t, callback);
    }

    /**
     * Post请求 异步
     * 使用 ICallback 回调可返回子线程中获得的网络数据
     *
     * @param url
     * @param t   对象
     */
    public static <T> void httpPost(final String url, final String mark, final T t, final Callback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {

                OkHttpClient okHttpClient = getClient();
                FormBody.Builder formBodyBuilder = new FormBody.Builder();
                formBodyBuilder.add(mark, Tool.getGson().toJson(t));
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
     * 使用 ICallback 回调可返回子线程中获得的网络数据
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
     *
     * @param url
     * @param callback
     */
    public static void httpGet(final String url, final Callback callback) {
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
     * get 异步请求
     *
     * @param basicUrl
     * @param params   发送的地址参数
     * @param callback
     */
    public static void httpGet(final String basicUrl, final Map<String, String> params, final Callback callback) {
        StringBuilder sb = new StringBuilder();
        for (String key : params.keySet()
        ) {
            sb.append("/" + key + "=" + params.get(key));
        }
        httpGet(basicUrl + sb.toString(), callback);
    }

    /**
     * http 返回的数据封装成 ResultData  并且 data json 数据 set 到 object 中
     *
     * @param response
     * @return
     */
    public static ResultData resposeToResultData(Response response, Type clazz) {
        try {

            String resposeStr = response.body().string();
            ResultData resultData = Tool.getGson().fromJson(resposeStr, ResultData.class);
            String json = resultData.getJson();
            if (!Tool.isEmpty(json)) {
                try {
                    Object obj = Tool.getGson().fromJson(json, clazz);
                    resultData.setObject(obj);
                } catch (Exception e) {
                    AndroidTool.showAnsyTost("json对象有问题:" + json);
                }

            }
            return resultData;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultData(Status.Error, "没有返回值");
    }

    /**
     * "请检查网络:"+tip + "超时"
     *
     * @param tip
     */
    public static void connetOutTime(String tip) {
        AndroidTool.closeProgressBar();
        AndroidTool.showAnsyTost("请检查网络:" + tip + "超时！！！",Status.Error);
    }

    /**
     * 自带 选中的行政代码 进行请求
     * @param url
     * @param callback
     */
    public static void httpPostAndDJZQDM(String url, Callback callback) {
        httpPost(url,"djzqdmsStr",  XZDMService.getSelectXZDMs(),callback);
    }

    /**
     * 自带 选中的行政代码 进行请求
     * @param url
     * @param callback
     */
    public static void httpPostContainsDJZQDM_User(String url, final MyCallback callback) {
        Long userId = UserService.getUserId();
        Map<String,Object> map = new HashMap<>();
        map.put("userid",userId);
        map.put("djzqdms", XZDMService.getSelectXZDMs());
        httpPostObjects(url, map, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                callback.call(getConnectOutTimeResult());

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                callback.call(new ResultData(Status.Success,response));
            }
        });

    }

    /**
     *得到 超时 回应
     * @return
     */
    public static ResultData getConnectOutTimeResult() {
        return  new ResultData(Status.Error,"服务器连接超时，请检查网络");
    }

    public static <T>void httpPost(String url, final T t, final Type type, final MyCallback myCallback) {
        httpPost(url, t, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                myCallback.call(getConnectOutTimeResult());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResultData resultData = resposeToResultData(response,type);
                myCallback.call(resultData);
            }
        });
    }
    public static <T> void httpPost(final String url, final String mark, final T t,final Type type,final MyCallback myCallback) {
        httpPost(url, mark, t, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                myCallback.call(getConnectOutTimeResult());
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResultData resultData = resposeToResultData(response,type);
                myCallback.call(resultData);
            }
        });
    }
}


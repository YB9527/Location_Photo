package com.xp.common;

import android.app.DownloadManager;
import android.preference.PreferenceActivity;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 一般工具类
 */

public class Tool {

    /**
     * 检查集合是否为空
     *
     * @param list
     * @return
     */
    public static boolean isEmpty(List list) {
        return list == null || list.isEmpty() ? true : false;
    }

    /**
     * 检查 字符串 是否为空
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        return str == null ? true:str.isEmpty();
    }


    private static AsyncHttpClient client = null;

    /**
     * 获取http请求对象
     *
     * @return
     */
    public static AsyncHttpClient getAsyncHttpClient() {
        if (client == null) {
            client = new AsyncHttpClient();
        }
        return client;
    }


    /**
     * 获取本机ip地址
     *
     * @return
     */
    public static String getHostAddress() {
        String hostAddress = "http://192.168.3.3:8080/";
        return hostAddress;
    }

    public static void httpUrlConnectionPost(String urlPath) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        client.post(urlPath, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }

    public static void httpUrlConnectionGet(String urlPath) throws Exception {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.get(urlPath, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {

            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {

            }
        });
    }

    /**
     * 检查文件夹是否存在
     *
     * @param dirPath   文件路径
     * @param isCreated 如果没有是否创建
     */
    public static boolean exitsDir(String dirPath, boolean isCreated) {
        File file = new File(dirPath);
        return exitsDir(file, isCreated);
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
     * 将字节流写入为文件
     *
     * @param name
     * @param binaryData
     */
    public static void saveFile(String name, byte[] binaryData) {
        File file = new File(name);    //1、建立连接
        Tool.exitsDir(file.getParent(), true);
        OutputStream os = null;
        try {
            //2、选择输出流,以追加形式(在原有内容上追加) 写出文件 必须为true 否则为覆盖
            os = new FileOutputStream(file, false);
//            //和上一句功能一样，BufferedInputStream是增强流，加上之后能提高输出效率，建议
//            os = new BufferedOutputStream(new FileOutputStream(file,true));
            os.write(binaryData, 0, binaryData.length);    //3、写入文件
            os.flush();    //将存储在管道中的数据强制刷新出去
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
    }

    /**
     * 只复制集合，对象并没有复制
     *
     * @param list
     * @return
     */
    public static <T> List<T> copyList(List<T> list) {
        List<T> results = new ArrayList<>();
        for (T t : list
        ) {
            results.add(t);
        }
        return results;
    }

    private static Gson gson;

    /**
     * 得到 只处理 @Expose json对象
     * @return
     */
    public static Gson getGson(){
        if(gson == null){
            gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation()
                    .create();
        }
        return  gson;
    }

    /**
     * 使用json功能复制对象
     * @param obj
     * @param <T>
     * @return
     */
    public static<T> T copyObject(T obj) {
        Gson gson = getGson();
        String json = gson.toJson(obj);
        return (T) gson.fromJson(json,obj.getClass());
    }
}

package com.xp.xzqy.service;

import com.google.gson.reflect.TypeToken;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.RedisTool;
import com.xp.common.tools.Tool;
import com.xp.common.po.ResultData;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


/**
 * Created by Administrator on 2020/3/31.
 */

public class XZDMService {
    public static String selectXZDMRedis = "selectXZDM";

    private static  List<String> selectXZDMs;

    /**
     * 设置选中的行政代码
     * @param selectXZDMs
     */
    public static  void setSelectXZDMs( List<String> selectXZDMs){
        XZDMService.selectXZDMs = selectXZDMs;
    }

    /**
     * 得到选中的行政代码
     * @return
     */
    public static List<String> getSelectXZDMs(){
        return selectXZDMs;
    }

    /**
     * 网络请求 得到选中的行政代码
     */
    public static void RequestWEB_SelectXZDMs(final Callback callback){
        OkHttpClientUtils.httpGet(RedisTool.getFindSelectDJZQDMSRedisURL(selectXZDMRedis),  callback);
    }
    public static void RequestWEB_SelectXZDMs(){
        OkHttpClientUtils.httpGet(RedisTool.getFindSelectDJZQDMSRedisURL(selectXZDMRedis),  new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AndroidTool.showAnsyTost("服务器无响应,请求选择的 行政代码 失败！！！");

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResultData resultData = OkHttpClientUtils.resposeToResultData(response, new TypeToken<List<String>>() {
                }.getType());
                if (resultData != null &&  resultData.getObject() != null) {
                    XZDMService.setSelectXZDMs((List<String>) resultData.getObject());
                } else {
                    AndroidTool.showAnsyTost(resultData.getMessage());
                }
            }
        });
    }
    /**
     * 返回 行政区域访问web时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "xzdm/";
    }

}

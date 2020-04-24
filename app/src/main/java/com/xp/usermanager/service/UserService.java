package com.xp.usermanager.service;

import com.xp.common.po.Status;
import com.xp.usermanager.fragments.Regist;
import com.xp.zjd.fragments.MapSetting;
import com.xp.zjd.fragments.ZJDArcgisMap;
import com.xp.zjd.fragments.ZJDFragment;
import com.xp.zjd.init.InitFragment;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.RedisTool;
import com.xp.common.tools.Tool;
import com.xp.common.po.ResultData;
import com.xp.usermanager.fragments.Login;
import com.xp.usermanager.po.User;
import com.xp.xzqy.service.XZDMService;
import com.xp.zjd.service.ArcgisService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class UserService {
    private static User user;

    public static String redisLoginUser = "user";

    /**
     * 返回 用户 访问web时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "user/";
    }

    public static void setUser(User user) {
        UserService.user = user;
    }

    /**
     * 得到登录的 user ，如果没有登录的话，会跳转到登录
     */
    public static User getUser() {

        if (user == null) {
            AndroidTool.replaceFrameLayout(new Login());
            return null;
        }
        return user;
    }

    /**
     * 返回 user的id ，如果user 为null 返回 -1
     *
     * @return
     */
    public static Long getUserId() {
        User user = getUser();
        if (user == null) {
            return -1L;
        }
        return user.getId();
    }


    /**
     * 查找本地数据库 ，是否已经登录账号了
     * 1、有：和数据比对是否正确，如果不正确，重新登录
     * 2、没有： 重新登录
     */
    public static void checkIfLogin(final User user) {


        if (user == null) {
            //为null 的登录
            new Login();
        } else {
            AndroidTool.showProgressBar();
            //和数据库的检查是否正确
            OkHttpClientUtils.httpPost(UserService.getURLBasic() + "login", user, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    AndroidTool.showAnsyTost("服务器无响应,登录失败！！！");
                    AndroidTool.closeProgressBar();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    ResultData resultData = OkHttpClientUtils.resposeToResultData(response, User.class);
                    if (resultData == null) {
                        AndroidTool.showAnsyTost("密码 或者 账号 为 错误");
                    } else if (resultData.getStatus() == Status.Error) {
                        AndroidTool.showAnsyTost(resultData.getMessage());
                    } else {
                        User user = (User) resultData.getObject();
                        UserService.setUser(user);
                        RedisTool.updateRedis(redisLoginUser, user);
                        //请求选中的行政代码
                        XZDMService.RequestWEB_SelectXZDMs();

                        //查看加载的 数据库
                        MapSetting.findRedisLoadTDT();

                        //转到欢迎界面
                        InitFragment initFragment = new InitFragment();
                        //AndroidTool.replaceFrameLayout(initFragment);
                        AndroidTool.showAnsyTost("欢迎："+user.getNickName()+" 登录",Status.Success);
                        AndroidTool.replaceFrameLayout(new ZJDFragment());
                    }
                    AndroidTool.closeProgressBar();
                }
            });
        }

    }
}

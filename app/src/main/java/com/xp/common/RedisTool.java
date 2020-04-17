package com.xp.common;

import com.xp.usermanager.service.UserService;

public class RedisTool {
    /**
     * 返回 访问 cache 时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "redis/";
    }

    /**
     * 返回 访问 cache 查找 json的路径 封装了 userid，mark数据
     * @param mark
     * @return
     */
    public static String getFindRedisURL(String mark){
        return getURLBasic()+"?mark="+mark+"&userid="+ UserService.getUserId();
    }
}

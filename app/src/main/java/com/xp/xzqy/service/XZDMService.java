package com.xp.xzqy.service;

import com.xp.common.Tool;

/**
 * Created by Administrator on 2020/3/31.
 */

public class XZDMService {
    public static String selectXZDMRedis = "selectXZDM";

    /**
     * 返回 行政区域访问web时的跟路径
     *
     * @return
     */
    public static String getURLBasic() {
        return Tool.getHostAddress() + "xzdm/";
    }

}

package com.xp.common;

import com.xp.zjd.po.ZJD;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArcgisTool {

    /**
     * 多个map 转换成 对象
     * @param maps   map 的key 值 是 T 的 方法名
     * @param <T>
     * @return
     */
    public static <T> List<T> FeatureResultToObj(List<Map<String, Object>> maps) {
        List<T> zjds = new ArrayList<>(maps.size());
        for (Map<String,Object> map:maps
             ) {

        }
        return null;
    }

}

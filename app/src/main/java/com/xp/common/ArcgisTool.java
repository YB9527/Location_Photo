package com.xp.common;

import com.xp.zjd.po.ZJD;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ArcgisTool {

    /**
     * 多个map 转换成 对象
     * @param <T>
     * @param maps   map 的key 值 是 T 的 方法名
     * @param tClass
     * @return
     */
    public static <T> List<T> FeatureResultToObj(List<Map<String, Object>> maps, Class<T> tClass) {
        List<T> zjds = new ArrayList<>(maps.size());
        Map<String,Method> methodMap = ReflectTool.getMethod(ReflectTool.MethodNameEnum.set,tClass);
        for (Map<String,Object> map:maps
             ) {
            T t = ReflectTool.getInstanceOfT(tClass);
            zjds.add(t);
            for (String methodName: map.keySet()
                 ) {
                Object value = map.get(methodName);
                if(value != null){
                    Method m = methodMap.get("set"+ methodName);
                    if(m != null){
                        try {
                            m.invoke(t,value);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return zjds;
    }
}

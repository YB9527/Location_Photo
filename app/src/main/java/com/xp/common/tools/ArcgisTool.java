package com.xp.common.tools;

import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.MapGeometry;
import com.esri.core.geometry.Point;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonParser;

import java.io.IOException;
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
    /*示例 try {
                 String jsonStr = "{\"x\":113.0,\"y\":23.0,\"spatialReference\":{\"wkid\":4326}}";
                 JsonFactory jsonFactory = new JsonFactory();
                 JsonParser jsonParser = jsonFactory.createJsonParser(jsonStr);
                 MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(jsonParser);
                 Point point2 = (Point) mapGeometry.getGeometry();

             }catch (Exception e){
                 e.printStackTrace();
             }*/
    private static  JsonFactory jsonFactory;

    /**
     *
     * @return  JSON 转 geometry 的工厂
     */
    public static JsonFactory getJsonFactory(){
        if(jsonFactory == null){
            jsonFactory = new JsonFactory();
        }
        return  jsonFactory;
    }

    /**
     * json 转 Geometry
     * @param jsonStr
     * @param <T>
     * @return
     */
    public static <T> T jsonToGeometry(String jsonStr) {
        //jsonStr = jsonStr.replace("102100","4326");
        //String jsonStr1 = "{\"x\":113.0,\"y\":23.0,\"spatialReference\":{\"wkid\":102100}";
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jsonParser = null;
        try {
            jsonParser = jsonFactory.createJsonParser(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(jsonParser);
        T t = (T) mapGeometry.getGeometry();
        /*
        if(jsonStr == null){
            return  null;
        }
        JsonParser jsonParser = null;
        try {
            jsonParser = jsonFactory.createJsonParser(jsonStr);
        } catch (IOException e) {
            e.printStackTrace();
        }
        MapGeometry mapGeometry = GeometryEngine.jsonToGeometry(jsonParser);
        T  t = (T) mapGeometry.getGeometry();*/
        return  t;
    }
}

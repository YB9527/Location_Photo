package com.xp.common;

import android.widget.Toast;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReflectTool {
    enum MethodNameEnum{
        set,
        get,
    }

    private static  ReflectTool reflectTool;
    public static ReflectTool getInstance(){
        if(reflectTool == null){
            reflectTool = new ReflectTool();
        }
        return  reflectTool;
    }
    /**
     *
     * @param methodName map 的主键值 ，T对象的 methodName 不允许重复
     * @param list
     * @param <T>
     * @return
     */
    public  static  <T> Map<String, T> getIDMap(String methodName,List<T> list) {
        Map<String, T> map = new HashMap<>();
        if(Tool.isEmpty(list)){
            return map;
        }
        Class tClass  = list.get(0).getClass() ;
        Method m=null;
        //Object[] paramters =new Object[1];
        try {
            m =  tClass.getMethod(methodName);
            for (T t :list
            ) {
               String key =  m.invoke(t).toString();
               if(map.containsKey(key)){
                   AndroidTool.showAnsyTost("主键重复："+key);
               }else{
                   map.put(key,t);
               }

            }
        } catch (Exception e) {
            AndroidTool.showAnsyTost(e.getMessage());
        }
        return map;
    }


    /**
     * 包含 get方法 、 set方法集合
     */
    public  static  class  MethodCustom{

        private  static MethodCustom methodCustom= null;
        public static <T>  MethodCustom getInstance(Class<T> tClass){
            methodCustom = new MethodCustom();
            methodCustom.getMethods =new ArrayList<>();
            methodCustom.setMehods =new ArrayList<>();
            Method[] ms = tClass.getMethods();
            for (Method m :
                    ms) {
                String name = m.getName();
                if(name.startsWith("get")){
                    methodCustom.getMethods.add(m);
                }else if(name.startsWith("set")){
                    methodCustom.setMehods.add(m);
                }
            }
            return  methodCustom;
        }

        private   MethodCustom(){

        }

        private List<Method> getMethods;
        private  List<Method> setMehods;

        public List<Method> getGetMethods() {
            return getMethods;
        }

        public void setGetMethods(List<Method> getMethods) {
            this.getMethods = getMethods;
        }

        public List<Method> getSetMehods() {
            return setMehods;
        }

        public void setSetMehods(List<Method> setMehods) {

            this.setMehods = setMehods;
        }

    }


    /**
     * 得到 泛型类的 方法名为主键 的方法
     * @param methodNameEnum 只有 get set 方法
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Map<String,Method> getMethod(MethodNameEnum methodNameEnum,Class<T> tClass){

        //T t = getInstanceOfT(tClass);
        MethodCustom methodCustom =MethodCustom.getInstance(tClass);
        List<Method> methods = null;
        switch (methodNameEnum){
            case get:
                methods = methodCustom.getGetMethods();

                break;
            case set:
                methods = methodCustom.getSetMehods();
                break;
        }
        if(methods != null){
           return  getMethodNameMap(methods);
        }
        return  null;
    }

    /**
     * 得到  方法名为主键 的方法
     * @param methods
     * @return
     */
    private static Map<String, Method> getMethodNameMap(List<Method> methods) {
        Map<String, Method> map = new HashMap<>();
        if(methods == null){
            return  null;
        }
        for (Method m: methods
             ) {
            map.put(m.getName(),m);

        }
        return  map;
    }


    /**
     * 创建实例化对象
     * @param <T>
     * @return
     */
    public static  <T> T getInstanceOfT(Class<T> tClass )
    {
        try
        {
            return tClass.newInstance();
        }
        catch (Exception e)
        {
            // Oops, no default constructor
            throw new RuntimeException(e);
        }
    }

}

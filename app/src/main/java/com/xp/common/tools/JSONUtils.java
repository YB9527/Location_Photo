package com.xp.common.tools;

import java.lang.reflect.Field;

public class JSONUtils {
    /**
     * obj 转换为JSON对象
     * @param obj
     * @return
     */
    public static String getBeanJson(Object obj){
        Field   fields[]   =   obj.getClass().getDeclaredFields();
        String[]   name   =   new   String[fields.length];
        Object[]   value   =   new   Object[fields.length];
        StringBuffer strBean=new StringBuffer();
        strBean.append("{");
        try{
            Field.setAccessible(fields,   true);
            for   (int i=0;i<name.length;i++)   {
                name[i]   =   fields[i].getName();
                value[i]   =   fields[i].get(obj);
                if(name[i] == "zjd"){
                    value[i] = value[i];
                }
                if(value[i] != null){
                    strBean.append("\""+name[i]+"\":\""+value[i]+"\"");
                    if(i<name.length-1){
                        strBean.append(",");
                    }
                }
            }
            strBean.append("}");
        }
        catch(Exception   e){
            e.printStackTrace();
        }
        return strBean.toString();

    }





}

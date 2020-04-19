package com.xp.common.tools;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.xp.usermanager.service.UserService;
import com.xp.zjd.po.FileSelect;

import java.lang.reflect.Type;
import java.util.List;

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
     *
     * @param mark
     * @return
     */
    public static String getFindRedisURL(String mark) {
        return getURLBasic() + "findredis?mark=" + mark + "&userid=" + UserService.getUserId();
    }
    public static String getFindSelectDJZQDMSRedisURL(String mark) {
        return getURLBasic() + "findselectdjzqdms?mark=" + mark + "&userid=" + UserService.getUserId();
    }
    /**
     * 修改 或者保存  缓存数据 的 url 地址
     *
     * @param mark
     * @return
     */
    public static String getUpdateRedisURL(String mark) {
        return getURLBasic() + "updateredis?mark=" + mark + "&userid=" + UserService.getUserId();
    }

    private static SQLiteDatabase db;

    /**
     * 得到本地 sqlite 数据库
     *
     * @return
     */
    private static SQLiteDatabase getSQLiteDatabase() {
        if (db == null) {
            PetDbHelper mDbHelper = new PetDbHelper(AndroidTool.getMainActivity());
            db = mDbHelper.getReadableDatabase();
        }
        return db;
    }

    /**
     * 查找本地 sqlite 的json 数据 如果没有返回 null
     *
     * @param mark
     * @return
     */
    public static String findRedis(String mark) {
        String json = null;
        SQLiteDatabase db = getSQLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("mark", mark);
        Cursor cursor = db.rawQuery("select json from redis where mark = ?", new String[]{mark});

        if(cursor !=null&&cursor.moveToFirst()&&cursor.getCount()>0) {
            json =  cursor.getString(0);
        }
        cursor.close();
        return json;
    }

    /**
     * 修改redis 如果没有就增加
     *
     * @param mark
     * @param json
     */
    public static void updateRedis(String mark, String json) {
        SQLiteDatabase db = getSQLiteDatabase();
        ContentValues values = new ContentValues();
        values.put("mark", mark);
        values.put("json", json);
        int count = db
                .update("redis", values, "mark" + " = ?", new String[]{mark});
        if (count == 0) {
            insertData(mark,json);
        }

    }

    /**
     * 修改redis 如果没有就增加
     * @param mark
     * @param obj   obj转换为json 进行保存
     */
    public static void updateRedis(String mark, Object obj) {
        updateRedis(mark, Tool.getGson().toJson(obj));
    }
    /**
     * 插入数据
     *
     * @param mark
     * @param json
     */
    private static void insertData(String mark, String json) {

        SQLiteDatabase db = getSQLiteDatabase();

        ContentValues values = new ContentValues();
        values.put("mark", mark);
        values.put("json", json);
        long count =  db.insert("redis", null, values);

    }

    /**
     * 在 本地数据中找 缓存数据， 并转换成json
     * @param mark
     * @param type  转换成的结果对象
     * @return
     */
    public static <T> T findRedis(String mark, Type type) {
        String json = findRedis(mark);
        if(Tool.isEmpty(json)){
            return  null;
        }
        return Tool.JsonToObject(json,type);
    }
}

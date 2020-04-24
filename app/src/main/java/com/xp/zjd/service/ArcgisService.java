package com.xp.zjd.service;

import com.google.gson.reflect.TypeToken;
import com.xp.common.po.ResultData;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.RedisTool;
import com.xp.xzqy.service.XZDMService;
import com.xp.zjd.fragments.GeodatabaseSelect;
import com.xp.zjd.fragments.TileSelect;
import com.xp.zjd.po.FileSelect;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ArcgisService {
    private static String redisMapTileSetting = "mapTileSetting";
    private static String redisMapGeodatabaseSetting = "mapGeodatabaseSetting";
    private static String redisMapLoadTDT = "mapLoadTDT";

    /**
     * 查询 是否加载了天地图
     *
     * @return
     */
    public static boolean findRedisLoadTDT() {
        Boolean isLoadTDT = RedisTool.findRedis(redisMapLoadTDT, Boolean.class);
        if (isLoadTDT == null) {
            isLoadTDT = false;
        }
        return isLoadTDT;
    }

    /**
     * 查询加载了的 切片，如果文件已经不存在了，直接删除
     *
     * @return
     */
    public static List<FileSelect> getTileFileSelected() {
        List<FileSelect> tileFileSelected = RedisTool.findRedis(redisMapTileSetting, new TypeToken<List<FileSelect>>() {
        }.getType());
        if (tileFileSelected == null) {
            return null;
        }
        for (int i = 0; i < tileFileSelected.size(); i++) {

            if (!(new File(tileFileSelected.get(i).getPath()).exists())) {
                tileFileSelected.remove(i);
                i--;
            }
        }
        return tileFileSelected;
    }

    /**
     * 查询 加载了的 geodatabase,如果文件已经不存在了，直接删除
     *
     * @return
     */
    public static List<FileSelect> getGeodatabaseFileSelected() {
        List<FileSelect> geodatabaseFileSelected = RedisTool.findRedis(redisMapGeodatabaseSetting, new TypeToken<List<FileSelect>>() {
        }.getType());
        if(geodatabaseFileSelected != null){
            for (int i = 0; i < geodatabaseFileSelected.size(); i++) {
                if (!geodatabaseFileSelected.get(i).getFile().exists()) {
                    geodatabaseFileSelected.remove(i);
                    i--;
                }
            }
        }

        return geodatabaseFileSelected;
    }

    /**
     * 修改 加载了的 geodatabase
     *
     * @param selectedFile
     */
    public static void updateRedisMapGeodatabaseSetting(List<FileSelect> selectedFile) {
        RedisTool.updateRedis(redisMapGeodatabaseSetting, selectedFile);
    }

    /**
     * 修改 加载了的 切片
     *
     * @param selectedFile
     */
    public static void updateRedisTileSelectSetting(List<FileSelect> selectedFile) {
        RedisTool.updateRedis(redisMapTileSetting, selectedFile);
    }

    /**
     * 修改 是否加载天地图
     *
     * @param checked null 代表 false
     */
    public static void updateRedisMapLoadTDT(Boolean checked) {
        if (checked == null) {
            RedisTool.updateRedis(redisMapLoadTDT, false);
        } else {
            RedisTool.updateRedis(redisMapLoadTDT, checked);
        }

    }
}

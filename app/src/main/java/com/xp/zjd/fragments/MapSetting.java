package com.xp.zjd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.xp.R;
import com.xp.common.po.Redis;
import com.xp.common.po.Status;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.RedisTool;
import com.xp.common.tools.Tool;
import com.xp.zjd.po.FileSelect;
import com.xp.zjd.service.ArcgisService;
import com.xp.zjd.service.ZJDService;

import java.io.File;
import java.util.List;

/**
 * map 设定页面
 */
public class MapSetting extends Fragment implements View.OnClickListener {
    private View view;

    public static  Boolean isLoadTDT ;
    private CheckBox cb_loadTDT;
    public static void findRedisLoadTDT() {
        isLoadTDT = ArcgisService.findRedisLoadTDT();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.fragment_map_setting, container, false);
        this.view = view;
        init();
        return view;
    }

    TileSelect tileSelect;
    GeodatabaseSelect geodatabaseSelect;

    private void init() {

        //查找本地数据中的记录
        List<FileSelect> tileFileSelected = ArcgisService.getTileFileSelected();
        tileSelect = new TileSelect(view,tileFileSelected);
        List<FileSelect> geodatabaseFileSelected = ArcgisService.getGeodatabaseFileSelected();
        if(!Tool.isEmpty(geodatabaseFileSelected)){
            geodatabaseSelect = new GeodatabaseSelect(view,geodatabaseFileSelected);
        }

     Button btu_downloadgeodatabase = view.findViewById(R.id.btu_downloadgeodatabase);
        btu_downloadgeodatabase.setOnClickListener(this);

        Button btu_submit = view.findViewById(R.id.btu_submit);
        btu_submit.setOnClickListener(this);

        findRedisLoadTDT();
        cb_loadTDT = view.findViewById(R.id.cb_loadTDT);
        cb_loadTDT.setChecked(isLoadTDT);
        cb_loadTDT.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_downloadgeodatabase:
                downloadGeodatabase();
                break;
            case R.id.btu_submit:
                btu_submit();
                break;
            case R.id.cb_loadTDT:
                loadTDT((CheckBox)v);
                break;
            default:
                break;
        }
    }

    /**
     * 点击 “加载天地图”  checkbox 事件
     * @param cb_loadTDT
     */
    private void loadTDT(CheckBox cb_loadTDT) {


    }

    /**
     * 地图设置 提交修改 ,保存到本地缓存
     */
    private void btu_submit() {

        if(geodatabaseSelect != null){
            ArcgisService.updateRedisMapGeodatabaseSetting( FileSelect.getSelectedFile(geodatabaseSelect.fileSelects));
        }

        if(tileSelect != null){
            ArcgisService.updateRedisTileSelectSetting( FileSelect.getSelectedFile(tileSelect.fileSelects));

        }
        ArcgisService.updateRedisMapLoadTDT(cb_loadTDT.isChecked());
        AndroidTool.showToast("保存成功", Status.Success);



    }

    /**
     * 弹出下载窗口，如果本地已经有此数据，按钮显示更新，没有就显示现在
     */
    private void downloadGeodatabase() {

    }

}

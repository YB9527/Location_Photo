package com.xp.zjd.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xp.R;
import com.xp.zjd.service.ZJDService;

import java.io.File;

/**
 * map 设定页面
 */
public class MapSetting extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.fragment_map_setting, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {

        TileSelect tileSelect = new TileSelect(view);
        GeodatabaseSelect geodatabaseSelect = new GeodatabaseSelect(view);

        Button btu_downloadgeodatabase = view.findViewById(R.id.btu_downloadgeodatabase);
        btu_downloadgeodatabase.setOnClickListener(this);

        Button btu_submit = view.findViewById(R.id.btu_submit);
        btu_submit.setOnClickListener(this);
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
            default:
                break;
        }
    }

    /**
     * 地图设置 提交修改
     */
    private void btu_submit() {

    }

    /**
     * 弹出下载窗口，如果本地已经有此数据，按钮显示更新，没有就显示现在
     */
    private void downloadGeodatabase() {

    }

}

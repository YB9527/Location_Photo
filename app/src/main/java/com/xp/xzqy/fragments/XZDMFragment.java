package com.xp.xzqy.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.xp.R;
import com.xp.common.AndroidTool;
import com.xp.common.OkHttpClientUtils;
import com.xp.common.RedisTool;
import com.xp.common.Tool;
import com.xp.xzqy.po.XZDM;
import com.xp.xzqy.po.XZDMVo;
import com.xp.xzqy.service.XZDMService;
import com.xp.zjd.fragments.GeodatabaseSelect;
import com.xp.zjd.fragments.TileSelect;
import com.xp.zjd.po.ZJD;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 数据加载时 按照 行政区域现在加载
 * 本页面 选择加载哪些行政区域
 */
public class XZDMFragment extends Fragment implements View.OnClickListener {
    private View view;
    private ListView listView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.xzdm_select, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {
        AndroidTool.showProgressBar();
        listView = view.findViewById(R.id.lv_xzqy);
        OkHttpClientUtils.httpGet(RedisTool.getFindRedisURL("selectxzdm") , new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AndroidTool.closeProgressBar();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resposeStr = response.body().string();
                List<XZDM> xzdms =  Tool.getGson().fromJson(resposeStr, new TypeToken<List<XZDM>>() {}.getType());

                XZDMSelect xzdmSelect = new XZDMSelect(view, xzdms);
                AndroidTool.closeProgressBar();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            default:
                break;
        }
    }

}


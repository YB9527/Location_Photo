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
import com.xp.common.ReflectTool;
import com.xp.common.Tool;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.xzqy.po.XZDM;
import com.xp.xzqy.po.XZDMVo;
import com.xp.xzqy.service.XZDMService;
import com.xp.zjd.fragments.GeodatabaseSelect;
import com.xp.zjd.fragments.TileSelect;
import com.xp.zjd.po.ZJD;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

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

        View view = inflater.inflate(R.layout.fragment_xzqy_setting, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {
        AndroidTool.showProgressBar();
        OkHttpClientUtils.httpGet(XZDMService.getURLBasic() + "findall", new Callback() {

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ResultData resultData = OkHttpClientUtils.ResposeToResultData(response, new TypeToken<List<XZDM>>() {
                }.getType());

                Object objXZDMs = resultData.getObject();
                if (objXZDMs != null) {
                    final List<XZDM> xzdms = (List<XZDM>) objXZDMs;

                    OkHttpClientUtils.httpGet(RedisTool.getFindRedisURL(XZDMService.selectXZDMRedis), new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            AndroidTool.closeProgressBar();
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                            ResultData resultData = OkHttpClientUtils.ResposeToResultData(response, new TypeToken<List<String>>() {
                            }.getType());
                            if (resultData.getStatus() == Status.Error) {
                                AndroidTool.showAnsyTost(resultData.getMessage());
                            } else if (resultData.getStatus() == Status.Success) {
                                Object objData = resultData.getObject();
                                final List<XZDMVo> results = new ArrayList<>();
                                if (objData != null) {
                                    List<String> xzdmsVos = (List<String>) objData;
                                    Map<String, XZDM> xzdmsMap = ReflectTool.getIDMap("getDJZQDM", xzdms);

                                    for (String djzqdm : xzdmsVos
                                    ) {
                                        XZDM xzdm = xzdmsMap.get(djzqdm);
                                        if (xzdm != null) {
                                            xzdms.remove(xzdm);
                                            results.add(new XZDMVo(xzdm,true));
                                        }
                                    }
                                }
                                for (XZDM xzdm : xzdms
                                ) {
                                    results.add(new XZDMVo(xzdm,false));
                                }
                                AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                         new XZDMSelect(view, results);
                                    }
                                });

                            }
                            AndroidTool.closeProgressBar();
                        }
                    });
                }

            }

            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

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


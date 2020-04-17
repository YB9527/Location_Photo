package com.xp.xzqy.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.xp.R;
import com.xp.common.AndroidTool;
import com.xp.common.FileTool;
import com.xp.common.OkHttpClientUtils;
import com.xp.common.RedisTool;
import com.xp.common.Tool;
import com.xp.xzqy.po.XZDM;
import com.xp.xzqy.po.XZDMVo;
import com.xp.xzqy.service.XZDMService;
import com.xp.zjd.fragments.TileSelect;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class XZDMSelect implements View.OnClickListener {
    private View view;
    private List<XZDMVo> xzdmVos;

    public XZDMSelect(View view, List<XZDMVo> xzdmVos) {
        this.view = view;
        this.xzdmVos = xzdmVos;
        ListView lv_xzqy = view.findViewById(R.id.lv_xzqy);
        XZDMSelectAdapter xzdmSelectAdapter = new XZDMSelectAdapter(view);
        lv_xzqy.setAdapter(xzdmSelectAdapter);

        Button btu_submit = view.findViewById(R.id.btu_submit);
        btu_submit.setVisibility(View.VISIBLE);
        btu_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.btu_submit:
                submit(xzdmVos);
                break;
        }

    }

    /**
     * 提交到服务器，保存为缓存数据
     * @param xzdmVos
     */
    private void submit(List<XZDMVo> xzdmVos) {
        List<String> selectXZDM = new ArrayList<>();
        for (XZDMVo xzdmVo: xzdmVos
             ) {
            if(Tool.isTrue(xzdmVo.getSelect())){
                selectXZDM.add(xzdmVo.getXzdm().getDJZQDM());
            }
        }

        OkHttpClientUtils.httpPost(RedisTool.getUpdateRedisURL(XZDMService.selectXZDMRedis),"json", selectXZDM, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AndroidTool.showAnsyTost("保存 超时！！！");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                AndroidTool.showAnsyTost("保存 成功！！！");
            }
        });
    }

    private class ViewHolder {
        TextView tv_djzqdm;
        TextView tv_djzqmc;
        CheckBox cb_isselect;
    }

    class XZDMSelectAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;
        private Context context;

        private XZDMSelectAdapter(View view) {
            context = view.getContext();
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return xzdmVos.size();
        }

        @Override
        public Object getItem(int position) {
            return xzdmVos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                View view = layoutInflater.inflate(R.layout.xzdm_select, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_djzqdm = view.findViewById(R.id.tv_djzqdm);
                viewHolder.tv_djzqmc = view.findViewById(R.id.tv_djzqmc);
                viewHolder.cb_isselect = view.findViewById(R.id.cb_isselect);

                convertView = view;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            XZDMVo xzdmVo = xzdmVos.get(position);

            viewHolder.tv_djzqdm.setText(xzdmVo.getXzdm().getDJZQDM());
            viewHolder.tv_djzqmc.setText(xzdmVo.getXzdm().getDJZQMC());
            if(xzdmVo.getSelect() != null && xzdmVo.getSelect()){
                viewHolder.cb_isselect.setChecked(true);
            }else{
                viewHolder.cb_isselect.setChecked(false);
            }
            //是否加载  添加点击事件
            viewHolder.cb_isselect.setTag(xzdmVo);
            viewHolder.cb_isselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   CheckBox cb_isselect = (CheckBox) v;
                   ((XZDMVo)cb_isselect.getTag()).setSelect(cb_isselect.isChecked());
                }
            });
            return convertView;
        }
    }
}

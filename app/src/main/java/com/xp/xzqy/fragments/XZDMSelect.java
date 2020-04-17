package com.xp.xzqy.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.xp.R;
import com.xp.common.FileTool;
import com.xp.xzqy.po.XZDM;
import com.xp.zjd.fragments.TileSelect;

import java.io.File;
import java.util.List;

public class XZDMSelect {
    private View view;
    private List<XZDM> xzdms;
    public XZDMSelect(View view, List<XZDM> xzdms) {
        this.view =view;
        this.xzdms = xzdms;
        ListView lv_xzqy = view.findViewById(R.id.lv_xzqy);
        XZDMSelectAdapter xzdmSelectAdapter = new XZDMSelectAdapter(view);
        lv_xzqy.setAdapter(xzdmSelectAdapter);
    }
    private class ViewHolder {
        TextView tv_djzqdm;
        TextView tv_djzqmc;
        CheckBox cb_isselect;


    }
    class  XZDMSelectAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;
        private Context context;
        private XZDMSelectAdapter(View view){
            context =  view.getContext();
            this.layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return  xzdms.size();
        }

        @Override
        public Object getItem(int position) {
            return xzdms.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (convertView == null) {
                View view = layoutInflater.inflate(R.layout.map_tile_select, null);
                viewHolder = new ViewHolder();
                viewHolder.tv_djzqdm = view.findViewById(R.id.tv_djzqdm);
                viewHolder.tv_djzqmc = view.findViewById(R.id.tv_djzqmc);
                viewHolder.cb_isselect = view.findViewById(R.id.cb_isselect);

                convertView = view;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            XZDM xzdm = xzdms.get(position);

            viewHolder.tv_djzqdm.setText(xzdm.getDJZQDM());
            viewHolder.tv_djzqmc.setText(xzdm.getDJZQMC());
            viewHolder.tv_djzqmc.setText(xzdm.getDJZQMC());

            return convertView;

        }
    }
}

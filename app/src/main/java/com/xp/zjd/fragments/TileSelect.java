package com.xp.zjd.fragments;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import com.xp.R;
import com.xp.common.FileTool;
import com.xp.zjd.ZJDTableAdapter;
import com.xp.zjd.photo.PhotoService;
import com.xp.zjd.po.ZJD;
import com.xp.zjd.service.ZJDService;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 地图的选择
 */
public class TileSelect {

    private View view;
    private List<File> files;
    /**
     *
     * @param view 地图设置页面
     */
    public TileSelect(View view) {
        this.view = view;
        files = new ArrayList<>();
        init();

    }

    /**
     * 先查找本地有哪些数据,如果时tpk 文件就添加
     */
    private void init() {
        File tpksDir = new File(ZJDService.getTPKsDir());
        if(tpksDir.exists()){
          File[] fileArray =   tpksDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if("tpk".equals(FileTool.getExtension(pathname.getAbsolutePath()))){
                        return  true;
                    }
                    return false;
                }
            });
            for (File file : fileArray
                 ) {
                files.add(file);
            }
        }
        TileSelectAdapter tileSelectAdapter = new TileSelectAdapter(view);
        ListView lv_tile = view.findViewById(R.id.lv_tile);
        lv_tile.setAdapter(tileSelectAdapter);
    }
    private class ViewHolder {
        TextView tv_mapname;
        CheckBox cb_isselect;
        TextView tv_file_size;
        TextView tv_file_createdate;

    }
    class  TileSelectAdapter extends BaseAdapter{

        LayoutInflater layoutInflater;
        private Context context;
        private TileSelectAdapter(View view){
            context =  view.getContext();
            this.layoutInflater = LayoutInflater.from(context);
        }
        @Override
        public int getCount() {
            return  files.size();
        }

        @Override
        public Object getItem(int position) {
            return files.get(position);
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
                viewHolder.tv_mapname = view.findViewById(R.id.tv_mapname);
                viewHolder.cb_isselect = view.findViewById(R.id.cb_isselect);
                viewHolder.tv_file_size = view.findViewById(R.id.tv_file_size);
                viewHolder.tv_file_createdate = view.findViewById(R.id.tv_file_createdate);
                convertView = view;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            File file = files.get(position);

            viewHolder.tv_mapname.setText(FileTool.getFileName(file.getAbsolutePath()));
            viewHolder.cb_isselect.setChecked(true);
            double size =  file.length()/1024/1024;

            viewHolder.tv_file_size.setText(String.format("%.2f", size)+"M");
            viewHolder.tv_file_createdate.setText(FileTool.getCreateDate(file));

            return convertView;

        }
    }
}

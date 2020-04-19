package com.xp.zjd.fragments;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.xp.R;
import com.xp.common.tools.DataTool;
import com.xp.common.tools.FileTool;
import com.xp.common.tools.ReflectTool;
import com.xp.zjd.po.FileSelect;
import com.xp.zjd.po.TileFileSelect;
import com.xp.zjd.service.ZJDService;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 地图的选择
 */
public class TileSelect {

    private View view;
    public List<FileSelect> fileSelects;

    /**
     * @param view 地图设置页面
     */
    public TileSelect(View view, List<FileSelect> tileFileSelected) {
        this.view = view;
        fileSelects = new ArrayList<>();
        init(tileFileSelected);
    }


    /**
     * 先查找本地有哪些数据,如果时tpk 文件就添加
     */
    private void init(List<FileSelect> tileFileSelected) {
        Map<String, FileSelect> map = ReflectTool.getIDMap("getName", tileFileSelected);
        File tpksDir = new File(ZJDService.getTPKsDir());
        if (tpksDir.exists()) {
            File[] fileArray = tpksDir.listFiles(new FileFilter() {
                @Override
                public boolean accept(File pathname) {
                    if ("tpk".equals(FileTool.getExtension(pathname.getAbsolutePath()))) {
                        return true;
                    }
                    return false;
                }
            });
            for (File file : fileArray
            ) {
                TileFileSelect tileFileSelect = TileFileSelect.getInstance(file);
                if (tileFileSelect != null) {
                    if (map.containsKey(tileFileSelect.getName())){
                        tileFileSelect.setIsselect(true);
                    }
                    fileSelects.add(tileFileSelect);
                }

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

    class TileSelectAdapter extends BaseAdapter {

        LayoutInflater layoutInflater;
        private Context context;

        private TileSelectAdapter(View view) {
            context = view.getContext();
            this.layoutInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return fileSelects.size();
        }

        @Override
        public Object getItem(int position) {
            return fileSelects.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }


        @RequiresApi(api = Build.VERSION_CODES.N)
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
            FileSelect fileSelect = fileSelects.get(position);

            viewHolder.tv_mapname.setText(fileSelect.getName());
            viewHolder.cb_isselect.setChecked(fileSelect.getIsselect());
            viewHolder.cb_isselect.setTag(fileSelect);
            viewHolder.cb_isselect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FileSelect fileSelect = (FileSelect)v.getTag();
                    fileSelect.setIsselect(!fileSelect.getIsselect());
                }
            });
            double size = fileSelect.getFileSize();

            viewHolder.tv_file_size.setText(String.format("%.2f", size) + "M");
            viewHolder.tv_file_createdate.setText(DataTool.dataFormat(fileSelect.getFileCreatedate()));

            return convertView;

        }
    }
}

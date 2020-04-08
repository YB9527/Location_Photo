package com.xp.cbd;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.TextHttpResponseHandler;
import com.xp.R;
import com.xp.cbd.photo.PhotoService;
import com.xp.cbd.photo.PhotosFragment;
import com.xp.cbd.po.DK;
import com.xp.common.AndroidTool;
import com.xp.common.Photo;
import com.xp.common.Tool;


import org.apache.http.Header;

import java.util.List;

/**
 * Created by Administrator on 2020/3/31.
 */

public class DKTableAdapter extends BaseAdapter {

    private List<DK> datas;
    private Context context;
    LayoutInflater layoutInflater;

    public DKTableAdapter(Context context) {
        this.context = context;
        init();
    }

    public DKTableAdapter(Context context, List<DK> datas) {
        this.context = context;
        this.datas = datas;
        init();
    }

    /**
     * 类初始
     */
    private void init() {
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas != null ? datas.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        return datas != null ? datas.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return datas != null ? datas.get(position).getId() : -1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            View view = layoutInflater.inflate(R.layout.cbd_dktableview, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_dkbm = view.findViewById(R.id.tv_dkbm);
            viewHolder.tv_dkmc = view.findViewById(R.id.tv_dkmc);
            viewHolder.tv_phtot_look = view.findViewById(R.id.tv_phtot_look);
            viewHolder.tv_unuploadCount = view.findViewById(R.id.unupload_count);
            viewHolder.tv_uploadCount = view.findViewById(R.id.upload_count);
            convertView = view;
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        DK dk = datas.get(position);
        viewHolder.tv_dkbm.setText(dk.getmDKBM());
        viewHolder.tv_dkmc.setText(dk.getmDKMC());

        //没有 查找过时，才去查找本地
        if(!dk.isSelectNative()){
            PhotoService.addNativePhoto(dk);
        }
        //照片
        viewHolder.tv_uploadCount.setText("已上传："+PhotoService.getPhotoState(dk,true).size());

        viewHolder.tv_unuploadCount.setText("已上传："+PhotoService.getPhotoState(dk,false).size());
        viewHolder.tv_phtot_look.setTag(dk);
        //照片被点击
        viewHolder.tv_phtot_look.setOnClickListener(new PhotoClick());
        return convertView;
    }
/*  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
      ViewHolder viewHolder = null;
      context = parent.getContext();
      if (convertView == null) {

          LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
          View view = layoutInflater.inflate(R.layout.cbd_dktableview, null);

          viewHolder = new ViewHolder();
          viewHolder.tv1 = view.findViewById(R.id.tv_dkbm);
          viewHolder.tv2 = view.findViewById(R.id.tv_dkmc);
          convertView = view;
          convertView.setTag(viewHolder);

      } else {
          viewHolder = (ViewHolder) convertView.getTag();
      }
      viewHolder.tv1.setText(datas.get(position).getmDKMC());
      viewHolder.tv2.setText(datas.get(position).getmDKBM());
      return convertView;
  }*/
/*    class ViewHolder {
        TextView tv1;
        TextView tv2;
    }*/

    private class ViewHolder {
        TextView tv_dkbm;
        TextView tv_dkmc;
        TextView tv_uploadCount;
        TextView tv_unuploadCount;
        Button tv_phtot_look;

    }

    //查看照片详情
    private class PhotoClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            DK dk = (DK) v.getTag();
            PhotosFragment fragment = new PhotosFragment(dk);
            AndroidTool.replaceFrameLayout(fragment);
        }
    }
}

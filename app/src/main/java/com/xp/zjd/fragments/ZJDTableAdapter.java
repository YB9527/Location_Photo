package com.xp.zjd.fragments;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.xp.R;
import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.zjd.service.PhotoService;
import com.xp.zjd.po.ZJD;
import com.xp.common.tools.AndroidTool;
import com.xp.zjd.service.ZJDService;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2020/3/31.
 */

public class ZJDTableAdapter extends BaseAdapter {

    private List<ZJD> datas;
    private Context context;
    LayoutInflater layoutInflater;
    private FragmentManager fragmentManager;
    public ZJDTableAdapter(FragmentManager fragmentManager,Context context) {
        this.fragmentManager =fragmentManager;
        this.context = context;
        this.datas = new ArrayList<>();
        init();
    }


    public void addDatas( List<ZJD> datas) {
        this.datas.addAll(datas);
        this.notifyDataSetChanged();
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
        return position;
    }

    public void removeItem(ZJD zjd){
        this.datas.remove(zjd);
        this.notifyDataSetChanged();
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
        ZJD zjd = datas.get(position);
        viewHolder.tv_dkbm.setText(zjd.getZDNUM());
        viewHolder.tv_dkmc.setText(zjd.getQUANLI());

        //没有 查找过时，才去查找本地
        if(!zjd.isSelectNative()){
            PhotoService.addNativePhoto(zjd);
        }
        //照片
        viewHolder.tv_uploadCount.setText("已上传："+PhotoService.getPhotoState(zjd,true).size());

        viewHolder.tv_unuploadCount.setText("未上传："+PhotoService.getPhotoState(zjd,false).size());
        viewHolder.tv_phtot_look.setTag(zjd);
        //照片被点击
        viewHolder.tv_phtot_look.setOnClickListener(new PhotoClick());
        return convertView;
    }

    public void setDatas(List<ZJD> zjds) {
        this.datas.clear();
        this.datas.addAll(zjds);
        this.notifyDataSetChanged();
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
           /* ZJD ZJD = (ZJD) v.getTag();
            PhotosFragment fragment = new PhotosFragment(ZJD);
            AndroidTool.replaceFrameLayout(fragment);*/

            final ZJD zjd = (ZJD) v.getTag();
            final List<ZJD> addZJDLists = new ArrayList<>();

            addZJDLists.add(zjd);
            MapDKDialog zjdDialog = MapDKDialog.newInstance(datas, addZJDLists, new MyCallback() {
                @Override
                public void call(ResultData result) {
                    if (result.getStatus() == Status.Error) {
                        AndroidTool.showAnsyTost(result.getMessage(), Status.Error);
                    } else  if(result.getStatus() == Status.Success){
                        if(result.getMessage().equals("2")){
                            //删除地块
                            ZJDService.deleteZJD(zjd, new MyCallback() {
                                @Override
                                public void call(ResultData resultData) {
                                    ZJDTableAdapter.this.removeItem(zjd);
                                }
                            });
                        }else if(result.getMessage().equals("1")){
                            //修改地块

                        }
                        //检查宗地编码有没有重复的，有的话，不能保存
                        //AndroidTool.showAnsyTost("保存成功：" + zjd.getZDNUM(), Status.Success);
                    }
                    AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            notifyDataSetChanged();
                        }
                    });

                }
            });
            //弹出 地块窗口，
            zjdDialog.show(fragmentManager);

        }
    }

}

package com.xp.zjd.fragments;


import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xp.R;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.Photo;
import com.xp.common.tools.Tool;
import com.xp.zjd.photo.PhotoService;
import com.xp.zjd.po.ZJD;
import com.xp.zjd.service.ZJDService;


import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 承包地地块页面
 */
public class ZJDFragment extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.cbd_dk_table, container, false);
        this.view = view;
        init();

        return view;
    }

    private ZJDTableAdapter adapter;

    private void init() {
        adapter = getAdapter(view);
        ListView listView = view.findViewById(R.id.listview_dktable);
        listView.setAdapter(adapter);

        Button btuUploadAllPhoto = view.findViewById(R.id.cbd_upload_all_photo);
        //注入要上传的所有照片
        btuUploadAllPhoto.setTag(null);
        btuUploadAllPhoto.setOnClickListener(this);

    }

    public ZJDTableAdapter getAdapter(View view) {
        AndroidTool.showProgressBar();
        final ZJDTableAdapter adapter = new ZJDTableAdapter(getFragmentManager(), view.getContext());

        OkHttpClientUtils.httpPostAndDJZQDM(ZJDService.getURLBasic() + "findbydjzqdm", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
               OkHttpClientUtils.connetOutTime("查找地块");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final ResultData resultData = OkHttpClientUtils.resposeToResultData(response,new TypeToken<List<ZJD>>() {}.getType());
                if(resultData != null && resultData.getObject() !=null){
                    AndroidTool.getMainActivity().runOnUiThread(new Runnable() {  //回到UI主线程
                        @Override
                        public void run() {
                            if(((List<ZJD>)resultData.getObject()).size() == 0){
                                AndroidTool.showAnsyTost("没有找到宅基地，可以重新选择行政区域", Status.Other);
                            }
                            adapter.addDatas((List<ZJD>)resultData.getObject());
                        }
                    });
                }else{
                    AndroidTool.showAnsyTost("没有找到宅基地，可以重新选择行政区域");
                }
                AndroidTool.closeProgressBar();
            }
        });

        return adapter;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbd_upload_all_photo:

                UploadAllPhoto();
                break;

        }

    }


    /**
     * 上传所有照片
     */
    public void UploadAllPhoto() {
        //

        OkHttpClientUtils.httpGet(ZJDService.getURLBasic() + "findall", new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                final List<Photo> photos = new ArrayList<>();
                final String responseStr = response.body().string();
                final List<ZJD> zjds = Tool.getGson().fromJson(responseStr, new TypeToken<List<ZJD>>() {
                }.getType());
                for (ZJD zjd :
                        zjds) {
                    List<Photo> unUpLoadPhotos = PhotoService.findUnUpLoadPhotos(zjd);
                    if (unUpLoadPhotos.size() > 0) {
                        photos.addAll(unUpLoadPhotos);
                    }
                }
                if (photos.size() > 0) {
                    AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final Activity activity = AndroidTool.getMainActivity();
                            AlertDialog.Builder builder = new AlertDialog.Builder(activity).setIcon(R.mipmap.ic_launcher).setTitle("提示")
                                    .setMessage("是否需要上传共：" + photos.size() + "照片").setPositiveButton("取消", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            Toast.makeText(activity, "取消", Toast.LENGTH_LONG).show();
                                        }
                                    }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {


                                            ZJDService.UploadAllPhoto(photos, new Callback() {
                                                @Override
                                                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                                }

                                                @Override
                                                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                                                    final String responseStr = response.body().string();
                                                    final List<ZJD> zjds = Tool.getGson().fromJson(responseStr, new TypeToken<List<ZJD>>() {
                                                    }.getType());
                                                    if (zjds.size() > 0) {
                                                        AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                adapter.setDatas(zjds);
                                                                Toast.makeText(AndroidTool.getMainActivity(), "上传成功", Toast.LENGTH_SHORT).show();

                                                            }
                                                        });
                                                    }
                                                }
                                            });

                                        }
                                    });
                            builder.create().show();
                        }
                    });

                } else {
                    AndroidTool.showAnsyTost("没有文件需要上传");
                }
            }
        });
    }


}

package com.xp.zjd.photo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.xp.R;
import com.xp.common.OkHttpClientUtils;
import com.xp.zjd.po.ZJD;
import com.xp.common.AndroidTool;
import com.xp.common.FileTool;
import com.xp.common.Photo;
import com.xp.common.Tool;

import org.apache.http.Header;
import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class PhotosFragment extends Fragment {
    private ZJD zjd;
    public static final int TAKE_CAMERA = 101;
    private Uri imageUri;
    private View view;
    LayoutInflater layoutInflater;


    public PhotosFragment(ZJD zjd) {
        this.zjd = zjd;

    }

    private CBDPhotoAdpater cbdPhotoAdpater;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.cbd_photo, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {
        this.layoutInflater = LayoutInflater.from(view.getContext());
        Button addphoto = view.findViewById(R.id.btu_cbd_photo_add);
        addphoto.setOnClickListener(new AddPhotoClickListener());

        ListView listView = view.findViewById(R.id.cbd_listview_photoshow);
        cbdPhotoAdpater = new CBDPhotoAdpater(Tool.copyList(zjd.getPhotos()));
        listView.setAdapter(cbdPhotoAdpater);

    }

    private class AddPhotoClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // 创建File对象，用于存储拍照后的图片
            //存放在手机SD卡的应用关联缓存目录下
            File outputImage = new File(PhotoService.dirRoot, "临时照片.jpg");

            Tool.exitsDir(outputImage.getParent(), true);
            // 从Android 6.0系统开始，读写SD卡被列为了危险权限，如果将图片存放在SD卡的任何其他目录，
            //   都要进行运行时权限处理才行，而使用应用关联 目录则可以跳过这一步
            try {
                if (outputImage.exists()) {
                    outputImage.delete();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

                  /* 7.0系统开始，直接使用本地真实路径的Uri被认为是不安全的，会抛 出一个FileUriExposedException异常。
                   而FileProvider则是一种特殊的内容提供器，它使用了和内 容提供器类似的机制来对数据进行保护，
                   可以选择性地将封装过的Uri共享给外部，从而提高了 应用的安全性*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //大于等于版本24（7.0）的场合
                imageUri = FileProvider.getUriForFile(view.getContext(), "com.xp.fileprovider", outputImage);
            } else {
                //小于android 版本7.0（24）的场合
                imageUri = Uri.fromFile(outputImage);
            }
            //启动相机程序
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//
            startActivityForResult(intent, TAKE_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case TAKE_CAMERA:
                if (resultCode == RESULT_OK) {
                    // 将拍摄的照片显示出来
                    String path = PhotoService.dirRoot + "临时照片.jpg";
                    Photo photo = new Photo(path, false);
                    editName(photo, 0);

                }
                break;
            default:
                break;
        }
    }

    private class ViewHolder {
        SmartImageView smartImageView;
        TextView cbd_photo_filename;
        Button btu_photo_delete;
        Button cbd_upload_photo;
        Button cbd_edit_filename;
    }

    /**
     * 弹窗 修改照片备注
     *
     * @param photo
     */
    public synchronized void editName(final Photo photo, final int state) {
        final EditText editText = new EditText(view.getContext());
        final String path = photo.getPath();
        final File file = new File(path);
        editText.setText(FileTool.getFileNameWithoutExtension(photo.getPath()));
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(view.getContext()).setIcon(R.mipmap.ic_launcher).setTitle("请输入描述")
                .setView(editText).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        Toast.makeText(view.getContext(), "取消", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String desc = PhotoService.getSaveDKPhotoPath(path, editText.getText().toString(), zjd);
                        if (path.equals(desc)) {
                            return;
                        }
                        //检查是否又此文件了
                        if (!PhotoService.exitsDKPhoto(zjd, desc)) {
                            photo.setPath(desc);
                            if (state == 0) {
                                FileTool.copyFile(path, desc);
                                photo.setZjd(zjd);
                                cbdPhotoAdpater.AddPhoto(photo);
                            } else {
                                //如果照片已经存在数据库，那么更新后台
                                if (photo.getUpload()) {

                                    //给服务器使用副本，如果没有问题才修改原件
                                    Photo photoCopy = Tool.CopyObject(photo);
                                    photoCopy.setPath(desc);
                                    Map<String, Object> map = new HashMap<>();
                                    map.put("photo", photoCopy);
                                    map.put("zjd", zjd);
                                    OkHttpClientUtils.httpPostObjects(PhotoService.getURLBasic() + "updatephoto", map, new Callback() {
                                        @Override
                                        public void onFailure(@NotNull Call call, @NotNull IOException e) {

                                        }

                                        @Override
                                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {


                                        }
                                    });
                                }
                                AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        new File(path).renameTo(new File(desc));
                                        photo.setPath(desc);
                                        cbdPhotoAdpater.notifyDataSetChanged();
                                    }
                                });
                            }
                        } else {
                            Toast.makeText(AndroidTool.getMainActivity(), "文件名重复，不能保存", Toast.LENGTH_SHORT).show();
                        }


                        //第一次保存文件
                        //修改文件名字


                    }
                });
        builder.create().show();
    }

    private class CBDPhotoAdpater extends BaseAdapter implements View.OnClickListener {

        private List<Photo> datas;

        public CBDPhotoAdpater(List<Photo> photos) {
            this.datas = photos;
        }

        public void AddPhoto(Photo p) {
            datas.add(p);
            this.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int position) {
            return datas != null ? datas.size() : 0;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Photo photo = datas.get(position);

            ViewHolder viewHolder = null;
            if (convertView == null) {
                View view = layoutInflater.inflate(R.layout.cbd_photo_single, null);
                viewHolder = new ViewHolder();
                viewHolder.smartImageView = view.findViewById(R.id.cbd_smartimage_pthoto);
                viewHolder.cbd_photo_filename = view.findViewById(R.id.cbd_photo_filename);
                viewHolder.btu_photo_delete = view.findViewById(R.id.cbd_photo_delete);
                viewHolder.cbd_upload_photo = view.findViewById(R.id.cbd_upload_photo);
                viewHolder.cbd_edit_filename = view.findViewById(R.id.cbd_edit_filename);

                viewHolder.btu_photo_delete.setTag(position);
                viewHolder.cbd_upload_photo.setTag(position);
                viewHolder.cbd_edit_filename.setTag(position);
                convertView = view;
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.cbd_photo_filename.setText(photo.getName());
            viewHolder.btu_photo_delete.setOnClickListener(this);
            if (photo.getUpload()) {
                viewHolder.cbd_upload_photo.setVisibility(View.GONE);//上传的话，就隐藏 上传按钮
            } else {
                viewHolder.cbd_upload_photo.setOnClickListener(this);
            }
            viewHolder.cbd_edit_filename.setOnClickListener(this);

            SmartImageView smartImageView = viewHolder.smartImageView;

            File file = new File(photo.getPath());
            //如果不存在，在网上下载
            if (!file.exists()) {
                showUrlImage(smartImageView, photo);
            } else {
                showImage(smartImageView, file);
            }


            return convertView;

        }

        /**
         * 先把网络照片下载本地了，再显示本地照片
         *
         * @param smartImageView
         * @param photo
         */
        private void showUrlImage(final SmartImageView smartImageView, final Photo photo) {

            AsyncHttpClient client = new AsyncHttpClient();
            String urlPath = PhotoService.getDKDownLoadUrlPath(zjd, photo);
            client.get(urlPath, new BinaryHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                    Tool.saveFile(photo.getPath(), binaryData);
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                    smartImageView.setImage(new SmartImage() {
                        @Override
                        public Bitmap getBitmap(Context context) {
                            return bitmap;
                        }
                    });
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {

                }
            });
        }

        /**
         * 显示image
         *
         * @param smartImageView
         * @param file
         */
        private void showImage(SmartImageView smartImageView, File file) {
            try {
                Uri imageUri = FileProvider.getUriForFile(view.getContext(), "com.xp.fileprovider", file);
                final Bitmap bitmap = BitmapFactory.decodeStream(AndroidTool.getMainActivity().getContentResolver().openInputStream(imageUri));
                smartImageView.setImage(new SmartImage() {
                    @Override
                    public Bitmap getBitmap(Context context) {
                        return bitmap;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        /***
         *  点击事件的处理
         * @param v
         */
        @Override
        public void onClick(View v) {
            Photo photo = datas.get((int) v.getTag());
            switch (v.getId()) {
                case R.id.cbd_upload_photo:
                    Button btu = v.findViewById(R.id.cbd_upload_photo);
                    uploadPhoto(photo, btu);
                    break;
                case R.id.cbd_photo_delete:
                    deletePhoto(photo);
                    break;
                case R.id.cbd_edit_filename:
                    editName(photo, 1);
                    break;
            }
        }


        /**
         * 上传照片
         *
         * @param photo
         */
        private void uploadPhoto(final Photo photo, final Button uploadBtu) {
            File file = new File(photo.getPath());
            OkHttpClient client = OkHttpClientUtils.getClient();
            MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown; charset=utf-8");
            RequestBody requestBody = new MultipartBody.Builder()
                    .addFormDataPart("photo", photo.toString())
                    .addFormDataPart("zjd", zjd.toString())
                    .addFormDataPart("file", file.getName(), RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                    .build();
            Request request = new Request.Builder()
                    .url(PhotoService.getURLBasic() + "upload")
                    .post(requestBody)
                    .build();
            Call call = client.newCall(request);
            //异步调用并设置回调函数
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    final String responseStr = response.body().string();
                    AndroidTool.getMainActivity().runOnUiThread(new Runnable() {  //回到UI主线程
                        @Override
                        public void run() {
                            if (responseStr.equals("0")) {
                                photo.setUpload(true);
                                uploadBtu.setVisibility(View.GONE);
                                Toast.makeText(view.getContext(), "上传成功！", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(view.getContext(), "上传失败！", Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            });

           /* AsyncHttpClient client = new AsyncHttpClient();
            String urlPath =PhotoService.getDKUpLoadUrlPath(zjd,photo);
            RequestParams params = new RequestParams();
            try {
                params.put("file", new File(photo.getPath()));
            } catch (Exception e) {
            }
            client.post(urlPath, params, new TextHttpResponseHandler() {
                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                }
                @Override
                public void onSuccess(int statusCode, Header[] headers, String responseString) {
                    photo.setUpload(true);
                    uploadBtu.setVisibility(View.GONE);

                    Toast.makeText(view.getContext(), "上传成功！", Toast.LENGTH_SHORT).show();
                }
            });*/
        }


        /**
         * 删除照片
         *
         * @param photo
         */
        private void deletePhoto(final Photo photo) {

            final CBDPhotoAdpater cbdPhotoAdpater = this;
            Map<String, Object> map = new HashMap<>();
            map.put("photo", photo);
            map.put("zjd", zjd);
            OkHttpClientUtils.httpPostObjects(PhotoService.getURLBasic() + "deletePhoto", map, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    AndroidTool.getMainActivity().runOnUiThread(new Runnable() {  //回到UI主线程
                        @Override
                        public void run() {
                            cbdPhotoAdpater.datas.remove(photo);
                            cbdPhotoAdpater.notifyDataSetChanged();
                            Toast.makeText(view.getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
                            //删除本地照片
                            FileTool.deleteFile(photo.getPath());
                        }
                    });
                }
            });

        }
    }
}

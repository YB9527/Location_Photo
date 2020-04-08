package com.xp.cbd.photo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BinaryHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.loopj.android.image.SmartImage;
import com.loopj.android.image.SmartImageView;
import com.xp.MainActivity;
import com.xp.R;
import com.xp.cbd.DKTableAdapter;
import com.xp.cbd.po.DK;
import com.xp.common.AndroidTool;
import com.xp.common.FileTool;
import com.xp.common.Photo;
import com.xp.common.Tool;

import org.apache.http.Header;
import org.apache.http.client.HttpClient;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.IllformedLocaleException;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static android.app.Activity.RESULT_OK;

public class PhotosFragment extends Fragment {
    private DK dk;
    public static final int TAKE_CAMERA = 101;
    private Uri imageUri;
    private View view;
    LayoutInflater layoutInflater;


    public PhotosFragment(DK dk) {
        this.dk = dk;

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
        cbdPhotoAdpater = new CBDPhotoAdpater(Tool.copyList(dk.getPhotos()));
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
                    Photo photo = new Photo(path,false);
                    editName(photo);

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
    public synchronized void editName(final Photo photo) {
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
                        //ToDo: 你想做的事情
                        //photo.setDescribe(editText.getText().toString());

                        String desc = PhotoService.getSaveDKPhotoPath(path, editText.getText().toString(), dk);
                        //检查是否又此文件了
                        if (!PhotoService.exitsDKPhoto(dk, desc)) {
                            FileTool.copyFile(path, desc);
                            photo.setPath(desc);
                            cbdPhotoAdpater.AddPhoto(photo);
                        } else {
                            Toast.makeText(AndroidTool.getMainActivity(), "文件名重复，不能保存", Toast.LENGTH_SHORT).show();
                        }
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
            if(photo.getUpload()){
                viewHolder.cbd_upload_photo.setVisibility(View.GONE);//上传的话，就隐藏 上传按钮
            }else {
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
            String urlPath = PhotoService.getDKDownLoadUrlPath(dk, photo);
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
                    uploadPhoto(photo,btu);
                    break;
                case R.id.cbd_photo_delete:
                    deletePhoto(photo);
                    break;
                case R.id.cbd_edit_filename:
                    editName(photo);
                    break;
            }
        }


        /**
         * 上传照片
         *
         * @param photo
         */
        private void uploadPhoto(final Photo photo, final Button uploadBtu) {

            AsyncHttpClient client = new AsyncHttpClient();
            String urlPath =PhotoService.getDKUpLoadUrlPath(dk,photo);
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
            });
        }


        /**
         * 删除照片
         *
         * @param photo
         */
        private void deletePhoto(Photo photo) {
            this.datas.remove(photo);
            this.notifyDataSetChanged();
            Toast.makeText(view.getContext(), "删除成功！", Toast.LENGTH_SHORT).show();
            //删除本地照片
            FileTool.deleteFile(photo.getPath());
        }
    }
}

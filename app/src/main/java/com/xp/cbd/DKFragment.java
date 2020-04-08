package com.xp.cbd;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;


import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.xp.R;
import com.xp.cbd.photo.PhotoService;
import com.xp.cbd.photo.PhotosFragment;
import com.xp.cbd.po.DK;
import com.xp.common.AndroidTool;
import com.xp.common.Photo;
import com.xp.common.Tool;


import java.util.ArrayList;
import java.util.List;

/**
 * 承包地地块页面
 */
public class DKFragment extends Fragment implements View.OnClickListener {
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
    private BaseAdapter adapter;
    private void init() {
        this.adapter = getAdapter(view);
        ListView listView =  view.findViewById(R.id.listview_dktable);
        listView.setAdapter(adapter);

        Button btuUploadAllPhoto = view.findViewById(R.id.cbd_upload_all_photo);
        //注入要上传的所有照片
        btuUploadAllPhoto.setTag(null);
        btuUploadAllPhoto.setOnClickListener(this);

    }

    public BaseAdapter getAdapter(View view) {
        List<DK> dks = new ArrayList<DK>();
        for (int i = 0; i < 5; i++) {
            DK dk = new DK(i + "", "hello YB  " + i);
            dk.setId(i);
            dks.add(dk);
            Photo photo = new Photo(PhotoService.dirRoot+"dkphoto/"+dk.getmDKBM()+"/timg.jpg",true);
           List<Photo> photos = new ArrayList<>();
           photos.add(photo);
           dk.setPhotos(photos);
        }
        DKTableAdapter adapter = new DKTableAdapter(view.getContext(), dks);
        return adapter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cbd_upload_all_photo:
                List<Photo> photos = (List<Photo>) v.getTag();
                break;

        }

    }



    /**
     * 上传照片
     * @param photos 准备要上传的照片
     */
    public void UploadAllPhoto(List<Photo> photos) {
        AndroidTool.showDilog("是否要全部上传");
    }


}

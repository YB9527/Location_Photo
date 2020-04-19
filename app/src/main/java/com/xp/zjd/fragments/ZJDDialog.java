package com.xp.zjd.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xp.R;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.Tool;
import com.xp.zjd.photo.PhotosFragment;
import com.xp.zjd.po.ZJD;
import com.xp.zjd.service.ZJDService;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class ZJDDialog extends DialogFragment {

    private List<ZJD> zjds;
    private  int currentIndex = 0 ;
    private ProgressBar progressBar;
    public static ZJDDialog newInstance(List<ZJD> zjds) {

        Bundle args = new Bundle();
        ZJDDialog fragment = new ZJDDialog();
        fragment.setArguments(args);
        fragment.zjds = zjds;

        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
        //window.setBackgroundDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.circle5_white));
        //window.setWindowAnimations(R.style.BottomDialog_Animation);
        //设置边距
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout((int) (dm.widthPixels * 0.72), ViewGroup.LayoutParams.WRAP_CONTENT);
    }
    View view;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

         view = inflater.inflate(R.layout.zjd_dk_dialog, container, false);
        initButton();
        lookdk(0);
        return view;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

/*
    public ZJDDialog(@NonNull Context context, List<Map<String, Object>> dksMap) {
        super(context);
        this.dksMap = dksMap;
    }

    public ZJDDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zjd_dk_dialog);
        initButton();
        setDKs(dksMap);


        lookdk(0);
    }*/

    private void initButton() {
        progressBar = view.findViewById(R.id.wait);
        final Button btuNext =view. findViewById(R.id.btunext);
        final Button btuLast = view.findViewById(R.id.btulast);
        btuNext.setEnabled(false);
        btuLast.setEnabled(true);
        if(zjds.size() == 1){
            btuNext.setVisibility(View.GONE);
            btuLast.setVisibility(View.GONE);
        }else{
            btuNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AndroidTool.replaceFrameLayout(R.id.framelayoutdk,new PhotosFragment(zjd));
                        if(currentIndex+1 >=  zjds.size()){
                            btuNext.setEnabled(false);
                        }else{
                            btuNext.setEnabled(true);
                        }
                    lookdk(++currentIndex);
                }
            });
            btuLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(currentIndex ==  0){
                        btuLast.setEnabled(false);
                    }else{
                        btuLast.setEnabled(true);
                    }
                    lookdk(--currentIndex);
                }
            });
        }
    }

    private void lookdk(int i) {

        progressBar.setVisibility(View.VISIBLE);
        ZJD zjd = this.zjds.get(i);
        OkHttpClientUtils.httpGet(ZJDService.getURLBasic() + "findbyzdnum?ZDNUM=" + 2, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String reponseStr = response.body().string();

                ZJD zjd =  Tool.getGson().fromJson(reponseStr,ZJD.class);
                PhotosFragment photosFragment = new PhotosFragment(zjd);

                List<Fragment> list =  getFragmentManager().getFragments();
                for (Fragment fragment: list
                ) {
                    if(fragment instanceof  DialogFragment){
                        FragmentManager fragmentManager =fragment.getChildFragmentManager();
                        FragmentTransaction transaction = fragmentManager.beginTransaction();
                        transaction.replace(R.id.framelayoutdk, photosFragment);
                        transaction.addToBackStack(null);
                        transaction.commit();

                        break;
                    }
                }
                AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                    }
                });
            }
        });

    }

    /**
     * 将数据到容器中
     * @param dksMap
     */
    private void setDKs(List<Map<String, Object>> dksMap) {

    }

}

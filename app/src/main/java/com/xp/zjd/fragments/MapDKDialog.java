package com.xp.zjd.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xp.R;
import com.xp.common.YBException.ZJDException;
import com.xp.common.po.MyCallback;
import com.xp.common.po.Redis;
import com.xp.common.po.Result;
import com.xp.common.po.Status;
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

public class MapDKDialog extends DialogFragment implements View.OnClickListener {

    private List<ZJD> zjds;
    private int currentIndex = 0;
    private ProgressBar progressBar;
    private MyCallback myCallback;

    public static MapDKDialog newInstance(List<ZJD> zjds ) {

      return  newInstance(zjds,null);
    }
    public static MapDKDialog newInstance(List<ZJD> zjds, MyCallback myCallback) {

        Bundle args = new Bundle();
        MapDKDialog fragment = new MapDKDialog();
        fragment.setArguments(args);
        fragment.zjds = zjds;
        fragment.myCallback = myCallback;
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
    private EditText et_zdnum;
    private EditText et_quanli;
    private EditText et_bz;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.map_dk_dialog, container, false);

        //文字 edittext
        et_zdnum = view.findViewById(R.id.et_zdnum);
        et_zdnum.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                zjds.get(currentIndex).setZDNUM(((EditText) v).getText().toString());
                return false;
            }
        });
        et_quanli = view.findViewById(R.id.et_zdnum);
        et_bz = view.findViewById(R.id.et_bz);

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
            setContentView(R.layout.map_dk_dialog);
            initButton();
            setDKs(dksMap);


            lookdk(0);
        }*/
    Button btu_submit;
    Button btu_cancel;

    private void initButton() {
        progressBar = view.findViewById(R.id.wait);
        final Button btuNext = view.findViewById(R.id.btunext);
        final Button btuLast = view.findViewById(R.id.btulast);

        btu_submit = view.findViewById(R.id.btu_submit);
        btu_submit.setOnClickListener(this);
        btu_cancel = view.findViewById(R.id.btu_cancel);
        btu_cancel.setOnClickListener(this);

        btuNext.setEnabled(false);
        btuLast.setEnabled(true);
        if (zjds.size() == 1) {
            btuNext.setVisibility(View.GONE);
            btuLast.setVisibility(View.GONE);
        } else {
            btuNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //AndroidTool.replaceFrameLayout(R.id.framelayoutdk,new PhotosFragment(zjd));
                    if (currentIndex + 1 >= zjds.size()) {
                        btuNext.setEnabled(false);
                    } else {
                        btuNext.setEnabled(true);
                    }
                    lookdk(++currentIndex);
                }
            });
            btuLast.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (currentIndex == 0) {
                        btuLast.setEnabled(false);
                    } else {
                        btuLast.setEnabled(true);
                    }
                    lookdk(--currentIndex);
                }
            });
        }
    }

    private void lookdk(int i) {
        ZJD zjd = this.zjds.get(i);
        String zdnum = zjd.getZDNUM();
        if (!Tool.isEmpty(zdnum)) {
            et_zdnum.setText(zdnum);
        }
        String quanli = zjd.getQUANLI();
        if (!Tool.isEmpty(quanli)) {
            et_quanli.setText(quanli);
        }

        {
            PhotosFragment photosFragment = new PhotosFragment(zjd);
            List<Fragment> list = getFragmentManager().getFragments();
            for (Fragment fragment : list
            ) {
                if (fragment instanceof DialogFragment) {
                    FragmentManager fragmentManager = fragment.getChildFragmentManager();
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
        //如果上传了，才查看服务器中的地块
        if (zjd.getUpload()) {
            progressBar.setVisibility(View.VISIBLE);
            OkHttpClientUtils.httpGet(ZJDService.getURLBasic() + "findbyzdnum?ZDNUM=" + 2, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String reponseStr = response.body().string();
                    ZJD zjd = Tool.getGson().fromJson(reponseStr, ZJD.class);

                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
        }
        //用id 查找照片， 不是 zdnum，因为可能还没有编码
        if (zjd.getId() != null) {

        }
    }

    /**
     * 将数据到容器中
     *
     * @param dksMap
     */
    private void setDKs(List<Map<String, Object>> dksMap) {

    }


    public void show(FragmentManager fragmentManager) {
        this.show(fragmentManager, "zjddialog");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_submit:
                submit();
                break;
            case R.id.btu_cancel:
                cancel();
                break;
        }
    }

    /**
     * 取消
     */
    private void cancel() {
        if(myCallback != null){
            myCallback.call(new Result(Status.Error, "取消了"));
        }

        this.getDialog().dismiss();
    }

    /**
     * 提交文字修改
     */
    private void submit() {
        ZJD zjd = zjds.get(currentIndex);
        zjd.setZDNUM(et_zdnum.getText().toString());
        zjd.setQUANLI(et_quanli.getText().toString());
        zjd.setBz(et_bz.getText().toString());
        try {
            ZJDService.updateDK(zjd, new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {

                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                }
            });
            this.getDialog().dismiss();
        } catch (ZJDException e) {
            AndroidTool.showToast(e.getMessage(), Status.Error);
        }

    }
}

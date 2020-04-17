package com.xp.usermanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.gson.reflect.TypeToken;
import com.xp.InitFragment;
import com.xp.R;
import com.xp.common.AndroidTool;
import com.xp.common.OkHttpClientUtils;
import com.xp.common.Tool;
import com.xp.usermanager.po.Level;
import com.xp.usermanager.po.User;
import com.xp.usermanager.service.UserService;
import com.xp.xzqy.po.XZDM;
import com.xp.xzqy.service.XZDMService;
import com.xp.zjd.po.ZJD;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class Login extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //View view = inflater.inflate(R.layout.cbd_dk_table,container,false);

        View view = inflater.inflate(R.layout.user_login, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {
        Button btu_submit = view.findViewById(R.id.btu_submit);
        btu_submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_submit:

                login();
                break;
            default:
                break;
        }
    }

    /**
     * 提交到数据库查找
     */
    private void login() {

        TextView tv_account = view.findViewById(R.id.tv_accout);
        String account = tv_account.getText().toString();
        TextView tv_password = view.findViewById(R.id.tv_password);
        String password = tv_password.getText().toString();
        if (Tool.isEmpty(account) || Tool.isEmpty(password)) {
            Toast.makeText(view.getContext(), "密码 或者 账号 为 空", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(account, password);
        user.setLevel(Level.one);
        AndroidTool.showProgressBar();
        OkHttpClientUtils.httpPost(UserService.getURLBasic() + "login", user, new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                AndroidTool.showAnsyTost("服务器无响应,登录失败！！！");
                AndroidTool.closeProgressBar();
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String resposeStr = response.body().string();
                if (Tool.isEmpty(resposeStr)) {
                    AndroidTool.showAnsyTost("密码 或者 账号 错误，登录失败！！！");
                    AndroidTool.closeProgressBar();
                    return;
                }
                final User loginUser = Tool.getGson().fromJson(resposeStr,User.class);
                UserService.setUser(loginUser);
                AndroidTool.getMainActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AndroidTool.showAnsyTost("恭喜："+loginUser.getNickName()+" 登录成功！");
                        AndroidTool.replaceFrameLayout(new InitFragment());
                        AndroidTool.closeProgressBar();
                    }
                });
            }
        });
    }

}

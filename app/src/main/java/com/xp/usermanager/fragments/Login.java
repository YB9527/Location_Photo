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

import com.xp.R;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.RedisTool;
import com.xp.common.tools.Tool;
import com.xp.usermanager.po.User;
import com.xp.usermanager.service.UserService;

public class Login extends Fragment implements View.OnClickListener {
    private View view;
    private TextView tv_account;
    private TextView tv_password;

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
        tv_account = view.findViewById(R.id.tv_accout);
        tv_password = view.findViewById(R.id.tv_password);
        btu_submit.setOnClickListener(this);
        User redisUser = RedisTool.findRedis(UserService.redisLoginUser, User.class);
        if (redisUser != null) {
            tv_account.setText(redisUser.getAccount());
            tv_password.setText(redisUser.getPassword());
        }
        //注册账号按钮
        view.findViewById(R.id.btu_registAccount).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_submit:
                login();
                break;
            case R.id.btu_registAccount:
                AndroidTool.replaceFrameLayout(new Regist());
                break;
            default:
                break;
        }
    }

    /**
     * 提交到数据库查找
     */
    private void login() {

        String account = tv_account.getText().toString();
        String password = tv_password.getText().toString();
        if (Tool.isEmpty(account) || Tool.isEmpty(password)) {
            Toast.makeText(view.getContext(), "密码 或者 账号 为 空", Toast.LENGTH_SHORT).show();
            return;
        }
        User user = new User(account, password);
        UserService.checkIfLogin(user);

// AndroidTool.showAnsyTost("恭喜：" + loginUser.getNickName() + " 登录成功！");

    }

}

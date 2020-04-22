package com.xp.usermanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.xp.R;
import com.xp.common.po.MyCallback;
import com.xp.common.po.ResultData;
import com.xp.common.po.Status;
import com.xp.common.tools.AndroidTool;
import com.xp.common.tools.OkHttpClientUtils;
import com.xp.common.tools.Tool;
import com.xp.usermanager.po.User;
import com.xp.usermanager.service.UserService;

import org.json.JSONObject;

public class UserSetting extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_user_setting, container, false);
        this.view = view;
        init();
        return view;
    }

    EditText et_nickname;
    EditText et_password;
    EditText et_repassword;
    EditText et_email;
    TextView tv_userlevel;

    private void init() {
        User user = UserService.getUser();
        et_nickname = view.findViewById(R.id.et_nickname);
        et_nickname.setText(user.getNickName());
        et_password = view.findViewById(R.id.et_password);
        et_password.setText(user.getPassword());
        et_repassword = view.findViewById(R.id.et_repassword);
        et_repassword.setText(user.getPassword());
        et_email = view.findViewById(R.id.et_email);
        et_email.setText(user.getEmail());
        tv_userlevel = view.findViewById(R.id.tv_userlevel);
        if (user.getLevel() != null) {
            tv_userlevel.setText(user.getLevel().getDescribe());
        }

        view.findViewById(R.id.btu_submit).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btu_submit:
                submit();
                break;
            default:
                break;
        }
    }

    /**
     * 提交后台
     */
    private void submit() {
        if (!et_password.getText().toString().equals(et_repassword.getText().toString())) {
            AndroidTool.showToast("两次输入的密码不一样", Status.Error);
            return;
        }

        User user = Tool.copyObject(UserService.getUser());
        user.setNickName(et_nickname.getText().toString());
        user.setPassword(et_password.getText().toString());
        user.setEmail(et_email.getText().toString());
        if (Tool.getGson().toJson(UserService.getUser()).equals(Tool.getGson().toJson(user))) {
            AndroidTool.showAnsyTost("没有修改不用提交", Status.Other);
            return;
        }
        OkHttpClientUtils.httpPost(UserService.getURLBasic() + "edituser", "po", user, user.getClass(), new MyCallback() {
            @Override
            public void call(ResultData resultData) {
                if (resultData.getStatus() == Status.Success) {
                    UserService.setUser((User) resultData.getObject());
                    AndroidTool.showAnsyTost("修改成功", Status.Success);
                } else {
                    AndroidTool.showAnsyTost(resultData.getMessage(), resultData.getStatus());
                }
            }
        });
    }
}

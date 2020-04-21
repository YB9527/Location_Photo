package com.xp.usermanager.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

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

public class Regist extends Fragment implements View.OnClickListener {
    private View view;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_regist, container, false);
        this.view = view;
        init();
        return view;
    }

    private void init() {
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

    private void submit() {
        EditText et_nickname = view.findViewById(R.id.et_nickname);
        EditText et_account = view.findViewById(R.id.et_account);
        EditText et_password = view.findViewById(R.id.et_password);
        EditText et_repassword = view.findViewById(R.id.et_repassword);
        EditText et_email = view.findViewById(R.id.et_email);

        String nickname = et_nickname.getText().toString();
        String account = et_account.getText().toString();
        String password = et_password.getText().toString();
        final String repassword = et_repassword.getText().toString();
        String email = et_email.getText().toString();

        if (Tool.isEmpty(nickname)) {
            AndroidTool.showToast("昵称为空", Status.Error);
            return;
        }
        if (Tool.isEmpty(account)) {
            AndroidTool.showToast("账号为空", Status.Error);
            return;
        }
        if (Tool.isEmpty(password)) {
            AndroidTool.showToast("密码为空", Status.Error);
            return;
        }
        if (!password.equals(repassword)) {
            AndroidTool.showToast("密码不一致", Status.Error);
            return;
        }
        if (Tool.isEmpty(email)) {
            AndroidTool.showToast("邮箱为空", Status.Error);
            return;
        }
        AndroidTool.showProgressBar();
        User user = new User();
        user.setNickName(nickname);
        user.setAccount(account);
        user.setPassword(password);
        user.setEmail(email);
        OkHttpClientUtils.httpPost(UserService.getURLBasic() + "regist",user,User.class, new MyCallback() {
            @Override
            public void call(ResultData result) {
                if(result.getStatus() == Status.Success){
                    AndroidTool.showAnsyTost("注册完毕，请等待管理员同意，再登录，谢谢合作",result.getStatus());
                }else{
                    AndroidTool.showAnsyTost(result.getMessage(),result.getStatus());
                }
                AndroidTool.closeProgressBar();
            }
        });

    }
}

package com.xp.common;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xp.MainActivity;
import com.xp.R;

/**
 * 处理Android 工具类
 */
public class AndroidTool {

    private static AppCompatActivity activity;
    private static ProgressBar pb_wait;
    /**
     * 页面必须要注入进来，才能使用里面的功能
     *
     * @param activity
     */
    public static void setMainActivity(AppCompatActivity activity) {
        AndroidTool.activity = activity;
    }
    public static void setProgressBar(ProgressBar pb_wait) {
        AndroidTool.pb_wait =pb_wait;

    }

    /**
     * 显示等待 圈
     */
    public static void showProgressBar() {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb_wait.setVisibility(View.VISIBLE);
            }
        });

    }

    /**
     * 关闭 等待圈
     */
    public static void closeProgressBar() {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                pb_wait.setVisibility(View.GONE);
            }
        });

    }


    public static AppCompatActivity getMainActivity() {
        return activity;
    }

    /**
     * 在主页面显示 fragment
     *
     * @param fragment 要显示的页面
     */
    public static void replaceFrameLayout(Fragment fragment) {
        replaceFrameLayout(R.id.framelayout_content,fragment);
    }

    /**
     * fragment 页面替换
     * @param srcFragment
     * @param descfragment
     */
    public static void replaceFrameLayout(Fragment srcFragment, Fragment descfragment) {
        replaceFrameLayout(srcFragment.getId(),descfragment);
    }

    /**
     * fragment 页面替换
     * @param srcFragmentId
     * @param descfragment
     */
    public static void replaceFrameLayout(int srcFragmentId, Fragment descfragment) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(srcFragmentId, descfragment);

        transaction.addToBackStack(null);
        transaction.commit();
    }

    /**
     * 两个按钮的 dialog
     */
    public static void showDilog(String tip) {

        AlertDialog.Builder builder = new AlertDialog.Builder(activity).setIcon(R.mipmap.ic_launcher).setTitle("提示")
                .setMessage(tip).setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        Toast.makeText(activity, "取消", Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //ToDo: 你想做的事情
                        Toast.makeText(activity, "确定", Toast.LENGTH_LONG).show();
                        dialogInterface.dismiss();
                    }
                });
        builder.create().show();
    }

    /**
     * 异步弹出提示窗口
     * @param tip
     */
    public static void showAnsyTost(final String tip) {
        getMainActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getMainActivity(),tip,Toast.LENGTH_SHORT).show();
            }
        });
    }




}

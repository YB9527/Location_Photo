package com.xp;

import android.widget.Toast;

import com.xp.common.AndroidTool;

public class CrashHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        try {
            //传入这个方法的参数e就是引起应用crash的异常，我们可以在这里获取异常信息，可以把异常信息上传到服务器以便统一分析，也可以保存在文件系统中
        } catch (Exception e1) {
            Toast.makeText(AndroidTool.getMainActivity(),e1.getMessage(),Toast.LENGTH_LONG).show();
        }
    }
}
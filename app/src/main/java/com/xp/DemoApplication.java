package com.xp;

import android.app.Application;

import androidx.multidex.MultiDex;



public class DemoApplication extends Application {

    private static DemoApplication mApp;

    @Override
    public void onCreate() {
        super.onCreate();


        mApp = this;
        // 初始化MultiDex
        MultiDex.install(this);
    }
    public static DemoApplication getApp() {
        return mApp;
    }
}

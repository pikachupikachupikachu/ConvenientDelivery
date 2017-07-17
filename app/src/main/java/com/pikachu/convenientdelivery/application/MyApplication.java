package com.pikachu.convenientdelivery.application;

import android.content.Context;

import org.litepal.LitePalApplication;

import cn.bmob.v3.Bmob;

/**
 * Application
 */

public class MyApplication extends LitePalApplication {

    private static Context context;

    public static Context getContext() {
        return context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        LitePalApplication.initialize(context);
        Bmob.initialize(this,Constant.BMOB_APP_KEY);
    }

}

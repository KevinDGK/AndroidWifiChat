package com.dgk.wifichat.app;

import android.app.Application;
import android.content.Context;

/**
 * Created by Kevin on 2016/7/27.
 *
 */
public class MyApplication extends Application {

    private static MyApplication application;
    private static Context ctx;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        ctx = getApplicationContext();


    }

    public static MyApplication getInstance() {
        return application;
    }

    public static Context getContext() {
        return ctx;
    }
}

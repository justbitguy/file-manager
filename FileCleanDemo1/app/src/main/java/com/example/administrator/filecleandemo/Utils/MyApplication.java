package com.example.administrator.filecleandemo.Utils;


import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
    private static Context context;

    private static MyApplication sInstance;
    public static MyApplication getInstance() {
        return sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        context=getApplicationContext();
    }
}

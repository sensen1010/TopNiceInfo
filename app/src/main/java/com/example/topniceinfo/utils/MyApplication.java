package com.example.topniceinfo.utils;

import android.app.Application;
import android.content.Context;

import androidx.viewpager.widget.ViewPager;

public class MyApplication extends Application {

    //@SuppressLint("StaticFieldLeak")
   public static Context context;
   public static ViewPager viewPager;
    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();
    }
}

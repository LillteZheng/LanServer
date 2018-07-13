package com.zhengsr.lanserver;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhengshaorui on 2018/7/13.
 */

public class MyApplication extends Application {
    public static  Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = this;
    }
}

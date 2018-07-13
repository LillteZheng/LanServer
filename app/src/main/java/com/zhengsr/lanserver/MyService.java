package com.zhengsr.lanserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.zhengsr.lanserver.server.LanAsyncManager;
import com.zhengsr.lanserver.server.LanServerBean;
import com.zhengsr.lanserver.server.request.CheckRequestHandle;
import com.zhengsr.lanserver.server.request.ImageRequestHandle;


public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // startServer();
        LanServerBean bean = LanServerBean.lanBuilder()
                .setPort(8080)
                .setDefaultHtml(CusUtil.getAssetsString("test.html"))
                .registerHandler("file", ImageRequestHandle.create(this))
                .registerHandler("password", CheckRequestHandle.create(this))
                .builder();
        LanAsyncManager.getInstance().startServer(bean.getBuilder());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        LanAsyncManager.getInstance().stopServer();
    }


}

package com.zhengsr.lanserver;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.zhengsr.lanserver.server.LanAsyncManager;
import com.zhengsr.lanserver.server.LanServerBean;


public class MyService extends Service {
    private static final String TAG = "MyService";

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return new MyBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
       // startServer();

    }

    class MyBinder extends Binder{

        void startLanServer(LanServerBean bean){
            LanAsyncManager.getInstance().startServer(bean.getBuilder());
        }

        void stopLanServer(){
            LanAsyncManager.getInstance().stopServer();
        }


    }

    @Override
    public void onDestroy() {
        super.onDestroy();


    }




}

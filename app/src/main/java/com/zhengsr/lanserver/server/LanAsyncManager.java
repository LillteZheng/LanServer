package com.zhengsr.lanserver.server;

import android.text.TextUtils;

import com.koushikdutta.async.AsyncServer;
import com.koushikdutta.async.http.server.AsyncHttpServer;
import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.util.Map;
import java.util.Set;

/**
 * Created by zhengshaorui on 2018/7/12.
 */

public class LanAsyncManager {
    private AsyncServer mAsyncServer = null;
    private AsyncHttpServer mHttpServer = null;
    private LanAsyncManager(){
        mAsyncServer = new AsyncServer();
        mHttpServer = new AsyncHttpServer();
    };

    private static class Holder{
        static LanAsyncManager INSTANCE = new LanAsyncManager();
    }

    public static LanAsyncManager getInstance(){
        return Holder.INSTANCE;
    }

    public void startServer(final LanServerBean.Builder builder){
        mHttpServer.get("/", new HttpServerRequestCallback() {
            @Override
            public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
                if (TextUtils.isEmpty(builder.getHtmlString())){
                    response.send("response success,but no file");
                }else {
                    response.send(builder.getHtmlString());
                }
            }
        });

        Set<Map.Entry<String,HttpServerRequestCallback>> entries = builder.getHandlerMap().entrySet();
        for (Map.Entry<String, HttpServerRequestCallback> entry : entries) {
            mHttpServer.get("/"+entry.getKey(), entry.getValue());
        }
        mHttpServer.listen(mAsyncServer,builder.getPort());
    }

    public void stopServer(){
        mHttpServer.stop();
        mAsyncServer.stop();
    }
}

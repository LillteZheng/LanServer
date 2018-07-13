package com.zhengsr.lanserver.server.request;

import android.content.Context;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.zhengsr.lanserver.CusUtil;


/**
 * Created by zhengshaorui on 2018/7/12.
 */

public class CheckRequestHandle implements HttpServerRequestCallback {
    private static final String TAG = "ImageRequestHandle";
    private Context mContext;
    private CheckRequestHandle(Context context){
        mContext = context;
    }
    public static CheckRequestHandle create(Context context){
        return new CheckRequestHandle(context);
    }



    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        response.send(CusUtil.getAssetsString("checkpass.html"));
    }
}

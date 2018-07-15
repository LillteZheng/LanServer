package com.zhengsr.lanserver.server.request;

import android.content.Context;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;
import com.zhengsr.lanserver.utils.CusUtil;


/**
 * Created by zhengshaorui on 2018/7/12.
 */

public class CheckRequestHandle implements HttpServerRequestCallback {
    private static final String TAG = "ImageRequestHandle";
    private Context mContext;
    private String mIpAddr;
    private int mPort;
    private String mPassword;
    private CheckRequestHandle(Context context,String ipAddr,int port){
        mContext = context;
        mIpAddr = ipAddr;
        mPort = port;
        mPassword = port+"";
    }
    public static CheckRequestHandle create(Context context,String ipAddr,int port){
        return new CheckRequestHandle(context,ipAddr,port);
    }


    public void updatePassword(String password){
        mPassword = password;
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {

        response.send(CusUtil.getCheckPasswordHtml(mIpAddr,mPort,mPassword));
    }
}

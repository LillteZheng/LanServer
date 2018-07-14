package com.zhengsr.lanserver.server.request;

import android.content.Context;
import android.util.Log;

import com.koushikdutta.async.http.server.AsyncHttpServerRequest;
import com.koushikdutta.async.http.server.AsyncHttpServerResponse;
import com.koushikdutta.async.http.server.HttpServerRequestCallback;

import java.io.InputStream;

/**
 * Created by zhengshaorui on 2018/7/12.
 */

public class ImageRequestHandle implements HttpServerRequestCallback {
    private static final String TAG = "ImageRequestHandle";
    private Context mContext;
    private ImageRequestHandle(Context context){
        mContext = context;
    }
    public static ImageRequestHandle create(Context context){
        return new ImageRequestHandle(context);
    }

    @Override
    public void onRequest(AsyncHttpServerRequest request, AsyncHttpServerResponse response) {
        try {
            InputStream is = mContext.getAssets().open("beauty.jpg");
            response.sendStream(is,is.available());
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "zsr --> onRequest: "+e.toString());
        }

    }
}

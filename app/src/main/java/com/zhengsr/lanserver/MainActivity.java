package com.zhengsr.lanserver;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.zhengsr.lanserver.server.LanServerBean;
import com.zhengsr.lanserver.server.request.CheckRequestHandle;
import com.zhengsr.lanserver.server.request.ImageRequestHandle;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private static final int SERVER_PORT = 8080;
    private static String DEVICE_IP = "192.168.0.189";
    private MyService.MyBinder mServiceBinder;
    private ImageView mQrCodeImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

        //绑定服务
        startService(new Intent(this,MyService.class));
        bindService(new Intent(this,MyService.class),
                mServiceConnection,BIND_AUTO_CREATE);

        
    }

    private void initView(){
        TextView textView = findViewById(R.id.notice_text);

        if (CusUtil.isWifiConnected()){
            DEVICE_IP = CusUtil.getWifiIpaddr();
            textView.setText("请在同个wifi下，扫描该二维码，或者浏览器中输入: "+
                    "http://"+DEVICE_IP+":"+SERVER_PORT);
        }else{
            textView.setText("您还未连接 wifi ");
        }
        mQrCodeImageView = findViewById(R.id.qr_image);
        Bitmap bitmap = QRCode.createQRCode( "http://"+DEVICE_IP+":"+SERVER_PORT,800);
        mQrCodeImageView.setImageBitmap(bitmap);

        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setChecked(true);
    }


    private void startLanServer(){
        LanServerBean bean = LanServerBean.lanBuilder()
                .setPort(8080)
                .setDefaultHtml(getDefaultString())
                .registerHandler("file", ImageRequestHandle.create(this))
                .registerHandler("password", CheckRequestHandle.create(this))
                .builder();

        if (mServiceBinder != null) {
            mServiceBinder.startLanServer(bean);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mServiceBinder != null) {
            mServiceBinder.stopLanServer();
        }
    }

    ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServiceBinder = (MyService.MyBinder) service;
            startLanServer();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServiceBinder = null;
        }
    } ;

    /**
     * 输出可用的html
     * @return
     */
    private String getDefaultString(){
        StringBuilder sb = new StringBuilder();
        sb.append(CusUtil.getAssetsString("test.html")).append("\n")
                .append("<img src=\"http://")
                .append(DEVICE_IP).append(":").append(SERVER_PORT)
                .append("/file?fileName=test.png\" ")
                .append("style=\"width:100%\"/>").append("\n")
                .append(" <div class=\"container\">").append("\n")
                .append(" <p>天行九歌焰灵姬</p>").append("\n")
                .append(" </div>").append("\n")
                .append(" </div>").append("\n")
                .append(" </body>").append("\n")
                .append(" </html>").append("\n");

        return sb.toString();

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
            Bitmap logo = BitmapFactory.decodeResource(getResources(),R.mipmap.lock);
            Bitmap bitmap = QRCode.createQRCode( "http://"+DEVICE_IP+":"+SERVER_PORT+"/password",800);
            mQrCodeImageView.setImageBitmap(bitmap);
        }else{
            Bitmap bitmap = QRCode.createQRCode( "http://"+DEVICE_IP+":"+SERVER_PORT,800);
            mQrCodeImageView.setImageBitmap(bitmap);
        }


    }
}

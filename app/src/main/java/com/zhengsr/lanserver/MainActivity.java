package com.zhengsr.lanserver;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dalimao.corelibrary.VerificationCodeInput;
import com.zhengsr.lanserver.server.LanServerBean;
import com.zhengsr.lanserver.server.request.CheckRequestHandle;
import com.zhengsr.lanserver.server.request.ImageRequestHandle;
import com.zhengsr.lanserver.utils.CusDialog;
import com.zhengsr.lanserver.utils.CusUtil;
import com.zhengsr.lanserver.utils.QRUtils;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private static final String TAG = "MainActivity";
    private static final int SERVER_PORT = 8080;
    private static String DEVICE_IP = "172.0.0.1";
    private MyService.MyBinder mServiceBinder;
    private ImageView mQrCodeImageView;
    private String mPassword;
    private CheckRequestHandle mCheckRequestHandle;

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

        //wifi是否连接
        if (CusUtil.isWifiConnected()){
            //获取 ip
            DEVICE_IP = CusUtil.getWifiIpaddr();
            StringBuilder sb = new StringBuilder();
            sb.append("请在同个wifi下，扫描该二维码，或者浏览器中输入: \n")
                    .append("http://"+DEVICE_IP+":"+SERVER_PORT)
                    .append("\n或者输入:\n")
                    .append("http://"+DEVICE_IP+":"+SERVER_PORT+"/password");
            textView.setText(sb.toString());
        }else{
            textView.setText("您还未连接 wifi ");
        }
        mQrCodeImageView = findViewById(R.id.qr_image);
        Bitmap bitmap = QRUtils.createQRCode( "http://"+DEVICE_IP+":"+SERVER_PORT,800);
        mQrCodeImageView.setImageBitmap(bitmap);

        CheckBox checkBox = findViewById(R.id.checkbox);
        checkBox.setOnCheckedChangeListener(this);
        checkBox.setChecked(true);
    }


    private void startLanServer(){
        mCheckRequestHandle = CheckRequestHandle.create(this,DEVICE_IP,SERVER_PORT);
        //配置数据，builder 模式
        LanServerBean bean = LanServerBean.lanBuilder()
                .setPort(SERVER_PORT)
                .setIpAddr(DEVICE_IP)
                //配置默认 html 字符串
                .setDefaultHtml(CusUtil.getDefaultString(DEVICE_IP,SERVER_PORT))
                //添加过滤器，当检测 image，输出图片
                .registerHandler("image", ImageRequestHandle.create(this))
                //添加过滤器，当检测 password，弹出密码验证框
                .registerHandler("password",mCheckRequestHandle)
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



    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked){
           /* */
            final CusDialog cusDialog = new CusDialog.Builder()
                    .setContext(this)
                    .setLayoutId(R.layout.dialog_password_layout)
                    .setWidth(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setHeight(ViewGroup.LayoutParams.WRAP_CONTENT)
                    .setOutSideDimiss(true)
                    .builder();
            //默认密码为端口
            mPassword = SERVER_PORT+"";
            final VerificationCodeInput input = cusDialog.getViewbyId(R.id.dialog_edit);
            input.setOnCompleteListener(new VerificationCodeInput.Listener() {
                @Override
                public void onComplete(String s) {
                    mPassword = s;
                }
            });
            cusDialog.setOnClickListener(R.id.dialog_btn, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cusDialog.dismiss();
                    mCheckRequestHandle.updatePassword(mPassword);
                    Bitmap bitmap = QRUtils.createQRCode( "http://"+DEVICE_IP+":"+SERVER_PORT+"/password",800);
                    mQrCodeImageView.setImageBitmap(bitmap);
                }
            });

        }else{
            Bitmap bitmap = QRUtils.createQRCode( "http://"+DEVICE_IP+":"+SERVER_PORT,800);
            mQrCodeImageView.setImageBitmap(bitmap);
        }


    }
}

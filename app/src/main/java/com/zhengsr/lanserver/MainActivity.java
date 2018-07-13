package com.zhengsr.lanserver;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    public static final int SERVER_PORT = 8080;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = findViewById(R.id.notice_text);

        if (CusUtil.isWifiConnected()){
            mTextView.setText("请在同个wifi下，扫描该二维码，或者浏览器中输入: "
                    +CusUtil.getWifiIpaddr()+" "+SERVER_PORT);
        }else{
            mTextView.setText("您还未连接 wifi ");
        }

        startService(new Intent(this,MyService.class));

    }
}

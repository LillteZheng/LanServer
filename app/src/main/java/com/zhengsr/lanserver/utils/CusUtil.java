package com.zhengsr.lanserver.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

import com.zhengsr.lanserver.MyApplication;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by zhengshaorui on 2018/7/13.
 */

public class CusUtil {
    private static final String TAG = "CusUtil";
    private static Context sContext = MyApplication.CONTEXT;

    /**
     * 获取assets的
     * @param fileName
     * @return
     */
    public static String getAssetsString(String fileName) {
        String htmlstring = "not found";
        try {
            InputStream is = sContext.getAssets().open(fileName);
            BufferedReader br = new BufferedReader(new InputStreamReader(is, "utf-8"));
            String line = null;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            htmlstring = sb.toString();


        } catch (IOException e) {
            e.printStackTrace();
        }
        return htmlstring;
    }

    /**
     * 判断wifi是否连接
     * @return
     */
    public static boolean isWifiConnected(){
        ConnectivityManager connectivityManager = (ConnectivityManager) sContext.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean isWIFIC = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
        return isWIFIC;
    }

    /**
     * 获取无线 ip 地址
     * @return
     */
    public static String getWifiIpaddr(){
        WifiManager wifiManager = (WifiManager) sContext.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());//得到IPV4地址
        return ipAddress;
    }

    /**
     * 将ip装换成字符创
     * @param ip
     * @return
     */
    private static  String intIP2StringIP(int ip) {
        return (ip & 0xFF) + "." +
                ((ip >> 8) & 0xFF) + "." +
                ((ip >> 16) & 0xFF) + "." +
                (ip >> 24 & 0xFF);
    }

    /**
     * 输出默认的html
     * @return
     */
    public static  String getDefaultString(String ipaddr,int port){
        StringBuilder sb = new StringBuilder();
        sb.append(CusUtil.getAssetsString("test.html")).append("\n")
                .append("<img src=\"http://")
                .append(ipaddr).append(":").append(port)
                .append("/image?fileName=test.png\" ")
                .append("style=\"width:100%\"/>").append("\n")
                .append(" <div class=\"container\">").append("\n")
                .append(" <p>天行九歌焰灵姬</p>").append("\n")
                .append(" </div>").append("\n")
                .append(" </div>").append("\n")
                .append(" </body>").append("\n")
                .append(" </html>").append("\n");

        return sb.toString();

    }

    /**
     * 输出验证密码的html
     * @return
     */
    public static  String getCheckPasswordHtml(String ipaddr,int port,String password){

        StringBuilder sb = new StringBuilder();
        sb.append(CusUtil.getAssetsString("checkpass.html")).append("\n")
                .append("<button class=\"button\" onclick=\"checkPassword(")
                .append(password+",'"+"http://"+ipaddr+":"+port+"')\">确定</button>").append("\n")
                .append(" </div>").append("\n")
                .append(" </div>").append("\n")
                .append(" </body>").append("\n")
                .append(" </html>").append("\n");


        return sb.toString();

    }
}

package example.ldgd.com.tongchuandevicepolling.application;

import android.app.Application;
import android.net.wifi.WifiManager;

import java.util.Arrays;

import example.ldgd.com.tongchuandevicepolling.R;
import example.ldgd.com.tongchuandevicepolling.util.UuidUtil;

/**
 * Created by ldgd on 2018/6/1.
 */

public class MyApplication extends Application {
    public static WifiManager.MulticastLock lock;
    private static byte[] appUuid = null;
    private static MyApplication instance;
    private static String ip;

    @Override
    public void onCreate() {
        super.onCreate();

        // 不显示报警数据
    //    LogUtil.allowE = false;

    /*    WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
         lock= manager.createMulticastLock("test wifi");*/


        // appUuid = Util.getUuid(127, -127);
        appUuid = UuidUtil.getUuid(this);
        instance = this;
        // 测试
        System.out.println("appuuid = " + Arrays.toString(appUuid));
        //System.out.println("MyApplication_appuuid = " + Arrays.toString(Util.getUuid(this)));

        // 获取IP地址
        ip = getString(R.string.ip);

    }



    public static byte[] getAppUuid() {
        // 测试
        // System.out.println("appuuid = " + Arrays.toString(appUuid));
        if (appUuid == null) {
            appUuid = UuidUtil.getUuid(127, -127);
        }
        return appUuid;
    }

    public static MyApplication getInstance() {
        return instance;
    }

    public static String getIp(){
        return ip;
    }
}

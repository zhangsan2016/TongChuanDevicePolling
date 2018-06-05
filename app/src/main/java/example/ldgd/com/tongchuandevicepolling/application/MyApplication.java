package example.ldgd.com.tongchuandevicepolling.application;

import android.app.Application;
import android.net.wifi.WifiManager;

/**
 * Created by ldgd on 2018/6/1.
 */

public class MyApplication extends Application {
    public static WifiManager.MulticastLock lock;

    @Override
    public void onCreate() {
        super.onCreate();

        // 不显示报警数据
    //    LogUtil.allowE = false;

    /*    WifiManager manager = (WifiManager) this
                .getSystemService(Context.WIFI_SERVICE);
         lock= manager.createMulticastLock("test wifi");*/
    }
}

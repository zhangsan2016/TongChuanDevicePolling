package example.ldgd.com.tongchuandevicepolling.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.IOException;

import example.ldgd.com.tongchuandevicepolling.R;
import example.ldgd.com.tongchuandevicepolling.service.DeviceService;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 开启服务
        Intent deviceService = new Intent(this, DeviceService.class);
        startService(deviceService);

    }

    public void reBoot(View view) throws IOException, InterruptedException {
        Log.e("xx","sfdsdf");
        String[] arrayRestart = {"su", "-c", "reboot"};
        closePhone(MainActivity.this, arrayRestart);


/*      Process proc =Runtime.getRuntime().exec(new String[]{"su","-c","reboot "});
        proc.waitFor();*/

        /*try {

            //获得ServiceManager类
            Class<?> ServiceManager = Class.forName("android.os.ServiceManager");
            //获得ServiceManager的getService方法
            Method getService = ServiceManager.getMethod("getService", java.lang.String.class);
            //调用getService获取RemoteService
            Object oRemoteService = getService.invoke(null,Context.POWER_SERVICE);
            //获得IPowerManager.Stub类
            Class<?> cStub = Class.forName("android.os.IPowerManager$Stub");
            //获得asInterface方法
            Method asInterface = cStub.getMethod("asInterface", android.os.IBinder.class);
            //调用asInterface方法获取IPowerManager对象
            Object oIPowerManager = asInterface.invoke(null, oRemoteService);
            //获得shutdown()方法
            Method shutdown = oIPowerManager.getClass().getMethod("shutdown",boolean.class,boolean.class);
            //调用shutdown()方法
            shutdown.invoke(oIPowerManager,false,true);
        } catch (Exception e) {
            Log.e("aa", e.toString(), e);
        }*/

    }


    @SuppressWarnings("unused")
    private void closePhone(Context context, String[] shutdown){
        try {
            Process  process = Runtime.getRuntime().exec(shutdown);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

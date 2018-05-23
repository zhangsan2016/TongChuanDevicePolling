package example.ldgd.com.tongchuandevicepolling.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import example.ldgd.com.tongchuandevicepolling.service.DeviceService;

/**
 * Created by ldgd on 2018/5/22.
 */

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals("com.ldgd.service.destroy")){
            Intent sevice = new Intent(context, DeviceService.class);
            context.startService(sevice);
        }
    }

}

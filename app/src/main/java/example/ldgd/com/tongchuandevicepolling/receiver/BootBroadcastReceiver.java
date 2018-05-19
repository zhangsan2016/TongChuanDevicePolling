package example.ldgd.com.tongchuandevicepolling.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import example.ldgd.com.tongchuandevicepolling.activity.MainActivity;

/**
 * Created by ldgd on 2018/5/18.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    private static final String ACTION  = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {
            Intent sayHelloIntent = new Intent(context, MainActivity.class);
            sayHelloIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(sayHelloIntent);
        }
    }
}

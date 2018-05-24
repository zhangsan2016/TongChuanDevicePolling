package example.ldgd.com.tongchuandevicepolling.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.SocketException;

import example.ldgd.com.tongchuandevicepolling.R;
import example.ldgd.com.tongchuandevicepolling.activity.MainActivity;
import example.ldgd.com.tongchuandevicepolling.basic.SecondProtocol;
import example.ldgd.com.tongchuandevicepolling.interfaces.PacketRec;
import example.ldgd.com.tongchuandevicepolling.net.UdpBroadcast;

/**
 * Created by ldgd on 2018/5/22.
 */

public class DeviceService extends Service implements PacketRec {

    private static final int NOTIFY_ID = 10;

    private UdpBroadcast broadcast = UdpBroadcast.getInstance();
    private String broadcastIP = "255.255.255.255";
    private int udpPort = 9988;
    private int port = 2222;
    private boolean broadcastStart = false;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 设置为前台进程
        useForeground("铜川设备轮询服务开启，每45分钟轮询一次！","铜川设备轮询服务开启，每45分钟轮询一次！");

        // 开启UDP服务
        startUdpService();

        // 保证进程不那么容易被杀死
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     *  开启udp服务
     */
    private void startUdpService() {
        broadcast.stop();
        try {

            byte[] data = SecondProtocol.initialize;

            broadcast.searchDevice(broadcastIP, port, udpPort,
                    this, data);

            broadcastStart = true;
        } catch (SocketException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            showToast("服务开启失败，可能端口被占用");
            broadcastStart = false;
        }

    }
    private void showToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //   showDialog();

        showNotification();

    }

    private void showNotification() {
        /**
         *  创建通知栏管理工具
         */

        NotificationManager notificationManager = (NotificationManager) getSystemService
                (NOTIFICATION_SERVICE);

        /**
         *  实例化通知栏构造器
         */

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);

        /**
         *  设置Builder
         */
        //设置标题
        mBuilder.setContentTitle("我是标题")
                //设置内容
                .setContentText("我是内容")
                //设置大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                //设置小图标
                .setSmallIcon(R.mipmap.ic_launcher_round)
                //设置通知时间
                .setWhen(System.currentTimeMillis())
                //首次进入时显示效果
                .setTicker("我是测试内容")
                //设置通知方式，声音，震动，呼吸灯等效果，这里通知方式为声音
                .setDefaults(Notification.DEFAULT_SOUND);
        //发送通知请求
        notificationManager.notify(10, mBuilder.build());
    }


   /* private void showDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Receive new Message show or not");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("owen", "Yes is clicked");
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.e("owen", "No is clicked");
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.show();
    }*/


    public void useForeground(CharSequence tickerText, String currSong) {
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationIntent, 0);
    /* Method 01
     * this method must SET SMALLICON!
     * otherwise it can't do what we want in Android 4.4 KitKat,
     * it can only show the application info page which contains the 'Force Close' button.*/
        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_launcher)
                .setTicker(tickerText)
                .setWhen(System.currentTimeMillis())
                .setContentTitle(getString(R.string.app_name))
                .setContentText(currSong)
                .setContentIntent(pendingIntent);
        Notification notification = mNotifyBuilder.build();

    /* Method 02
    Notification notification = new Notification(R.drawable.ic_launcher, tickerText,
            System.currentTimeMillis());
    notification.setLatestEventInfo(PlayService.this, getText(R.string.app_name),
            currSong, pendingIntent);
    */

        startForeground(NOTIFY_ID, notification);
    }

    @Override
    public void onDestroy() {

        Intent intent = new Intent("com.ldgd.service.destroy");
        sendBroadcast(intent);
        super.onDestroy();
        // 关闭前台进程
        stopForeground(true);
    }

    @Override
    public void Receive(byte[] data, DatagramPacket packet, int session) {

    }
}

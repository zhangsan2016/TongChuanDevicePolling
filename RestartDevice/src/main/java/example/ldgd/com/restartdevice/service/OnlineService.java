package example.ldgd.com.restartdevice.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.PowerManager.WakeLock;
import android.widget.Toast;

import java.util.Arrays;

import example.ldgd.com.restartdevice.R;
import example.ldgd.com.restartdevice.client.appuser.Message;
import example.ldgd.com.restartdevice.client.appuser.UDPClientBase;
import example.ldgd.com.restartdevice.util.Util;


public class OnlineService extends Service {
    WakeLock wakeLock;
    MyUdpClient myUdpClient;
    private final String TAG_REQUEST = "MY_TAG";
    private SharedPreferences preferences;
    public static final byte[] UUID = new byte[]{12, 12, 12, 12, 12, 12, 12, 12};

    public class MyUdpClient extends UDPClientBase {

        public MyUdpClient(byte[] uuid, int appid, String serverAddr,
                           int serverPort) throws Exception {
            super(uuid, appid, serverAddr, serverPort);
        }

        @Override
        public boolean hasNetworkConnection() {
            return Util.hasNetwork(OnlineService.this);
        }

        @Override
        public void trySystemSleep() {
            tryReleaseWakeLock();
        }

        @Override
        public void onPushMessage(Message message) {
            // 测试
            System.out.println("message = " + Arrays.toString(message.getData()));


        }
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        resetClient();
        return super.onStartCommand(intent, flags, startId);
    }

    protected void resetClient() {
        if (this.myUdpClient != null) {
            try {
                myUdpClient.stop();
            } catch (Exception e) {
            }
        }
        try {
            // Property property = new Property(getApplicationContext());
            // String[] uuidStr = property.getContent("useruuid").split(",");
            // byte[] uuid = new byte[8];
            // for (int i = 0; i < uuidStr.length; i++) {
            // uuid[i] = Byte.parseByte(uuidStr[i]);
            // }
            // // 测试输出uuid
            // System.out.println(Arrays.toString(uuid));
            // System.out.println("心跳包 = " + Arrays.toString(uuid));
            myUdpClient = new MyUdpClient(UUID, 1, this.getResources()
                    .getString(R.string.ip), 9966);
            myUdpClient.setHeartbeatInterval(50);
            myUdpClient.start();
            // System.out.println("心跳包开启成功");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this.getApplicationContext(),
                    "操作失败：" + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    protected void tryReleaseWakeLock() {
        if (wakeLock != null && wakeLock.isHeld() == true) {
            wakeLock.release();
        }
    }


}

package example.ldgd.com.restartdevice;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import example.ldgd.com.restartdevice.client.appserver.Pusher;
import example.ldgd.com.restartdevice.service.OnlineService;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 启动心跳包服务
        Intent online = new Intent(this, OnlineService.class);
        startService(online);
    }

    public void restartDevice(View view) {
        new Thread() {
            public void run() {
                Pusher pusher = null;
                try {

                    // 获取当前应用的uuid
                    byte[] appUuid = OnlineService.UUID;
                    byte[] data = new byte[10];
                    data[0] = 0;
                    data[1] = 9;

                    System.arraycopy(appUuid, 0, data, 2,
                            appUuid.length);

                    pusher = new Pusher(getString(R.string.ip), 9966, 5000);
                    pusher.push0x20Message(new byte[]{1,2,1,2,1,2,1,2}, data);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (pusher != null) {
                        try {
                            pusher.close();
                        } catch (Exception e) {
                        }
                    }
                }
                // showToast("发送成功");
            }
        }.start();
    }
}

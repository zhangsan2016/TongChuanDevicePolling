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
import android.util.Log;
import android.widget.Toast;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import example.ldgd.com.tongchuandevicepolling.R;
import example.ldgd.com.tongchuandevicepolling.activity.MainActivity;
import example.ldgd.com.tongchuandevicepolling.basic.BasicSendType;
import example.ldgd.com.tongchuandevicepolling.basic.SecondProtocol;
import example.ldgd.com.tongchuandevicepolling.bean.HeartBeatBean;
import example.ldgd.com.tongchuandevicepolling.interfaces.PacketRec;
import example.ldgd.com.tongchuandevicepolling.net.GetIp;
import example.ldgd.com.tongchuandevicepolling.net.UdpBroadcast;
import example.ldgd.com.tongchuandevicepolling.rotocol.ToLowComOrder;
import example.ldgd.com.tongchuandevicepolling.util.Converter;
import example.ldgd.com.tongchuandevicepolling.util.LogUtil;
import example.ldgd.com.tongchuandevicepolling.util.MyHttpRequest;

import static android.content.ContentValues.TAG;

/**
 * Created by ldgd on 2018/5/22.
 */

public class DeviceService extends Service implements PacketRec {

    private static final int NOTIFY_ID = 10;

    private UdpBroadcast broadcast = UdpBroadcast.getInstance();
    private String broadcastIP = "255.255.255.255";
   // private String broadcastIP = "172.23.255.255";

    private int udpPort = 9988;
    private int port = 2222;
    private InetAddress localHostLANAddress;
    private boolean broadcastStart = false;
    private String subNetMask;  // 子网掩码
    /**
     * 格式化小数
     */
    DecimalFormat df = new DecimalFormat("0.00");

    private ArrayList<HeartBeatBean> listHeartBean = new ArrayList<HeartBeatBean>(); // 用于判断上来的心跳是否存在与集合中
    /**
     * key：row
     * value：heartBeatBean
     */
    private Map<Integer, HeartBeatBean> mapHeartBean = new HashMap<Integer, HeartBeatBean>();



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // 设置为前台进程
        useForeground("铜川设备轮询服务开启，每45分钟轮询一次！", "铜川设备轮询服务开启，每45分钟轮询一次！");

        // 开启UDP服务
        startUdpService();

        // 保证进程不那么容易被杀死
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启udp服务
     */
    private void startUdpService() {
        broadcast.stop();
        try {

            // TODO Auto-generated method stub
            try {
                localHostLANAddress = GetIp.getLocalHostLANAddress();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
         //   localIp = localHostLANAddress.getHostAddress();
//            subNetMask = GetIp.getSubNetWay(localHostLANAddress);
            //broadcastIP = GetIp.getBroadcastAddress(localIp, subNetMask);

            byte[] data = SecondProtocol.initialize;

            broadcast.searchDevice(broadcastIP, port, udpPort,
                    this, data);

            broadcastStart = true;

            // 开启轮询,获取设备信息上传到数据库
            startPolling((45 * 60 * 1000));

        } catch (SocketException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
            showToast("服务开启失败，可能端口被占用");
            broadcastStart = false;
        }
    }

    /**
     * 定时器
     */
    private Timer timer;
    private PollingTask pollingTask;

    private void startPolling(int pollingTime) {

        if (broadcastStart) {

            timer = new Timer();
            pollingTask = new PollingTask();
            timer.schedule(pollingTask, new Date(), pollingTime);

        }else{
            // 关闭定时器
            closeTimer();
        }


    }

    class PollingTask extends TimerTask {

        @Override
        public void run() {


            LogUtil.e("mapHeartBean = " + mapHeartBean.size());
            if(mapHeartBean.size() == 0){
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            /**
             * 遍历表格中的每一行，获取参数信息
             */
            traverseRow();
            // System.out.println("PollingTask");
        }
    }

    private void closeTimer() {
        if (pollingTask != null) {
            pollingTask.cancel();
            pollingTask = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public void traverseRow() {
        try {
            byte[] data = ToLowComOrder.getRefreshDev(BasicSendType.SINGLECAST);

            if (data != null) {
                Iterator it = mapHeartBean.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry entry = (Map.Entry) it.next();
                    Object key = entry.getKey();
                    HeartBeatBean beatBean = (HeartBeatBean) entry.getValue();
                    broadcast.sendData(beatBean.getIp(),
                            Integer.valueOf(port), data,
                            DeviceService.this);
                }

            }
        } catch (Exception e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
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
    public void Receive(final byte[] data, DatagramPacket packet, int session) {

        final String ip = packet.getAddress().getHostAddress();
        int port = packet.getPort();
        byte order = data[1];

//        System.out.println("PollingMonitoringJFrame2" + "Receive = " + ip + ":"
//                + port + "" + Arrays.toString(data));
        LogUtil.e("PollingMonitoringJFrame2" + "Receive = " + ip + ":"
                + port + "" + Arrays.toString(data));

        if (order == (SecondProtocol.hbeat | SecondProtocol.needReturn)) { // 判断命令是不是心跳包
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
            Date date = new Date();
            String online = format.format(date);
            //
            int length = Converter.bytesToInt2(Arrays.copyOfRange(data, 4, 6));
            if (length != 18) {
                return;
            }
            byte[] mac = Arrays.copyOfRange(data, 6, 12);
            String Mac = Converter.bytesToHexString(mac);
            byte[] serial = Arrays.copyOfRange(data, 12, 24);
            String Serial = Converter.bytesToHexString(serial);

            int size = listHeartBean.size();
            /**
             * 判断上来的心跳包 集合中是否已经存在 1.判断mac地址是否已经存在，存在的话 直接更新时间
             */
            for (HeartBeatBean b : listHeartBean) {
                if (b.getMac().equals(Mac)) {
                    b.setHeartBeatTime(date.getTime());
                    if (b.getIp().equals(ip) && b.getSerial().equals(Serial)) {
                        int row = b.getRow();
                        mapHeartBean.put(row, b);

                    } else {

                        int row = b.getRow();
                        b.setIp(ip);
                        b.setSerial(Serial);
                        mapHeartBean.put(row, b);
                        Log.e(TAG, "Receive:listHeartBean  = " + listHeartBean.size() );

                    }
                    try {
                        broadcast.sendData(ip, port, new byte[]{0x02, 0x10,
                                0x00, 0x00, 0x00, 0x00});
                        return;
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }
            }

            HeartBeatBean hbeat = new HeartBeatBean();
            hbeat.setRow(size);
            hbeat.setIp(ip);
            hbeat.setMac(Mac);
            hbeat.setSerial(Serial);
            hbeat.setStatus("在线" + online);
            hbeat.setHeartBeatTime(date.getTime());
            listHeartBean.add(hbeat);
            mapHeartBean.put(size, hbeat);
            System.out.println("ip为" + ip + "心跳包上来了");

            try {
                broadcast.sendData(ip, port, new byte[]{0x02, 0x10, 0x00,
                        0x00, 0x00, 0x00});
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } else if (data[8] == 3) { // 参数入库

            new Thread(new Runnable() {
                public void run() {
                    // TODO Auto-generated method stub

                    //	本机地址高8位 本机地址低8位
                    //  摄像头号1  1 1 摄像头号2  2 2
                    // mac地址
                    String hexMac = Converter.bytesToHexString(Arrays
                            .copyOfRange(data, 17, 23));
                    // 网关
                    String gataway = (data[23] & 0xff) + "."
                            + (data[24] & 0xff) + "." + (data[25] & 0xff) + "."
                            + (data[26] & 0xff);
                    // 子网掩码
                    String submask = (data[27] & 0xff) + "."
                            + (data[28] & 0xff) + "." + (data[29] & 0xff) + "."
                            + (data[30] & 0xff);
                    //版本号
                    String versionF = (data[31] & 0xff) + "";
                    String versionS = (data[32] & 0xff) + "";
                    String versionT = (data[33] & 0xff) + "";
                    //  更新时间
                    String updateTime = (data[34] & 0xff) + "/"
                            + (data[35] & 0xff) + "/" + (data[36] & 0xff);
                    // 连接开关
                    String isCreamFirstOpen = (data[38] == 0x01) ? "开" : "关";
                    // IP地址
                    String CreamFirIp = (data[39] & 0xff) + "."
                            + (data[40] & 0xff) + "." + (data[41] & 0xff) + "."
                            + (data[42] & 0xff);
                    // 端口号
                    String CreamFirPort = Converter.bytesToInt2(new byte[]{
                            data[43], data[44]})
                            + "";
                    // 连接开关
                    String isCreamSecOpen = (data[46] == 0x01) ? "开" : "关";
                    // IP地址
                    String CreamFSecIp = (data[47] & 0xff) + "."
                            + (data[48] & 0xff) + "." + (data[49] & 0xff) + "."
                            + (data[50] & 0xff);
                    //端口号
                    String CreamSecPort = Converter.bytesToInt2(new byte[]{
                            data[51], data[52]})
                            + "";
                    // 电压V
                    String volt = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[53], data[54]}) / 100)
                            + "v";
                    // 电流A
                    String ampere = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[55], data[56]}) / 100)
                            + "A";
                    // 功率W
                    String power = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[57], data[58]}) / 100)
                            + "w";
                    // 光照Lux
                    String lux = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[59], data[60]}))
                            + "lux";
                    // 有害气体浓度0.1PPM
                    String pm = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[61], data[62]}) / 10)
                            + "PPM";
                    // 温度0.1℃
                    String temp = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[63], data[64]}) / 10)
                            + "℃";
                    //  湿度0.1%
                    String hum = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[65], data[66]}) / 10)
                            + "%";
                    // 气压0.1kPa
                    String pressure = df.format((float) Converter
                            .bytesToInt2(new byte[]{data[67], data[68]}) / 10)
                            + "kPa";
                    //  PVC1PPM
                    String pvc = 0.00 + "ppm";
                    //  水位值
                    String waterValue = "0.00";
                    if (data.length >= 73) {
                        pvc = df.format((float) Converter
                                .bytesToInt2(new byte[]{data[69], data[70]}))
                                + "ppm";
                        waterValue = df.format((float) Converter
                                .bytesToInt2(new byte[]{data[71], data[72]}));
                    }

                    try {
                        // 24个参数
                        HashMap<String, Object> parames = new HashMap<String, Object>();
                        parames.put("deviceIp", ip);  // 设备ip地址
                        parames.put("hexMac", hexMac);  // mac地址
                        parames.put("gataway", gataway); // 网关
                        parames.put("submask", submask);   // 子网掩码
                        parames.put("versionF", versionF); // 版本号
                        parames.put("versionS", versionS);
                        parames.put("versionT", versionT);
                        parames.put("updateTime", updateTime); // 更新时间
                        parames.put("isCreamFirstOpen", data[38]); // 连接开关
                        parames.put("creamFirIp", CreamFirIp);  // IP地址
                        parames.put("creamFirPort", CreamFirPort); // 端口号
                        parames.put("isCreamSecOpen", data[46]);  //  连接开关
                        parames.put("creamFSecIp", CreamFSecIp);  // ip地址
                        parames.put("creamSecPort", CreamSecPort); // 相机端口号
                        parames.put("volt", df.format((float) Converter
                                .bytesToInt2(new byte[]{data[53], data[54]}) / 100));  // 电压
                        parames.put("ampere", df.format((float) Converter
                                .bytesToInt2(new byte[]{data[55], data[56]}) / 100)); // 电流
                        parames.put("power", df.format((float) Converter
                                .bytesToInt2(new byte[]{data[57], data[58]}) / 100));  // 功率
                        parames.put("lux", df.format((float) Converter
                                .bytesToInt2(new byte[]{data[59], data[60]})));  // 光照
                        parames.put("pm", df.format((float) Converter
                                .bytesToInt2(new byte[]{data[61], data[62]}) / 10));  // 有害气体浓度
                        parames.put(
                                "temp",
                                Converter.bytesToInt2(new byte[]{data[65],
                                        data[66]}) / 10);  // 温度0.1℃
                        parames.put("hum",
                                df.format((float) Converter
                                        .bytesToInt2(new byte[]{data[65],
                                                data[66]}) / 10));  // 湿度
                        parames.put("pressure", df.format((float) Converter
                                .bytesToInt2(new byte[]{data[67], data[68]}) / 10));  // 气压
                        parames.put("pvc", 0.00);  // PVC1PPM
                        parames.put("waterValue", 0.00);  // 水位值

                        //String getUrl = "http://192.168.1.155:8080/ExhibitionCameraDisplay/InsertDeviceParameterAction";
                        //String getUrl = "http://121.40.194.91:8080/ExhibitionCameraDisplay/InsertDeviceParameterAction";
                        //String getUrl = "http://120.26.216.74:9090/ExhibitionCameraDisplay/InsertDeviceParameterAction";

                        String getUrl = "http://121.40.194.91:8080/ExhibitionCameraDisplay/InsertDeviceParameterAction";

                        String res = MyHttpRequest.sendGet(getUrl, parames, "utf-8");
                        // System.out.println("Get请求2:"+ res);

                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        Toast.makeText(DeviceService.this, "网络连接超时", Toast.LENGTH_SHORT).show();
                        //	showAlert("网络连接超时");
                    }

                }

            });

        }

    }
}

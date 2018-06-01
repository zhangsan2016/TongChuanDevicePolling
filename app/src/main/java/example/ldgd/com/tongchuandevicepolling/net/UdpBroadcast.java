package example.ldgd.com.tongchuandevicepolling.net;

import android.util.Log;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Arrays;

import example.ldgd.com.tongchuandevicepolling.interfaces.PacketRec;
import example.ldgd.com.tongchuandevicepolling.util.CheckCRC;
import example.ldgd.com.tongchuandevicepolling.util.Converter;
import example.ldgd.com.tongchuandevicepolling.util.LogUtil;

import static android.content.ContentValues.TAG;


public class UdpBroadcast {

    private DatagramSocket detectSocket;
    private static UdpBroadcast broadcast = new UdpBroadcast();
    private Thread thread;
    private Receive rec;
    private PacketRec packetFrame;
    /**
     * 单灯设备界面
     */
    private PacketRec singleDeviceFrame;
    DatagramPacket out;
    long LastTime = 0;
    int getTime = 0;

    private UdpBroadcast() {
    }

    ;

    public static UdpBroadcast getInstance() {
        return broadcast;
    }

    // Use this port to send broadcast packet

    /**
     * @param broadcastIp ：广播的ip地址 broadcastPort: 广播的port []data:6个字节 。
     *                    前面四个字节是本机IP，后面两个字节是端口号（用于TCP通信）
     * @category udp广播 检索设备
     **/
    public void searchDevice(String broadcastIp, int broadcastPort,
                             int udpPort, final PacketRec packetFrame, byte[] data)
            throws SocketException {

        this.packetFrame = packetFrame;
        detectSocket = new DatagramSocket(udpPort);
        // Send packet thread
        System.out.println("Send thread started.");
        try {
            byte[] buf = new byte[1024];

            int packetPort = broadcastPort;
            InetAddress hostAddress = InetAddress.getByName(broadcastIp);

            buf = data;

            out = new DatagramPacket(buf, buf.length, hostAddress, packetPort);
            LogUtil.e(Arrays.toString(buf) + ":" + buf.length + "   " + hostAddress + ":" + packetPort);


        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "searchDevice: UnknownHostException = " + e.getMessage().toString());
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    detectSocket.send(out);
                    LogUtil.e("detectSocket.send(out);  成功");
                    rec = new Receive();
                    thread = new Thread(rec);
                    thread.start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }




    public void stop() {

        if (detectSocket != null) {
            rec.change(false);
            if (!thread.isInterrupted()) {
                thread.interrupt();
                thread.interrupted();
            }
            if (detectSocket.isConnected()) {
                detectSocket.disconnect();
            }
            detectSocket.close();
            detectSocket = null;
        }

    }


    public void sendData(final String ip, final int port, final byte[] data)
            throws Exception {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                InetAddress hostAddress;
                try {
                    hostAddress = InetAddress.getByName(ip);
                    out = new DatagramPacket(data,

                            data.length, hostAddress, port);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                detectSocket.send(out);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                } catch (UnknownHostException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }

            }
        }).start();

    }

    class Receive implements Runnable {
        boolean flag = true;

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (flag) {
                //synchronized (detectSocket) {
                byte[] buf = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buf, buf.length);

                try {
                    LogUtil.e( "  detectSocket.receive(packet)......." );
                    detectSocket.receive(packet);
                    byte[] data = packet.getData();
                    LogUtil.e("packet.getLength() = " + packet.getLength());
                    //	System.out.println("packet.getLength() = " + packet.getLength());


                    if (packet.getLength() > 5) {
                        // packetFrame.Receive(data, packet, 0);
                        byte[] getData = Arrays
                                .copyOfRange(packet.getData(), 6,
                                        packet.getLength() - 3);
                        System.out.println("校验的数据:"
                                + Arrays.toString(getData));
                        byte[] getCrc = Arrays.copyOfRange(
                                packet.getData(), packet.getLength() - 3,
                                packet.getLength() - 1);
                        int getCRC = Converter.bytesToInt2(getCrc);
                        int calcCRC = Converter.bytesToInt2(CheckCRC
                                .crc(getData));
                        if (getCRC == calcCRC) {
                            System.out.println("CRC校验成功");
                        }
                        byte[] session = new byte[]{data[2], data[3]};
                        int sessions = Converter.bytesToInt2(session);
                        String ip = packet.getAddress().getHostAddress();
                        byte order = data[1];
                        if (order == 0x10 || order == 0x11) {
                            packetFrame.Receive(
                                    Arrays.copyOfRange(packet.getData(), 0,
                                            packet.getLength()), packet,
                                    sessions);

                            // 测试
                        /*	if(packetFrame instanceof PollingMonitoringJFrame2){
                                System.out.println("packetFrame = PollingMonitoringJFrame2"  );
							}else if(packetFrame instanceof SingleDeviceFrame){
								System.out.println("packetFrame = SingleDeviceFrame"  );
							}*/
                        } else {
                               /* if(data[8]== BasicProtocol.FunctionType.reREADALARMSTATUS ){
                                    packetFrame.Receive( Arrays.copyOfRange(packet.getData(), 0,packet.getLength()), packet,sessions);
                                }else if(data[8] == 3 ){
                                	singleDeviceFrame.Receive( Arrays.copyOfRange(packet.getData(), 0,packet.getLength()), packet,sessions);
                                }
								if (mapSingles == null) {
									// packetFrame.Receive(new
									// byte[]{0},null,1);
								} else {
									Set<Entry<String, SingleLightSingleJFrame>> entrySet = mapSingles
											.entrySet();
									for (Entry<String, SingleLightSingleJFrame> entry : entrySet) {
										if (entry.getKey().equals(ip)) {
											if(sDevFrame != entry.getValue()){
												sDevFrame = entry.getValue();
											}
										*//*	if(System.currentTimeMillis()-LastTime<100){
                                            }else{*//*
                                                getTime++;
												System.out.println("获得次数"+getTime);
												LastTime = System.currentTimeMillis();
												sDevFrame.Receive(Arrays.copyOfRange(packet.getData(),0,packet.getLength()),packet, sessions);
										//	}
										}
									}

								}*/

                        }
                        String rcvd = "Received from "
                                + packet.getSocketAddress();
                        System.out.println(rcvd);
                    }
                } catch (IOException e) {
                    System.out.println("关闭");
                }
            }
        }
        //	}

        void change(boolean status) {
            flag = status;
        }

    }


    /**
     * 轮询设备获取参数
     *
     * @param ip
     * @param port
     * @param allData
     * @param packetFrame
     * @throws Exception
     */
    public void sendData(final String ip, final int port, final byte[] allData,
                         final PacketRec packetFrame) throws Exception {
        // TODO Auto-generated method stub
        try {
            // 设置调用对象，方便回调返回信息
            this.singleDeviceFrame = packetFrame;
            InetAddress hostAddress;
            hostAddress = InetAddress.getByName(ip);
            out = new DatagramPacket(allData,
                    allData.length, hostAddress, port);
            if (detectSocket != null) {
                detectSocket.send(out);
            }
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
        }

    }


}

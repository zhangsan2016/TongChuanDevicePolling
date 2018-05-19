/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.runnable;


import java.util.Map;
import java.util.Set;

import example.ldgd.com.tongchuandevicepolling.bean.HeartBeatBean;
import example.ldgd.com.tongchuandevicepolling.interfaces.PollingRec;

/**
 * @author CJK
 *
 */
public class PollingRunnable implements Runnable{
    private final int OFFLINETIME = 2*60;
    private PollingRec frame;
    private Map<Integer, HeartBeatBean> mapHeartBean;
    private boolean flag = true;
    private int pollingInterval;
    /**
     * 
     */
    public PollingRunnable(PollingRec frame,int pollingInterval) {
        // TODO Auto-generated constructor stub
        this.frame = frame;
      
        this.pollingInterval = pollingInterval;
    }
    
    public void setHeartBean( Map<Integer, HeartBeatBean> mapHeartBean){
        this.mapHeartBean = mapHeartBean;
    }
    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (flag) {
            if (mapHeartBean.size() > 0) {
                Set<Map.Entry<Integer, HeartBeatBean>> entrySet = mapHeartBean
                        .entrySet();
                for (Map.Entry<Integer, HeartBeatBean> entry : entrySet) {
                    long lastTime = entry.getValue().getHeartBeatTime();
                    // System.out.println("上次发送时间"+lastTime);
                    if (System.currentTimeMillis() - lastTime > OFFLINETIME * 1000) {
                        frame.onPollingRec(PollingRec.OFFLINE, entry.getKey(),true);
                    }
                }
            }
            try {
                Thread.sleep(pollingInterval * 1000);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    public void changeContuie(boolean flag){
        this.flag = flag;
    }
    
}

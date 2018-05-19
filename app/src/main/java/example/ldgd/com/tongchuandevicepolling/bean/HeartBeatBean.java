/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.bean;

/**
 * @author CJK
 *
 */
public class HeartBeatBean {
	private int row; //行号
	private String ip; //ip地址
	private String mac; //mac地址
	private String serial; //序列号
	private String status; //设备状态
	private long heartBeatTime; //心跳时间
	/**
	 * @return the heartBeatTime
	 */
	public long getHeartBeatTime() {
		return heartBeatTime;
	}
	/**
	 * @param heartBeatTime the heartBeatTime to set
	 */
	public void setHeartBeatTime(long heartBeatTime) {
		this.heartBeatTime = heartBeatTime;
	}
	/**
	 * @return the row
	 */
	public int getRow() {
		return row;
	}
	/**
	 * @param row the row to set
	 */
	public void setRow(int row) {
		this.row = row;
	}
	/**
	 * @return the ip
	 */
	public String getIp() {
		return ip;
	}
	/**
	 * @param ip the ip to set
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	/**
	 * @return the mac
	 */
	public String getMac() {
		return mac;
	}
	/**
	 * @param mac the mac to set
	 */
	public void setMac(String mac) {
		this.mac = mac;
	}
	/**
	 * @return the serial
	 */
	public String getSerial() {
		return serial;
	}
	/**
	 * @param serial the serial to set
	 */
	public void setSerial(String serial) {
		this.serial = serial;
	}
	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}
	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}
}

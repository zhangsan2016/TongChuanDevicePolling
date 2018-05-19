/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.net;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;

/**
 * @author CJK
 * 
 */
public class GetIp {
	private static StringBuilder maskStr;

	public static void main(String[] args) throws UnknownHostException,
			SocketException {
		System.out.println("Host:\t"
				+ InetAddress.getLocalHost().getHostAddress() + "\n");

		System.out.println(getLocalHostLANAddress().getHostAddress());
		Enumeration<NetworkInterface> en = NetworkInterface
				.getNetworkInterfaces();
		Enumeration<InetAddress> addresses;
		while (en.hasMoreElements()) {
			NetworkInterface networkinterface = en.nextElement();
			// System.out.println(networkinterface.getName());
			addresses = networkinterface.getInetAddresses();
			while (addresses.hasMoreElements()) {
				InetAddress inet = addresses.nextElement();
				if (inet instanceof Inet4Address) {
					System.out.println(inet.getHostAddress());

				}
			}
		}

	}

	public static InetAddress getLocalHostLANAddress()
			throws UnknownHostException {
		try {
			InetAddress candidateAddress = null;
			// 遍历所有的网络接口
			for (Enumeration ifaces = NetworkInterface.getNetworkInterfaces(); ifaces
					.hasMoreElements();) {
				NetworkInterface iface = (NetworkInterface) ifaces
						.nextElement();
				// 在所有的接口下再遍历IP
				for (Enumeration inetAddrs = iface.getInetAddresses(); inetAddrs
						.hasMoreElements();) {
					InetAddress inetAddr = (InetAddress) inetAddrs
							.nextElement();
					if (!inetAddr.isLoopbackAddress()) {// 排除loopback类型地址
						if (inetAddr.isSiteLocalAddress()) {
							// 如果是site-local地址，就是它了
							return inetAddr;
						} else if (candidateAddress == null) {
							// site-local类型的地址未被发现，先记录候选地址
							candidateAddress = inetAddr;
						}
					}
				}
			}
			if (candidateAddress != null) {
				return candidateAddress;
			}
			// 如果没有发现 non-loopback地址.只能用最次选的方案
			InetAddress jdkSuppliedAddress = InetAddress.getLocalHost();
			if (jdkSuppliedAddress == null) {
				throw new UnknownHostException(
						"The JDK InetAddress.getLocalHost() method unexpectedly returned null.");
			}
			return jdkSuppliedAddress;
		} catch (Exception e) {
			UnknownHostException unknownHostException = new UnknownHostException(
					"Failed to determine LAN address: " + e);
			unknownHostException.initCause(e);
			throw unknownHostException;
		}
	}

	public static String getSubNetWay(InetAddress ip) {
		//InetAddress ip = null;
		NetworkInterface ni = null;
		try {
			ip = InetAddress.getLocalHost();
			ni = NetworkInterface.getByInetAddress(ip);// 搜索绑定了指定IP地址的网络接口
		} catch (Exception e) {
			e.printStackTrace();
		}
		List<InterfaceAddress> list = ni.getInterfaceAddresses();// 获取此网络接口的全部或部分
																	// InterfaceAddresses
																	// 所组成的列表
		if (list.size() > 0) {
			int mask = list.get(0).getNetworkPrefixLength(); // 子网掩码的二进制1的个数
			maskStr = new StringBuilder();
			int[] maskIp = new int[4];
			for (int i = 0; i < maskIp.length; i++) {
				maskIp[i] = (mask >= 8) ? 255 : (mask > 0 ? (mask & 0xff) : 0);
				mask -= 8;
				maskStr.append(maskIp[i]);
				if (i < maskIp.length - 1) {
					maskStr.append(".");
				}
			}
			System.out.println("SubnetMask:" + maskStr);
			;
		}
		return maskStr.toString();

	}
	
	 /** 
     * 根据本机IP和子网掩码，计算子网广播地址 
     * @param ip 本机ip 
     * @param subnet 本机子网掩码 
     * @return 
     */  
     public static String getBroadcastAddress(String ip,String subnet) {  
                      
           
         String[] ips = ip.split("\\.");   
         String[] subnets = subnet.split("\\.");  
         StringBuffer sb = new StringBuffer();  
         for(int i = 0; i < ips.length; i++) {  
             ips[i] = String.valueOf((~Integer.parseInt(subnets[i]))|(Integer.parseInt(ips[i])));  
             sb.append(turnToStr(Integer.parseInt(ips[i])));  
             if(i != (ips.length-1))  
                 sb.append(".");  
         }                                      
         return turnToIp(sb.toString());  
    }  
  

     /** 
      * 把带符号整形转换为二进制 
      * @param num 
      * @return 
      */  
    private static String turnToStr(int num) {  
        String str = "";  
        str = Integer.toBinaryString(num);            
        int len = 8 - str.length();  
        // 如果二进制数据少于8位,在前面补零.  
        for (int i = 0; i < len; i++) {  
            str = "0" + str;  
        }  
        //如果num为负数，转为二进制的结果有32位，如1111 1111 1111 1111 1111 1111 1101 1110  
        //则只取最后的8位.  
        if (len < 0)  
            str = str.substring(24, 32);  
        return str;  
    }      
      
    /** 
     * 把二进制形式的ip，转换为十进制形式的ip 
     * @param str 
     * @return 
     */  
    private static String turnToIp(String str){  
        String[] ips = str.split("\\.");  
        StringBuffer sb = new StringBuffer();  
        for (int i = 0; i < ips.length; i++) {  
            sb.append(turnToInt(ips[i]));  
            sb.append(".");  
        }            
        sb.deleteCharAt(sb.length() - 1);  
        return sb.toString();  
    }  
  
    /** 
     * 把二进制转换为十进制 
     * @param str 
     * @return 
     */  
    private static int turnToInt(String str){  
        int total = 0;  
        int top = str.length();  
        for (int i = 0; i < str.length(); i++) {  
            String h = String.valueOf(str.charAt(i));  
            top--;  
            total += ((int) Math.pow(2, top)) * (Integer.parseInt(h));  
        }  
        return total;  
    }  
}

/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.net;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.Vector;

/**
 * @author CJK
 *
 */
public class NetWorkInfo {

	    private static Process p;        
	      
	    public void SubnetAddress(){          
	    }  
	          
	    /** 
	     * 获取本机IP 
	     * @return 
	     */  
	    private  String getIpAddressFromLinux(){  
	        Enumeration netInterfaces;  
	        String result=null;  
	        try {  
	            netInterfaces = NetworkInterface.getNetworkInterfaces();      
	            InetAddress ip = null;  
	            while(netInterfaces.hasMoreElements()){  
	                NetworkInterface ni=(NetworkInterface)netInterfaces.nextElement();  
	                ip=(InetAddress) ni.getInetAddresses().nextElement();  
	                if( !ip.isLoopbackAddress()&& ip.getHostAddress().indexOf(":")==-1)  
	                {  
	                    result=ip.getHostAddress();  
	                    break;  
	                }  
	                else                  
	                     ip=null;  
	               }  
	        } catch (SocketException e) {              
	            e.printStackTrace();  
	        }  
	        return result;  
	    }  
	      
	    /** 
	     * 获取本机IP 
	     * @return 
	     */  
	    private String getIpAddressForWindow(){  
	        String result=null;  
	        InetAddress inet;  
	        try {  
	            inet = InetAddress.getLocalHost();  
	            result = inet.getHostAddress();  
	        } catch (UnknownHostException e) {  
	            e.printStackTrace();  
	        }          
	        return result;  
	    }  
	      
	    /** 
	     * 使用ipconfig/all命令 
	     * 获取windows系统上的子网掩码 
	     * @return 
	     */  
	    private String getSubnetAddressForWindow(){  
	        String cmd = "cmd.exe /c ipconfig/all"; //window下获取子网掩码的命令  
	        String find ="Subnet Mask";//子网掩码的提示          
	          
	        Vector ipConfigs;    
	        ipConfigs = execute(cmd);        
	        Object[] ipConfigArray=ipConfigs.toArray();  
	        String result = null;  
	        for(int i=0;i<ipConfigArray.length;i++){  
	            String ipConfig = ipConfigArray[i].toString();  
	            if(!ipConfig.equalsIgnoreCase("") && ipConfig.indexOf(find)!=-1){  
	                String[] subnet = ipConfig.split(":");  
	                result = subnet[1].substring(1);  
	                break;  
	            }  
	        }  
	        return result;  
	    }   
	      
	    /** 
	     * 使用ifconfig命令 
	     * 获取linux系统上的子网掩码 
	     * @return 
	     */  
	    private String getSubnetAddressForLinux(){                  
	        String cmd = "ifconfig"; //linux下获取子网掩码的命令  
	        String find = "Mask";//子网掩码的提示  
	              
	        Vector ipConfigs;    
	        ipConfigs = execute(cmd);        
	        Object[] ipConfigArray=ipConfigs.toArray();  
	        String result = null;  
	        for(int i=0;i<ipConfigArray.length;i++){  
	            String ipConfig = ipConfigArray[i].toString();  
	            if(!ipConfig.equalsIgnoreCase("") && ipConfig.indexOf(find)!=-1){  
	                  
	                String[] subnet1 = ipConfig.split(find);  
	                String[] subnet = subnet1[1].split(":");  
	                result = subnet[1];                  
	                break;  
	            }  
	        }  
	        return result;  
	    }   
	      
	    /** 
	     * 执行命令 
	     * @param shellCommand 
	     * @return 
	     */  
	    private Vector execute(String shellCommand){               
	        try{  
	            Start(shellCommand);               
	            Vector vResult=new Vector();               
	            DataInputStream in=new DataInputStream(p.getInputStream());               
	            BufferedReader reader=new BufferedReader(new InputStreamReader(in));               
	            String line;    
	            do{  
	                line=reader.readLine();    
	                if(line==null){    
	                    break;    
	                }    
	                else{    
	                    vResult.addElement(line);    
	                }    
	            }while(true);    
	            reader.close();    
	            return   vResult;    
	          }catch(Exception e){               
	              return null;    
	          }    
	    }   
	      
	    private void Start(String shellCommand){           
	        try{    
	            if(p!=null){    
	                p.destroy();    
	                p=null;      
	            }    
	            Runtime sys=Runtime.getRuntime();    
	            p=sys.exec(shellCommand);    
	        }catch(Exception   e){    
	            e.printStackTrace();    
	        }    
	    }    
	          
	    /** 
	     * 根据本机IP和子网掩码，计算子网广播地址 
	     * @param ip 本机ip 
	     * @param subnet 本机子网掩码 
	     * @return 
	     */  
	     public String getBroadcastAddress() {  
	         String os = System.getProperty("os.name").substring(0, 3);  
	         String subnet;  
	         String ip;
	            if(os.equalsIgnoreCase("win")){  
	                subnet = getSubnetAddressForWindow();
	                ip = getIpAddressForWindow();
	            }  
	            else{  
	                subnet = getSubnetAddressForLinux();  
	                ip = getIpAddressFromLinux();
	            }              
	           
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
	  
	     public String getIP() {  
	         String os = System.getProperty("os.name").substring(0, 3);  
	         String ip;
	            if(os.equalsIgnoreCase("win")){  
	                ip = getIpAddressForWindow();
	            }  
	            else{  
	                ip = getIpAddressFromLinux();
	            }              
	         return ip;  
	    }  
	     
	     
	     /** 
	      * 把带符号整形转换为二进制 
	      * @param num 
	      * @return 
	      */  
	    private String turnToStr(int num) {  
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
	    private String turnToIp(String str){  
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
	    private int turnToInt(String str){  
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



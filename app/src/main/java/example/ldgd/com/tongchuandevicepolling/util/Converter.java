/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.util;

/**
 * @author CJK
 *
 */
public class Converter {
	
	public static byte[] StringIpToByte(String ip){
		String []ips = ip.split("\\.");
		byte [] data = new byte[4];
		
		for(int i=0;i<ips.length;i++){
			int a = Integer.valueOf(ips[i]);
			data[i] = intToByte(a);
		}
		return data;
	}

	public static byte DecStringToByte(String DecData) {

		int a = Integer.valueOf(DecData);
		byte data = intToByte(a);
		return data;
	}
	
	public static String ByteToStringIp(byte[] ips){
		StringBuilder sb = new StringBuilder();
		for(int i=0;i<ips.length;i++){
			String s;
			if(i==ips.length-1){
				 s = byteToInt(ips[i])+"";
			}
			else{
				s = byteToInt(ips[i])+".";
			}
			sb.append(s);
		}
		return sb.toString();
	}
	
	public static byte[] intToBytes2(int value)   
	{   
	    byte[] src = new byte[2];   
	    src[0] = (byte) ((value>>8)&0xFF);    
	    src[1] = (byte) (value & 0xFF);       
	    return src;  
	}  
	public static String bytesToHexString(byte src) {
		StringBuilder stringBuilder = new StringBuilder("");
		int v = src & 0xFF;
		String hv = Integer.toHexString(v).toUpperCase();
		if (hv.length() < 2) {
			stringBuilder.append(0);
		}
		stringBuilder.append(hv);

		return stringBuilder.toString();
	}
	
	/**
	 * @category byte[] תint ��λ��ǰ
	 * */
	public static int bytesToInt2(byte[] src) {   
	    int value;    
	    value = (int) (((src[0] & 0xFF)<<8)  
	            |(src[1] & 0xFF));  
	    return value;  
	}  
	
	public static int bytesToInt2(byte Hsrc,byte Lsrc) {  
	    int value;    
	    value = (int) (((Hsrc & 0xFF)<<8)  
	            |(Lsrc & 0xFF));  
	    return value;  
	} 
	
	public static byte intToByte(int x) { 
	    return (byte) x; 
	} 
	 
	public static int byteToInt(byte b) { 
	    //Java ���ǰ� byte �����з��������ǿ���ͨ������� 0xFF ���ж�������õ������޷�ֵ  
	    return b & 0xFF; 
	} 
	
	public static byte[] HexStringBytes(String src) {
		if (null == src || 0 == src.length()) {
			return null;
		}
		if(src.length()%2!=0){
			src = "0"+src;
		}
		byte[] ret = new byte[src.length()/2];
		byte[] tmp = src.getBytes();
		// System.out.println("tmp="+Arrays.toString(tmp));
		/*
		 * int length = 0; if (tmp.length % 2 != 0) { length=(tmp.length+1)/2; }
		 * length=tmp.length;
		 */
		for (int i = 0; i < (tmp.length / 2); i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
		}

		return ret;
	}

	public static byte uniteBytes(byte src0, byte src1) {
		byte _b0 = Byte.decode("0x" + new String(new byte[] { src0 }))
				.byteValue();
		_b0 = (byte) (_b0 << 4);
		// System.out.println("_b0="+_b0);
		byte _b1 = Byte.decode("0x" + new String(new byte[] { src1 }))
				.byteValue();
		byte ret = (byte) (_b0 ^ _b1);
		return ret;
	}
	/**
	 * @category byte 转 16进制字符串
	 * */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}
	/**
	 * @category byte 转 16进制带空格字符串
	 * */
	public static String bToHexspaceStr(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv+" ");
		}
		return stringBuilder.toString().toUpperCase();
	}
	
	/**
	 * @category 8位转字节
	 * */
	public static byte bitToByte(int one,int two, int three,int four,int five,int six,int seven,int eight){
		
		byte data = (byte) (one|(two<<1)|three<<2|four<<3|five<<4|six<<5|seven<<6|eight<<7);
		return data;
	}
	
	/**
	 * @category 4位转字节
	 * */
	public static byte bit4ToByte(int one,int two, int three,int four){
		
		byte data = (byte) (one|(two<<1)|three<<2|four<<3);
		return data;
	}
}

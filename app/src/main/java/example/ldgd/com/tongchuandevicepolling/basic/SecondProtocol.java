/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.basic;


import example.ldgd.com.tongchuandevicepolling.util.ArrayTool;
import example.ldgd.com.tongchuandevicepolling.util.Converter;

/**
 * @author CJK
 *
 */
public class SecondProtocol {
	private static byte version = 0x01;
	public static byte hbeat = 0x10; //心跳
	public static byte needReturn = 0x11; //需要应答
	public static byte noNeedReturn = 0x00; //不需要应答
	
	public final static byte[] initialize = new byte[]{version,0x70,0x00,0x00,0x00,0x00};  //初始化网络
	public final static byte[] heartbeat = new byte[]{version,0x10};   //心跳包
	public final static byte[] stopSession = new byte[]{version,0x60}; //结束会话
	public final static byte[] sendNeedRe = new byte[]{version,0x21};  //需要返回值的下行命令
	public final static byte[] sendNoNeedRe = new byte[]{version,0x20}; //不需要返回的下行命令
	public static byte[] combineData(byte[] head,byte[] session,byte[]basicData){
		int length = 0;
		if(basicData ==null){
			length = 0;
			byte[] basicLen = Converter.intToBytes2(length);
			byte[] data = ArrayTool.copyMoreArrays(head,session,basicLen);
			return data;
		}
		else{
			length = basicData.length;
			byte[] basicLen = Converter.intToBytes2(length);
			byte[] data = ArrayTool.copyMoreArrays(head,session,basicLen,basicData);
			return data;
		}
		
		
		
	}
	
	public byte[] initialize(){
		return initialize;
	}
}

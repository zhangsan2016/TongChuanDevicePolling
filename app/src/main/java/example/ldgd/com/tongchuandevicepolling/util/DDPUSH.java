/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.util;

/**
 * @author CJK
 *
 */
public class DDPUSH {
	private final static byte[] version = new byte[]{0x01};
	public static byte[] addData(byte[] content){
		int length = content.length;
		byte[] len = Converter.intToBytes2(length);
		byte[] crc = CheckCRC.crc(content);
		byte[] data = ArrayTool.copyMoreArrays(version,len,content,crc);
		return data;
	}

}

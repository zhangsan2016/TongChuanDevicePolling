/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.levelTwo;


import example.ldgd.com.tongchuandevicepolling.util.ArrayTool;
import example.ldgd.com.tongchuandevicepolling.util.Converter;


/**
 * @author CJK
 * 
 */
public class LeveltwoProtocol {
	private byte[] version = new byte[] { 0x02 };
	private byte[] needReType = new byte[] { 0x21 };
	private byte[] NoNeedReType = new byte[] { 0x20 };
	private byte[] EndSession = new byte[] { 0x60 };
	private byte[] length = new byte[2];
	private byte[] sendType = new byte[2];

	public byte[] combineData(boolean isNeedReturn, byte[] sessionFlag,
			byte[] basicData) throws Exception {
		if (sessionFlag.length != 2 || basicData.length == 0) {
			throw new Exception("数组错误");
		}
		if (isNeedReturn) {
			sendType = needReType;
		} else {
			sendType = NoNeedReType;
		}
		int len = basicData.length;
		length = Converter.intToBytes2(len);
		byte[] data = ArrayTool.copyMoreArrays(version, sendType, sessionFlag,
				basicData);
		return data;
	}

}

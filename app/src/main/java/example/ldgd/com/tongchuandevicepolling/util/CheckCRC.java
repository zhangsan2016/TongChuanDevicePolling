package example.ldgd.com.tongchuandevicepolling.util;

import java.util.Arrays;


public class CheckCRC {
	public static byte[] contentAndCrc=null;
	/**
	 * @param text
	 * @return
	 * @return
	 */
	public static byte[] headtoByte(String text) {
		byte[] head = HexStringBytes(text);
		return head;
	}

	public static byte[] datatoByte(String text) {
		byte[] data = HexString2Bytes(text);
		return data;
	}

	public static String checkCRC(byte[] IDandAddress, byte[] content) {
		// byte[] data = new byte[14];
		// data = HexString2Bytes(sb);
		// System.out.println(Arrays.toString(data));
		// System.out.println(Arrays.toString(data));
		// byte[] head = { (byte) 0xEE, 0x00, 0x00, (byte) 0x82 };
		byte[]  buffer = new byte[IDandAddress.length + content.length];
		System.arraycopy(IDandAddress, 0, buffer, 0, IDandAddress.length);
		System.arraycopy(content, 0, buffer, IDandAddress.length, content.length);
		System.out.println("要检验crc的数据包："+Arrays.toString(buffer));
		int crc = CRC16.calcCrc16(buffer);
		System.out.println("校验码:" + String.format("0x%04x", crc));
		String str_crc=String.format("%04x", crc);
		byte []crc_andtail=HexStringBytes(str_crc);
		contentAndCrc=new byte[buffer.length+crc_andtail.length];
		System.arraycopy(buffer, 0, contentAndCrc, 0, buffer.length);
		System.arraycopy(crc_andtail, 0, contentAndCrc, buffer.length, crc_andtail.length);
		//System.out.println(Arrays.toString(crc_andtail));
		return String.format("%04x", crc);

	}
	public static byte[] CRC(byte[] IDandAddress, byte[] content) {
		// byte[] data = new byte[14];
		// data = HexString2Bytes(sb);
		// System.out.println(Arrays.toString(data));
		// System.out.println(Arrays.toString(data));
		// byte[] head = { (byte) 0xEE, 0x00, 0x00, (byte) 0x82 };
		byte[]  buffer = new byte[IDandAddress.length + content.length];
		System.arraycopy(IDandAddress, 0, buffer, 0, IDandAddress.length);
		System.arraycopy(content, 0, buffer, IDandAddress.length, content.length);
		System.out.println("要检验crc的数据包："+Arrays.toString(buffer));
		int crc = CRC16.calcCrc16(buffer);
		System.out.println("校验码:" + String.format("0x%04x", crc));
		String str_crc=String.format("%04x", crc);
		byte []crc_andtail=HexStringBytes(str_crc);
/*		contentAndCrc=new byte[buffer.length+crc_andtail.length];
		System.arraycopy(buffer, 0, contentAndCrc, 0, buffer.length);
		System.arraycopy(crc_andtail, 0, contentAndCrc, buffer.length, crc_andtail.length);*/
		//System.out.println(Arrays.toString(crc_andtail));
		return crc_andtail;

	}
	public static byte[] HexString2Bytes(String src) {
		if (null == src || 0 == src.length()) {
			return null;
		}
		byte[] ret = new byte[14];
		byte[] tmp = src.getBytes();
		// System.out.println("tmp="+Arrays.toString(tmp));
		/*
		 * int length = 0; if (tmp.length % 2 != 0) { length=(tmp.length+1)/2; }
		 * length=tmp.length;
		 */
		for (int i = 0; i < (tmp.length / 2); i++) {
			ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);

		}
		if (tmp.length % 2 != 0) {
			ret[tmp.length / 2] = uniteBytes((byte) 48, tmp[tmp.length / 2 * 2]);
		}

		return ret;
	}

	public static byte[] HexStringBytes(String src) {
		if (null == src || 0 == src.length()) {
			return null;
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
	/*   public void changeMessage( byte[] message, int length )
	    {
	        setChanged();
	        byte[] temp = new byte[length];
	        System.arraycopy( message, 0, temp, 0, length );
	        notifyObservers( temp );
	    }*/
	public static byte[] hexStringToByte(String hex) {
		int len = (hex.length() / 2);
		byte[] result = new byte[len];
		char[] achar = hex.toCharArray();
		for (int i = 0; i < len; i++) {
			int pos = i * 2;
			result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
		}
		return result;
	}
	private static int toByte(char c) {
		byte b = (byte) "0123456789ABCDEF".indexOf(c);
		return b;
	}

	public  static byte[] crc(byte[] data){
		int crc = CRC16.calcCrc16(data);
		String str_crc = String.format("%04x",crc);
		byte []CRC = HexStringBytes(str_crc);
		return  CRC;
	}

	/* public static void main(String []args) throws IOException{
		byte []data = new byte[38168];
		File file = new File("e:\\103_W5500.bin");
		byte []mTotal = new byte[]{};
		if (file.isFile() && file.exists()) { // 判断文件是否存在
			//text_describe.setText("文件字节：" + file_size);
			FileInputStream is = new FileInputStream(file);
			// 设定读取的字节数
			int n = 128;
			byte buffer[] = new byte[n];
			// 读取输入流
			while ((is.read(buffer, 0, n) != -1) && (n > 0)) {					
				mTotal = ArrayTool.copyArrays(mTotal, buffer);
				buffer = new byte[n];
			}
			System.out.println(Converter.bToHexspaceStr(mTotal));
		System.out.println(Converter.bToHexspaceStr(crc(mTotal)));
		}
	 }*/
}

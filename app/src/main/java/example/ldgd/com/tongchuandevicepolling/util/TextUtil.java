/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.util;


/**
 * @author CJK
 * 
 */
public class TextUtil {
	public static boolean isEmpty(String... str) {
		int len = str.length;
		for (int i = 0; i < len; i++) {
			if (str[i].equals("") || null == str[i]) {
				return true;
			}
		}
		return false;
	}

	public static byte[] StringIpTobytes(String... ip) {
		int size = ip.length;
		byte []data = new byte[size];
		for(int i=0;i<size;i++){
			
			data[i] = Converter.intToByte(Integer.valueOf(ip[i]));
		}
		return data;
	}
	
/*	public static byte[] TextTobytes(JTextField...fields ){
		int size = fields.length;
		byte []data = new byte[size];
		for(int i=0;i<size;i++){
			String content = fields[i].getText().toString().trim();
			if(isEmpty(content)){
				return null;
			}
			data[i] = Converter.DecStringToByte(content);
		}
		return data;
	}*/
}

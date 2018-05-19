package example.ldgd.com.tongchuandevicepolling.util;

public class ArrayTool {
	public static byte[] getByteUuid(String uuid) {
		byte[] byteUuid = new byte[8];
		System.out.println(uuid);
		String[] struuid = uuid.split(",");
		for (int i = 0; i < byteUuid.length; i++) {
			byteUuid[i] = Byte.parseByte(struuid[i]);
		}
		return byteUuid;
	}

	public static byte[] copyArrays(byte[] first, byte[] last) {
		byte[] data = new byte[first.length + last.length];
		System.arraycopy(first, 0, data, 0, first.length);
		System.arraycopy(last, 0, data, first.length, last.length);
		return data;

	}
	
	public static byte[] copyMoreArrays(byte[] ...arrays) {
		int arraysNum = arrays.length;
		int sum =0;
		for(int i=0;i<arraysNum;i++){
			int length = arrays[i].length;
			sum = sum+ length;
		}
		byte[]data = new byte[sum];
		int length = 0;
		for(int i=0;i<arraysNum;i++){
			System.arraycopy(arrays[i], 0, data, length, arrays[i].length);
			length += arrays[i].length;
		}		
		return data;
		
	}

	
 
}

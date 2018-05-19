/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.basic;


import example.ldgd.com.tongchuandevicepolling.util.ArrayTool;
import example.ldgd.com.tongchuandevicepolling.util.CheckCRC;
import example.ldgd.com.tongchuandevicepolling.util.Converter;

/**
 * @author CJK
 *
 */
public class BasicProtocol {
	private static byte[]head = new byte[]{(byte)0xEE};
	private static byte[]tail = new byte[]{(byte)0xEF};
	private static byte[]contentLen = new byte[2];
	private byte[]crc = new byte[2];
	
	public  static byte[] combineData(byte[] ordFunction,byte[] ordAddress,byte[] ordContent) throws Exception{
		if(ordFunction.length!=2||ordAddress.length!=4){
			throw new Exception("数组错误");
		}
		int length =0;
		byte[] tempData;
		if(ordContent==null){
			length =0;
			contentLen[0] =0 ;
			contentLen[1] = 0;
			tempData = ArrayTool.copyMoreArrays(head,ordFunction,ordAddress,contentLen);
		}
		else{
		length = ordContent.length;
		contentLen = Converter.intToBytes2(length);
		tempData = ArrayTool.copyMoreArrays(head,ordFunction,ordAddress,contentLen,ordContent);  
		}
		
		//封装前面的数据，用以crc检验
		  
		byte[] crc = CheckCRC.crc(tempData);
		byte[] data = ArrayTool.copyMoreArrays(tempData,crc,tail); 		
		return data;				
	}
	
	public class FunctionType {
/*
		空包	0xEE	0x00	0x00
		恢复出厂设置	0xEE	0x00	0x01
		写设备ID号	0xEE	0x00	0x17
		返回写设备ID号	0xEE	0x00	0x18
		设备固件升级	0xEE	0x00	0x23
		返回设备固件升级确认	0xEE	0x00	0x24
		设备固件数据包	0xEE	0x00	0x25
		返回设备固件数据包确认	0xEE	0x00	0x26
		设备固件CRC校验（数据包发送完成后）	0xEE	0x00	0x27
		返回CRC校验（数据包发送完成后）	0xEE	0x00	0x28														
		重启	0xEE	0x01	0x01
		刷新设备信息	0xEE	0x01	0x02
		返回设备信息	0xEE	0x01	0x03
		连接目标摄像头IP	0xEE	0x01	0x04
		状态返回	0xEE	0x01	0x05
		IP配置	0xEE	0x01	0x06
		状态返回	0xEE	0x01	0x07 */
		
		

			public static final byte RESET = (byte)0x01;
		//	public static final byte STARTSERVER = (byte)0x02;
		//	public static final byte RESTART = (byte)0x01;
			public static final byte REFRESH = (byte)0x02;
			public static final byte reREFRESH = (byte)0x03;
			public static final byte CONNECTIP = (byte)0x04;
			public static final byte reCONNECTIP = (byte)0x05;
			public static final byte CONFIGUREIP = (byte)0x06;
			public static final byte reCONFIGUREIP = (byte)0x07;
			//public static final byte UPDATA = (byte)0x06;
		//	public static final byte WRITEID = (byte)0x17;
			public static final byte reWRITEID = (byte)0x18;
 	/*		public static final byte startUpdate = (byte)0x23;
			public static final byte reStartUpdate = (byte)0x24;
			public static final byte UPDATEING = (byte)0x25;
			public static final byte reUPDATEING = (byte)0x26;
			public static final byte CHECKCRC = (byte)0x27;
			public static final byte reCHECKCRC = (byte)0x28;*/
			
			/*恢复出厂设置		0xEE	0x00	0x01
			校时	0xEE			0x00	0x02
			曲线定时				0xEE	0x00	0x03
			返回曲线定时确认		0xEE	0x00	0x04
			实时调光				0xEE	0x00	0x05
			返回实时调光确认		0xEE	0x00	0x06
			雷达开关				0xEE	0x00	0x07
			返回雷达确认			0xEE	0x00	0x08
			设置阈值				0xEE	0x00	0x09*/
		/*	读取报警电压电流阈值	0xEE	0x00	0x0B
			返回警报电压电流阈值	0xEE	0x00	0x0C
			删除设备组地址		0xEE	0x00	0x0D
			返回删除设备组号确认	0xEE	0x00	0x0E
			写组地址				0xEE	0x00	0x0F
			返回写组地址确认		0xEE	0x00	0x10
			读警报状态			0xEE	0x00	0x11
			返回读警报状态确认		0xEE	0x00	0x12
			读取时间				0xEE	0x00	0x13
			返回时间				0xEE	0x00	0x14
			读取光强				0xEE	0x00	0x15
			返回光强				0xEE	0x00	0x16
			写设备ID号			0xEE	0x00	0x17
			返回写设备ID号		0xEE	0x00	0x18
			读取电参				0xEE	0x00	0x19
			返回电参				0xEE	0x00	0x1A
			清除警报				0xEE	0x00	0x1B
			返回清除警报确认		0xEE	0x00	0x1C
			读取设备状态			0xEE	0x00	0x1D
			返回设备状态			0xEE	0x00	0x1E
			读设备ID号			0xEE	0x00	0x1F
			返回设备ID号			0xEE	0x00	0x20
			读取设备当前版本号		0xEE	0x00	0x21
			返回设备版本号		0xEE	0x00	0x22
			设备固件升级			0xEE	0x00	0x23
			返回设备固件升级确认	0xEE	0x00	0x24
			设备固件数据包		0xEE	0x00	0x25
			返回设备固件数据包确认	0xEE	0x00	0x26
			设备固件CRC校验		0xEE	0x00	0x27
			返回CRC校验			0xEE	0x00	0x28
			报警开关			    0xEE	0x00	0x29
			返回报警开关确认	    0xEE	0x00	0x2A
			添加扩展指令			0xEE	0x00	0x2B
			返回添加扩展指令确认	0xEE	0x00	0x2C
			删除扩展指令			0xEE	0x00	0x2D
			返回删除指令确认		0xEE	0x00	0x2E
			模式应答使能			0xEE	0x00	0x2F
			返回模式应答使能确认	0xEE	0x00	0x30
			连续读取电参频率		0xEE	0x00	0x31
			返回电参频率			0xEE	0x00	0x32
			重启（单、广播）            0xEE    0x01    0x01

			IP配置				0xEE	0x01	0x06
			状态返回				0xEE	0x01	0x07*/


			public static final byte RESETTIME = (byte)0x02;
			public static final byte TIMING = (byte)0x03;
			public static final byte reTIMING = (byte)0x04;
			public static final byte TURNLIGHT = (byte)0x05;
			public static final byte reTURNLIGHT = (byte)0x06;
			public static final byte TURNRADAR = (byte)0x07;
			public static final byte reTURNRADAR = (byte)0x08;
			public static final byte SETTHRESHOLD = (byte)0x09;
			public static final byte reSETTHRESHOLD = (byte)0x0A;
			public static final byte READTHRESHOLD =(byte)0x0B;
			public static final byte reREADTHRESHOLD = (byte)0x0C;
			public static final byte DELETEGROUPNUM = (byte)0x0D;
			public static final byte reDELETEGROUPNUM = (byte)0x0E;
			public static final byte WRITEGROUPNUM = (byte)0x0F;
			public static final byte reWRITEGROUPNUM = (byte)0x10;
			public static final byte READALARMSTATUS = (byte)0x11;
			public static final byte reREADALARMSTATUS = (byte)0x12;
			public static final byte READTIME = (byte)0x13;
			public static final byte reREADTIME = (byte)0x14;
			public static final byte READLIGHTINTENSITY = (byte)0x15;
			public static final byte reREADLIGHTINTENSITY = (byte)0x16;
			public static final byte WRITEID = (byte)0x17;
			public static final byte reWIRTEID = (byte)0x18;
			public static final byte READPARAM = (byte)0x19;
			public static final byte reREADPARAM = (byte)0x1A;
			public static final byte CLEARALARM = (byte)0x1B;
			public static final byte reCLEARALARM = (byte)0x1C;
			public static final byte READDEVSTATUS = (byte)0x1D;
			public static final byte reREADDEVSTATUS = (byte)0x1E;
			public static final byte READID = (byte)0x1F;
			public static final byte reREADID = (byte)0x20;
			public static final byte READVERSION = (byte)0x21;
			public static final byte reREADVERSION =(byte)0x22;			
			public static final byte startUpdate = (byte)0x23;
			public static final byte reStartUpdate = (byte)0x24;
			public static final byte UPDATEING = (byte)0x25;
			public static final byte reUPDATEING = (byte)0x26;
			public static final byte CHECKCRC = (byte)0x27;
			public static final byte reCHECKCRC = (byte)0x28;
			public static final byte TURN_ALARM = (byte)0x29;
			public static final byte reTURN_ALARM = (byte)0x2A;
			
			public static final byte RESTART =(byte)0x01;
			public static final byte CONFIGIP = (byte)0x06;
			public static final byte reCONFIGIP = (byte)0x07;
		}

}

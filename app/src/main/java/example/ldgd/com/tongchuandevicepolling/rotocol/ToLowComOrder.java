/**
 * 
 */
package example.ldgd.com.tongchuandevicepolling.rotocol;


import example.ldgd.com.tongchuandevicepolling.basic.BasicProtocol;
import example.ldgd.com.tongchuandevicepolling.basic.BasicSendType;
import example.ldgd.com.tongchuandevicepolling.basic.SecondProtocol;
import example.ldgd.com.tongchuandevicepolling.util.ArrayTool;
import example.ldgd.com.tongchuandevicepolling.util.Converter;

/**
 * @author CJK
 *
 */
public class ToLowComOrder {
	private final static byte VERSION = 0X02;
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category
	 */
	public static byte[] getReset(byte broadcast) throws Exception {
		// TODO Auto-generated method stub
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x20;
		byte[] function = new byte[]{broadcast, BasicProtocol.FunctionType.RESET};
		byte[] content = new byte[]{(byte)0xA1,(byte)0XAA};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	
	/**
	 * @author CJK
	 * @category 重启命令
	 * **/
	public static byte[] getRestart(byte broadcast) throws Exception {
		// TODO Auto-generated method stub
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x20;
		byte[] function = new byte[]{(byte) (broadcast|0x01),BasicProtocol.FunctionType.RESTART};
		byte[] content = new byte[]{(byte)0xA1,(byte)0XAA};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 刷新设备信息
	 * **/
	public static byte[] getRefreshDev(byte broadcast) throws Exception{
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
		byte[] function = new byte[]{(byte) (broadcast|0x01),BasicProtocol.FunctionType.REFRESH};
		byte[] content = null;
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 连接目标摄像ip
	 **/
	public static byte[] getConnectCrm(byte[] content) throws Exception{
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
        
		byte[] function = new byte[]{(byte) (BasicSendType.SINGLECAST|0x01),BasicProtocol.FunctionType.CONNECTIP};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 写入设备ID
	 **/
	public static byte[] getWriteID(byte[] content) throws Exception{
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
		byte[] function = new byte[]{(byte) (BasicSendType.SINGLECAST),BasicProtocol.FunctionType.WRITEID};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}

	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 配置ip
	 */
	public static byte[] getConfigure(byte[] content) throws Exception {
		// TODO Auto-generated method stub
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
		byte[] function = new byte[]{(byte) (BasicSendType.SINGLECAST|0x01),BasicProtocol.FunctionType.CONFIGUREIP};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}

	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 启动单播升级，发送版本号固件大小
	 */
	public static byte[] getStartUpdate(byte[] content) throws Exception {
		// TODO Auto-generated method stub
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
		byte[] function = new byte[]{(byte) (BasicSendType.SINGLECAST|0x00),BasicProtocol.FunctionType.startUpdate};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 启动广播播升级，发送版本号固件大小
	 */
	public static byte[] getStartBroadcastUpdate(byte[] content) throws Exception {
		// TODO Auto-generated method stub
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x20;
		byte[] function = new byte[]{(byte) (BasicSendType.BROADCAST|0x00),BasicProtocol.FunctionType.startUpdate};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	
	
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 包序号加文件内容
	 */
	public static byte[] getUpdataPartFile(int sendNum2, byte[] file) throws Exception {
		// TODO Auto-generated method stub
		byte []firmwareNum = Converter.intToBytes2(sendNum2);
		byte []content = ArrayTool.copyArrays(firmwareNum, file);
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
		byte[] function = new byte[]{(byte) (BasicSendType.SINGLECAST|0x00),BasicProtocol.FunctionType.UPDATEING};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 包序号加文件内容,广播
	 */
	public static byte[] getbroadUpdataPartFile(int sendNum2, byte[] file) throws Exception {
		// TODO Auto-generated method stub
		byte []firmwareNum = Converter.intToBytes2(sendNum2);
		byte []content = ArrayTool.copyArrays(firmwareNum, file);
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x20;
		byte[] function = new byte[]{(byte) (BasicSendType.BROADCAST|0x00),BasicProtocol.FunctionType.UPDATEING};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 固件的CRC
	 */
	public static byte[] getFirewareCRC(byte[] crc) throws Exception {
		// TODO Auto-generated method stub
		byte []content = crc;
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x21;
		byte[] function = new byte[]{(byte) (BasicSendType.SINGLECAST|0x00),BasicProtocol.FunctionType.CHECKCRC};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 固件的CRC
	 */
	public static byte[] getBroadcastFirewareCRC(byte[] crc) throws Exception {
		// TODO Auto-generated method stub
		byte []content = crc;
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] =  (byte) 0x20;
		byte[] function = new byte[]{(byte) (BasicSendType.BROADCAST|0x00),BasicProtocol.FunctionType.CHECKCRC};
		byte[] address = new byte[4];
		byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category
	 */
	public static byte[] getResetData(byte type,byte[] content) throws Exception {
		// TODO Auto-generated method stub
		 return comBineBasicAndSecond(type, BasicProtocol.FunctionType.RESET, content);
        
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 校时
	 * @param time:当前时间    basicSendType:发送的类型(1.广播 2.组播 3.单播)
	 */
	public static byte [] getResetTimeData(byte[] time,byte basicSenType) throws Exception{
		/*byte []content = time;
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] = (byte) (basicSenType!= basicSendType.SINGLECAST?0x20:0x21);
        byte[] function = new byte[]{(byte) (basicSenType|0x00),basicProtocol.FunctionType.RESETTIME};
        byte[] address = new byte[4];
        byte[] basicData;
		basicData = basicProtocol.combineData(function, address, content);
		byte[] session = new byte[2];
        byte[] allData = secondProtocol.combineData(sendType, session, basicData);
        return allData;    */    
        return comBineBasicAndSecond(basicSenType, BasicProtocol.FunctionType.RESETTIME, time);
	}
	/**
     * @author CJK
	 * @throws Exception 
     * @category 读取固件版本号和时间
     */
    public static byte[] getReadDateAndVersion(byte basicSenType) throws Exception {
        // TODO Auto-generated method stub
        return comBineBasicAndSecond(basicSenType, BasicProtocol.FunctionType.READVERSION, null);
    }
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 定时
	 * @param time:当前时间    basicSendType:发送的类型(1.广播 2.组播 3.单播)
	 */
	public static byte [] getTimingData(byte[] contents,byte basicSenType) throws Exception{       
        return comBineBasicAndSecond(basicSenType, BasicProtocol.FunctionType.TIMING, contents);
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category
	 */
	public static byte[] getReadTimeData(byte singlecast) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(singlecast, BasicProtocol.FunctionType.READTIME, null);
	}
	
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 调光
	 * @param   basicSendType:发送的类型(1.广播 2.组播 3.单播)
	 */
	public static byte [] getTurnLightgData(byte[] contents,byte basicSenType) throws Exception{       
        return comBineBasicAndSecond(basicSenType, BasicProtocol.FunctionType.TURNLIGHT, contents);
	}
	
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 设置阈值
	 */
	public static byte[] getSetThreshold(byte[] content, byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.SETTHRESHOLD, content);
	}
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 雷达开关
	 */
	public static byte[] getTurnRadar(byte[] content, byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.TURNRADAR, content);
	}
	
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 清除报警
	 */
	public static byte[] getClearAlarm(byte[] content, byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.CLEARALARM, content);
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 删除组地址
	 */
	public static byte[] getDeleteGroupNumData(byte[] bGroupNum, byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.DELETEGROUPNUM, bGroupNum);
	}
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 写组地址
	 */
	public static byte[] getWriteGroupNumData(byte[] bGroupNum, byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.WRITEGROUPNUM, bGroupNum);
	}
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 读取阈值
	 */
	public static byte[] getReadThreshold(byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.READTHRESHOLD, null);
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 读取警报状态
	 */
	public static byte[] getReadAlarm(byte sendType) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.READALARMSTATUS, null);
	}
	
	/**
     * @throws Exception 
     * @author CJK
     * @category 打开/关闭警报
     */
    public static byte[] getTurnAlarm(byte[] content,byte sendType) throws Exception {
        // TODO Auto-generated method stub
        return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.TURN_ALARM, content);
    }
    /**
     * @throws Exception 
     * @author CJK
     * @category
     */
    public static byte[] getRadar(byte[] content, byte sendType) throws Exception {
        // TODO Auto-generated method stub
        return comBineBasicAndSecond(sendType, BasicProtocol.FunctionType.TURNRADAR, content);
    }
    /**
     * @throws Exception 
     * @author CJK
     * @category 重启
     */
    public static byte[] getRestart(byte[] content,byte sendType) throws Exception {
        // TODO Auto-generated method stub
        return comBineBasicAndSecond((byte)(sendType|0x01), BasicProtocol.FunctionType.RESTART, content);
    }
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 读取ID
	 */
	public static byte[] getReadIdData(byte singlecast) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(singlecast, BasicProtocol.FunctionType.READID, null);
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 读取光强
	 */
	public static byte[] getReadLightIntensityData(byte []light,byte singlecast) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(singlecast, BasicProtocol.FunctionType.READLIGHTINTENSITY, light);
	}
	
	/**
	 * @author CJK
	 * @throws Exception 
	 * @category 读取电参
	 */
	public static byte[] getReadEleParamData(byte singlecast) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(singlecast, BasicProtocol.FunctionType.READPARAM, null);
	}
	
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 读取设备状态
	 */
	public static byte[] getReadStatus(byte singlecast) throws Exception {
		// TODO Auto-generated method stub
		return comBineBasicAndSecond(singlecast, BasicProtocol.FunctionType.READDEVSTATUS, null);
	}
	/**
	 * @throws Exception 
	 * @author CJK
	 * @category 合并基础协议和二级协议
	 * @param contents:发送内容   basicSendType:发送的类型(1.广播 2.组播 3.单播) functionType：功能码
	 */
	public static byte []comBineBasicAndSecond(byte basicSenType,byte functionType,byte[] contents) throws Exception{
		byte []content = contents;
		// 版本号、推送类型
		byte[] sendType = new byte[2];
        sendType[0] = VERSION;
        sendType[1] = (byte) (basicSenType!= BasicSendType.SINGLECAST?0x20:0x21);
        
        // 功能码
        byte[] function = new byte[]{(byte) (basicSenType|0x00),functionType};
        // 地址
        byte[] address = new byte[4];
        
        // 合并数据协议
        byte[] basicData;
		basicData = BasicProtocol.combineData(function, address, content);
		// 合并二级协议
		byte[] session = new byte[2];
        byte[] allData = SecondProtocol.combineData(sendType, session, basicData);
        return allData;  
	}

    

    

	

	

	

	


	
}

package example.ldgd.com.tongchuandevicepolling.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	public static String getDate(String format){
		  Date date=new Date();
		  SimpleDateFormat dateformat=new SimpleDateFormat(format); 
		  String time=dateformat.format(date);
		  return time;		
	}
	
	public static byte getWeek(String week){
		byte weeks = 0;
		switch (week) {
		case "星期一":
			weeks = 1;
			break;
		case "星期二":
			weeks = 2;
			break;
		case "星期三":
			weeks = 3;
			break;
		case "星期四":
			weeks = 4;
			break;
		case "星期五":
			weeks =5;
			break;
		case "星期六":
			weeks = 6;
			break;
		case "星期日":
			weeks = 7 ;
			break;
		default:
			break;
		}
		return weeks;
		
	}

	/**
	 * @author CJK
	 * @category 解析yy-MM-dd HH:mm:ss EEEE 
	 */
	public static byte[] getByteDate(String time) {
		// TODO Auto-generated method stub
		String []sTime = time.split(" ");
		String []yymmdd = sTime[0].split("-");
		byte year = Byte.valueOf(yymmdd[0]);
		byte month = Byte.valueOf(yymmdd[1]);
		byte day = Byte.valueOf(yymmdd[2]);			
		byte week = Byte.valueOf(getWeek(sTime[2]));
		String [] hhmmss = sTime[1].split(":");
		byte hour = Byte.valueOf(hhmmss[0]);
		byte minute = Byte.valueOf(hhmmss[1]);
		byte second = Byte.valueOf(hhmmss[2]);
		return new byte[]{year,month,day,hour,minute,second,week};
	}

}

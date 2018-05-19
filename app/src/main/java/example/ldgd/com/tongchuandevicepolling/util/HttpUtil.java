package example.ldgd.com.tongchuandevicepolling.util;

import java.io.BufferedReader;    
import java.io.InputStreamReader;    
import java.io.OutputStreamWriter;    
import java.net.HttpURLConnection;    
import java.net.URL;  
import java.util.HashMap;    
import java.util.Map.Entry;  
  
import org.htmlparser.Parser;  
import org.htmlparser.beans.StringBean;  
import org.htmlparser.util.ParserException;    
    

public class HttpUtil {


		public String http(String url, HashMap<String, String> params)
				throws ParserException {
			URL u = null;
			HttpURLConnection con = null;
			// 构建请求参数
			StringBuffer sb = new StringBuffer();
			if (params != null) {
				for (Entry<String, String> e : params.entrySet()) {
					sb.append(e.getKey());
					sb.append("=");
					sb.append(e.getValue());
					sb.append("&");
				}
				// sb.substring(0, sb.length() - 1);
				sb = sb.deleteCharAt(sb.length() - 1);
			}
			// System.out.println("send_url:" + url);
			// System.out.println("send_data:" + sb.toString());
			// 尝试发送请求
			try {
				u = new URL(url);
				con = (HttpURLConnection) u.openConnection();
				// // POST 只能为大写，严格限制，post会不识别
				con.setRequestMethod("POST");
				con.setDoOutput(true);
				con.setDoInput(true);
				con.setUseCaches(false);
				con.setRequestProperty("Content-Type",
						"application/x-www-form-urlencoded");
				OutputStreamWriter osw = new OutputStreamWriter(
						con.getOutputStream(), "UTF-8");
				osw.write(sb.toString());
				osw.flush();
				osw.close();
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (con != null) {
					con.disconnect();
				}
			}

			// 读取返回内容
			StringBuffer buffer = new StringBuffer();
			try {
				// 一定要有返回值，否则无法把请求发送给server端。
				BufferedReader br = new BufferedReader(new InputStreamReader(
						con.getInputStream(), "UTF-8"));
				String temp;
				while ((temp = br.readLine()) != null) {
					buffer.append(temp);
					buffer.append("\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// 用parser解析输出内容
			StringBean sBean = new StringBean();
			sBean.setLinks(false);// 设置不需要得到页面所包含的链接信息
			sBean.setReplaceNonBreakingSpaces(true);// 设置将不间断空格由正规空格所替代
			sBean.setCollapse(true);// 设置将一序列空格由一个单一空格所代替
			Parser parser = new Parser();
			parser.setInputHTML(buffer.toString());
			parser.reset();
			parser.visitAllNodesWith(sBean);
			String text = sBean.getStrings();
			return text;
		}

	/*	public static void main(String[] args) throws ParserException {
			HashMap<String, String> parames = new HashMap<String, String>();
			parames.put("usrname", "my");
			parames.put("password", "123");
			HttpUtil hu = new HttpUtil();
			String ret = hu.http("http://mail.163.com/", parames);
			System.out.println(ret);
		}*/

}

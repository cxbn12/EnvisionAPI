package com.envision.api;

import java.io.BufferedReader;
//import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *  Post Method
 */
public class HttpPostUrl {

	/**
	 * send post request to URL
	 * @param url
	 * @param paramMap
	 * @return response result
	 */
	public static String sendPost(String url, Map<String, String> paramMap) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// Open connection with URL
			URLConnection conn = realUrl.openConnection();
			// Set up general property for Request
			conn.setRequestProperty("accept", " */*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
			conn.setRequestProperty("Charset", "UTF-8");
			// send post request need set below
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// get output Stream of URLConnection object 
			out = new PrintWriter(conn.getOutputStream());

			// set up request property
			String param = "";
			if (paramMap != null && paramMap.size() > 0) {
				Iterator<String> ite = paramMap.keySet().iterator();
				while (ite.hasNext()) {
					String key = ite.next();// key
					String value = paramMap.get(key);
					param += key + "=" + value + "&";
				}
				param = param.substring(0, param.length() - 1);
			}

			// send request parameter
			out.print(param);
			// flush output stream
			out.flush();
			
			//we write data to local file
			//FileOutputStream fOutputStream = new FileOutputStream("C:\\Users\\Sunseap\\Documents\\data.xml");
			//OutputStreamWriter iWriter = new OutputStreamWriter(fOutputStream);
			
			// define BufferedReader input stream and read URL's response
			in = new BufferedReader(
					new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.err.println(" POST reqeust got error" + e);
			e.printStackTrace();
		}
		// using finally module to close output/input stream
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}
	
    /**
     * Data stream post request
     * @param urlStr
     * @param strInfo
     
	public static String doPost(String urlStr, String strInfo) {
		String reStr="";
		try {
			URL url = new URL(urlStr);
			URLConnection con = url.openConnection();
			con.setDoOutput(true);
			con.setRequestProperty("Pragma:", "no-cache");
			con.setRequestProperty("Cache-Control", "no-cache");
			con.setRequestProperty("Content-Type", "application/json");
			OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());
			out.write(new String(strInfo.getBytes("utf-8")));
			out.flush();
			out.close();
			BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
			String line = "";
			for (line = br.readLine(); line != null; line = br.readLine()) {
				reStr += line;
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return reStr;
	}
	*/

	/**
	 * test main method
	 * @param args
	 */
	public static void main(String[] args) {
		/**Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("type", "211");
		mapParam.put("mdmids","1c17e79fed006000");
		mapParam.put("token","SID_W_1534229156033_bcb1ad4b-3ae1-43ab-87e4-0ae181fa1bde");
		mapParam.put("points","GMT.ActivePW");
		String pathUrl = "https://eos.envisioncn.com/solar-api/domainService/getmdmidspoints";
		String result = sendPost(pathUrl, mapParam);
		System.out.println(result);
		**/
		Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("mdmids","1c17e79fed006000");
		mapParam.put("token","SID_W_1534233678866_bdafdba6-4a0c-41e5-96dd-c6251197d473");
		mapParam.put("points", "GMT.ActivePW");
		mapParam.put("fields", "value");
		//mapParam.put("fields","name");
		String pathUrl = "https://eos.envisioncn.com/solar-api/domainService/getmdmidspoints";
		String result = sendPost(pathUrl, mapParam);
		System.out.println(result);
		
		/**Map<String, String> mapParam = new HashMap<String, String>();
		mapParam.put("mdmids","1c17e79fed006000");
		mapParam.put("token","SID_W_1534233678866_bdafdba6-4a0c-41e5-96dd-c6251197d473");
		mapParam.put("metrics", "");//metrics 不支持  https://eos.envisioncn.com/solar-api/domainService/getPointDetailCurrentDay  不支持point，代码不支持中文
		mapParam.put("begin_time","2018-08-12 6:00:00");
		mapParam.put("end_time","2018-08-13 6:00:00");
		mapParam.put("time_group","D");
		String pathUrl = "https://eos.envisioncn.com/solar-api/metricService/multiMetrics";
		String result = sendPost(pathUrl, mapParam);
		System.out.println(result);
		*/
	}

}
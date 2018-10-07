package com.envision.api;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class ConnectionFactory {

	//private double API_VERSION = 1.0;
	private String API = "";
	
	private String METHOD ="POST";
	private String TYPE = "*/*";
	private String USER_AGENT = "Mozilla/5.0";
	private String data = "";
	private URL connection;
	private HttpURLConnection finalConnection;
	
	private HashMap<String,String> fields = new HashMap<String,String>();
	
	//construct  string array[]
	public ConnectionFactory(String[] endpoint,String url){
		 //url包括整个api
		this.API = url;													
		for(int i = 0; i<endpoint.length;i++){
			String[] points = endpoint[i].split(",");
			for(int f = 0; f<points.length;f++){
				 // fields[0] 是key  fields[1] 是value
				fields.put(points[f].split("=")[0], points[f].split("=")[1]);  
			}
		}
	}
	
	// concat 合并url 和后面的key value 参数成一个url字符串
	public String buildConnection(){
		StringBuilder content = new StringBuilder();
		if(!this.getEndpoints().equalsIgnoreCase("") && !this.getEndpoints().isEmpty()){
			String vars = "";
			String vals = "";
			try {
				for(Map.Entry<String,String> entry: fields.entrySet()){
					vars = entry.getKey();
					vals = entry.getValue();
					data +=("&"+vars+"="+vals); 
				}
				if(data.startsWith("&")){ 
					data = data.replaceFirst("&", "");  
						}
				connection = new URL(API);
				BufferedReader reader = new BufferedReader(new InputStreamReader(readWithAccess(connection,data)));
				String line;
				while((line = reader.readLine()) != null){
					content.append(line + "\n");
				}
				reader.close();
				return content.toString();
			}catch(Exception e){
				System.err.println(e.getMessage());
			}
		}else {
			return null;
		}
		return null;
	}
	
	//readWithAccess() 方法处理字节流
	private InputStream readWithAccess(URL url,String data){
		try{
			byte[] out = data.toString().getBytes();
			finalConnection = (HttpURLConnection)url.openConnection();
			finalConnection.setRequestMethod(METHOD);
			finalConnection.setDoOutput(true);
			finalConnection.addRequestProperty("User-Agent", USER_AGENT);
			finalConnection.addRequestProperty("accept",TYPE);
			finalConnection.connect();
			try{
				java.io.OutputStream os = finalConnection.getOutputStream();
				os.write(out);
			} catch(Exception e) {
				System.err.println(e.getMessage());
			}
			return finalConnection.getInputStream();
		}catch(Exception e){
			System.err.println(e.getMessage());
			return null;
		}
	}
	
	
	
    // method to call
	public String getEndpoints() {
		return fields.toString();
	}
	public String getEndpointValue(String key){
		return fields.get(key);
	}
	public void setUserAgent(String userAgent){
		this.USER_AGENT = userAgent;
	}
	public void setMethod(String method){
		this.METHOD =method;
	}
	public void setSubmissionType(String type){
		this.TYPE = type;
	}

}

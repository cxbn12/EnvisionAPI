package com.envision.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;

import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;

public class Example {

	  public static void main(String[] args){

		    String baseRestURL = "https://eos.envisioncn.com";
			String APIPath = "/solar-api/loginService/login";
			String completeRestURL = baseRestURL + APIPath;
			String username = "xiaobing.cai"; 
			String password = "E4FlameH#";
			
			//Create HTTPClient - Used to make Request to API
			HttpClient httpClient = null;
			CookieStore httpCookieStore = new BasicCookieStore();
			HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
			httpClient = builder.build();
			
			try {
			HttpPost httpRequest = new HttpPost(completeRestURL);
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "*/*");
			StringEntity body =new StringEntity("{\"username\": \""+username+"\",\"password\": \""+password+"\"}");
			httpRequest.setEntity(body);
			 
			HttpResponse response = httpClient.execute(httpRequest);
			
		    String setCookie = response.getLastHeader("Set-Cookie").getValue();  
			String global_id = setCookie.substring("global_id=".length(), setCookie.indexOf(";"));  
			
			  String url = "https://eos.envisioncn.com/solar-api/mdmService/getSupportedPoints";
			  String[] fields = {
					  "type:211",
					  "token:" + global_id
			  };

			  ConnectionFactory connection = new ConnectionFactory(fields,url);
			  
			    String response1 = connection.buildConnection();
			    System.out.println(response1);
			    
			    JSONObject json =JSONObject.fromObject(response1.toString());
				System.out.println(json.toString());
				
				//export file
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    Date date = new Date(); 
				String fileName = "Envision" + "_" + df.format(date);
				
				String output = json.toString();
				System.out.println(output);
				Example Envision = new Example(); 
				Envision.saveDataToFile(fileName,output);
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		  
		    /**
		     * 
		     * System.out.println(connection);	
		     *  System.out.println(connection.getEndpointValue("type"));
		     * System.out.println(connection.getEndpointValue("token"));
		     *
		    */
		  
		    /**String response1 = connection.buildConnection();
		    System.out.println(response1);
		    
		    JSONObject json =JSONObject.fromObject(response1.toString());
			System.out.println(json.toString());
			
			//export file
		    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		    Date date = new Date(); 
			String fileName = "Envision" + "_" + df.format(date);
			
			String output = json.toString();
			Example Envision = new Example(); 
			Envision.saveDataToFile(fileName,output); */
			
			/**
			 * get specific object In Json
			 *JSONObject obj = (JSONObject) json.get("对象");
			 *System.out.println(obj.toString());
			 *get value of specific  attribute 
			 *String val = obj.getString("属性");
			 *System.out.println(val);
			 *Java传入用户名和密码并自动提交表单实现登录到其他系统的实例代码
			 *https://blog.csdn.net/maguanghui_2012/article/details/54909004
			 *https://blog.csdn.net/bobo89455100/article/details/53729565		
			 *https://blog.csdn.net/Hi_Boy_/article/details/80598363
			 *jsonString=JsonFormatTool.formatJson(json);
			*/

	  }
	  

		
		//Creates an AuthToken  
		public static String createAuthToken(String baseRestURL, HttpClient httpClient, String username, String password){
			
			String APIPath = "/solar-api/loginService/login";
			String completeRestURL = baseRestURL + APIPath;
			System.out.println("REST API URL: " + completeRestURL);
		    
			// Define the server endpoint to send the HTTP request
			
			try {
				//URL serverUrl;
				// URL serverUrl = new URL(completeRestURL);
				HttpPost httpRequest = new HttpPost(completeRestURL);
				httpRequest.setHeader("Content-Type", "application/json");
				httpRequest.setHeader("Accept", "*/*");
				StringEntity body =new StringEntity("{\"username\": \""+username+"\",\"password\": \""+password+"\"}");
				httpRequest.setEntity(body);
				 
				HttpResponse response = httpClient.execute(httpRequest);
				
			    String setCookie = response.getLastHeader("Set-Cookie").getValue();
				String global_id = setCookie.substring("global_id=".length(), setCookie.indexOf(";"));  
				System.out.println("global_id:" + global_id);  
				
				
				/**Header[] headers = (Header[]) response.getAllHeaders();
				for (int i = 0; i < headers.length; i++){
					Header header = headers[i];
					//System.out.println(header.getName() + " : " + header.getValue());
					if (header.getName().equalsIgnoreCase("global_id")){
						return header.getValue();
					}
				}
				
				return null; 
				*/
				
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null; 
		} 

	 
	//write to local file
	  private void saveDataToFile(String fileName,String data) {
		  
			BufferedWriter writer = null;
	        File file = new File("C:\\Users\\Sunseap\\EnvisionData\\"+ fileName + ".json");
	        //if file does not exist create one
	        if(!file.exists()){
	            try {
	                file.createNewFile();
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	        try {
	            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,false), "UTF-8"));
	            writer.write(data);
	        } catch (IOException e) {
	            e.printStackTrace();
	        }finally {
	            try {
	                if(writer != null){
	                    writer.close();
	                }
	            } catch (IOException e) {
	                e.printStackTrace();
	            }
	        }
	       System.out.println("\n File output successful！");
	    }
	
}

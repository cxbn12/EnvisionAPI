package com.envision.api;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;

//import net.sf.json.JSONArray;
//import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSON;

import java.sql.*;


public class Example {
	
	  public static void main(String[] args) throws ClassNotFoundException, SQLException{
		    
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
			
				String url_api = "https://eos.envisioncn.com/solar-api/metricService/multiMetrics";

				//get the date before current date
				Date dNow = new Date();  
				Date dBefore = new Date();

				Calendar calendar = Calendar.getInstance(); 
				calendar.setTime(dNow);
				calendar.add(Calendar.DAY_OF_MONTH, -1); 
				dBefore = calendar.getTime();

				SimpleDateFormat system_df = new SimpleDateFormat("yyyy-MM-dd");
				//print the date before today
				System.out.println("Patching date:"+ system_df.format(dBefore));
				
				String[] fields = {
						"mdmids=1c17e79fed006000",
						"token=" + global_id,
						"metrics=GMT.APProduction",		
						"begin_time="+system_df.format(dBefore)+" 00:00:00",
						"end_time="+system_df.format(dBefore)+" 00:00:00",
						"time_group=D"
				};
				
				
				ConnectionFactory connection = new ConnectionFactory(fields,url_api);
			  
			    String response1 = connection.buildConnection();   
			    //System.out.println(response1);
			    
			    
			   //JSONObject jsonObject =JSONObject.fromObject(response1);
			   com.alibaba.fastjson.JSONObject jsonObject=JSON.parseObject(response1);
			   //System.out.println(jsonObject.getString("metrics").substring(2,20));
			   
			   
			    String energy1 = jsonObject.getString("metrics").substring(22,32);
			   	String date1 = jsonObject.getString("metrics").substring(75,85);
			   	
			    //extract energy data
			   	System.out.println("Energy:"+energy1+" Date:"+date1);
			   	
			   /**
			   //System.out.println(jsonObject.getString("metrics").substring(88,106));
			   String energy2 = jsonObject.getString("metrics").substring(107,119);
			   String date2 = jsonObject.getString("metrics").substring(159,171);
			   String energy3 = jsonObject.getString("metrics").substring(193,205);
			   String date3 = jsonObject.getString("metrics").substring(245,257);
   			   **/
			   	
				//connect mysql database driver and link to database
	            Class.forName("com.mysql.jdbc.Driver");
	            
	            
	            String url="jdbc:mysql://192.168.1.235:3306/monitoring";      
	            //String url="jdbc:mysql://158.140.140.168:3306/monitoring";  
	            Connection conn = DriverManager.getConnection(url,"root","E4FlameH");
	            Statement stmt = conn.createStatement(); 
	            String sql_select = "select max(record_id) from monitoring.energy_patched";
	            ResultSet rs = stmt.executeQuery(sql_select);
	            
	            while(rs.next()) {
	            	int record_id = rs.getInt(1)+1;
	            	System.out.println("Record_id:" + record_id);
	            	
	            	//prepared statement for sql statement
		            String sql = "insert into monitoring.energy_patched values(?,?,?,?)";
		         
		            PreparedStatement pst = conn.prepareStatement(sql,com.mysql.jdbc.Statement.RETURN_GENERATED_KEYS);
		            pst.setInt(1,record_id);
		            pst.setString(2,date1);
		            pst.setInt(3, 988);
		            pst.setString(4,energy1);
		            pst.executeUpdate();
		            System.out.println(pst);
	            }

	            stmt.close();
	            conn.close();
				
				
				//export file
			    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
			    Date simple_date = new Date(); 
				String fileName = "Envision" + "_" + df.format(simple_date);
				
				String output = jsonObject.toString();
				Example Envision = new Example(); 
				Envision.saveDataToFile(fileName,output);
				
			
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

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
	        // write to File
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
	     //  System.out.println("\n File output successful！");
	    }
	  
}

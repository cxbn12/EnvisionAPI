package com.envision.api;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ThreadLocalRandom;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.concurrent.ThreadLocalRandom;


import org.apache.commons.ssl.Base64;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import com.microstrategy.web.objects.WebIServerSession;
import com.microstrategy.web.objects.WebObjectsException;
import com.microstrategy.web.objects.WebObjectsFactory;


public class REST_Create_Locate_UpdateCubeDynamically {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		//CONFIG PARAMETERS:
		String baseRestURL = "https://env-73627.customer.cloud.microstrategy.com/MicroStrategyLibrary";
		String cubeName = "MyCube";
		String username = "steve";
		String password = "";
		String projectID = "B7CA92F04B9FAE8D941C3E9B7E0CD754"; 
		String updatePolicy = "add";
		
		//Create HTTPClient
		HttpClient httpClient = null;
		CookieStore httpCookieStore = new BasicCookieStore();
		HttpClientBuilder builder = HttpClientBuilder.create().setDefaultCookieStore(httpCookieStore);
		httpClient = builder.build();
		
		
		//Create Session
		String authToken = createAuthToken(baseRestURL, httpClient, username, password);
		System.out.println("Auth Token: " + authToken);
		
		if(authToken == null){
			System.out.println("Error: Unable to generate authToken");
			return;
		}
		
		//The below sample will generate data that creates an Attribute of ID, and a metric of Price
		
		//Create row(s) of data for cube:
		int id_val = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
		int price_val = ThreadLocalRandom.current().nextInt(10000, 99999 + 1);
		String JSONString = "[{\"ID\":"+id_val+",\"Price\":"+price_val+"}]";
		String base64EncodedData = encodeJSON(JSONString);
		
		//The table will define 2 columns called ID and Price of type 'double'. We will then map the ID column to an attribute and the price column to a metric in the table structure JSON. 
		
		//define create table structure
		String tableStructureCreate = "{\"name\":\""+cubeName+"\",\"tables\":[{\"data\":\""+base64EncodedData+"\",\"name\":\"Table\",\"columnHeaders\":[{\"name\":\"ID\",\"dataType\":\"DOUBLE\"},{\"name\":\"Price\",\"dataType\":\"DOUBLE\"}]}],\"metrics\":[{\"name\":\"Price\",\"dataType\":\"number\",\"expressions\":[{\"formula\":\"Table.Price\"}]}],\"attributes\":[{\"name\":\"ID\",\"attributeForms\":[{\"category\":\"ID\",\"expressions\":[{\"formula\":\"Table.ID\"}],\"dataType\":\"double\"}]}]}";
		
		//define update table structure 
		String tableStructureUpdate = "{\"name\":\"Table\",\"columnHeaders\":[{\"name\":\"ID\",\"dataType\":\"DOUBLE\"},{\"name\":\"Price\",\"dataType\":\"DOUBLE\"}],\"data\":\""+base64EncodedData+"\"}";
		
		//Create or update cube
		createOrUpdateCube(baseRestURL, authToken, httpClient, projectID, updatePolicy, cubeName, tableStructureCreate, tableStructureUpdate);
	}
	
	//Creates an AuthToken
	public static String createAuthToken(String baseRestURL, HttpClient httpClient, String username, String password){
		String APIPath = "/api/auth/login";
		String completeRestURL = baseRestURL + APIPath;
		System.out.println(completeRestURL);
	    
		// Define the server endpoint to send the HTTP request to
	    URL serverUrl;
		try {
			serverUrl = new URL(completeRestURL);
		
			HttpPost httpRequest = new HttpPost(completeRestURL);
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "application/json");
			StringEntity body =new StringEntity("{\"username\": \""+username+"\",\"password\": \""+password+"\",\"loginMode\": 1,\"applicationType\": 35}");
			httpRequest.setEntity(body);
			 
			HttpResponse response = httpClient.execute(httpRequest);
			
			Header[] headers = (Header[]) response.getAllHeaders();
			for (int i = 0; i < headers.length; i++){
				Header header = headers[i];
				//System.out.println(header.getName() + " : " + header.getValue());
				if (header.getName().equalsIgnoreCase("X-MSTR-AuthToken")){
					return header.getValue();
				}
			}
			
			return null;
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	
	//Performs a metadata search to pull items back from the MyReports folder
	public static String searchPersonalFolderForMyReports(String baseRestURL, String authToken, HttpClient httpClient, String projectID){
		String APIPath = "/api/folders/myPersonalObjects";
		String completeRestURL = baseRestURL + APIPath;
		System.out.println(completeRestURL);
	    
		

		try {
			HttpGet httpRequest = new HttpGet(completeRestURL);
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setHeader("X-MSTR-AuthToken", authToken);
			httpRequest.setHeader("X-MSTR-ProjectID", projectID);
		
			HttpResponse response = httpClient.execute(httpRequest);
						
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseString);
			JSONArray jsonArray = new JSONArray(responseString);
			
		
			//Locate my reports ID
			for (int i = 0; i < jsonArray.length(); i++){		
				JSONObject obj =  (JSONObject) jsonArray.get(i);
				if(obj.get("name").toString().equalsIgnoreCase("My Reports")){
					return obj.getString("id");
				}
				
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	//Returns a list of objects contained in the MyReports folder
	public static String searchMyReportsForCube(String baseRestURL, String authToken, HttpClient httpClient, String projectID,String myReportsID, String cubeName){
		String APIPath = "/api/folders/" + myReportsID;
		String completeRestURL = baseRestURL + APIPath;
		System.out.println(completeRestURL);
	    
		try {
			HttpGet httpRequest = new HttpGet(completeRestURL);
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setHeader("X-MSTR-AuthToken", authToken);
			httpRequest.setHeader("X-MSTR-ProjectID", projectID);
		
			HttpResponse response = httpClient.execute(httpRequest);
						
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseString);
			JSONArray jsonArray = new JSONArray(responseString);
			
		
			//Locate my reports ID
			for (int i = 0; i < jsonArray.length(); i++){		
				JSONObject obj =  (JSONObject) jsonArray.get(i);
				if(obj.get("name").toString().equalsIgnoreCase(cubeName) && obj.getInt("type")==3){
					return obj.getString("id");
				}
				
			}
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;
	}
	
	public static String createOrUpdateCube(String baseRestURL, String authToken, HttpClient httpClient, String projectID, String updatePolicy, String cubeName, String tableStructureCreate, String tableStructureUpdate){
		//First action is to see if the cube exists in the metadata within the My Reports folder:
		//Search Personal folder
		String myReportsID = searchPersonalFolderForMyReports(baseRestURL, authToken, httpClient, projectID);
		System.out.println("MyReportsFolder ID: " + myReportsID);
		
		//If the ID is located, next we query for the ID of the cube in this folder:
		//Search for cube in my reports
		String cubeID = searchMyReportsForCube(baseRestURL, authToken, httpClient, projectID, myReportsID, cubeName);
		System.out.println("CubeID: " + cubeID);
		
		//If the cube is null, then the cube should be created:
		if(cubeID == null){
			System.out.println("Unable to locate Cube named " + cubeName);
			
			//Create cube:
			String responseString = createCubeWithInitialData(baseRestURL, authToken, httpClient, projectID, tableStructureCreate);
			return responseString;
		}
		else{
			System.out.println("Cube with name " + cubeName + " exists, append data to cube" );
		
			
			//Update cube:
			updateCubeWithAdditionalData(baseRestURL, authToken, httpClient, projectID, tableStructureUpdate, cubeID, updatePolicy);
		}
	
		return null;
	}
	
	//Performs a POST Request to initially write data to a cube with the variables provided
	public static String createCubeWithInitialData(String baseRestURL, String authToken, HttpClient httpClient, String projectID, String tableStructure){
		String APIPath = "/api/datasets";
		String completeRestURL = baseRestURL + APIPath;
		System.out.println("REST API URL: "  + completeRestURL);
		
		try {
			HttpPost httpRequest = new HttpPost(completeRestURL);
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setHeader("X-MSTR-AuthToken", authToken);
			httpRequest.setHeader("X-MSTR-ProjectID", projectID);
			//httpRequest.setHeader("Set-Cookie", JSESSIONID);
			httpRequest.setHeader("updatePolicy", "Add");
			StringEntity body;
		
			body = new StringEntity(tableStructure);
			httpRequest.setEntity(body);
			HttpResponse response = httpClient.execute(httpRequest);
			
			
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			System.out.println(responseString);
			
			return responseString;
			
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	
	//Performs a POST Request to update  data to a cube with the variables provided
	public static String updateCubeWithAdditionalData(String baseRestURL, String authToken, HttpClient httpClient, String projectID, String tableStructure, String datasetID, String updatePolicy){
		System.out.println("PatchCubeWithAdditionalData");
		System.out.println("-------");
		System.out.println("TableStructure: " + tableStructure);
		System.out.println("CubeID: " + datasetID);
		System.out.println("UpdatePolicy: " + updatePolicy);
		
	    String APIPath = "/api/datasets/"+datasetID+"/tables/Table";
		String completeRestURL = baseRestURL + APIPath;
		System.out.println(completeRestURL);
		
		System.out.println("-------");
	    
		try {
			HttpPatch httpRequest = new HttpPatch(completeRestURL);
			httpRequest.setHeader("Content-Type", "application/json");
			httpRequest.setHeader("Accept", "application/json");
			httpRequest.setHeader("X-MSTR-AuthToken", authToken);
			httpRequest.setHeader("updatePolicy", updatePolicy);
			
			httpRequest.setHeader("X-MSTR-ProjectID", projectID);
			StringEntity body;
		
			body = new StringEntity(tableStructure);
			httpRequest.setEntity(body);
			HttpResponse response = httpClient.execute(httpRequest);
			
			
			HttpEntity entity = response.getEntity();
			String responseString = EntityUtils.toString(entity, "UTF-8");
			System.out.println("STATUS: " + response.getStatusLine().getStatusCode());
			System.out.println("PATCH RESPONSE: " + responseString);
			
			
			return responseString;
			
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static String encodeJSON(String JSONString){
		
		// encode data on your side using BASE64
		byte[]   bytesEncoded = Base64.encodeBase64(JSONString.getBytes());
		//System.out.println("ecncoded value is " + new String(bytesEncoded ));
		String encoded = new String(bytesEncoded); //base64

		return encoded;
	}
	
}
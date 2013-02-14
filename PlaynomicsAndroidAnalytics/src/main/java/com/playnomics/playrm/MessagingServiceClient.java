package com.playnomics.playrm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Date;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

public class MessagingServiceClient {
	
	private final String baseUrl;
	private final long appId;
	private final String userId;
	private final String cookieId;
	
	public MessagingServiceClient (String baseUrl, long appId, String userId, 
			String cookieId){
		this.baseUrl = baseUrl;
		this.appId = appId;
		this.userId = userId;
		this.cookieId = cookieId;
	}
	
	public JSONObject requestAd(String frameId, String caller, 
			int width, int height) {
		
		JSONObject jObj = null;
		try {
			Date date=new Date(0);
			Long time = date.getTime();
			
		
			String url = baseUrl + "?a="+ this.appId + 
					"&u=" + this.userId + 
					"&p=" + caller +
					"&t=" + time +
					"&b=" + this.cookieId +
					"&f=" + frameId +
					"&c=" + height + 
					"&d=" + width + 
					"&esrc=aj&ever=1";
											
			// defaultHttpClient
		    DefaultHttpClient httpClient = new DefaultHttpClient();
		    HttpPost httpPost = new HttpPost(url);
		    HttpResponse httpResponse = httpClient.execute(httpPost);
		    HttpEntity httpEntity = httpResponse.getEntity();
		    InputStream is = httpEntity.getContent();  
		    
		    //FIXME: byte buffer(java) and check to see if activity is killed
		    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
		
		    StringBuilder sb = new StringBuilder();
		    String line = null;
		    while ((line = reader.readLine()) != null) {
		        sb.append(line + "\n");
		    }
		    is.close();		    
			jObj = new JSONObject(sb.toString());
			
		} catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
		} catch (ClientProtocolException e) {
		    e.printStackTrace();
		} catch (IOException e) {
		    e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return jObj;
	}
}

package com.playnomics.client;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.TreeMap;

public class AssetClient {
	
	public enum ResponseStatus{
		SUCCESS,
		FAILURE
	}
	
	public class AssetResponse{
		
		private ResponseStatus status;
		public ResponseStatus getStatus(){
			return status;
		}
		
		private String requestUrl;
		public String getRequestUrl(){
			return requestUrl;
		}
		
		private Exception exception;
		public Exception getException(){
			return exception;
		}
		
		private byte[] data;
		public byte[] getData(){
			return data;
		}
		
		public AssetResponse(String requestUrl, Exception exception){
			this.status = ResponseStatus.FAILURE;
			this.requestUrl = requestUrl;
		}
		
		public AssetResponse(String requestUrl, byte[] data){
			this.status = ResponseStatus.SUCCESS;
			this.requestUrl = requestUrl;
			this.data = data;
		}
		
		public AssetResponse(String requestUrl, ResponseStatus status){
			this.status = status;
			this.requestUrl = requestUrl;
		}
	}
	
	protected IHttpConnectionFactory connectionFactory;
	
	public AssetClient(IHttpConnectionFactory connectionFactory){
		this.connectionFactory = connectionFactory;
	}
	
	public AssetResponse requestAsset(String baseUrl, String path, TreeMap<String, Object> queryParameters){
		String assetUrl = this.connectionFactory.buildUrl(baseUrl, path, queryParameters);
		return requestAsset(assetUrl);
	}
	
	public AssetResponse requestAsset(String url){
		HttpURLConnection connection = null;
		BufferedInputStream bufferedIn = null; 
		AssetResponse response;
		try {
			connection = connectionFactory.startConnectionForUrl(url);
			if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
				response = new AssetResponse(url, ResponseStatus.FAILURE);
			} else {
				InputStream inputStream = connection.getInputStream();			
				int bufferSize = 1024 * 4;// 4KB
				
				bufferedIn = new BufferedInputStream(inputStream, bufferSize);
				ByteArrayOutputStream bufferedOut = new ByteArrayOutputStream();
				int read;
				while((read = bufferedIn.read()) != -1){
					bufferedOut.write(read);
				}
				bufferedIn.close();
				
				byte[] data = bufferedOut.toByteArray();
				response = new AssetResponse(url, data);
			}
		} catch (IOException e) {
			response = new AssetResponse(url, e);
		} finally {
			if(connection != null){
				connection.disconnect();
			}
		}
		return response;
	}
}

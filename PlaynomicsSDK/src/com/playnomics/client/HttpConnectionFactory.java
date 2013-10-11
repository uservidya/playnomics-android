package com.playnomics.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;

public class HttpConnectionFactory implements IHttpConnectionFactory {
	Logger logger;
	public HttpConnectionFactory(Logger logger){
		this.logger = logger;
	}
	
	public HttpURLConnection startConnectionForUrl(String urlString) 
			throws IOException {
		try{
			URL url = new URL(urlString);
			return ((HttpURLConnection) url.openConnection());
		} catch(MalformedURLException ex){
			logger.log(LogLevel.WARNING, ex, "Could not generate a valid URL from this String %s", urlString);
			return null;
		}		
	}
}

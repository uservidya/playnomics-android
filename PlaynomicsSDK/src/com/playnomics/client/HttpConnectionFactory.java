package com.playnomics.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TreeMap;

import com.playnomics.util.Logger;
import com.playnomics.util.Util;
import com.playnomics.util.Logger.LogLevel;

public class HttpConnectionFactory implements IHttpConnectionFactory, IUrlBuilder {
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

	public String buildUrl(String url, String path,
			TreeMap<String, Object> queryParameters)
			throws UnsupportedEncodingException {
		if(Util.stringIsNullOrEmpty(url)){
			return null;
		}
		
		StringBuilder builder = new StringBuilder(url);
		
		if(!Util.stringIsNullOrEmpty(path)){
			builder.append(url.endsWith("/") ? path : String.format("/%s", path));
		}
		
		if(queryParameters != null){
			boolean hasQueryString = builder.toString().contains("?");
			boolean firstParam = true;
			
			for(String key : queryParameters.keySet()){
				if(Util.stringIsNullOrEmpty(key)){
					continue;
				}
				
				Object value = queryParameters.get(key);
				if(value == null){
					continue;
				}
				
				builder.append((!hasQueryString && firstParam) 
						? String.format("?%s=%s", key, URLEncoder.encode(value.toString(), Util.UT8_ENCODING))
						: String.format("&%s=%s", key, URLEncoder.encode(value.toString(), Util.UT8_ENCODING)));
				firstParam = false;
			}
		}
		return builder.toString();
	}
	
	
}

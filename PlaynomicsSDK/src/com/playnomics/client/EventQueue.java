package com.playnomics.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.playnomics.events.PlaynomicsEvent;
import com.playnomics.util.Util;

public class EventQueue{

	private String apiUrl;
	private ConcurrentLinkedQueue<String> eventUrlQueue;
	private Util util;
	
	public EventQueue(Util util, String apiUrl){
		this.util = util;
		this.apiUrl = apiUrl;
		eventUrlQueue = new ConcurrentLinkedQueue<String>();
	}
	
	public void enqueueEvent(PlaynomicsEvent event) throws UnsupportedEncodingException{
		String eventUrl = buildUrl(apiUrl, event.getUrlPath(), event.getEventParameters());
		enqueueEventUrl(eventUrl);
	}
	
	public void enqueueEventUrl(String eventUrl){
		if(!util.stringIsNullOrEmpty(eventUrl)){
			eventUrlQueue.add(eventUrl);
		}
	}
	
	public boolean isEmpty(){
		return eventUrlQueue.isEmpty();
	}
	
	public String dequeueEventUrl(){
		return eventUrlQueue.remove();
	}
	
	String buildUrl(String url, String path, Map<String, Object> queryParameters) throws UnsupportedEncodingException{
		if(util.stringIsNullOrEmpty(url)){
			return null;
		}
		
		StringBuilder builder = new StringBuilder(url);
		
		if(path != null && path.isEmpty()){
			builder.append(url.endsWith("/") ? String.format("/%s", path) : path);
		}
		
		boolean hasQueryString = builder.toString().endsWith("?");
		boolean firstParam = false;
		
		for(String key : queryParameters.keySet()){
			if(util.stringIsNullOrEmpty(key)){
				continue;
			}
			
			Object value = queryParameters.get(key);
			if(value == null){
				continue;
			}
			
			builder.append(hasQueryString && firstParam 
					? String.format("%s=%s", key, URLEncoder.encode(value.toString(), Util.UT8_ENCODING))
					: String.format("?%s=%s", key, URLEncoder.encode(value.toString(), Util.UT8_ENCODING)));
			firstParam = false;
		}
		return builder.toString();
	}
}

package com.playnomics.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.playnomics.events.PlaynomicsEvent;
import com.playnomics.util.IConfig;
import com.playnomics.util.Util;

public class EventQueue{
	private IConfig config;
	private ConcurrentLinkedQueue<String> eventUrlQueue;
	
	public EventQueue(IConfig config){
		this.config = config;
		eventUrlQueue = new ConcurrentLinkedQueue<String>();
	}
	
	public void enqueueEvent(PlaynomicsEvent event) throws UnsupportedEncodingException{
		String eventUrl = buildUrl(config.getEventsUrl(), event.getUrlPath(), event.getEventParameters());
		enqueueEventUrl(eventUrl);
	}
	
	public void enqueueEventUrl(String eventUrl){
		if(!Util.stringIsNullOrEmpty(eventUrl)){
			eventUrlQueue.add(eventUrl);
		}
	}
	
	public boolean isEmpty(){
		return eventUrlQueue.isEmpty();
	}
	
	public String dequeueEventUrl(){
		return eventUrlQueue.remove();
	}
	
	public static String buildUrl(String url, String path, Map<String, Object> queryParameters) throws UnsupportedEncodingException{
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

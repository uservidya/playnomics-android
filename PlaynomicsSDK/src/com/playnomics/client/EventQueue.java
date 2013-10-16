package com.playnomics.client;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.playnomics.events.PlaynomicsEvent;
import com.playnomics.util.IConfig;
import com.playnomics.util.Util;

public class EventQueue implements IEventQueue{
	private IConfig config;
	private IUrlBuilder builder;
	private ConcurrentLinkedQueue<String> eventUrlQueue;
	
	public EventQueue(IConfig config, IUrlBuilder builder){
		this.config = config;
		eventUrlQueue = new ConcurrentLinkedQueue<String>();
		this.builder = builder;
	}
	
	public void enqueueEvent(PlaynomicsEvent event) throws UnsupportedEncodingException{
		String eventUrl = builder.buildUrl(config.getEventsUrl(), event.getUrlPath(), event.getEventParameters());
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
}

package com.playnomics.session;

import com.playnomics.client.EventQueue;
import com.playnomics.client.EventWorker;
import com.playnomics.util.*;
import com.playnomics.client.HttpConnectionFactory;

public class Session {
	private EventWorker eventWorker;
	private EventQueue eventQueue;
	private Util util;
	private Config config; 
	private static final Object syncLock = new Object();

	//private session data
	private long applicationId;
	private String userId;
	private LargeGeneratedId sessionId;
	private LargeGeneratedId instanceId;

	private boolean testMode = false;
	public void setTestMode(boolean value){
		this.testMode = value;
	}
	
	private String overrideEventsUrl;
	public void setOverrideEventsUrl(String url){
		this.overrideEventsUrl = url;
	}
	
	public String getEventsUrl() {
		if(!util.stringIsNullOrEmpty(this.overrideEventsUrl)){
			return this.overrideEventsUrl;
		}
		if (this.testMode) {
			return config.getTestEventsUrl();
		}
		return config.getProdEventsUrl();
	}

	private String overrideMessagingUrl;
	public void setOverrideMessagingUrl(String url){
		this.overrideMessagingUrl = url;
	}
	
	public String getMessagingUrl() {
		if(!util.stringIsNullOrEmpty(this.overrideMessagingUrl)){
			return this.overrideMessagingUrl;
		}
		if (this.testMode) {
			return config.getTestMessagingUrl();
		}
		return config.getProdMessagingUrl();
	}
	
	private Session(){
		this.util = new Util();
		this.config = new Config();
		this.eventQueue = new EventQueue(util, this.getEventsUrl());
		this.eventWorker = new EventWorker(this.eventQueue, new HttpConnectionFactory());
	}
	
	private static Session instance;
	public static Session getInstance(){
		synchronized (Session.syncLock) {
			if(instance == null){
				instance = new Session();
			}
			return Session.instance;
		}
	}
	
	public void start(long applicationId, String userId){
		this.userId = userId;
		start(applicationId);
	}

	public void start(long applicationId){
		this.applicationId = applicationId;
	
		//session start code here
	}
	
	private void pause(){
		
	}
	
	private void resume(){
		
	}
	
}

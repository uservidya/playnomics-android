package com.playnomics.session;

import com.playnomics.client.EventQueue;
import com.playnomics.client.EventWorker;
import com.playnomics.util.*;
import com.playnomics.client.HttpConnectionFactory;

public class Session {

	private EventWorker eventWorker;
	private EventQueue eventQueue;
	private Util util;
	private static final Object syncLock = new Object();
	
	private String eventUrl;
	
	private String getEventUrl(){
		return eventUrl;
	}
	
	private String messagingUrl;	
	
	
	//private session data
	private long applicationId;
	private String userId;
	
	private Session(){
		this.util = new Util();
		this.eventQueue = new EventQueue(util, getEventUrl());
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
	
}

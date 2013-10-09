package com.playnomics.session;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import android.content.Context;

import com.playnomics.client.EventQueue;
import com.playnomics.client.EventWorker;
import com.playnomics.util.*;
import com.playnomics.util.Logger.LogLevel;
import com.playnomics.client.HttpConnectionFactory;
import com.playnomics.events.AppPageEvent;
import com.playnomics.events.AppRunningEvent;
import com.playnomics.events.AppStartEvent;
import com.playnomics.events.ImplicitEvent;
import com.playnomics.events.PlaynomicsEvent;
import com.playnomics.events.UserInfoEvent;

public class Session {
	
	private enum SessionState{
		NOT_STARTED,
		STARTED,
		PAUSED
	};
	
	//session
	private SessionState sessionState;
	private EventWorker eventWorker;
	private EventQueue eventQueue;
	private Util util;
	private Config config; 
	private static final Object syncLock = new Object();
	private ServiceManager serviceManager;
	private DeviceManager deviceManager;

	//session data
	private long applicationId;
	private String userId;
	private String breadcrumbId;
	private LargeGeneratedId sessionId;
	private LargeGeneratedId instanceId;

	private boolean enablePushNotifications;
	public void setEnabledPushNotifications(boolean value){
		this.enablePushNotifications = value;
	}
	
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
	
	public void start(long applicationId, String userId, Context context){
		this.userId = userId;
		start(applicationId, context);
	}

	public void start(long applicationId, Context context){
		
		try{
			this.applicationId = applicationId;
			//session start code here
			if(sessionState == SessionState.STARTED){
				return;
			}
			
			if(sessionState == SessionState.PAUSED){
				resume();
				return;
			}
			
			sessionState = SessionState.STARTED;
			
			this.serviceManager = new ServiceManager(context);
			this.deviceManager = new DeviceManager(context, this.serviceManager);
			boolean settingsChanged = this.deviceManager.synchronizeDeviceSettings();
			
			this.breadcrumbId = this.deviceManager.getAndroidDeviceId();
			
			if(util.stringIsNullOrEmpty(this.userId)){
				this.userId = this.breadcrumbId;
			}
			
			//start the background UI service
			
			//send appRunning or appPage
			LargeGeneratedId lastSessionId = deviceManager.getPreviousSessionId();
			
			EventTime lastEventTime = deviceManager.getLastEventTime();
			GregorianCalendar threeMinutesAgo = new GregorianCalendar(Util.TIME_ZONE_GMT);
			threeMinutesAgo.add(Calendar.MINUTE, -3);
			
			boolean sessionLapsed = (lastEventTime.compareTo(threeMinutesAgo) < 0) || 
										lastSessionId == null;
			
			ImplicitEvent implicitEvent;
			if(sessionLapsed){
				this.sessionId = new LargeGeneratedId(util);
				this.instanceId = this.sessionId;
				
				implicitEvent = new AppStartEvent(config, getSessionInfo(), instanceId);
			} else {
				this.sessionId = lastSessionId;
				this.instanceId = new LargeGeneratedId(util);
				implicitEvent = new AppPageEvent(config, getSessionInfo(), instanceId);
			}
			
			eventQueue.enqueueEvent(implicitEvent);
			eventWorker.start();
			
			if(settingsChanged){
				onDeviceSettingsUpdated();
			}
		} catch(Exception ex){
			Logger.log(LogLevel.ERROR, ex, "Could not start session");
			sessionState = SessionState.NOT_STARTED;
		}
	}
	
	void pause(){
		
	}
	
	void resume(){
		
	}
	
	void stop(){
		
	}
	
	void receivedTouchEvent(){
		
	}
	
	private GameSessionInfo getSessionInfo(){
		return new GameSessionInfo(this.applicationId, this.userId, this.breadcrumbId, this.sessionId);
	}
	
	private void onDeviceSettingsUpdated() throws UnsupportedEncodingException{
		if(enablePushNotifications && this.deviceManager.getPushRegistrationId() == null){
			registerForPushNotifcations();
		} else {
			UserInfoEvent event = new UserInfoEvent(this.config, getSessionInfo(), this.deviceManager.getPushRegistrationId(),
					this.deviceManager.getAndroidDeviceId());
			eventQueue.enqueueEvent(event);
		}
	}
	
	void registerForPushNotifcations(){
		
	}
}

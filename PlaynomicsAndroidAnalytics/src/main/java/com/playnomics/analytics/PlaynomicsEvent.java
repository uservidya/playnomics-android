package com.playnomics.analytics;

import java.util.Date;

public class PlaynomicsEvent {
	
	public enum EventTypes {
		appStart, appPage, appRunning, appPause, appResume, appStop
	};

	private EventTypes eventType;
	private Date eventTime;
	private String applicationId;
	private String userId;
	private String cookieId;
	private String sessionId;
	private String instanceId;
	private Date sessionStartTime;
	private int sequence;
	private int timeZoneOffset;
	private int clicks;
	private int totalClicks;
	private int keys;
	private int totalKeys;
	private int collectMode;
	
	public PlaynomicsEvent(EventTypes eventType, String applicationId, String userId, String cookieId,
		String sessionId, String instanceId, Date sessionStartTime, int sequence, int timeZoneOffset, int clicks,
		int totalClicks, int keys, int totalKeys, int collectMode) {
	
		eventTime = new Date();
		this.eventType = eventType;
		this.applicationId = applicationId;
		this.userId = userId;
		this.cookieId = cookieId;
		this.sessionId = sessionId;
		this.instanceId = instanceId;
		this.sessionStartTime = sessionStartTime;
		this.sequence = sequence;
		this.timeZoneOffset = timeZoneOffset;
		this.clicks = clicks;
		this.totalClicks = totalClicks;
		this.keys = keys;
		this.totalKeys = totalKeys;
		this.collectMode = collectMode;
	}
	
	
	public EventTypes getEventType() {
	
		return eventType;
	}

	
	public void setEventType(EventTypes eventType) {
	
		this.eventType = eventType;
	}

	
	public Date getEventTime() {
	
		return eventTime;
	}

	
	public void setEventTime(Date eventTime) {
	
		this.eventTime = eventTime;
	}

	
	public String getApplicationId() {
	
		return applicationId;
	}

	
	public void setApplicationId(String applicationId) {
	
		this.applicationId = applicationId;
	}

	
	public String getUserId() {
	
		return userId;
	}

	
	public void setUserId(String userId) {
	
		this.userId = userId;
	}

	
	public String getCookieId() {
	
		return cookieId;
	}

	
	public void setCookieId(String cookieId) {
	
		this.cookieId = cookieId;
	}

	
	public String getSessionId() {
	
		return sessionId;
	}

	
	public void setSessionId(String sessionId) {
	
		this.sessionId = sessionId;
	}

	
	public String getInstanceId() {
	
		return instanceId;
	}

	
	public void setInstanceId(String instanceId) {
	
		this.instanceId = instanceId;
	}

	
	public Date getSessionStartTime() {
	
		return sessionStartTime;
	}

	
	public void setSessionStartTime(Date sessionStartTime) {
	
		this.sessionStartTime = sessionStartTime;
	}

	
	public int getSequence() {
	
		return sequence;
	}

	
	public void setSequence(int sequence) {
	
		this.sequence = sequence;
	}

	
	public int getTimeZoneOffset() {
	
		return timeZoneOffset;
	}

	
	public void setTimeZoneOffset(int timeZoneOffset) {
	
		this.timeZoneOffset = timeZoneOffset;
	}

	
	public int getClicks() {
	
		return clicks;
	}

	
	public void setClicks(int clicks) {
	
		this.clicks = clicks;
	}

	
	public int getTotalClicks() {
	
		return totalClicks;
	}

	
	public void setTotalClicks(int totalClicks) {
	
		this.totalClicks = totalClicks;
	}

	
	public int getKeys() {
	
		return keys;
	}

	
	public void setKeys(int keys) {
	
		this.keys = keys;
	}

	
	public int getTotalKeys() {
	
		return totalKeys;
	}

	
	public void setTotalKeys(int totalKeys) {
	
		this.totalKeys = totalKeys;
	}

	
	public int getCollectMode() {
	
		return collectMode;
	}

	
	public void setCollectMode(int collectMode) {
	
		this.collectMode = collectMode;
	}

	public String toString() {
	
		return eventTime.toString() + ": " + eventType.toString();
	}
}
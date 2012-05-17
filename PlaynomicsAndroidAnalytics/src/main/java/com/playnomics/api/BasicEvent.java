package com.playnomics.api;

import java.util.Date;

import com.playnomics.api.PlaynomicsEvent;

public class BasicEvent extends PlaynomicsEvent {
	
	private String cookieId;
	private String sessionId;
	private String instanceId;
	private Date sessionStartTime;
	private Date pauseTime;
	private int sequence;
	private int timeZoneOffset;
	private int clicks;
	private int totalClicks;
	private int keys;
	private int totalKeys;
	private int collectMode;
	
	protected BasicEvent(EventType eventType, Long applicationId, String userId, String cookieId,
		String sessionId, String instanceId, Date sessionStartTime, int sequence, int clicks,
		int totalClicks, int keys, int totalKeys, int collectMode) {
	
		super(eventType, applicationId, userId);
		this.cookieId = cookieId;
		this.sessionId = sessionId;
		this.instanceId = instanceId;
		this.sessionStartTime = sessionStartTime;
		this.sequence = sequence;
		this.clicks = clicks;
		this.totalClicks = totalClicks;
		this.keys = keys;
		this.totalKeys = totalKeys;
		this.collectMode = collectMode;
	}
	
	protected BasicEvent(EventType eventType, Long applicationId, String userId, String cookieId,
		String sessionId, String instanceId, int timeZoneOffset) {
	
		super(eventType, applicationId, userId);
		this.cookieId = cookieId;
		this.sessionId = sessionId;
		this.instanceId = instanceId;
		this.timeZoneOffset = timeZoneOffset;
	}
	
	protected String getCookieId() {
	
		return cookieId;
	}
	
	protected void setCookieId(String cookieId) {
	
		this.cookieId = cookieId;
	}
	
	protected String getSessionId() {
	
		return sessionId;
	}
	
	protected void setSessionId(String sessionId) {
	
		this.sessionId = sessionId;
	}
	
	protected String getInstanceId() {
	
		return instanceId;
	}
	
	protected void setInstanceId(String instanceId) {
	
		this.instanceId = instanceId;
	}
	
	protected Date getSessionStartTime() {
	
		return sessionStartTime;
	}
	
	protected void setSessionStartTime(Date sessionStartTime) {
	
		this.sessionStartTime = sessionStartTime;
	}
	
	protected Date getPauseTime() {
	
		return pauseTime;
	}
	
	protected void setPauseTime(Date pauseTime) {
	
		this.pauseTime = pauseTime;
	}
	
	protected int getSequence() {
	
		return sequence;
	}
	
	protected void setSequence(int sequence) {
	
		this.sequence = sequence;
	}
	
	protected int getTimeZoneOffset() {
	
		return timeZoneOffset;
	}
	
	protected void setTimeZoneOffset(int timeZoneOffset) {
	
		this.timeZoneOffset = timeZoneOffset;
	}
	
	protected int getClicks() {
	
		return clicks;
	}
	
	protected void setClicks(int clicks) {
	
		this.clicks = clicks;
	}
	
	protected int getTotalClicks() {
	
		return totalClicks;
	}
	
	protected void setTotalClicks(int totalClicks) {
	
		this.totalClicks = totalClicks;
	}
	
	protected int getKeys() {
	
		return keys;
	}
	
	protected void setKeys(int keys) {
	
		this.keys = keys;
	}
	
	protected int getTotalKeys() {
	
		return totalKeys;
	}
	
	protected void setTotalKeys(int totalKeys) {
	
		this.totalKeys = totalKeys;
	}
	
	protected int getCollectMode() {
	
		return collectMode;
	}
	
	protected void setCollectMode(int collectMode) {
	
		this.collectMode = collectMode;
	}
}

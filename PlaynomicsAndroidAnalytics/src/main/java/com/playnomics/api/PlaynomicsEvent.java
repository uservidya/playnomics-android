package com.playnomics.api;

import java.io.Serializable;
import java.util.Date;

abstract class PlaynomicsEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	protected enum EventType {
		appStart, appPage, appRunning, appPause, appResume, appStop, userInfo, sessionStart, sessionEnd,
		gameStart, gameEnd, transaction, invitationSent, invitationResponse
	};
	
	private EventType eventType;
	private Date eventTime;
	private Long applicationId;
	private String userId;
	private String sessionId;
	
	
	protected PlaynomicsEvent(EventType eventType, String sessionId, Long applicationId, String userId) {
	
		super();
		eventTime = new Date();
		this.eventType = eventType;
		this.sessionId = sessionId;
		this.applicationId = applicationId;
		this.userId = userId;
	}
		
	public PlaynomicsEvent() {
		
	}

	protected EventType getEventType() {
	
		return eventType;
	}
	
	protected void setEventType(EventType eventType) {
	
		this.eventType = eventType;
	}
	
	protected Date getEventTime() {
	
		return eventTime;
	}
	
	protected void setEventTime(Date eventTime) {
	
		this.eventTime = eventTime;
	}
	
	protected String getSessionId() {
		
		return sessionId;
	}
	
	protected void setSessionId(String sessionId) {
	
		this.sessionId = sessionId;
	}
	
	protected Long getApplicationId() {
	
		return applicationId;
	}
	
	protected void setApplicationId(Long applicationId) {
	
		this.applicationId = applicationId;
	}
	
	protected String getUserId() {
	
		return userId;
	}
	
	protected void setUserId(String userId) {
	
		this.userId = userId;
	}
	
	public String toString() {
	
		return eventTime.toString() + ": " + eventType.toString();
	}
	
	protected String addOptionalParam(String url, String param, Object value) {
		
		if (value != null) {
			url += "&" + param + "=" + value.toString();
		}
		
		return url;
	}
	
	public abstract String toQueryString();
}
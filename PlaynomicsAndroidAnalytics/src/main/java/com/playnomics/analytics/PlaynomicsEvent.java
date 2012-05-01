package com.playnomics.analytics;

import java.util.Date;

class PlaynomicsEvent {
	
	protected enum EventType {
		appStart, appPage, appRunning, appPause, appResume, appStop, userInfo, sessionStart, sessionEnd,
		gameStart, gameEnd, transaction, invitationSent, invitationResponse
	};
	
	private EventType eventType;
	private Date eventTime;
	private String applicationId;
	private String userId;
	
	
	protected PlaynomicsEvent(EventType eventType, String applicationId, String userId) {
	
		super();
		eventTime = new Date();
		this.eventType = eventType;
		this.applicationId = applicationId;
		this.userId = userId;
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
	
	protected String getApplicationId() {
	
		return applicationId;
	}
	
	protected void setApplicationId(String applicationId) {
	
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
}
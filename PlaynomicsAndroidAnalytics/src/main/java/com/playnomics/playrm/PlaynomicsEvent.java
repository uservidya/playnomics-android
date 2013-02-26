package com.playnomics.playrm;

import java.io.Serializable;
import java.util.Date;

abstract class PlaynomicsEvent implements Serializable {

	private static final long serialVersionUID = 1L;

	protected enum EventType {
		appStart, appPage, appRunning, appPause, appResume, appStop, 
		userInfo, sessionStart, sessionEnd, gameStart, gameEnd, transaction,
		invitationSent, invitationResponse, milestone, 
	};

	private EventType eventType;
	private Date eventTime;
	private Long applicationId;
	private String userId;
	private String internalSessionId;

	protected String baseUrl;

	protected PlaynomicsEvent(EventType eventType, String internalSessionId,
			Long applicationId, String userId) {
		super();

		this.baseUrl = PlaynomicsSession.getBaseEventUrl();
		this.eventTime = new Date();
		this.eventType = eventType;
		this.internalSessionId = internalSessionId;
		this.applicationId = applicationId;
		this.userId = userId;
	}

	protected PlaynomicsEvent(String eventUrl) {
		this.baseUrl = eventUrl;
	}

	public PlaynomicsEvent() {
		this.baseUrl = PlaynomicsSession.getBaseEventUrl();
	}

	protected String getInternalSessionId() {
		return this.internalSessionId;
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
		return internalSessionId;
	}

	protected void setSessionId(String sessionId) {
		this.internalSessionId = sessionId;
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

	public String getEventUrl() {
		return baseUrl + toQueryString();
	}

	public boolean appendSourceInformation() {
		return true;
	}

	protected abstract String toQueryString();
}
package com.playnomics.analytics;

import java.util.Date;


public class PlaynomicsEvent {
	
	public enum EventTypes {EVENT_START, EVENT_PAUSE, EVENT_STOP, EVENT_CLICK, EVENT_KEY, EVENT_SWIPE, EVENT_MISC};
	
	private EventTypes eventType;
	private Date eventDate;
	private String values;
	
	public PlaynomicsEvent(EventTypes eventType, String values) {
	
		eventDate = new Date();
		this.eventType = eventType;
		this.values = values;
	}

	
	public EventTypes getEventType() {
	
		return eventType;
	}

	
	public void setEventType(EventTypes eventType) {
	
		this.eventType = eventType;
	}

	
	public Date getEventDate() {
	
		return eventDate;
	}

	
	public void setEventDate(Date eventDate) {
	
		this.eventDate = eventDate;
	}

	
	public String getValues() {
	
		return values;
	}

	
	public void setValues(String values) {
	
		this.values = values;
	}
	
	public String toString() {
		
		return eventDate.toString() + ": " + eventType.toString() + ", " + values;
	}
}	
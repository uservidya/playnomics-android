package com.playnomics.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EventTime extends GregorianCalendar{
	
	private static final long serialVersionUID = 1L;

	public EventTime(){
		super(TimeZone.getTimeZone("GMT"));
	}
	
	public EventTime(long millisecondsSinceEpoch){
		this();
		((Calendar)this).setTimeInMillis(millisecondsSinceEpoch);
	}
	
	@Override
	public String toString(){
		return String.format("%l", this.getTimeInMillis());
	}
}

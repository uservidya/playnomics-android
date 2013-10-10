package com.playnomics.util;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

public class EventTime extends GregorianCalendar implements
		Comparable<Calendar> {

	private static final long serialVersionUID = 1L;

	public EventTime() {
		super(Util.TIME_ZONE_GMT);
	}

	public EventTime(long millisecondsSinceEpoch) {
		this();
		((Calendar) this).setTimeInMillis(millisecondsSinceEpoch);
	}

	public static int getMinutesTimezoneOffset() {
		// get the offset local timezone from GMT in ms (local time + offset =
		// UTC time)
		int millisecondsOffset = TimeZone.getDefault().getRawOffset();
		int minutesOffset = millisecondsOffset / (60 * 1000);
		// flip the sign, because we want to view the calculation as UTC -
		// offset = localTime
		return minutesOffset * -1;
	}

	@Override
	public String toString() {
		return String.format("%l", getTimeInMillis());
	}
}

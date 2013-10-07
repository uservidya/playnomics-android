package com.playnomics.util;

import java.util.Random;
import java.util.TimeZone;

public class Util {
	public static long generateRandomLong(){
		Random rand = new Random();
		return rand.nextLong();
	}
	
	public static String getSdkVersion(){
		return "1.0.0";
	}
	
	public static int getMinutesTimezoneOffset(){
		//get the offset local timezone from GMT in ms (local time + offset = UTC time)
		int millisecondsOffset = TimeZone.getDefault().getRawOffset();		
		int minutesOffset = millisecondsOffset/(60 * 1000);
		//flip the sign, because we want to view the calculation as UTC - offset = localTime
		return minutesOffset * -1;
	}
	
	public static int getAppRunningIntervalSeconds(){
		return 60;
	}
	
	public static int getCollectionMode(){
		return 7;
	}
}

package com.playnomics.util;

import java.util.Random;
import java.util.TimeZone;

public class Util {	
	public long generatePositiveRandomLong(){
		Random rand = new Random();
		return Math.abs(rand.nextLong());
	}
	
	public String getSdkVersion(){
		return "1.0.0";
	}
	
	public String getSdkName(){
		return "aj";
	}
	
	public int getMinutesTimezoneOffset(){
		//get the offset local timezone from GMT in ms (local time + offset = UTC time)
		int millisecondsOffset = TimeZone.getDefault().getRawOffset();		
		int minutesOffset = millisecondsOffset/(60 * 1000);
		//flip the sign, because we want to view the calculation as UTC - offset = localTime
		return minutesOffset * -1;
	}
	
	public int getAppRunningIntervalSeconds(){
		return 60;
	}
	
	public int getCollectionMode(){
		return 7;
	}
}

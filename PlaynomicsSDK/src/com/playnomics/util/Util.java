package com.playnomics.util;

import java.util.Random;
import java.util.TimeZone;

public class Util {	
	
	public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
	public static final String UT8_ENCODING = "UTF-8";
	
	public long generatePositiveRandomLong(){
		Random rand = new Random();
		return Math.abs(rand.nextLong());
	}

	public boolean stringIsNullOrEmpty(String value){
		return (value == null || value.isEmpty());
	}
}

package com.playnomics.util;

import java.util.Random;
import java.util.TimeZone;

import android.content.Context;
import android.provider.Settings;

public class Util implements IRandomGenerator {

	public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
	public static final String UT8_ENCODING = "UTF-8";

	public long generatePositiveRandomLong() {
		Random rand = new Random();
		return Math.abs(rand.nextLong());
	}
	
	public String getDeviceIdFromContext(Context context){
		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	public static boolean stringIsNullOrEmpty(String value) {
		return (value == null || value.isEmpty());
	}
}

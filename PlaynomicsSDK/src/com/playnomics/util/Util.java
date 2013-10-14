package com.playnomics.util;

import java.util.Random;
import java.util.TimeZone;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings;

import com.playnomics.util.Logger.LogLevel;

public class Util implements IRandomGenerator {

	public static final TimeZone TIME_ZONE_GMT = TimeZone.getTimeZone("GMT");
	public static final String UT8_ENCODING = "UTF-8";

	private Logger logger;

	public Util(Logger logger){
		this.logger = logger;
	}
	
	public long generatePositiveRandomLong() {
		Random rand = new Random();
		return Math.abs(rand.nextLong());
	}
	
	public String getDeviceIdFromContext(Context context){
		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}
	
	public int getApplicationVersionFromContext(Context context){
		try {
			PackageManager packageManager = context.getPackageManager();
			PackageInfo info;
			info = packageManager.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException ex) {
			// according to Google's docs this should never happen
			logger.log(LogLevel.WARNING, ex,
					"Could not obtain the application version from the package manager");
			// in the event of a failure always return a -1
			return -1;
		}
	}

	public static boolean stringIsNullOrEmpty(String value) {
		return (value == null || value.isEmpty());
	}
}

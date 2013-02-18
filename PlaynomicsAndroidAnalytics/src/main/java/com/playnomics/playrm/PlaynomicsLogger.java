package com.playnomics.playrm;

import android.util.Log;

/**
 * This class wraps the android logger and optionally turn it off, because 
 * android code libraries cannot be called from junit tests.
 * 
 */
class PlaynomicsLogger{
	
	private static boolean deviceLoggingEnabled = true;
	
	public static boolean deviceLoggingEnabled(){
		return PlaynomicsLogger.deviceLoggingEnabled;
	}
	
	public static void setEnabled(boolean enabled){
		PlaynomicsLogger.deviceLoggingEnabled = enabled;
	}
	
	public static void v(String tag, String message){
		if(deviceLoggingEnabled){
			Log.v(tag, message);
		}
	}
	
	public static void v(String tag, String message, Throwable t){
		if(deviceLoggingEnabled){
			Log.v(tag, message, t);
		}
	}
	
	public static void d(String tag, String message){
		if(deviceLoggingEnabled){
			Log.d(tag, message);
		}
	}
	
	public static void d(String tag, String message, Throwable t){
		if(deviceLoggingEnabled){
			Log.d(tag, message, t);
		}
	}
	
	public static void e(String tag, String message){
		if(deviceLoggingEnabled){
			Log.e(tag, message);
		}
	}
	
	public static void e(String tag, String message, Throwable t){
		if(deviceLoggingEnabled){
			Log.e(tag, message, t);
		}
	}
	
	public static void w(String tag, String message){
		if(deviceLoggingEnabled()){
			Log.w(tag, message);
		}
	}
	
	public static void w(String tag, String message, Throwable t){
		if(deviceLoggingEnabled()){
			Log.w(tag, message, t);
		}
	}
	
	public static void i(String tag, String message){
		if(deviceLoggingEnabled){
			Log.i(tag, message);
		}
	}
	
	public static void i(String tag, String message, Throwable t){
		if(deviceLoggingEnabled){
			Log.i(tag, message, t);
		}
	}
}

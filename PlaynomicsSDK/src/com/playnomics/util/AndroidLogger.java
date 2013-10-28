package com.playnomics.util;

import android.util.Log;

import com.playnomics.util.Logger.LogLevel;

public class AndroidLogger implements LogWriter {

	private String logTag;

	public AndroidLogger(String logTag) {
		this.logTag = logTag;
	}

	public void writeLog(LogLevel logLevel, String format, Object[] args) {
		int level = getAndroidLogLevel(logLevel);
		Log.println(level, logTag, String.format(format, args));
	}

	public void writeLog(LogLevel logLevel, Exception exception) {
		int level = getAndroidLogLevel(logLevel);
		Log.println(level, logTag,
				String.format("Exception: \r\n %s", exception.getMessage()));
		Log.println(level, logTag, String.format("Stack Tracke: \r\n %s",
				exception.getStackTrace().toString()));
	}

	private int getAndroidLogLevel(LogLevel level) {
		if (level == LogLevel.DEBUG) {
			return Log.DEBUG;
		}
		if (level == LogLevel.ERROR) {
			return Log.ERROR;
		}
		if (level == LogLevel.VERBOSE) {
			return Log.VERBOSE;
		}
		if (level == LogLevel.WARNING) {
			return Log.WARN;
		}
		throw new IllegalArgumentException("Invalid log level");
	}

}

package com.playnomics.android.util;

import com.playnomics.android.util.LogWriter;
import com.playnomics.android.util.Logger.LogLevel;

public class UnitTestLogWriter implements LogWriter {
	public void writeLog(LogLevel level, String format, Object[] args) {
		System.out.println(String.format(format, args));
	}

	public void writeLog(LogLevel level, Exception exception) {
		System.out.println(exception.getMessage());
	}
}

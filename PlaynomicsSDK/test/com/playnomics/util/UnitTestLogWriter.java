package com.playnomics.util;

import com.playnomics.util.Logger.LogLevel;

public class UnitTestLogWriter implements LogWriter {
	public void writeLog(LogLevel level, String format, Object[] args) {
		System.out.println(String.format(format, args));
	}

	public void writeLog(LogLevel level, Exception exception) {
		System.out.println(exception.getMessage());
	}
}

package com.playnomics.util;

import com.playnomics.util.Logger.LogLevel;

public class UnitTestLogWriter implements LogWriter {
	public void writeLog(LogLevel level, String format, Object[] args) {
		System.out.printf(format, args);
	}

	public void writeLog(LogLevel level, Exception exception) {
		System.out.print(exception.getMessage());
	}
}

package com.playnomics.util;

public interface LogWriter {
	void writeLog(Logger.LogLevel level, String format, Object[] args);

	void writeLog(Logger.LogLevel level, Exception exception);
}

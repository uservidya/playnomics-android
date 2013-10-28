package com.playnomics.util;

public class Logger {
	public enum LogLevel {
		VERBOSE(0), DEBUG(1), WARNING(2), ERROR(3), NONE(4);

		private int level;

		LogLevel(int level) {
			this.level = level;
		}

		public int level() {
			return level;
		}
	}

	private LogLevel logLevel = LogLevel.ERROR;

	public void setLogLevel(LogLevel level) {
		this.logLevel = level;
	}

	private LogWriter logWriter;

	public Logger(LogWriter logWriter) {
		this.logWriter = logWriter;
	}

	public void log(LogLevel logLevel, String format, Object... args) {
		int authorizedLogLevel = this.logLevel.level();
		int targetLogLevel = logLevel.level();
		if (authorizedLogLevel <= targetLogLevel) {
			// can log the the message
			logWriter.writeLog(logLevel, format, args);
		}
	}

	public void log(LogLevel logLevel, Exception ex) {
		int authorizedLogLevel = this.logLevel.level();
		int targetLogLevel = logLevel.level();
		if (authorizedLogLevel <= targetLogLevel) {
			// can log the the message
			logWriter.writeLog(logLevel, ex);
		}
	}

	public void log(LogLevel logLevel, Exception ex, String message,
			Object... args) {
		int authorizedLogLevel = this.logLevel.level();
		int targetLogLevel = logLevel.level();
		if (authorizedLogLevel <= targetLogLevel) {
			// can log the the message
			logWriter.writeLog(logLevel, message, args);
			logWriter.writeLog(logLevel, ex);
		}
	}
}

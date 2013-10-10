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

	private static LogLevel logLevel;

	public static void setLogLevel(LogLevel level) {
		Logger.logLevel = level;
	}

	public static void log(LogLevel logLevel, String message, Object... args) {
		int authorizedLogLevel = Logger.logLevel.level();
		int targetLogLevel = logLevel.level();
		if (authorizedLogLevel <= targetLogLevel) {
			// can log the the message
		}
	}

	public static void log(LogLevel logLevel, Exception ex) {
		int authorizedLogLevel = Logger.logLevel.level();
		int targetLogLevel = logLevel.level();
		if (authorizedLogLevel <= targetLogLevel) {
			// can log the the message
		}
	}

	public static void log(LogLevel logLevel, Exception ex, String message,
			Object... args) {
		int authorizedLogLevel = Logger.logLevel.level();
		int targetLogLevel = logLevel.level();
		if (authorizedLogLevel <= targetLogLevel) {
			// can log the the message
		}
	}
}

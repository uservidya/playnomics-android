package com.playnomics.android.util;

import static org.mockito.Mockito.*;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.playnomics.android.util.LogWriter;
import com.playnomics.android.util.Logger;
import com.playnomics.android.util.Logger.LogLevel;

public class LoggerTest {

	@Mock
	private LogWriter writerMock;

	private Logger logger;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);

		logger = new Logger(writerMock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDebugLogging() {
		logger.setLogLevel(LogLevel.DEBUG);
		testLogger(logger, 3);
	}

	@Test
	public void testVerboseLogging() {
		logger.setLogLevel(LogLevel.VERBOSE);
		testLogger(logger, 4);
	}

	@Test
	public void testWarnLogging() {
		logger.setLogLevel(LogLevel.WARNING);
		testLogger(logger, 2);
	}

	@Test
	public void testErrorLogging() {
		logger.setLogLevel(LogLevel.ERROR);
		testLogger(logger, 1);
	}

	@Test
	public void testNoLogging() {
		logger.setLogLevel(LogLevel.NONE);
		testLogger(logger, 0);
	}

	private void testLogger(Logger logger, int maxCalls) {
		Exception ex = new Exception();
		logger.log(LogLevel.VERBOSE, ex);
		logger.log(LogLevel.DEBUG, ex);
		logger.log(LogLevel.WARNING, ex);
		logger.log(LogLevel.ERROR, ex);

		String message = "message";
		logger.log(LogLevel.VERBOSE, message);
		logger.log(LogLevel.DEBUG, message);
		logger.log(LogLevel.WARNING, message);
		logger.log(LogLevel.ERROR, message);

		logger.log(LogLevel.VERBOSE, ex, message);
		logger.log(LogLevel.DEBUG, ex, message);
		logger.log(LogLevel.WARNING, ex, message);
		logger.log(LogLevel.ERROR, ex, message);

		verify(writerMock, Mockito.atMost(maxCalls * 2)).writeLog(
				any(LogLevel.class), eq(ex));
		verify(writerMock, Mockito.atMost(maxCalls * 2)).writeLog(
				any(LogLevel.class), eq(message), (Object[]) any(Object.class));
	}

}

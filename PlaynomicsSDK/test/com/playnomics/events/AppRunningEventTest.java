package com.playnomics.events;

import static org.junit.Assert.*;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Config;
import com.playnomics.util.EventTime;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.util.Util;

public class AppRunningEventTest extends PlaynomicsEventTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testAppRunning() {
		Util util = new Util();
		LargeGeneratedId instanceId = new LargeGeneratedId(util);
		GameSessionInfo sessionInfo = getGameSessionInfo();
		Config config = new Config();
		// 1 minute ago
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 60);
		EventTime startTime = new EventTime(calendar.getTimeInMillis());

		int sequenceNumber = 10;
		int touches = 5;
		int totalTouches = 15;

		int keysPressed = 0;
		int totalKeysPressed = 0;

		AppRunningEvent event = new AppRunningEvent(config, sessionInfo,
				instanceId, startTime, sequenceNumber, touches, totalTouches);
		testCommonEventParameters(config, event, sessionInfo);

		Map<String, Object> params = event.getEventParameters();
		assertEquals("Insance ID is set", instanceId, params.get("i"));
		assertEquals("Sequence is set", sequenceNumber, params.get("q"));
		assertEquals("Touches is set", touches, params.get("c"));
		assertEquals("Total Touches is set", totalTouches, params.get("e"));
		assertEquals("Keys pressed is set", keysPressed, params.get("k"));
		assertEquals("Total keys pressed is set", totalKeysPressed,
				params.get("l"));
		assertEquals("Session start time is set", startTime, params.get("r"));
		assertEquals("Capture mode is set", config.getCollectionMode(),
				params.get("m"));
		assertEquals("Interval is set",
				config.getAppRunningIntervalMilliseconds(), params.get("d"));
	}
}

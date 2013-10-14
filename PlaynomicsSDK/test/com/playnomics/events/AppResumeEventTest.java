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
import com.playnomics.util.IConfig;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.util.Util;

public class AppResumeEventTest extends PlaynomicsEventTest {

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
	public void testAppResume() {
		IConfig config = new Config();
		Util util = new Util(logger);
		LargeGeneratedId instanceId = new LargeGeneratedId(util);
		GameSessionInfo sessionInfo = getGameSessionInfo();

		// 2 minutes ago
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 60 * 2);
		EventTime pauseTime = new EventTime(calendar.getTimeInMillis());

		// another 2 minutes back
		calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND) - 60 * 2);
		EventTime startTime = new EventTime(calendar.getTimeInMillis());

		assertTrue("Pause time greater than start time",
				pauseTime.compareTo(startTime) > 0);
		int sequenceNumber = 10;
		AppResumeEvent event = new AppResumeEvent(config, sessionInfo,
				instanceId, startTime, pauseTime, sequenceNumber);
		testCommonEventParameters(config, event, sessionInfo);

		Map<String, Object> params = event.getEventParameters();
		assertEquals("Insance ID is set", instanceId, params.get("i"));
		assertEquals("Sequence is set", sequenceNumber, params.get("q"));
		assertEquals("Session start time is set", startTime, params.get("r"));
		assertEquals("Session pause time is set", pauseTime, params.get("p"));
	}
}

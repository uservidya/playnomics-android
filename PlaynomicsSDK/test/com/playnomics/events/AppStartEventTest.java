package com.playnomics.events;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.util.Util;

public class AppStartEventTest extends PlaynomicsEventTest {

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
	public void testAppStart() {
		LargeGeneratedId instanceId = LargeGeneratedId.generateNextValue();
		GameSessionInfo sessionInfo = getGameSessionInfo();
		
		AppPageEvent event = new AppPageEvent(sessionInfo, instanceId);
		testCommonEventParameters(event, sessionInfo);
		
		Map<String, Object> params = event.getEventParameters();
		assertEquals("Instance ID is set", instanceId, params.get("i"));
		assertEquals("Time zone is set", Util.getMinutesTimezoneOffset(), params.get("z"));
	}
}

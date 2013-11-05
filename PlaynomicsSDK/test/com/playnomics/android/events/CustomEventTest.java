package com.playnomics.android.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import java.util.Map;

import com.playnomics.android.events.CustomEvent;
import com.playnomics.android.events.CustomEvent.CustomEventType;
import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.Config;
import com.playnomics.android.util.IConfig;
import com.playnomics.android.util.Util;

public class CustomEventTest extends PlaynomicsEventTest {

	@Mock
	private Util utilMock;

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
	public void testCustomEventToString() {
		CustomEventType milestoneType = CustomEventType.CUSTOM_EVENT_1;
		assertEquals("MilestoneType has correct string representation",
				"CUSTOM1", milestoneType.toString());
	}

	@Test
	public void testCustomEvent() {
		GameSessionInfo sessionInfo = getGameSessionInfo();
		CustomEventType milestone25 = CustomEventType.CUSTOM_EVENT_25;

		long milestoneId = 100L;

		IConfig config = new Config();

		when(utilMock.generatePositiveRandomLong()).thenReturn(milestoneId);

		CustomEvent event = new CustomEvent(config, utilMock, sessionInfo,
				milestone25);
		testCommonEventParameters(config, event, sessionInfo);

		Map<String, Object> params = event.getEventParameters();
		assertEquals("Milestone Name is set", milestone25, params.get("mn"));
		assertEquals("Milestone ID is set", milestoneId, params.get("mi"));
		verify(utilMock).generatePositiveRandomLong();
	}
}

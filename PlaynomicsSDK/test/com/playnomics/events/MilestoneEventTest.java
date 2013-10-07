package com.playnomics.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import org.mockito.Mock;
import static org.mockito.Mockito.*;
import java.util.Map;

import com.playnomics.events.MilestoneEvent.MilestoneType;
import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Util;

public class MilestoneEventTest extends PlaynomicsEventTest {

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
	public void testMilestoneToString() {
		MilestoneType milestoneType = MilestoneType.MilestoneCustom1;
		assertEquals("MilestoneType has correct string representation", "CUSTOM1", milestoneType.toString());
	}
	
	@Test
	public void testMilestoneEvent(){
		GameSessionInfo sessionInfo = getGameSessionInfo();
		MilestoneType milestone25 = MilestoneType.MilestoneCustom25;
		
		long milestoneId = 100L;
		
		when(utilMock.getSdkName()).thenReturn("aj");
		when(utilMock.getSdkVersion()).thenReturn("1.0.0");
		when(utilMock.generatePositiveRandomLong()).thenReturn(milestoneId);
		
		MilestoneEvent event = new MilestoneEvent(utilMock, sessionInfo, milestone25);
		testCommonEventParameters(utilMock, event, sessionInfo);
		
		Map<String, Object> params = event.getEventParameters();
		assertEquals("Milestone Name is set", milestone25, params.get("mn"));
		assertEquals("Milestone ID is set", milestoneId, params.get("mi"));
		
		verify(utilMock, times(2)).getSdkName();
		verify(utilMock, times(2)).getSdkVersion();
		verify(utilMock).generatePositiveRandomLong();
	}
}
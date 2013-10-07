package com.playnomics.events;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import java.util.Map;

import com.playnomics.events.MilestoneEvent.MilestoneType;
import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Util;

public class MilestoneEventTest extends PlaynomicsEventTest {

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
		Util util = new Util();
		GameSessionInfo sessionInfo = getGameSessionInfo();
		MilestoneType milestone25 = MilestoneType.MilestoneCustom25;
	
		MilestoneEvent event = new MilestoneEvent(util, sessionInfo, milestone25);
		testCommonEventParameters(util, event, sessionInfo);
		
		Map<String, Object> params = event.getEventParameters();
		assertEquals("Milestone Name is set", milestone25, params.get("mn"));
	}
}

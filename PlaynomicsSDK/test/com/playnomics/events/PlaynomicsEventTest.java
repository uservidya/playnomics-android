package com.playnomics.events;


import static org.junit.Assert.*;

import java.util.Map;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.*;


import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.util.Util;


public class PlaynomicsEventTest {
	
	@Before
	public void initMocks(){
		MockitoAnnotations.initMocks(this);
	}
	
	protected GameSessionInfo getGameSessionInfo(){
		Util util = new Util();
		LargeGeneratedId sessionId = new LargeGeneratedId(util);
		return new GameSessionInfo(1L, "userId", "breadcrumId", sessionId);
	}
	
	protected void testCommonEventParameters(Util util, PlaynomicsEvent event, GameSessionInfo sessionInfo){
		Map<String, Object> params = event.getEventParameters();
		
		assertEquals("Application ID is set", sessionInfo.getApplicationId(), params.get("a"));
		assertEquals("User ID is set", sessionInfo.getApplicationId(), params.get("a"));
		assertEquals("Breadcrumb ID is set", sessionInfo.getBreadcrumbId(), params.get("b"));
		assertEquals("Session ID is set", sessionInfo.getSessionId(), params.get(event.getSessionKey()));
		assertEquals("Event time is set", event.getEventTime(), params.get("t"));
		assertEquals("SDK Name is set", util.getSdkName(), params.get("esrc"));
		assertEquals("SDK Name is set", util.getSdkVersion(), params.get("ever"));
		
		if(event instanceof ImplicitEvent){
			assertEquals("Session key is set correctly", event.getSessionKey(), "s");
		} else {
			assertEquals("Session key is set correctly", event.getSessionKey(), "jsh");
		}
		
		if(event instanceof AppPageEvent){
			assertEquals("Has correct URL Path", "appPage", event.getBaseUrl());
		} else if (event instanceof AppStartEvent){
			assertEquals("Has correct URL Path", "appStart", event.getBaseUrl());
		} else if (event instanceof AppPauseEvent){
			assertEquals("Has correct URL Path", "appPause", event.getBaseUrl());
		} else if (event instanceof AppResumeEvent){
			assertEquals("Has correct URL Path", "appResume", event.getBaseUrl());
		} else if (event instanceof TransactionEvent){
			assertEquals("Has correct URL Path", "transaction", event.getBaseUrl());
		} else if (event instanceof MilestoneEvent){
			assertEquals("Has correct URL Path", "milestone", event.getBaseUrl());
		} else if (event instanceof UserInfoEvent){
			assertEquals("Has correct URL Path", "userInfo", event.getBaseUrl());
		}
	}
}

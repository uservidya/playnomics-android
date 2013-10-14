package com.playnomics.events;

import static org.junit.Assert.assertEquals;

import java.util.Map;

import org.junit.Before;
import org.mockito.MockitoAnnotations;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.IConfig;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.util.Logger;
import com.playnomics.util.UnitTestLogWriter;
import com.playnomics.util.Util;

public class PlaynomicsEventTest {

	protected Logger logger;
	
	@Before
	public void initMocks() {
		logger = new Logger(new UnitTestLogWriter());
		MockitoAnnotations.initMocks(this);
	}

	protected GameSessionInfo getGameSessionInfo() {
		Util util = new Util(logger);
		LargeGeneratedId sessionId = new LargeGeneratedId(util);
		return new GameSessionInfo(1L, "userId", "breadcrumId", sessionId);
	}

	protected void testCommonEventParameters(IConfig config,
			PlaynomicsEvent event, GameSessionInfo sessionInfo) {
		Map<String, Object> params = event.getEventParameters();

		assertEquals("Application ID is set", sessionInfo.getApplicationId(),
				params.get("a"));
		assertEquals("User ID is set", sessionInfo.getApplicationId(),
				params.get("a"));
		assertEquals("Breadcrumb ID is set", sessionInfo.getBreadcrumbId(),
				params.get("b"));
		assertEquals("Session ID is set", sessionInfo.getSessionId(),
				params.get(event.getSessionKey()));
		assertEquals("Event time is set", event.getEventTime(), params.get("t"));
		assertEquals("SDK Name is set", config.getSdkName(), params.get("esrc"));
		assertEquals("SDK Name is set", config.getSdkVersion(),
				params.get("ever"));

		if (event instanceof ImplicitEvent) {
			assertEquals("Session key is set correctly", event.getSessionKey(),
					"s");
		} else {
			assertEquals("Session key is set correctly", event.getSessionKey(),
					"jsh");
		}

		if (event instanceof AppPageEvent) {
			assertEquals("Has correct URL Path", "appPage", event.getUrlPath());
		} else if (event instanceof AppStartEvent) {
			assertEquals("Has correct URL Path", "appStart", event.getUrlPath());
		} else if (event instanceof AppPauseEvent) {
			assertEquals("Has correct URL Path", "appPause", event.getUrlPath());
		} else if (event instanceof AppResumeEvent) {
			assertEquals("Has correct URL Path", "appResume",
					event.getUrlPath());
		} else if (event instanceof TransactionEvent) {
			assertEquals("Has correct URL Path", "transaction",
					event.getUrlPath());
		} else if (event instanceof MilestoneEvent) {
			assertEquals("Has correct URL Path", "milestone",
					event.getUrlPath());
		} else if (event instanceof UserInfoEvent) {
			assertEquals("Has correct URL Path", "userInfo", event.getUrlPath());
		}
	}
}

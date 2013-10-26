package com.playnomics.messaging;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.app.Activity;

import com.playnomics.client.FrameDataClient;
import com.playnomics.util.Config;
import com.playnomics.util.Logger;
import com.playnomics.util.UnitTestLogWriter;
import com.playnomics.util.Util;

public class MessagingManagerTest {

	@Mock
	private Util utilMock;
	@Mock
	private FrameDataClient dataClientMock;
	
	@Mock
	private Activity activityMock;

	private MessagingManager messagingManager;
		
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		Config config = new Config();
		UnitTestLogWriter writer = new UnitTestLogWriter();
		Logger logger = new Logger(writer);
		messagingManager = new MessagingManager(config, dataClientMock, utilMock, logger);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testPreloadFramesLoadsRequestsDataOnce() {
		String[] frameIds = new String[]{"frame1"};
		messagingManager.preloadFrames(frameIds);
		messagingManager.showFrame("frame1", activityMock, null);
		verify(dataClientMock, Mockito.atMost(1)).loadFrameInBackground(any(Frame.class));
	}

	@Test
	public void testPreloadMultipleFrames(){
		String[] frameIds = new String[]{"frame1", "frame2","frame3"};
		messagingManager.preloadFrames(frameIds);
		verify(dataClientMock, Mockito.atLeast(3)).loadFrameInBackground(any(Frame.class));
	}

	@Test
	public void testShowFrameNoPreload() {
		messagingManager.showFrame("frame1", activityMock, null);
		verify(dataClientMock, Mockito.atMost(1)).loadFrameInBackground(any(Frame.class));
	}
}

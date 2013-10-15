package com.playnomics.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.playnomics.events.MilestoneEvent;
import com.playnomics.events.MilestoneEvent.MilestoneType;
import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Config;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.util.Logger;
import com.playnomics.util.UnitTestLogWriter;
import com.playnomics.util.Util;

public class EventWorkerTest {
	
	@Mock 
	private HttpURLConnection connectionMock;
	@Mock 
	private HttpConnectionFactory factoryMock;
	
	private EventQueue queue;
	private EventWorker worker;
	private GameSessionInfo sessionInfo;
	private MilestoneEvent event;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
	
		when(factoryMock.startConnectionForUrl(any(String.class))).thenReturn(connectionMock);
		
		Logger logger = new Logger(new UnitTestLogWriter());
		Config config = new Config();
		Util util = new Util(logger);
		queue = new EventQueue(config);
		worker = new EventWorker(queue, factoryMock, logger);
		
		sessionInfo = new GameSessionInfo(1L, "userId", "breadcrumbId", new LargeGeneratedId(10L));
		event = new MilestoneEvent(config, util, sessionInfo, MilestoneType.MilestoneCustom1);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testRequestSuccesses() throws IOException, InterruptedException {
		when(connectionMock.getResponseCode()).thenReturn(200);
		queue.enqueueEvent(event);
		worker.start();
		Thread.sleep(1500);
		worker.stop();
		assertTrue("Queue is empty", queue.isEmpty());
	}
	
	@Test
	public void testRequestFailures() throws IOException, InterruptedException{
		when(connectionMock.getResponseCode()).thenReturn(404);
		queue.enqueueEvent(event);
		worker.start();
		Thread.sleep(1500);
		worker.stop();
		assertFalse("Queue is not empty", queue.isEmpty());
	}
}

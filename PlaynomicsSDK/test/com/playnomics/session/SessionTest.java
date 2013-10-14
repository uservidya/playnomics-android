package com.playnomics.session;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.Context;
import android.view.ContextMenu;

import com.playnomics.client.HttpConnectionFactory;
import com.playnomics.client.IEventQueue;
import com.playnomics.client.IEventWorker;
import com.playnomics.client.StubEventQueue;
import com.playnomics.events.AppStartEvent;
import com.playnomics.events.UserInfoEvent;
import com.playnomics.util.Config;
import com.playnomics.util.ContextWrapper;
import com.playnomics.util.EventTime;
import com.playnomics.util.Logger;
import com.playnomics.util.UnitTestLogWriter;
import com.playnomics.util.Util;


public class SessionTest {

	private Long appId = 10L;
	private String userId = "testUser";
	private String deviceId = "deviceId";
	
	@Mock 
	private HttpConnectionFactory factoryMock;
	@Mock 
	private HttpURLConnection connectionMock;

	@Mock
	private Context contextMock;
	
	@Mock 
	private Util utilMock;
	@Mock
	private ContextWrapper contextWrapperMock;
	
	@Mock
	private IHeartBeatProducer producerMock;
	
	@Mock
	private IActivityObserver observerMock;
	
	@Mock
	private IEventWorker eventWorker;
	
	private Session session;
	private StubEventQueue eventQueue;
	
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
				
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		
		when(contextWrapperMock.getContext()).thenReturn(contextMock);
		//device ID setup
		when(utilMock.getDeviceIdFromContext(contextMock)).thenReturn(deviceId);
		//cache mock setup
		//when(contextMock.getSharedPreferences("com.playnomics.cache", Context.MODE_PRIVATE)).thenReturn(preferencesMock);
		//when(preferencesMock.edit()).thenReturn(editorMock);
		
		//the connection just works
		when(factoryMock.startConnectionForUrl(any(String.class))).thenReturn(connectionMock);
		when(connectionMock.getResponseCode()).thenReturn(200);
		
		eventQueue = new StubEventQueue();
		
		Config config = new Config();
		Logger logger = new Logger(new UnitTestLogWriter());
		session = new Session(config, utilMock, factoryMock, logger, eventQueue, eventWorker, observerMock, producerMock);
	}

	@After
	public void tearDown() throws Exception {
	}
	


	@Test
	/**
	 * Should queue one event, appStart
	 */
	public void testStartLapse(){
		fail("Not implemented");
	}
	
	@Test
	public void testStartNewDevice() throws IOException{
		/*
		when(utilMock.getApplicationVersionFromContext(contextMock)).thenReturn(1);
		
		when(preferencesMock.getString("pushId", null)).thenReturn(null);
		when(preferencesMock.getInt("sessionId", -1)).thenReturn(-1);
		when(preferencesMock.getInt("appVersion", 0)).thenReturn(0);
		when(preferencesMock.getInt("lastEventTime", 0)).thenReturn(0);
		when(preferencesMock.getInt("sessionStartTime", 0)).thenReturn(0);
		*/
		when(contextWrapperMock.getContext()).thenReturn(contextMock);
		when(contextWrapperMock.getLastEventTime()).thenReturn(null);
		when(contextWrapperMock.getLastSessionStartTime()).thenReturn(null);
		when(contextWrapperMock.getPreviousSessionId()).thenReturn(null);
		when(contextWrapperMock.synchronizeDeviceSettings()).thenReturn(true);
	
		session.setApplicationId(appId);
		session.setUserId(userId);
		session.start(contextWrapperMock);
		
		assertEquals("Application ID is set", appId, session.getApplicationId());
		assertEquals("User ID is set", userId, session.getUserId());
		assertEquals("Breadcrumb ID is set", deviceId, session.getBreadcrumbId());
		assertEquals("Session state is started", SessionStateMachine.SessionState.STARTED, session.getSessionState());
		
		Object event = (AppStartEvent)eventQueue.queue.remove();
		assertTrue("AppStart queued", event instanceof AppStartEvent);
		
		EventTime startTime = ((AppStartEvent)event).getEventTime();
		assertEquals("Session start time set", startTime, session.getSessionStartTime());
		
		Object nextEvent = (UserInfoEvent)eventQueue.queue.remove();
		assertTrue("UserInfo queued", nextEvent instanceof UserInfoEvent);
		//2 events are queued
		assertTrue("2 events are queued", eventQueue.isEmpty());
		//verify that contextWrapper is called
	
		verify(contextWrapperMock).setLastSessionStartTime(startTime);
		verify(contextWrapperMock).setPreviousSessionId(session.getSessionId());
	}	
	
	@Test
	/**
	 * Should queue one event, appPage
	 */
	public void testStartNoLapse(){
		fail("Not implemented");
	}
	
	@Test 
	public void testMilestone(){
		fail("Not implemented");
	}
	
	@Test 
	public void testMilestoneNoStart(){
		fail("Not implemented");
	}
	

	@Test
	public void testTransaction(){
		fail("Not implemented");
	}
	
	@Test 
	public void testTransactionNoStart(){
		fail("Not implemented");
	}
}

package com.playnomics.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import android.app.Activity;
import android.content.Context;

import com.playnomics.client.HttpConnectionFactory;
import com.playnomics.client.IEventWorker;
import com.playnomics.client.StubEventQueue;
import com.playnomics.events.AppPageEvent;
import com.playnomics.events.AppPauseEvent;
import com.playnomics.events.AppResumeEvent;
import com.playnomics.events.AppStartEvent;
import com.playnomics.events.CustomEvent;
import com.playnomics.events.CustomEvent.CustomEventType;
import com.playnomics.events.TransactionEvent;
import com.playnomics.events.UserInfoEvent;
import com.playnomics.messaging.MessagingManager;
import com.playnomics.util.Config;
import com.playnomics.util.ContextWrapper;
import com.playnomics.util.EventTime;
import com.playnomics.util.LargeGeneratedId;
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
	
	@Mock
	private Activity activityMock;
	
	@Mock
	private MessagingManager messagingManagerMock;
	
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
		
		eventQueue = new StubEventQueue();
		
		Config config = new Config();
		Logger logger = new Logger(new UnitTestLogWriter());
		session = new Session(config, utilMock, factoryMock, logger, eventQueue, eventWorker, observerMock, producerMock, messagingManagerMock);
	}

	@After
	public void tearDown() throws Exception {
	}
	
	@Test 
	public void testStartNoApplicationId(){
		session.start(contextWrapperMock);
		assertEquals("Session state is not started", SessionStateMachine.SessionState.NOT_STARTED, session.getSessionState());
		assertTrue("No events were queued", eventQueue.queue.isEmpty());
	}
	
	@Test
	public void testStartNoUserId(){
		testNewDevice(null);
	}
	
	@Test
	public void testStartNewDevice(){
		testNewDevice(userId);
	}	
	
	public void testNewDevice(String userId){
		long newSessionId = 1;
		
		when(utilMock.generatePositiveRandomLong()).thenReturn(newSessionId);
		
		when(contextWrapperMock.getLastEventTime()).thenReturn(null);
		when(contextWrapperMock.getLastSessionStartTime()).thenReturn(null);
		when(contextWrapperMock.getPreviousSessionId()).thenReturn(null);
		when(contextWrapperMock.synchronizeDeviceSettings()).thenReturn(true);
	
		session.setApplicationId(appId);
		session.setUserId(userId);
		session.start(contextWrapperMock);
		
		assertEquals("Application ID is set", appId, session.getApplicationId());
		assertEquals("User ID is set", userId == null ? deviceId : userId, session.getUserId());
		assertEquals("Breadcrumb ID is set", deviceId, session.getBreadcrumbId());
		assertEquals("Session state is started", SessionStateMachine.SessionState.STARTED, session.getSessionState());
		assertEquals("Session ID is generated", newSessionId, session.getSessionId().getId());
		assertEquals("Session ID = Instance ID", session.getSessionId(), session.getInstanceId());
		
		Object event = eventQueue.queue.remove();
		assertTrue("AppStart queued", event instanceof AppStartEvent);
		
		EventTime startTime = ((AppStartEvent)event).getEventTime();
		assertEquals("Session start time set", startTime, session.getSessionStartTime());
		
		Object nextEvent = eventQueue.queue.remove();
		assertTrue("UserInfo queued", nextEvent instanceof UserInfoEvent);
		//2 events are queued
		assertTrue("2 events are queued", eventQueue.isEmpty());
		
		//verify that contextWrapper is called
		verify(contextWrapperMock).setLastSessionStartTime(startTime);
		verify(contextWrapperMock).setPreviousSessionId(session.getSessionId());
		
		verify(producerMock).start(session);
		verify(observerMock).setStateMachine(session);
		verify(eventWorker).start();
	}
	
	@Test
	public void testStartNoLapseOldDeviceData(){
		testOldDevice(false, false);
	}
	
	@Test
	public void testStartNoLapseNewDeviceData(){
		testOldDevice(false, true); 
	}
	
	@Test
	public void testLapseOldDeviceData(){
		testOldDevice(true, false);
	}
	
	@Test
	public void testLapseNewDeviceData(){
		testOldDevice(true, false);
	}
	
	private void testOldDevice(boolean lapsed, boolean deviceDataChanged){
		long nextId = 2;
		when(utilMock.generatePositiveRandomLong()).thenReturn(nextId);
		
		LargeGeneratedId oldSessionId = new LargeGeneratedId(1);
		
		GregorianCalendar startTime = new GregorianCalendar();
		GregorianCalendar lastTime = new GregorianCalendar();
		if(lapsed){
			startTime.add(Calendar.MINUTE, -5);
			lastTime.add(Calendar.MINUTE, -4);
		} else {
			startTime.add(Calendar.MINUTE, -2);
			lastTime.add(Calendar.MINUTE, -1);
		}
		
		EventTime startEventTime = new EventTime(startTime.getTimeInMillis());
		EventTime lastEventTime = new EventTime(lastTime.getTimeInMillis());
		
		when(contextWrapperMock.getLastEventTime()).thenReturn(lastEventTime);
		when(contextWrapperMock.getLastSessionStartTime()).thenReturn(startEventTime);
		when(contextWrapperMock.getPreviousSessionId()).thenReturn(oldSessionId);
		when(contextWrapperMock.synchronizeDeviceSettings()).thenReturn(deviceDataChanged);
		
		session.setApplicationId(appId);
		session.setUserId(userId);
		session.start(contextWrapperMock);
		
		assertEquals("Application ID is set", appId, session.getApplicationId());
		assertEquals("User ID is set", userId, session.getUserId());
		assertEquals("Breadcrumb ID is set", deviceId, session.getBreadcrumbId());
		assertEquals("Session state is started", SessionStateMachine.SessionState.STARTED, session.getSessionState());
		
		Object event = eventQueue.queue.remove();
		
		if(lapsed){
			assertTrue("AppStart queued", event instanceof AppStartEvent);
			assertEquals("Session start time set", ((AppStartEvent)event).getEventTime(), session.getSessionStartTime());
			assertEquals("Session ID is generated", nextId, session.getSessionId().getId());
			assertEquals("Session ID = Instance ID", session.getSessionId(), session.getInstanceId());
		} else {
			assertTrue("AppPage queued", event instanceof AppPageEvent);
			assertEquals("Session start time set", startEventTime, session.getSessionStartTime());
			assertEquals("Session ID is set from old session", oldSessionId, session.getSessionId());
			assertEquals("Instance ID is new", nextId, session.getInstanceId().getId());
		}
	
		if(deviceDataChanged){
			Object nextEvent = eventQueue.queue.remove();
			assertTrue("UserInfo queued", nextEvent instanceof UserInfoEvent);
			//2 events are queued
			assertTrue("2 events are queued", eventQueue.isEmpty());
		} else {
			//1 events are queued
			assertTrue("1 event is queued", eventQueue.isEmpty());
		}
		
		verify(producerMock).start(session);
		verify(observerMock).setStateMachine(session);
		verify(eventWorker).start();
	}
	
	@Test 
	public void testMilestone(){
		//start up the session
		testStartNewDevice();
		session.customEvent(CustomEventType.CUSTOM_EVENT_1);
		
		Object event = eventQueue.queue.remove();
		assertTrue("Milestone queued", event instanceof CustomEvent);
	}
	
	@Test 
	public void testMilestoneNoStart(){
		session.customEvent(CustomEventType.CUSTOM_EVENT_1);
		assertTrue("No events were queued", eventQueue.queue.isEmpty());
	}
	

	@Test
	public void testTransaction(){
		testStartNewDevice();
		
		float price = .99f;
		int quantity = 10;
		session.transactionInUSD(price, quantity);
		Object event = eventQueue.queue.remove();
		assertTrue("Transaction queued", event instanceof TransactionEvent);
	}
	
	@Test 
	public void testTransactionNoStart(){
		float price = .99f;
		int quantity = 10;
		session.transactionInUSD(price, quantity);
		assertTrue("No events were queued", eventQueue.queue.isEmpty());
	}
	
	@Test
	public void testAttribution(){
		testStartNewDevice();
		String source = "source";
		String campaign = "campaign";
		session.attributeInstall(source, campaign, null);
		Object event = eventQueue.queue.remove();
		assertTrue("Transaction queued", event instanceof UserInfoEvent);
	}
	
	@Test
	public void testAttributionNoStart(){
		String source = "source";
		String campaign = "campaign";
		session.attributeInstall(source, campaign, null);
		assertTrue("No events were queued", eventQueue.queue.isEmpty());
	}
	
	@Test
	public void testOnActivityResume(){
		testStartNewDevice();
		session.onActivityResumed(activityMock);
		verify(observerMock).observeNewActivity(activityMock, session);
		verify(messagingManagerMock).onActivityResumed(activityMock);
	}
	
	@Test
	public void testOnActivityResumeNoStart(){
		session.onActivityResumed(activityMock);
		verify(observerMock, Mockito.never()).observeNewActivity(activityMock, session);
		verify(messagingManagerMock, Mockito.never()).onActivityResumed(activityMock);
	}
	
	@Test
	public void testOnActivityPause(){
		testStartNewDevice();
		session.onActivityPaused(activityMock);
		verify(observerMock).forgetLastActivity();
		verify(messagingManagerMock).onActivityPaused(activityMock);
	}
	
	@Test
	public void testOnActivityPauseNoStart(){
		session.onActivityPaused(activityMock);
		verify(observerMock, Mockito.never()).forgetLastActivity();
		verify(messagingManagerMock, Mockito.never()).onActivityPaused(activityMock);
	}
	
	@Test
	public void testPauseResume(){
		testStartNewDevice();
		
		session.pause();
		verify(producerMock).stop();
		verify(eventWorker).stop();
		
		session.resume();
		verify(producerMock, Mockito.atMost(2)).start(session);
		verify(eventWorker, Mockito.atMost(2)).start();
		
		Object pauseEvent = eventQueue.queue.remove();
		assertTrue("Pause event queued", pauseEvent instanceof AppPauseEvent);
		Object resumeEvent = eventQueue.queue.remove();
		assertTrue("Resume event queued", resumeEvent instanceof AppResumeEvent);
	}
	
	@Test
	public void testPauseResumeNoStart(){
		session.pause();
		session.resume();
		assertTrue("No events were queued", eventQueue.queue.isEmpty());
	}
}

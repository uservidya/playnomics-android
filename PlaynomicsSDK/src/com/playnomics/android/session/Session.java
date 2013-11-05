package com.playnomics.android.session;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;

import com.playnomics.android.client.IEventQueue;
import com.playnomics.android.client.IEventWorker;
import com.playnomics.android.client.IHttpConnectionFactory;
import com.playnomics.android.events.AppPageEvent;
import com.playnomics.android.events.AppPauseEvent;
import com.playnomics.android.events.AppResumeEvent;
import com.playnomics.android.events.AppRunningEvent;
import com.playnomics.android.events.AppStartEvent;
import com.playnomics.android.events.CustomEvent;
import com.playnomics.android.events.ImplicitEvent;
import com.playnomics.android.events.TransactionEvent;
import com.playnomics.android.events.UserInfoEvent;
import com.playnomics.android.messaging.MessagingManager;
import com.playnomics.android.sdk.IPlaynomicsPlacementDelegate;
import com.playnomics.android.util.ContextWrapper;
import com.playnomics.android.util.EventTime;
import com.playnomics.android.util.IConfig;
import com.playnomics.android.util.LargeGeneratedId;
import com.playnomics.android.util.Logger;
import com.playnomics.android.util.Util;
import com.playnomics.android.util.Logger.LogLevel;

public class Session implements SessionStateMachine, TouchEventHandler,
		HeartBeatHandler, ICallbackProcessor {
	// session
	private SessionState sessionState;

	public SessionState getSessionState() {
		return sessionState;
	}

	private Logger logger;
	private IEventWorker eventWorker;
	private IEventQueue eventQueue;
	private Util util;
	private IConfig config;
	private ContextWrapper contextWrapper;
	private IActivityObserver observer;
	private IHeartBeatProducer producer;
	private MessagingManager messagingManager;

	// session data
	private Long applicationId;

	public Long getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}

	private String userId;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getBreadcrumbId() {
		return androidId;
	}

	private String androidId;

	public String getAndroidId() {
		return androidId;
	}

	private LargeGeneratedId sessionId;

	LargeGeneratedId getSessionId() {
		return sessionId;
	}

	private LargeGeneratedId instanceId;

	LargeGeneratedId getInstanceId() {
		return instanceId;
	}

	private EventTime sessionStartTime;

	EventTime getSessionStartTime() {
		return sessionStartTime;
	}

	private EventTime sessionPauseTime;

	EventTime getSessionPauseTime() {
		return sessionPauseTime;
	}

	private AtomicInteger sequence;
	private AtomicInteger touchEvents;
	private AtomicInteger allTouchEvents;
	private boolean enablePushNotifications;

	public void setEnabledPushNotifications(boolean value) {
		enablePushNotifications = value;
	}

	public Session(IConfig config, Util util,
			IHttpConnectionFactory connectionFactory, Logger logger,
			IEventQueue eventQueue, IEventWorker eventWorker,
			IActivityObserver activityObserver, IHeartBeatProducer producer,
			MessagingManager messagingManager) {
		this.logger = logger;
		this.sessionState = SessionState.NOT_STARTED;
		this.util = util;
		this.config = config;
		this.eventQueue = eventQueue;
		this.eventWorker = eventWorker;
		this.observer = activityObserver;
		this.producer = producer;
		this.messagingManager = messagingManager;
		// this is done to make Session more testable
		// we want MessagingManager to be mocked out when we start the session
		this.messagingManager.setSession(this);
	}

	public void start(ContextWrapper contextWrapper) {
		try {
			if (applicationId == null) {
				throw new NullPointerException("Application ID must be set");
			}

			// session start code here
			if (sessionState == SessionState.STARTED) {
				return;
			}

			if (sessionState == SessionState.PAUSED) {
				resume();
				return;
			}

			sessionState = SessionState.STARTED;
			this.contextWrapper = contextWrapper;
			boolean settingsChanged = contextWrapper
					.synchronizeDeviceSettings();

			androidId = util.getDeviceIdFromContext(contextWrapper.getContext());

			if (Util.stringIsNullOrEmpty(userId)) {
				userId = androidId;
			}

			sequence = new AtomicInteger(1);
			touchEvents = new AtomicInteger(0);
			allTouchEvents = new AtomicInteger(0);
			sequence = new AtomicInteger(0);

			// start the background UI service

			// send appRunning or appPage
			LargeGeneratedId lastSessionId = contextWrapper
					.getPreviousSessionId();

			EventTime lastEventTime = contextWrapper.getLastEventTime();
			GregorianCalendar threeMinutesAgo = new GregorianCalendar(
					Util.TIME_ZONE_GMT);
			threeMinutesAgo.add(Calendar.MINUTE, -3);

			boolean sessionLapsed = (lastEventTime != null && lastEventTime
					.compareTo(threeMinutesAgo) < 0) || lastSessionId == null;

			ImplicitEvent implicitEvent;
			if (sessionLapsed) {
				sessionId = new LargeGeneratedId(util);
				instanceId = sessionId;

				implicitEvent = new AppStartEvent(config, getSessionInfo(),
						instanceId);
				sessionStartTime = implicitEvent.getEventTime();
				contextWrapper.setLastSessionStartTime(sessionStartTime);
				contextWrapper.setPreviousSessionId(sessionId);
			} else {
				sessionId = lastSessionId;
				instanceId = new LargeGeneratedId(util);
				implicitEvent = new AppPageEvent(config, getSessionInfo(),
						instanceId);
				sessionStartTime = contextWrapper.getLastSessionStartTime();
			}

			eventQueue.enqueueEvent(implicitEvent);
			eventWorker.start();
			producer.start(this);
			observer.setStateMachine(this);

			if (settingsChanged) {
				onDeviceSettingsUpdated();
			}
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not start session");
			sessionState = SessionState.NOT_STARTED;
		}
	}

	public void pause() {
		try {
			if (sessionState != SessionState.STARTED) {
				return;
			}
			sessionPauseTime = new EventTime();
			AppPauseEvent event = new AppPauseEvent(config, getSessionInfo(),
					instanceId, sessionStartTime, sequence.get(),
					touchEvents.get(), allTouchEvents.get());
			sequence.incrementAndGet();
			eventQueue.enqueueEvent(event);
			eventWorker.stop();
			producer.stop();
			sessionState = SessionState.PAUSED;
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not pause session");
		}
	}

	public void resume() {
		try {
			if (sessionState != SessionState.PAUSED) {
				return;
			}

			AppResumeEvent event = new AppResumeEvent(config, getSessionInfo(),
					instanceId, sessionStartTime, sessionPauseTime,
					sequence.get());
			eventQueue.enqueueEvent(event);
			eventWorker.start();
			producer.start(this);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not pause session");
		}
	}

	public void onHeartBeat(long heartBeatIntervalSeconds) {
		try {
			sequence.incrementAndGet();
			AppRunningEvent event = new AppRunningEvent(config,
					getSessionInfo(), instanceId, sessionStartTime,
					sequence.get(), touchEvents.get(), allTouchEvents.get());
			eventQueue.enqueueEvent(event);
			// reset the touch events
			touchEvents.set(0);
			allTouchEvents.set(0);

		} catch (UnsupportedEncodingException exception) {
			logger.log(LogLevel.ERROR, exception, "Could not log appRunning");
		}
	}

	public void onTouchEventReceived() {
		touchEvents.incrementAndGet();
		allTouchEvents.incrementAndGet();
	}

	private GameSessionInfo getSessionInfo() {
		return new GameSessionInfo(applicationId, userId, androidId, sessionId);
	}

	private void assertSessionStarted() {
		if (!(sessionState == SessionState.STARTED || sessionState == SessionState.PAUSED)) {
			throw new IllegalStateException("Session must be started");
		}
	}

	private void onDeviceSettingsUpdated() throws UnsupportedEncodingException {
		if (enablePushNotifications
				&& contextWrapper.getPushRegistrationId() == null) {
			registerForPushNotifcations();
		} else {
			UserInfoEvent event = new UserInfoEvent(config, getSessionInfo(),
					contextWrapper.getPushRegistrationId(),
					util.getDeviceIdFromContext(contextWrapper.getContext()));
			eventQueue.enqueueEvent(event);
		}
	}

	void registerForPushNotifcations() {

	}

	// explicit events
	public void transactionInUSD(float priceInUSD, int quantity) {
		try {
			assertSessionStarted();
			TransactionEvent event = new TransactionEvent(config, util,
					getSessionInfo(), quantity, priceInUSD);
			eventQueue.enqueueEvent(event);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not send transaction");
		}
	}

	public void attributeInstall(String source, String campaign,
			Date installDate) {
		try {
			assertSessionStarted();
			UserInfoEvent event = new UserInfoEvent(config, getSessionInfo(),
					source, campaign, installDate);
			eventQueue.enqueueEvent(event);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex,
					"Could not send install attribution information");
		}
	}

	public void customEvent(String customEventName) {
		try {
			assertSessionStarted();
			CustomEvent event = new CustomEvent(config, util, getSessionInfo(),
					customEventName);
			eventQueue.enqueueEvent(event);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not send custom event");
		}
	}

	// activity pause/resume
	public void onActivityResumed(Activity activity) {
		try {
			assertSessionStarted();
			observer.observeNewActivity(activity, this);
			messagingManager.onActivityResumed(activity);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not attach activity");
		}
	}

	public void onActivityPaused(Activity activity) {
		try {
			assertSessionStarted();
			observer.forgetLastActivity();
			messagingManager.onActivityPaused(activity);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not detach activity");
		}
	}

	public void processUrlCallback(String url) {
		eventQueue.enqueueEventUrl(url);
	}
	
	/* Messaging */
	public void preloadPlacements(String[] placementNames){
		try{
			assertSessionStarted();
			messagingManager.preloadPlacements(placementNames);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not preload placements");
		}
	}
	
	public void showPlacement(String placementName, Activity activity, IPlaynomicsPlacementDelegate delegate){
		try{
			assertSessionStarted();
			messagingManager.showPlacement(placementName, activity, delegate);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not preload placements");
		}
	}
	
	public void hidePlacement(String placementName){
		try{
			assertSessionStarted();
			messagingManager.hidePlacement(placementName);
		} catch (Exception ex) {
			logger.log(LogLevel.ERROR, ex, "Could not preload placements");
		}
	}
}

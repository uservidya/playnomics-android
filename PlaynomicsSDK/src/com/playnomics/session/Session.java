package com.playnomics.session;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.concurrent.atomic.AtomicInteger;

import android.app.Activity;
import android.content.Context;

import com.playnomics.client.EventQueue;
import com.playnomics.client.EventWorker;
import com.playnomics.util.*;
import com.playnomics.util.Logger.LogLevel;
import com.playnomics.client.HttpConnectionFactory;
import com.playnomics.events.AppPageEvent;
import com.playnomics.events.AppPauseEvent;
import com.playnomics.events.AppResumeEvent;
import com.playnomics.events.AppRunningEvent;
import com.playnomics.events.AppStartEvent;
import com.playnomics.events.ImplicitEvent;
import com.playnomics.events.MilestoneEvent;
import com.playnomics.events.TransactionEvent;
import com.playnomics.events.UserInfoEvent;

public class Session implements SessionStateMachine, TouchEventHandler,
		HeartBeatHandler {
	// session
	private SessionState sessionState;

	public SessionState getSessionState() {
		return sessionState;
	}

	private EventWorker eventWorker;
	private EventQueue eventQueue;
	private Util util;
	private Config config;
	private static final Object syncLock = new Object();
	private ServiceManager serviceManager;
	private DeviceManager deviceManager;
	private UIObserver observer;
	private HeartBeatProducer producer;

	// session data
	private long applicationId;
	private String userId;
	private String breadcrumbId;
	private LargeGeneratedId sessionId;
	private LargeGeneratedId instanceId;
	private EventTime sessionStartTime;
	private EventTime sessionPauseTime;

	private AtomicInteger sequence;

	private AtomicInteger touchEvents;

	public int getTouchEvents() {
		return touchEvents.get();
	}

	private AtomicInteger allTouchEvents;

	public int getAllTouchEvents() {
		return allTouchEvents.get();
	}

	private boolean enablePushNotifications;

	public void setEnabledPushNotifications(boolean value) {
		enablePushNotifications = value;
	}

	private boolean testMode = false;

	public void setTestMode(boolean value) {
		testMode = value;
	}

	private String overrideEventsUrl;

	public void setOverrideEventsUrl(String url) {
		overrideEventsUrl = url;
	}

	public String getEventsUrl() {
		if (!util.stringIsNullOrEmpty(overrideEventsUrl)) {
			return overrideEventsUrl;
		}
		if (testMode) {
			return config.getTestEventsUrl();
		}
		return config.getProdEventsUrl();
	}

	private String overrideMessagingUrl;

	public void setOverrideMessagingUrl(String url) {
		overrideMessagingUrl = url;
	}

	public String getMessagingUrl() {
		if (!util.stringIsNullOrEmpty(overrideMessagingUrl)) {
			return overrideMessagingUrl;
		}
		if (testMode) {
			return config.getTestMessagingUrl();
		}
		return config.getProdMessagingUrl();
	}

	private Session() {
		sessionState = SessionState.NOT_STARTED;
		util = new Util();
		config = new Config();
		eventQueue = new EventQueue(util, getEventsUrl());
		eventWorker = new EventWorker(eventQueue, new HttpConnectionFactory());
		observer = new UIObserver(this, this);
		producer = new HeartBeatProducer(this, config);
	}

	private static Session instance;

	public static Session getInstance() {
		synchronized (Session.syncLock) {
			if (instance == null) {
				instance = new Session();
			}
			return Session.instance;
		}
	}

	// Session life-cycle
	public void start(long applicationId, String userId, Context context) {
		this.userId = userId;
		start(applicationId, context);
	}

	public void start(long applicationId, Context context) {

		try {
			this.applicationId = applicationId;
			// session start code here
			if (sessionState == SessionState.STARTED) {
				return;
			}

			if (sessionState == SessionState.PAUSED) {
				resume();
				return;
			}

			sessionState = SessionState.STARTED;

			serviceManager = new ServiceManager(context);
			deviceManager = new DeviceManager(context, serviceManager);
			boolean settingsChanged = deviceManager.synchronizeDeviceSettings();

			breadcrumbId = deviceManager.getAndroidDeviceId();

			if (util.stringIsNullOrEmpty(userId)) {
				userId = breadcrumbId;
			}

			sequence = new AtomicInteger(1);
			touchEvents = new AtomicInteger(0);
			allTouchEvents = new AtomicInteger(0);
			sequence = new AtomicInteger(0);

			// start the background UI service

			// send appRunning or appPage
			LargeGeneratedId lastSessionId = deviceManager
					.getPreviousSessionId();

			EventTime lastEventTime = deviceManager.getLastEventTime();
			GregorianCalendar threeMinutesAgo = new GregorianCalendar(
					Util.TIME_ZONE_GMT);
			threeMinutesAgo.add(Calendar.MINUTE, -3);

			boolean sessionLapsed = (lastEventTime.compareTo(threeMinutesAgo) < 0)
					|| lastSessionId == null;

			ImplicitEvent implicitEvent;
			if (sessionLapsed) {
				sessionId = new LargeGeneratedId(util);
				instanceId = sessionId;

				implicitEvent = new AppStartEvent(config, getSessionInfo(),
						instanceId);
				sessionStartTime = implicitEvent.getEventTime();
				deviceManager.setLastSessionStartTime(sessionStartTime);
			} else {
				sessionId = lastSessionId;
				instanceId = new LargeGeneratedId(util);
				implicitEvent = new AppPageEvent(config, getSessionInfo(),
						instanceId);
				sessionStartTime = deviceManager.getLastSessionStartTime();
			}

			eventQueue.enqueueEvent(implicitEvent);
			eventWorker.start();
			producer.start();
			if (settingsChanged) {
				onDeviceSettingsUpdated();
			}
		} catch (Exception ex) {
			Logger.log(LogLevel.ERROR, ex, "Could not start session");
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
					getTouchEvents(), getAllTouchEvents());
			sequence.incrementAndGet();
			eventQueue.enqueueEvent(event);
			eventWorker.stop();
			producer.stop();
		} catch (Exception ex) {
			Logger.log(LogLevel.ERROR, ex, "Could not pause session");
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
			producer.start();
		} catch (Exception ex) {
			Logger.log(LogLevel.ERROR, ex, "Could not pause session");
		}
	}

	public void onHeartBeat(int heartBeatIntervalSeconds) {
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
			Logger.log(LogLevel.ERROR, exception, "Could not log appRunning");
		}
	}

	public void onTouchEventReceived() {
		touchEvents.incrementAndGet();
		allTouchEvents.incrementAndGet();
	}

	private GameSessionInfo getSessionInfo() {
		return new GameSessionInfo(applicationId, userId, breadcrumbId,
				sessionId);
	}

	private void assertSessionStarted() {
		if (sessionState != SessionState.STARTED
				|| sessionState != SessionState.PAUSED) {
			throw new IllegalStateException("Session must be started");
		}
	}

	private void onDeviceSettingsUpdated() throws UnsupportedEncodingException {
		if (enablePushNotifications
				&& deviceManager.getPushRegistrationId() == null) {
			registerForPushNotifcations();
		} else {
			UserInfoEvent event = new UserInfoEvent(config, getSessionInfo(),
					deviceManager.getPushRegistrationId(),
					deviceManager.getAndroidDeviceId());
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
			Logger.log(LogLevel.ERROR, ex, "Could not send transaction");
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
			Logger.log(LogLevel.ERROR, ex,
					"Could not send install attribution information");
		}
	}

	public void milestone(MilestoneEvent.MilestoneType milestoneType) {
		try {
			assertSessionStarted();
			MilestoneEvent event = new MilestoneEvent(config, util,
					getSessionInfo(), milestoneType);
			eventQueue.enqueueEvent(event);
		} catch (Exception ex) {
			Logger.log(LogLevel.ERROR, ex, "Could not send milestone");
		}
	}

	// activity attach/detach

	public void attachActivity(Activity activity) {
		observer.observeNewActivity(activity);
	}

	public void detachActivity() {
		observer.forgetLastActivity();
	}
}

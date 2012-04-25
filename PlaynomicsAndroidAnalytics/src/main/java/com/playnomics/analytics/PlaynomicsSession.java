package com.playnomics.analytics;

import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.provider.Settings.Secure;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.playnomics.analytics.PlaynomicsEvent.EventType;

public class PlaynomicsSession {
	
	private static final String TAG = PlaynomicsSession.class.getSimpleName();
	// TODO: Externalize this string
	private static final String PLAYNOMICS_BASE_URL = "https://test.b.playnomics.net/v1/";
	private static final String SETTING_LAST_SESSION_ID = "lastSessionId";
	private static final String SETTING_EVENT_LIST = "eventList";
	
	private static final int UPDATE_INTERVAL = 60000;
	private static Timer eventTimer;
	private static List<PlaynomicsEvent> eventList = new CopyOnWriteArrayList<PlaynomicsEvent>();
	private static PlaynomicsTimerTask timerTask = new PlaynomicsTimerTask();
	private static Activity activity;
	private static Callback activityCallback;
	private static SharedPreferences settings;
	private static Editor editor;
	
	// Tracking values
	private static int collectMode = 7;
	
	private static int sequence = 0;
	
	private static String applicationId;
	private static String cookieId;
	private static String sessionId;
	private static String instanceId;
	private static Date sessionStartTime;
	private static String userId;
	private static int timeZoneOffset;
	private static int clicks;
	private static int totalClicks;
	private static int keys;
	private static int totalKeys;
	
	private static Date pauseTime;
	
	private static enum SessionStates {
		UNKNOWN, STARTED, PAUSED, STOPPED
	}
	
	private static SessionStates sessionState = SessionStates.UNKNOWN;
	
	// Prevent instantiation
	private PlaynomicsSession() {
	
	}
	
	public static void start(Activity activity, String apiKey, String userId) {
	
		PlaynomicsSession.userId = userId;
		start(activity, apiKey);
	}
	
	public static void start(Activity activity, String applicationId) {
	
		if (sessionState == SessionStates.STARTED)
			return;
		
		// If paused, resume and get out of here
		if (sessionState == SessionStates.PAUSED) {
			resume();
			return;
		}
		
		sessionState = SessionStates.STARTED;
		PlaynomicsSession.applicationId = applicationId;
		
		PlaynomicsSession.activity = activity;
		activityCallback = activity.getWindow().getCallback();
		
		// Startup the event timer to send events back to the server
		if (eventTimer == null) {
			eventTimer = new Timer(true);
			eventTimer.scheduleAtFixedRate(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
		}
		
		activity.getWindow().setCallback(new Callback() {
			
			@Override
			public ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
			
				return activityCallback.onWindowStartingActionMode(callback);
			}
			
			@Override
			public void onWindowFocusChanged(boolean hasFocus) {
			
				activityCallback.onWindowFocusChanged(hasFocus);
				
				Log.i(TAG, "onWindowFocusChanged: " + hasFocus);
				// Are we pausing?
				// CR: could this be another activity in the same app?
				if (!hasFocus) {
					pause();
				}
			}
			
			@Override
			public void onWindowAttributesChanged(android.view.WindowManager.LayoutParams attrs) {
			
				activityCallback.onWindowAttributesChanged(attrs);
			}
			
			@Override
			public boolean onSearchRequested() {
			
				return activityCallback.onSearchRequested();
			}
			
			@Override
			public boolean onPreparePanel(int featureId, View view, Menu menu) {
			
				return activityCallback.onPreparePanel(featureId, view, menu);
			}
			
			@Override
			public void onPanelClosed(int featureId, Menu menu) {
			
				activityCallback.onPanelClosed(featureId, menu);
			}
			
			@Override
			public boolean onMenuOpened(int featureId, Menu menu) {
			
				return activityCallback.onMenuOpened(featureId, menu);
			}
			
			@Override
			public boolean onMenuItemSelected(int featureId, MenuItem item) {
			
				return activityCallback.onMenuItemSelected(featureId, item);
			}
			
			@Override
			public void onDetachedFromWindow() {
			
				activityCallback.onDetachedFromWindow();
			}
			
			@Override
			public View onCreatePanelView(int featureId) {
			
				return activityCallback.onCreatePanelView(featureId);
			}
			
			@Override
			public boolean onCreatePanelMenu(int featureId, Menu menu) {
			
				return activityCallback.onCreatePanelMenu(featureId, menu);
			}
			
			@Override
			public void onContentChanged() {
			
				activityCallback.onContentChanged();
			}
			
			@Override
			public void onAttachedToWindow() {
			
				activityCallback.onAttachedToWindow();
			}
			
			@Override
			public void onActionModeStarted(ActionMode mode) {
			
				activityCallback.onActionModeStarted(mode);
			}
			
			@Override
			public void onActionModeFinished(ActionMode mode) {
			
				activityCallback.onActionModeFinished(mode);
			}
			
			@Override
			public boolean dispatchTrackballEvent(MotionEvent event) {
			
				return activityCallback.dispatchTrackballEvent(event);
			}
			
			@Override
			public boolean dispatchTouchEvent(MotionEvent event) {
			
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					clicks += 1;
					totalClicks += 1;
				}
				return activityCallback.dispatchTouchEvent(event);
			}
			
			@Override
			public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent event) {
			
				return activityCallback.dispatchPopulateAccessibilityEvent(event);
			}
			
			@Override
			public boolean dispatchKeyShortcutEvent(KeyEvent event) {
			
				return activityCallback.dispatchKeyShortcutEvent(event);
			}
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
			
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					keys += 1;
					totalKeys += 1;
				}
				
				return activityCallback.dispatchKeyEvent(event);
			}
			
			@Override
			public boolean dispatchGenericMotionEvent(MotionEvent event) {
			
				return activityCallback.dispatchGenericMotionEvent(event);
			}
		});
		
		sequence = 1;
		clicks = 0;
		totalClicks = 0;
		keys = 0;
		totalKeys = 0;
		
		sessionStartTime = new Date();
		
		// Calc to conform to wacky javascript format
		timeZoneOffset = TimeZone.getDefault().getRawOffset() / -60000;
		collectMode = 7;
		
		settings = activity.getSharedPreferences("playnomics", Context.MODE_PRIVATE);
		editor = settings.edit();
		
		EventType eventType;
		
		// Send an appStart if it has been > 3 min since the last session;
		// otherwise send an appPage
		if (settings.getLong("lastSessionStartTime", 0) - sessionStartTime.getTime() > 180000) {
			sessionId = RandomGenerator.createRandomHex();
			editor.putString(SETTING_LAST_SESSION_ID, sessionId);
			instanceId = sessionId;
			eventType = EventType.appStart;
		}
		else {
			sessionId = settings.getString(SETTING_LAST_SESSION_ID, "");
			instanceId = RandomGenerator.createRandomHex();
			eventType = EventType.appPage;
		}
		
		editor.putLong("lastSessionStartTime", sessionStartTime.getTime());
		editor.commit();
		
		// Get unique ID for device; may be null on emulator
		cookieId = Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID);
		if (cookieId == null)
			cookieId = "UNKNOWN_DEVICE_ID";
		
		
		// Restore event list from persistent storage
		Gson gson = new Gson();
		String json = settings.getString(SETTING_EVENT_LIST, null);
		
		if (json != null) {
			Type collectionType = new TypeToken<List<PlaynomicsEvent>>() {
			}.getType();
			List<PlaynomicsEvent> savedEventList = gson.fromJson(json, collectionType);
			eventList.addAll(savedEventList);
		}
		
		PlaynomicsEvent pe = new PlaynomicsEvent(eventType, applicationId, userId, cookieId, sessionId,
			instanceId, timeZoneOffset);
		eventList.add(pe);
	}
	
	public static void pause() {
	
		if (sessionState.equals(SessionStates.PAUSED))
			return;
		
		sessionState = SessionStates.PAUSED;
		
		PlaynomicsEvent pe = new PlaynomicsEvent(EventType.appPause, PlaynomicsSession.applicationId, userId,
			cookieId, sessionId, instanceId, timeZoneOffset);
		pauseTime = new Date();
		pe.setSessionStartTime(sessionStartTime);
		eventList.add(pe);
	}
	
	public static void resume() {
	
		if (sessionState.equals(SessionStates.STARTED))
			return;
		
		sessionState = SessionStates.STARTED;
		PlaynomicsEvent pe = new PlaynomicsEvent(EventType.appResume, applicationId, userId,
			cookieId, sessionId, instanceId, timeZoneOffset);
		pe.setPauseTime(pauseTime);
		pe.setSessionStartTime(sessionStartTime);
		sequence += 1;
		pe.setSequence(sequence);
		eventList.add(pe);
	}
	
	public static void stop() {
	
		if (sessionState.equals(SessionStates.STOPPED))
			return;
		
		sessionState = SessionStates.STOPPED;
		PlaynomicsEvent pe = new PlaynomicsEvent(EventType.appStop, applicationId, userId, cookieId, sessionId,
			instanceId, timeZoneOffset);
		eventList.add(pe);
		// TODO: Revisit stopping the timer logic; we might want to keep trying
		// if offline
		timerTask.run();
		eventTimer.cancel();
		
		// Save eventList to disk for later sending
		if (eventList.size() > 0) {
			Gson gson = new Gson();
			String json = gson.toJson(eventList);
			editor.putString(SETTING_EVENT_LIST, json);
			editor.commit();
		}
		
		// Restore original callback
		activity.getWindow().setCallback(activityCallback);
	}
	
	private static class PlaynomicsTimerTask extends TimerTask {
		
		@Override
		public void run() {
		
			sequence += 1;
			PlaynomicsEvent runningPE = new PlaynomicsEvent(EventType.appRunning, applicationId, userId, cookieId,
				sessionId, instanceId, sessionStartTime, sequence, clicks, totalClicks, keys, totalKeys, collectMode);
			eventList.add(runningPE);
			
			// Reset keys/clicks
			keys = 0;
			clicks = 0;
			
			for (PlaynomicsEvent pe : eventList) {
				
				// Send the event and remove if successful
				if (sendToServer(pe))
					eventList.remove(pe);
			}
		}
		
		private boolean sendToServer(PlaynomicsEvent pe) {
		
			try {
				// Set common params
				String eventUrl = PLAYNOMICS_BASE_URL
					+ pe.getEventType()
					+ "?t=" + pe.getEventTime().getTime()
					+ "&a=" + pe.getApplicationId()
					+ "&b=" + pe.getCookieId()
					+ "&s=" + pe.getSessionId()
					+ "&i=" + pe.getInstanceId();
				
				switch (pe.getEventType()) {
				
					case appStart:
					case appPage:
						eventUrl += "&z=" + pe.getTimeZoneOffset();
						break;
					
					case appRunning:
						eventUrl += "&r=" + pe.getSessionStartTime().getTime()
							+ "&q=" + pe.getSequence()
							+ "&d=" + UPDATE_INTERVAL
							+ "&c=" + pe.getClicks()
							+ "&e=" + pe.getTotalClicks()
							+ "&k=" + pe.getKeys()
							+ "&l=" + pe.getTotalKeys()
							+ "&m=" + pe.getCollectMode();
						break;
					
					case appResume:
						eventUrl += "&p=" + pe.getPauseTime().getTime();
					case appPause:
						eventUrl += "&r=" + pe.getSessionStartTime().getTime()
							+ "&q=" + pe.getSequence();
				}
				
				Log.i(TAG, "Sending event to server: " + eventUrl);
				HttpURLConnection con = (HttpURLConnection) new URL(eventUrl).openConnection();
				// con.setRequestMethod("HEAD");
				return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
			} catch (Exception e) {
//				Log.e(TAG, e.getMessage());
				e.printStackTrace();
				return false;
			}
		}
	}
}

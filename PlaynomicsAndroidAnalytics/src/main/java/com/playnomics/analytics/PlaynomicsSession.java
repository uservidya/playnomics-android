package com.playnomics.analytics;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.provider.Settings.Secure;
import android.util.Log;
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
import com.playnomics.analytics.SocialEvent.ResponseType;
import com.playnomics.analytics.TransactionEvent.CurrencyCategory;
import com.playnomics.analytics.TransactionEvent.TransactionType;
import com.playnomics.analytics.UserInfoEvent.UserInfoSex;
import com.playnomics.analytics.UserInfoEvent.UserInfoSource;
import com.playnomics.analytics.UserInfoEvent.UserInfoType;

public class PlaynomicsSession {
	
	public enum APIResult {
		SENT, QUEUED, STOPPED, ALREADY_STARTED, ALREADY_STOPPED, SESSION_RESUMED, START_NOT_CALLED, NO_INTERNET_PERMISSION, FAIL_UNKNOWN
	};
	
	private static final String TAG = PlaynomicsSession.class.getSimpleName();
	private static final String SETTING_LAST_SESSION_START_TIME = "lastSessionStartTime";
	private static final String SETTING_LAST_USER_ID = "lastUserId";
	private static final String SETTING_LAST_SESSION_ID = "lastSessionId";
	private static final String SETTING_BASIC_EVENT_LIST = "basicEventList";
	private static final String SETTING_USER_INFO_EVENT_LIST = "userInfoEventList";
	private static final String SETTING_GAME_EVENT_LIST = "gameEventList";
	private static final String SETTING_TRANSACTION_EVENT_LIST = "transactionEventList";
	private static final String SETTING_SOCIAL_EVENT_LIST = "socialEventList";
	
	private static final int UPDATE_INTERVAL = 60000;
	private static final int COLLECTION_MODE = 7;
	private static boolean hasInternetPermission = true;
	private static Timer eventTimer;
	
	// Event lists for various types of events
	private static List<BasicEvent> basicEventList = new CopyOnWriteArrayList<BasicEvent>();
	private static List<UserInfoEvent> userInfoEventList = new CopyOnWriteArrayList<UserInfoEvent>();
	private static List<GameEvent> gameEventList = new CopyOnWriteArrayList<GameEvent>();
	private static List<TransactionEvent> transactionEventList = new CopyOnWriteArrayList<TransactionEvent>();
	private static List<SocialEvent> socialEventList = new CopyOnWriteArrayList<SocialEvent>();
	
	private static PlaynomicsTimerTask timerTask = new PlaynomicsTimerTask();
	private static Activity activity;
	private static Callback activityCallback;
	private static SharedPreferences settings;
	private static Editor editor;
	private static EventSender eventSender = new EventSender();
	
	// Tracking values
	private static int collectMode = 7;
	private static int sequence = 0;
	
	private static String applicationId;
	private static String cookieId;
	private static String sessionId;
	private static String instanceId;
	private static Date sessionStartTime;
	private static String userId = "";
	private static int timeZoneOffset;
	private static int clicks;
	private static int totalClicks;
	private static int keys;
	private static int totalKeys;
	
	private static Date pauseTime;
	
	private static enum SessionState {
		UNKNOWN, STARTED, PAUSED, STOPPED
	}
	
	private static SessionState sessionState = SessionState.UNKNOWN;
	
	// Prevent instantiation
	private PlaynomicsSession() {
	
	}
	
	public static APIResult start(Activity activity, String apiKey, String userId) {
	
		PlaynomicsSession.userId = userId;
		return start(activity, apiKey);
	}
	
	public static APIResult start(Activity activity, String applicationId) {
	
		Log.i(TAG, "start() called");
		
		if (activity.getApplication().getPackageManager()
			.checkPermission(permission.INTERNET, activity.getApplication().getPackageName()) != PackageManager.PERMISSION_GRANTED) {
			hasInternetPermission = false;
			return APIResult.NO_INTERNET_PERMISSION;
		}
		
		if (sessionState == SessionState.STARTED)
			return APIResult.ALREADY_STARTED;
		
		// If paused, resume and get out of here
		if (sessionState == SessionState.PAUSED) {
			resume();
			return APIResult.SESSION_RESUMED;
		}
		
		sessionState = SessionState.STARTED;
		PlaynomicsSession.applicationId = applicationId;
		
		PlaynomicsSession.activity = activity;
		activityCallback = activity.getWindow().getCallback();
		
		// Startup the event timer to send events back to the server
		if (eventTimer == null) {
			eventTimer = new Timer(true);
			eventTimer.scheduleAtFixedRate(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
		}
		
		activity.getWindow().setCallback(new Callback() {
			
			// @Override
			// public ActionMode
			// onWindowStartingActionMode(android.view.ActionMode.Callback
			// callback) {
			//
			// return activityCallback.onWindowStartingActionMode(callback);
			// }
			
			@Override
			public void onWindowFocusChanged(boolean hasFocus) {
			
				activityCallback.onWindowFocusChanged(hasFocus);
				
				Log.i(TAG, "onWindowFocusChanged: " + hasFocus);
				// Are we pausing?
				// CR: could this be another activity in the same app?
				if (hasFocus) {
					resume();
				}
				else {
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
			
			// @Override
			// public void onActionModeStarted(ActionMode mode) {
			//
			// activityCallback.onActionModeStarted(mode);
			// }
			//
			// @Override
			// public void onActionModeFinished(ActionMode mode) {
			//
			// activityCallback.onActionModeFinished(mode);
			// }
			
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
			
			// @Override
			// public boolean dispatchKeyShortcutEvent(KeyEvent event) {
			//
			// return activityCallback.dispatchKeyShortcutEvent(event);
			// }
			
			@Override
			public boolean dispatchKeyEvent(KeyEvent event) {
			
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					keys += 1;
					totalKeys += 1;
				}
				
				return activityCallback.dispatchKeyEvent(event);
			}
			
			// @Override
			// public boolean dispatchGenericMotionEvent(MotionEvent event) {
			//
			// return activityCallback.dispatchGenericMotionEvent(event);
			// }
		});
		
		sequence = 1;
		clicks = 0;
		totalClicks = 0;
		keys = 0;
		totalKeys = 0;
		
		sessionStartTime = new Date();
		
		// Calc to conform to minute offset format
		timeZoneOffset = TimeZone.getDefault().getRawOffset() / -60000;
		// Collection mode for Android
		collectMode = COLLECTION_MODE;
		
		settings = activity.getSharedPreferences("playnomics", Context.MODE_PRIVATE);
		editor = settings.edit();
		
		// TODO: Make a generic method using reflection
		// Restore event lists from persistent storage
		Gson gson = new Gson();
		String json = settings.getString(SETTING_BASIC_EVENT_LIST, null);
		if (json != null) {
			Type collectionType = new TypeToken<List<BasicEvent>>() {
			}.getType();
			List<BasicEvent> savedEventList = gson.fromJson(json, collectionType);
			basicEventList.addAll(savedEventList);
		}
		
		json = settings.getString(SETTING_USER_INFO_EVENT_LIST, null);
		if (json != null) {
			Type collectionType = new TypeToken<List<UserInfoEvent>>() {
			}.getType();
			List<UserInfoEvent> savedEventList = gson.fromJson(json, collectionType);
			userInfoEventList.addAll(savedEventList);
		}
		
		json = settings.getString(SETTING_GAME_EVENT_LIST, null);
		if (json != null) {
			Type collectionType = new TypeToken<List<GameEvent>>() {
			}.getType();
			List<GameEvent> savedEventList = gson.fromJson(json, collectionType);
			gameEventList.addAll(savedEventList);
		}
		
		json = settings.getString(SETTING_TRANSACTION_EVENT_LIST, null);
		if (json != null) {
			Type collectionType = new TypeToken<List<TransactionEvent>>() {
			}.getType();
			List<TransactionEvent> savedEventList = gson.fromJson(json, collectionType);
			transactionEventList.addAll(savedEventList);
		}
		
		json = settings.getString(SETTING_SOCIAL_EVENT_LIST, null);
		if (json != null) {
			Type collectionType = new TypeToken<List<SocialEvent>>() {
			}.getType();
			List<SocialEvent> savedEventList = gson.fromJson(json, collectionType);
			socialEventList.addAll(savedEventList);
		}
		
		EventType eventType;
		
		// Send an appStart if it has been > 3 min since the last session or a
		// different user
		// otherwise send an appPage
		if (sessionStartTime.getTime() - settings.getLong(SETTING_LAST_SESSION_START_TIME, 0) > 180000
			|| !settings.getString(SETTING_LAST_USER_ID, "").equals(userId)) {
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
		
		editor.putString(SETTING_LAST_USER_ID, userId);
		editor.putLong(SETTING_LAST_SESSION_START_TIME, sessionStartTime.getTime());
		editor.commit();
		
		// Get unique ID for device; may be null on emulator
		cookieId = Secure.getString(activity.getContentResolver(), Secure.ANDROID_ID);
		if (cookieId == null)
			cookieId = "UNKNOWN_DEVICE_ID";
		
		BasicEvent be = new BasicEvent(eventType, applicationId, userId, cookieId, sessionId,
			instanceId, timeZoneOffset);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(be)) {
			return APIResult.SENT;
		}
		else {
			basicEventList.add(be);
			return APIResult.QUEUED;
		}
	}
	
	private static void pause() {
	
		Log.i(TAG, "pause() called");
		if (sessionState.equals(SessionState.PAUSED))
			return;
		
		sessionState = SessionState.PAUSED;
		
		BasicEvent be = new BasicEvent(EventType.appPause, PlaynomicsSession.applicationId, userId,
			cookieId, sessionId, instanceId, timeZoneOffset);
		pauseTime = new Date();
		be.setSessionStartTime(sessionStartTime);
		// Try to send and queue if unsuccessful
		if (!eventSender.sendToServer(be))
			basicEventList.add(be);
	}
	
	private static void resume() {
	
		Log.i(TAG, "resume() called");
		if (sessionState.equals(SessionState.STARTED))
			return;
		
		sessionState = SessionState.STARTED;
		BasicEvent be = new BasicEvent(EventType.appResume, applicationId, userId,
			cookieId, sessionId, instanceId, timeZoneOffset);
		be.setPauseTime(pauseTime);
		be.setSessionStartTime(sessionStartTime);
		sequence += 1;
		be.setSequence(sequence);
		// Try to send and queue if unsuccessful
		if (!eventSender.sendToServer(be))
			basicEventList.add(be);
	}
	
	public static APIResult stop() {
	
		Log.i(TAG, "stop() called");
		if (sessionState.equals(SessionState.STOPPED))
			return APIResult.ALREADY_STOPPED;
		
		sessionState = SessionState.STOPPED;
		eventTimer.cancel();
		
		// Save eventLists to disk for later sending
		saveEventList(SETTING_BASIC_EVENT_LIST, basicEventList);
		saveEventList(SETTING_USER_INFO_EVENT_LIST, userInfoEventList);
		saveEventList(SETTING_GAME_EVENT_LIST, gameEventList);
		saveEventList(SETTING_TRANSACTION_EVENT_LIST, transactionEventList);
		saveEventList(SETTING_SOCIAL_EVENT_LIST, socialEventList);
		
		// Restore original callback
		activity.getWindow().setCallback(activityCallback);
		return APIResult.STOPPED;
		
	}
	
	private static void saveEventList(String setting, List<?> eventList) {
	
		if (eventList.size() > 0) {
			Gson gson = new Gson();
			String json = gson.toJson(eventList);
			editor.putString(setting, json);
			editor.commit();
		}
	}
	
	public static APIResult userInfo() throws StartNotCalledException {
	
		return userInfo(UserInfoType.update, null, null, null, null, null, null, null);
	}
	
	public static APIResult userInfo(UserInfoType type, String country, String subdivision, UserInfoSex sex,
		Date birthday, UserInfoSource source, String sourceCampaign, Date installTime) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}
		
		UserInfoEvent uie = new UserInfoEvent(applicationId, userId, type, country, subdivision, sex, birthday,
			source, sourceCampaign, installTime);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(uie)) {
			return APIResult.SENT;
		}
		else {
			userInfoEventList.add(uie);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult sessionStart(String sessionId, String site) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		GameEvent ge = new GameEvent(EventType.sessionStart, applicationId, userId, sessionId, site,
			null, null, null, null);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(ge)) {
			return APIResult.SENT;
		}
		else {
			gameEventList.add(ge);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult sessionEnd(String sessionId, String reason) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		GameEvent ge = new GameEvent(EventType.sessionEnd, applicationId, userId, sessionId, null,
			null, null, null, reason);
		// Try to send and queue if unsuccessful
		
		if (eventSender.sendToServer(ge)) {
			return APIResult.SENT;
		}
		else {
			gameEventList.add(ge);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult gameStart(String instanceId, String sessionId, String site, String type, String gameId) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		GameEvent ge = new GameEvent(EventType.gameStart, applicationId, userId, sessionId, site,
			instanceId, type, gameId, null);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(ge)) {
			return APIResult.SENT;
		}
		else {
			gameEventList.add(ge);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult gameEnd(String instanceId, String sessionId, String reason) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		GameEvent ge = new GameEvent(EventType.gameEnd, applicationId, userId, sessionId, null,
			instanceId, null, null, reason);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(ge)) {
			return APIResult.SENT;
		}
		else {
			gameEventList.add(ge);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult transaction(long transactionId, String itemId, double quantity, TransactionType type,
		String otherUserId, String[] currencyTypes, double[] currencyValues, CurrencyCategory[] currencyCategories) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		TransactionEvent te = new TransactionEvent(EventType.transaction, applicationId, userId, transactionId, itemId,
			quantity, type, otherUserId, currencyTypes, currencyValues, currencyCategories);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(te)) {
			return APIResult.SENT;
		}
		else {
			transactionEventList.add(te);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult invitationSent(String invitationId, String recipientUserId, String recipientAddress,
		String method) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		SocialEvent se = new SocialEvent(EventType.invitationSent, applicationId, userId, invitationId,
			recipientUserId, recipientAddress, method, null);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(se)) {
			return APIResult.SENT;
		}
		else {
			socialEventList.add(se);
			return APIResult.QUEUED;
		}
	}
	
	public static APIResult invitationResponse(String invitationId, ResponseType response) {
	
		if (sessionState != SessionState.STARTED) {
			return APIResult.START_NOT_CALLED;
		}
		else if (!hasInternetPermission) {
			return APIResult.NO_INTERNET_PERMISSION;
		}

		SocialEvent se = new SocialEvent(EventType.invitationResponse, applicationId, userId, invitationId, null, null,
			null, response);
		// Try to send and queue if unsuccessful
		if (eventSender.sendToServer(se)) {
			return APIResult.SENT;
		}
		else {
			socialEventList.add(se);
			return APIResult.QUEUED;
		}
	}
	
	protected static class PlaynomicsTimerTask extends TimerTask {
		
		@Override
		public void run() {
		
			// Only send appRunning event if we are started
			if (sessionState == SessionState.STARTED) {
				sequence += 1;
				BasicEvent runningBE = new BasicEvent(EventType.appRunning, applicationId, userId, cookieId,
					sessionId, instanceId, sessionStartTime, sequence, clicks, totalClicks, keys, totalKeys, collectMode);
				basicEventList.add(runningBE);
				
				// Reset keys/clicks
				keys = 0;
				clicks = 0;				
			}
			
			for (BasicEvent be : basicEventList) {
				
				if (eventSender.sendToServer(be))
					basicEventList.remove(be);
			}
			
			for (UserInfoEvent uie : userInfoEventList) {
				
				if (eventSender.sendToServer(uie))
					userInfoEventList.remove(uie);
			}
			
			for (GameEvent ge : gameEventList) {
				
				if (eventSender.sendToServer(ge))
					gameEventList.remove(ge);
			}
			
			for (TransactionEvent te : transactionEventList) {
				
				if (eventSender.sendToServer(te))
					gameEventList.remove(te);
			}
			
			for (SocialEvent se : socialEventList) {
				
				if (eventSender.sendToServer(se))
					gameEventList.remove(se);
			}
		}
	}
}

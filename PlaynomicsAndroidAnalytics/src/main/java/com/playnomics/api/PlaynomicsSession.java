/*
 * 
 */
package com.playnomics.api;

import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android.Manifest.permission;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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

import com.playnomics.api.PlaynomicsConstants.CurrencyCategory;
import com.playnomics.api.PlaynomicsConstants.ResponseType;
import com.playnomics.api.PlaynomicsConstants.TransactionType;
import com.playnomics.api.PlaynomicsConstants.UserInfoSex;
import com.playnomics.api.PlaynomicsConstants.UserInfoSource;
import com.playnomics.api.PlaynomicsConstants.UserInfoType;
import com.playnomics.api.PlaynomicsEvent.EventType;

// TODO: Auto-generated Javadoc
/**
 * The Class PlaynomicsSession.
 */
public class PlaynomicsSession {
	
	public enum APIResult {
		
		SENT,
		QUEUED,
		SWITCHED,
		STOPPED,
		ALREADY_STARTED,
		ALREADY_SWITCHED,
		ALREADY_STOPPED,
		SESSION_RESUMED,
		START_NOT_CALLED,
		NO_INTERNET_PERMISSION,
		FAIL_UNKNOWN
	};
	
	private static final String TAG = PlaynomicsSession.class.getSimpleName();
	private static final String SETTING_LAST_SESSION_START_TIME = "lastSessionStartTime";
	private static final String SETTING_LAST_USER_ID = "lastUserId";
	private static final String SETTING_LAST_SESSION_ID = "lastSessionId";
	
	private static final String FILE_PLAYNOMICS_EVENT_LIST = "playnomicsEventList";
	
	private static final int UPDATE_INTERVAL = 60000;
	private static final int COLLECTION_MODE = 7;
	
	private static boolean hasInternetPermission = true;
	
	private static Timer eventTimer;
	
	private static List<PlaynomicsEvent> playnomicsEventList = new CopyOnWriteArrayList<PlaynomicsEvent>();
	
	private static PlaynomicsTimerTask timerTask = new PlaynomicsTimerTask();
	
	private static Activity activity;
	private static Callback activityCallback;
	private static SharedPreferences settings;
	private static Editor editor;
	private static EventSender eventSender = new EventSender();
	
	private static ScreenReceiver screenReceiver = null;
	private static IntentFilter screenIntentFilter = new IntentFilter();
	
	// Tracking values
	private static int collectMode = 7;
	private static int sequence = 0;
	private static Long applicationId;
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
	
	private enum SessionState {
		
		UNKNOWN,
		STARTED,
		PAUSED,
		STOPPED
	}
	
	private static SessionState sessionState = SessionState.UNKNOWN;
	
	// Prevent instantiation
	private PlaynomicsSession() {
	
	}
	
	/**
	 * Start.
	 * 
	 * @param activity
	 *            the activity
	 * @param applicationId
	 *            the application id
	 * @param userId
	 *            the user id
	 * @return the API Result
	 */
	public static APIResult start(Activity activity, Long applicationId, String userId) {
	
		PlaynomicsSession.userId = userId;
		return start(activity, applicationId);
	}
	
	/**
	 * Start.
	 * 
	 * @param activity
	 *            the activity
	 * @param applicationId
	 *            the application id
	 * @return the API Result
	 */
	@SuppressWarnings("unchecked")
	public static APIResult start(Activity activity, Long applicationId) {
	
		APIResult result;
		
		try {
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
			
			// Setup the activity callback function(s)
			setActivityCallback(activity);
			if (!screenIntentFilter.hasAction(Intent.ACTION_SCREEN_OFF))
				screenIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
			if (!screenIntentFilter.hasAction(Intent.ACTION_SCREEN_ON))
				screenIntentFilter.addAction(Intent.ACTION_SCREEN_ON);
			
			if (screenReceiver == null) {
				screenReceiver = new ScreenReceiver();
				activity.registerReceiver(screenReceiver, screenIntentFilter);
			}
			
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
			
			playnomicsEventList.clear();
			List<?> eventList = (List<BasicEvent>) ObjectCacheUtil.getObject(activity, FILE_PLAYNOMICS_EVENT_LIST);
			if (eventList != null)
				playnomicsEventList.addAll((List<PlaynomicsEvent>) eventList);
			
			EventType eventType;
			
			// Send an appStart if it has been > 3 min since the last session or
			// a
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
			
			// Set userId to cookieId if it isn't present
			if (userId == null)
				userId = cookieId;
			
			BasicEvent be = new BasicEvent(eventType, applicationId, userId, cookieId, sessionId,
				instanceId, timeZoneOffset);
			
			// Try to send and queue if unsuccessful
			if (eventSender.sendToServer(be)) {
				result = APIResult.SENT;
			}
			else {
				playnomicsEventList.add(be);
				result = APIResult.QUEUED;
			}
			// Startup the event timer to send events back to the server
			if (eventTimer == null) {
				eventTimer = new Timer(true);
				eventTimer.scheduleAtFixedRate(timerTask, UPDATE_INTERVAL, UPDATE_INTERVAL);
			}
			
		} catch (Exception e) {
			
			// TODO: Send error to server
			result = APIResult.FAIL_UNKNOWN;
		}
		
		return result;
	}
	
	/**
	 * Sets the activity callback.
	 * 
	 * @param activity
	 *            the new activity callback
	 */
	private static void setActivityCallback(Activity activity) {
	
		PlaynomicsSession.activity = activity;
		activityCallback = activity.getWindow().getCallback();
		activity.getWindow().setCallback(new Callback() {
			
			@Override
			public void onWindowFocusChanged(boolean hasFocus) {
			
				activityCallback.onWindowFocusChanged(hasFocus);
				
				// Get out of here is we are finishing the activity
				if (PlaynomicsSession.activity.isFinishing())
					return;
				
				// Are we pausing?
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
			public boolean dispatchKeyEvent(KeyEvent event) {
			
				if (event.getAction() == KeyEvent.ACTION_DOWN) {
					keys += 1;
					totalKeys += 1;
				}
				
				return activityCallback.dispatchKeyEvent(event);
			}
		});
	}
	
	/**
	 * Pause.
	 */
	protected static void pause() {
	
		Log.i(TAG, "pause() called");
		if (sessionState.equals(SessionState.PAUSED))
			return;
		
		sessionState = SessionState.PAUSED;
		
		BasicEvent be = new BasicEvent(EventType.appPause, PlaynomicsSession.applicationId, userId,
			cookieId, sessionId, instanceId, timeZoneOffset);
		pauseTime = new Date();
		be.setSequence(sequence);
		be.setSessionStartTime(sessionStartTime);
		// Try to send and queue if unsuccessful
		if (!eventSender.sendToServer(be))
			playnomicsEventList.add(be);
	}
	
	/**
	 * Resume.
	 */
	protected static void resume() {
	
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
			playnomicsEventList.add(be);
	}
	
	/**
	 * Switch activity.
	 * 
	 * @param activity
	 *            the activity
	 * @return the API Result
	 */
	public static APIResult switchActivity(Activity activity) {
	
		APIResult result;
		
		try {
			Log.i(TAG, "switchActivity() called");
			if (sessionState != SessionState.STARTED) {
				return APIResult.START_NOT_CALLED;
			}
			
			if (PlaynomicsSession.activity != activity) {
				// Restore original callback
				PlaynomicsSession.activity.getWindow().setCallback(activityCallback);
				setActivityCallback(activity);
				result = APIResult.SWITCHED;
			}
			else {
				result = APIResult.ALREADY_SWITCHED;
			}
		} catch (Exception e) {
			result = APIResult.FAIL_UNKNOWN;
		}
		
		return result;
	}
	
	/**
	 * Stop.
	 * 
	 * @return the API Result
	 */
	public static APIResult stop() {
	
		APIResult result;
		
		try {
			Log.i(TAG, "stop() called");
			if (sessionState.equals(SessionState.STOPPED))
				return APIResult.ALREADY_STOPPED;
			
			if (activity.isFinishing()) {
				sessionState = SessionState.STOPPED;
				eventTimer.cancel();
				activity.unregisterReceiver(screenReceiver);
				screenReceiver = null;
				// Save eventList to disk for later sending
				ObjectCacheUtil.saveObject(playnomicsEventList, activity, FILE_PLAYNOMICS_EVENT_LIST);
				
				// Restore original callback
				activity.getWindow().setCallback(activityCallback);
				PlaynomicsSession.activity = null;
			}
			
			result = APIResult.STOPPED;
		} catch (Exception e) {
			// TODO: send error to server
			result = APIResult.FAIL_UNKNOWN;
		}
		
		return result;
	}
	
	/**
	 * User info.
	 * 
	 * @return the API Result
	 * @throws StartNotCalledException
	 *             the start not called exception
	 */
	public static APIResult userInfo() {
	
		return userInfo(UserInfoType.update, null, null, null, null, "", null, null);
	}
	
	/**
	 * User info.
	 * 
	 * @param type
	 *            the type
	 * @param country
	 *            the country
	 * @param subdivision
	 *            the subdivision
	 * @param sex
	 *            the sex
	 * @param birthday
	 *            the birthday
	 * @param source
	 *            the source
	 * @param sourceCampaign
	 *            the source campaign
	 * @param installTime
	 *            the install time
	 * @return the API Result
	 */
	public static APIResult userInfo(UserInfoType type, String country, String subdivision, UserInfoSex sex,
		Date birthday, UserInfoSource source, String sourceCampaign, Date installTime) {
	
		return userInfo(type, country, subdivision, sex, birthday, source.toString(), sourceCampaign, installTime);
	}
	
	/**
	 * User info.
	 * 
	 * @param type
	 *            the type
	 * @param country
	 *            the country
	 * @param subdivision
	 *            the subdivision
	 * @param sex
	 *            the sex
	 * @param birthday
	 *            the birthday
	 * @param source
	 *            the source
	 * @param sourceCampaign
	 *            the source campaign
	 * @param installTime
	 *            the install time
	 * @return the API Result
	 */
	public static APIResult userInfo(UserInfoType type, String country, String subdivision, UserInfoSex sex,
		Date birthday, String source, String sourceCampaign, Date installTime) {
	
		UserInfoEvent uie = new UserInfoEvent(applicationId, userId, type, country, subdivision, sex, birthday,
			source, sourceCampaign, installTime);
		return sendOrQueueEvent(uie);
	}
	
	/**
	 * Session start.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param site
	 *            the site
	 * @return the API Result
	 */
	public static APIResult sessionStart(String sessionId, String site) {
	
		GameEvent ge = new GameEvent(EventType.sessionStart, applicationId, userId, sessionId, site, null, null, null,
			null);
		return sendOrQueueEvent(ge);
		
	}
	
	/**
	 * Session end.
	 * 
	 * @param sessionId
	 *            the session id
	 * @param reason
	 *            the reason
	 * @return the API Result
	 */
	public static APIResult sessionEnd(String sessionId, String reason) {
	
		GameEvent ge = new GameEvent(EventType.sessionEnd, applicationId, userId, sessionId, null, null, null, null,
			reason);
		return sendOrQueueEvent(ge);
	}
	
	/**
	 * Game start.
	 * 
	 * @param instanceId
	 *            the instance id
	 * @param sessionId
	 *            the session id
	 * @param site
	 *            the site
	 * @param type
	 *            the type
	 * @param gameId
	 *            the game id
	 * @return the API Result
	 */
	public static APIResult gameStart(String instanceId, String sessionId, String site, String type, String gameId) {
	
		GameEvent ge = new GameEvent(EventType.gameStart, applicationId, userId, sessionId, site, instanceId, type,
			gameId, null);
		return sendOrQueueEvent(ge);
	}
	
	/**
	 * Game end.
	 * 
	 * @param instanceId
	 *            the instance id
	 * @param sessionId
	 *            the session id
	 * @param reason
	 *            the reason
	 * @return the API Result
	 */
	public static APIResult gameEnd(String instanceId, String sessionId, String reason) {
	
		GameEvent ge = new GameEvent(EventType.gameEnd, applicationId, userId, sessionId, null, instanceId, null, null,
			reason);
		return sendOrQueueEvent(ge);
	}
	
	/**
	 * Transaction.
	 * 
	 * @param transactionId
	 *            the transaction id
	 * @param itemId
	 *            the item id
	 * @param quantity
	 *            the quantity
	 * @param type
	 *            the type
	 * @param otherUserId
	 *            the other user id
	 * @param currencyTypes
	 *            the currency types
	 * @param currencyValues
	 *            the currency values
	 * @param currencyCategories
	 *            the currency categories
	 * @return the API Result
	 */
	public static APIResult transaction(long transactionId, String itemId, double quantity, TransactionType type,
		String otherUserId, String[] currencyTypes, double[] currencyValues, CurrencyCategory[] currencyCategories) {
	
		TransactionEvent te = new TransactionEvent(EventType.transaction, applicationId, userId, transactionId, itemId,
			quantity, type, otherUserId, currencyTypes, currencyValues, currencyCategories);
		return sendOrQueueEvent(te);
	}
	
	/**
	 * Invitation sent.
	 * 
	 * @param invitationId
	 *            the invitation id
	 * @param recipientUserId
	 *            the recipient user id
	 * @param recipientAddress
	 *            the recipient address
	 * @param method
	 *            the method
	 * @return the API Result
	 */
	public static APIResult invitationSent(String invitationId, String recipientUserId, String recipientAddress,
		String method) {
	
		SocialEvent se = new SocialEvent(EventType.invitationSent, applicationId, userId, invitationId,
			recipientUserId, recipientAddress, method, null);
		return sendOrQueueEvent(se);
	}
	
	/**
	 * Invitation response.
	 * 
	 * @param invitationId
	 *            the invitation id
	 * @param response
	 *            the response
	 * @return the API Result
	 */
	public static APIResult invitationResponse(String invitationId, ResponseType response) {
	
		SocialEvent se = new SocialEvent(EventType.invitationResponse, applicationId, userId, invitationId, null, null,
			null, response);
		return sendOrQueueEvent(se);
	}
	
	private static APIResult sendOrQueueEvent(PlaynomicsEvent pe) {
	
		APIResult result;
		
		try {
			if (sessionState != SessionState.STARTED) {
				return APIResult.START_NOT_CALLED;
			}
			else if (!hasInternetPermission) {
				return APIResult.NO_INTERNET_PERMISSION;
			}
			
			// Try to send and queue if unsuccessful
			if (eventSender.sendToServer(pe)) {
				result = APIResult.SENT;
			}
			else {
				playnomicsEventList.add(pe);
				result = APIResult.QUEUED;
			}
		} catch (Exception e) {
			// TODO: send error to server
			result = APIResult.FAIL_UNKNOWN;
		}
		
		return result;
	}
	
	/**
	 * The Class PlaynomicsTimerTask.
	 */
	protected static class PlaynomicsTimerTask extends TimerTask {
		
		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.TimerTask#run()
		 */
		@Override
		public void run() {
		
			try {
				// Only send appRunning event if we are started
				if (sessionState == SessionState.STARTED) {
					sequence += 1;
					BasicEvent runningBE = new BasicEvent(EventType.appRunning, applicationId, userId, cookieId,
						sessionId, instanceId, sessionStartTime, sequence, clicks, totalClicks, keys, totalKeys,
						collectMode);
					playnomicsEventList.add(runningBE);
					
					// Reset keys/clicks
					keys = 0;
					clicks = 0;
				}
				
				// Exit method if any sendToServer call fails (we'll try again time)
				for (PlaynomicsEvent pe : playnomicsEventList) {
					
					if (eventSender.sendToServer(pe))
						playnomicsEventList.remove(pe);
					else
						return;
				}
			} catch (Exception e) {
				// TODO: send error to server
			}
		}
	}
}

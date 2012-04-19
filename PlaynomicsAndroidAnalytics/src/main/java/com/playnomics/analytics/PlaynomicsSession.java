package com.playnomics.analytics;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.ActionMode;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window.Callback;
import android.view.accessibility.AccessibilityEvent;

public class PlaynomicsSession {
	
	private static final String TAG = PlaynomicsSession.class.getSimpleName();
	
	private static final int UPDATE_INTERVAL = 10000;
	private static Timer eventTimer;
	private static List<PlaynomicsEvent> eventList = new CopyOnWriteArrayList<PlaynomicsEvent>();
	private static PlaynomicsTimerTask timerTask = new PlaynomicsTimerTask();
	private static String apiKey;
	private static Activity activity;
	private static String userId;
	private static Callback activityCallback;
	
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
	
	public static void start(Activity activity, String apiKey) {
	
		if (sessionState.equals(SessionStates.STARTED))
			return;
		
		sessionState = SessionStates.STARTED;
		PlaynomicsSession.apiKey = apiKey;
		PlaynomicsSession.activity = activity;
		activityCallback = activity.getWindow().getCallback();

		// Startup the event timer to send events back to the server
		eventTimer = new Timer(true);
		eventTimer.scheduleAtFixedRate(timerTask, 1000, UPDATE_INTERVAL);
		
		
		activity.getWindow().setCallback(new Callback() {
			
			@Override
			public ActionMode onWindowStartingActionMode(android.view.ActionMode.Callback callback) {
			
				// TODO Auto-generated method stub
				return activityCallback.onWindowStartingActionMode(callback);
			}
			
			@Override
			public void onWindowFocusChanged(boolean hasFocus) {
			
				activityCallback.onWindowFocusChanged(hasFocus);
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
					// TODO: add to queue
					PlaynomicsEvent pe = new PlaynomicsEvent(PlaynomicsEvent.EventTypes.EVENT_CLICK, "blah");
					eventList.add(pe);
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
			
				if (event.getAction()  == KeyEvent.ACTION_DOWN) {
					PlaynomicsEvent pe = new PlaynomicsEvent(PlaynomicsEvent.EventTypes.EVENT_KEY, "blah");
					eventList.add(pe);					
				}
		
				return activityCallback.dispatchKeyEvent(event);
			}
			
			@Override
			public boolean dispatchGenericMotionEvent(MotionEvent event) {
			
				return activityCallback.dispatchGenericMotionEvent(event);
			}
		});
		
		PlaynomicsEvent pe = new PlaynomicsEvent(PlaynomicsEvent.EventTypes.EVENT_START, "blah");
		eventList.add(pe);
	}
	
	public static void pause() {
	
		if (sessionState.equals(SessionStates.PAUSED))
			return;
		sessionState = SessionStates.PAUSED;
		PlaynomicsEvent pe = new PlaynomicsEvent(PlaynomicsEvent.EventTypes.EVENT_PAUSE, "blah");
		eventList.add(pe);
	}
	
	public static void stop() {
	
		if (sessionState.equals(SessionStates.STOPPED))
			return;
		
		sessionState = SessionStates.STOPPED;
		PlaynomicsEvent pe = new PlaynomicsEvent(PlaynomicsEvent.EventTypes.EVENT_STOP , "blah");
		eventList.add(pe);
		// TODO: Revisit stopping the timer logic; we might want to keep trying if offline
		timerTask.run();
		eventTimer.cancel();
		
		// Restore original callback
		activity.getWindow().setCallback(activityCallback);
	}
	
	public static void logEvent(PlaynomicsEvent pe) {

		eventList.add(pe);
	}
	
	public static String getUserId() {
	
		return userId;
	}
	
	public static void setUserId(String userId) {
	
		PlaynomicsSession.userId = userId;
	}

	private static class PlaynomicsTimerTask extends TimerTask {
		
		@Override
		public void run() {
		
			for (PlaynomicsEvent pe : eventList) {
				
				// Send the event and remove if successful
				if (sendToServer(pe))
					eventList.remove(pe);
			}
		}
		
		private boolean sendToServer(PlaynomicsEvent pe) {
		
			// TODO: implement
			// TODO: add server availability check
			Log.i(TAG, "Sending event to server for session" + apiKey + ": " + pe.toString());
			return true;
		}
	}
}

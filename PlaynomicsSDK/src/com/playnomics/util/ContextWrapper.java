package com.playnomics.util;

import android.content.Context;
import android.content.SharedPreferences;

public class ContextWrapper {

	static final String CACHE_NAME = "com.playnomics.cache";
	static final String PUSH_ID_CACHE_KEY = "pushId";
	static final String LAST_EVENT_TIME_CACHE_KEY = "lastEventTime";
	static final String SESSION_START_TIME_CACHE_KEY = "sessionStartTime";
	static final String APP_VERSION_CACHE_KEY = "appVersion";
	static final String SESSION_ID_KEY = "sessionId";

	private SharedPreferences preferences;
	private Logger logger;
	private Util util;
	private Context context;
	
	public ContextWrapper(Context context, Logger logger, Util util) {
		this.logger = logger;
		this.util = util;
		this.context = context;
		preferences = context.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
	}
	
	public Context getContext(){
		return context;
	}
	
	public EventTime getLastEventTime() {
		return getEventTimeValue(ContextWrapper.LAST_EVENT_TIME_CACHE_KEY);
	}

	public void setLastEventTime(EventTime time) {
		setEventTimeValue(ContextWrapper.LAST_EVENT_TIME_CACHE_KEY, time);
	}

	public EventTime getLastSessionStartTime() {
		return getEventTimeValue(ContextWrapper.SESSION_START_TIME_CACHE_KEY);
	}

	public void setLastSessionStartTime(EventTime time) {
		setEventTimeValue(ContextWrapper.SESSION_START_TIME_CACHE_KEY, time);
	}

	public LargeGeneratedId getPreviousSessionId() {
		Long sessionId = preferences.getLong(SESSION_ID_KEY, -1);
		if (sessionId < 0) {
			// session ID was never saved
			return null;
		}

		return new LargeGeneratedId(sessionId);
	}

	public void setPreviousSessionId(LargeGeneratedId sessionId) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(SESSION_ID_KEY, sessionId.getId());
		editor.commit();
	}

	public String getPushRegistrationId() {
		return preferences.getString(ContextWrapper.PUSH_ID_CACHE_KEY, null);
	}

	public void setPushRegistrationId(String pushId) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putString(ContextWrapper.PUSH_ID_CACHE_KEY, pushId);
		editor.commit();
	}

	public int getApplicationVersion() {
		return preferences.getInt(ContextWrapper.PUSH_ID_CACHE_KEY, -1);
	}

	private void setApplicationVersion(int version) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putInt(ContextWrapper.APP_VERSION_CACHE_KEY, version);
		editor.commit();
	}

	private int getCurrentAppVersion() {
		return util.getApplicationVersionFromContext(context);
	}

	public boolean synchronizeDeviceSettings() {
		int cachedVersion = getApplicationVersion();
		int currentVersion = getCurrentAppVersion();
		if (cachedVersion != currentVersion) {
			setApplicationVersion(currentVersion);
			// the push ID is no longer valid
			setPushRegistrationId(null);
			return true;
		}
		return false;
	}

	private EventTime getEventTimeValue(String key) {
		long lastEventTimeMilliseconds = preferences.getLong(key, -1);
		
		if(lastEventTimeMilliseconds >= 0){
			return new EventTime(lastEventTimeMilliseconds);
		}
		return null;
	}

	private void setEventTimeValue(String key, EventTime value) {
		SharedPreferences.Editor editor = preferences.edit();
		editor.putLong(key, value.getTimeInMillis());
		editor.commit();
	}
}

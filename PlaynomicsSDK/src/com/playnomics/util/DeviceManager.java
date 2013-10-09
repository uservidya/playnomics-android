package com.playnomics.util;

import com.playnomics.util.Logger.LogLevel;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.provider.Settings;

public class DeviceManager {

	static final String CACHE_NAME = "com.playnomics.cache";
	static final String PUSH_ID_CACHE_KEY = "pushId";
	static final String LAST_EVENT_TIME_CACHE_KEY = "eventTime";
	static final String APP_VERSION_CACHE_KEY = "appVersion";
	static final String SESSION_ID_KEY = "sessionId";

	private Context context;
	private ServiceManager manager;
	private SharedPreferences preferences;

	public DeviceManager(Context context, ServiceManager serviceManager) {
		this.context = context;
		this.manager = serviceManager;
		this.preferences = this.context.getSharedPreferences(CACHE_NAME,
				Context.MODE_PRIVATE);
	}

	public String getAndroidDeviceId() {
		return Settings.Secure.getString(this.context.getContentResolver(),
				Settings.Secure.ANDROID_ID);
	}

	public EventTime getLastEventTime() {
		long lastEventTimeMilliseconds = this.preferences.getLong(
				DeviceManager.LAST_EVENT_TIME_CACHE_KEY, 0);
		return new EventTime(lastEventTimeMilliseconds);
	}

	public void setLastEventTime(EventTime time) {
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putLong(DeviceManager.LAST_EVENT_TIME_CACHE_KEY,
				time.getTimeInMillis());
		editor.commit();
	}
	
	public LargeGeneratedId getPreviousSessionId(){
		Long sessionId = this.preferences.getLong(SESSION_ID_KEY, -1);
		if(sessionId < 0){
			//session ID was never saved
			return null;
		}
		
		return new LargeGeneratedId(sessionId);
	}
	
	public void getPreviousSessionId(LargeGeneratedId sessionId){
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putLong(SESSION_ID_KEY, sessionId.getId());
		editor.commit();
	}

	public String getPushRegistrationId() {
		return this.preferences
				.getString(DeviceManager.PUSH_ID_CACHE_KEY, null);
	}

	public void setPushRegistrationId(String pushId) {
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putString(DeviceManager.PUSH_ID_CACHE_KEY, pushId);
		editor.commit();
	}

	public int getApplicationVersion() {
		return this.preferences.getInt(DeviceManager.PUSH_ID_CACHE_KEY, -1);
	}

	private void setApplicationVersion(int version) {
		SharedPreferences.Editor editor = this.preferences.edit();
		editor.putInt(DeviceManager.APP_VERSION_CACHE_KEY, version);
		editor.commit();
	}

	private int getCurrentAppVersion() {
		try {
			PackageManager packageManager = this.manager.getPackageManager();
			PackageInfo info;
			info = packageManager.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException ex) {
			// according to Google's docs this should never happen
			Logger.log(LogLevel.DEBUG, ex,
					"Could not obtain the application version from the package manager");
			// in the event of a failure always return a -1
			return -1;
		}
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
}

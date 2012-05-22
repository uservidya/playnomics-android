package com.playnomics.api;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import android.util.Log;

class EventSender {
	
	// TODO: Externalize this string or add test/prod modes
	private static final String TAG = EventSender.class.getSimpleName();
	private String version;
	private String baseUrl;
	
	private boolean testMode = false;
	
	public EventSender() {
	
		this(false);
	}
	
	public EventSender(boolean testMode) {
	
		try {
			this.testMode = testMode;
			ResourceBundle b = ResourceBundle.getBundle("playnomicsAndroidAnalytics");
			version = b.getString("version");
			baseUrl = b.getString("baseUrl");
		} catch (Exception e) {
			// TODO: Send info to server
			e.printStackTrace();
		}
	}
	
	protected boolean sendToServer(String eventUrl) {
	
		try {
			// Add version info
			eventUrl += "&esrc=aj&ever=" + version;
			// Hack to use JUnit testing w/o emulator
			if (testMode)
				System.out.println("Sending event to server: " + eventUrl);
			else
				Log.i(TAG, "Sending event to server: " + eventUrl);
			
			HttpURLConnection con = (HttpURLConnection) new URL(eventUrl).openConnection();
			// con.setRequestMethod("HEAD");
			con.setConnectTimeout(5000);
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			if (testMode)
				System.out.println("Send failed: " + e.getMessage());
			else
				Log.i(TAG, "Send failed: " + e.getMessage());
			
			return false;
		}
	}
	
	protected boolean sendToServer(PlaynomicsEvent pe) {
	
		return sendToServer(baseUrl + pe.toQueryString());
	}
}

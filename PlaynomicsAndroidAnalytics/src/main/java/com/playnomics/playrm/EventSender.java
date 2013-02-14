package com.playnomics.playrm;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;

import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

class EventSender {
	
	private static final String TAG = EventSender.class.getSimpleName();
	
	private String version;
	private String baseUrl;
	private int connectTimeout;
	private ResourceBundle resourceBundle;
	
	private boolean useConsoleLogging = false;
	
	public EventSender() {
	
		this(false, false);
	}
	
	public EventSender(boolean useConsoleLogging, boolean testMode) {
	
		try {
			this.useConsoleLogging = useConsoleLogging;
			resourceBundle = ResourceBundle.getBundle("playnomicsAndroidAnalytics");
			version = resourceBundle.getString("version");
			// Are we in test mode?
			setTestMode(testMode);
			
			connectTimeout = new Integer(resourceBundle.getString("connectTimeout"));
		} catch (Exception e) {
			// TODO: Send info to server
			e.printStackTrace();
		}
	}
	
	protected void setTestMode(boolean testMode) {
	
		// Are we in test mode?
		if (testMode)
			baseUrl = resourceBundle.getString("baseTestUrl");
		else
			baseUrl = resourceBundle.getString("baseProdUrl");
	}
	
	protected boolean sendToServer(String eventUrl) {
	
		try {
			// Add version info
			eventUrl += "&esrc=aj&ever=" + version;		
			HttpURLConnection con = (HttpURLConnection) new URL(eventUrl).openConnection();
			con.setConnectTimeout(connectTimeout);		
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		}
		catch (Exception e) {
			return false;
		}
	}
	

	protected boolean sendToServer(PlaynomicsEvent pe) {
		return sendToServer(baseUrl + pe.toQueryString());
	}
}

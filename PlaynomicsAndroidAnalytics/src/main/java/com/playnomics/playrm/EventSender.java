package com.playnomics.playrm;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import android.util.Log;

class EventSender {

	private static final String TAG = EventSender.class.getSimpleName();
	private int connectTimeout;
	private ResourceBundle resourceBundle;

	public EventSender() {
		try {
			resourceBundle = PlaynomicsSession.getResourceBundle();
			connectTimeout = new Integer(
					resourceBundle.getString("connectTimeout"));
		} catch (Exception e) {
			// TODO: Send info to server
			e.printStackTrace();
		}
	}

	protected boolean sendToServer(PlaynomicsEvent event) {
		try {
			String eventUrl = event.getEventUrl();
			String version = resourceBundle.getString("version");
			// Add version info
			if (event.appendSourceInformation()) {
				eventUrl += "&esrc=aj&ever=" + version;
			}
			HttpURLConnection con = (HttpURLConnection) new URL(eventUrl)
					.openConnection();
			con.setConnectTimeout(connectTimeout);
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
			return false;
		}
	}
}

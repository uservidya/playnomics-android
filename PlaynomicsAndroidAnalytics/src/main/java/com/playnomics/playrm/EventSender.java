package com.playnomics.playrm;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

class EventSender {

	private static final String TAG = EventSender.class.getSimpleName();

	protected boolean sendToServer(PlaynomicsEvent event) throws IOException {
        String eventUrl = event.getEventUrl();
    	// Add version info
		if (event.appendSourceInformation()) {
			eventUrl += "&esrc=aj&ever=" + PlaynomicsSession.getSDKVersion();
		}
			
		PlaynomicsLogger.d(TAG, "Requesting event url: " + eventUrl);

		HttpURLConnection con = (HttpURLConnection) new URL(eventUrl).openConnection();
		con.setConnectTimeout(PlaynomicsSession.getConnectionTimeout());

		PlaynomicsLogger.d(TAG, "HTTP Response: " + con.getResponseCode());
		boolean isSuccess = (con.getResponseCode() == HttpURLConnection.HTTP_OK);
        con.disconnect();
        return isSuccess;
	}
}

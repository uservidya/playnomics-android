package com.playnomics.playrm;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

class EventSender {

	private static final String TAG = EventSender.class.getSimpleName();

	protected boolean sendToServer(PlaynomicsEvent event) {
		try {
			String eventUrl = event.getEventUrl();
			if (event.appendSourceInformation()) {
				eventUrl += "&esrc=aj&ever=" + PlaynomicsSession.getVersion();
			}
			eventUrl = URLEncoder.encode(eventUrl,
					PlaynomicsSession.getEncoding());

			PlaynomicsLogger.d(TAG, "Requesting event url: " + eventUrl);

			HttpURLConnection con = (HttpURLConnection) new URL(eventUrl)
					.openConnection();
			con.setConnectTimeout(PlaynomicsSession.getConnectionTimeout());

			PlaynomicsLogger.d(TAG, "HTTP Response: " + con.getResponseCode());
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			PlaynomicsLogger.d(TAG, e.getMessage());
			return false;
		}
	}
}

package com.playnomics.analytics;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import android.util.Log;

import com.playnomics.analytics.PlaynomicsEvent.EventType;

public class EventSender {
	
	// TODO: Externalize this string or add test/prod modes
	private static final String PLAYNOMICS_BASE_URL = "https://test.b.playnomics.net/v1/";
	private static final String TAG = EventSender.class.getSimpleName();
	private static final int UPDATE_INTERVAL = 60000;
	
	private boolean testMode = false;
	
	public EventSender() {
	
	}
	
	public EventSender(boolean testMode) {
	
		this.testMode = testMode;
	}
	
	private String addOptionalParam(String url, String param, Object value) {
	
		if (value != null) {
			url += "&" + param + "=" + value.toString();
		}
		
		return url;
	}
	
	protected boolean sendToServer(String eventUrl) {
	
		try {
			// Hack to use JUnit testing w/o emulator
			if (testMode)
				System.out.println("Sending event to server: " + eventUrl);
			else
				Log.i(TAG, "Sending event to server: " + eventUrl);
			HttpURLConnection con = (HttpURLConnection) new URL(eventUrl).openConnection();
			// con.setRequestMethod("HEAD");
			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
		} catch (Exception e) {
			// Log.e(TAG, e.getMessage());
			e.printStackTrace();
			return false;
		}
	}

	protected boolean sendToServer(SocialEvent se) {
		
		// Set common params
		String eventUrl = PLAYNOMICS_BASE_URL
			+ se.getEventType()
			+ "?t=" + se.getEventTime().getTime()
			+ "&a=" + se.getApplicationId()
			+ "&u=" + se.getUserId()
			+ "&ii=" + se.getInvitationId();
		
		if (se.getEventType() == EventType.invitationResponse) {
			eventUrl += "&ie=" + se.getResponse()
				+ "&ir=" + se.getRecipientUserId();	
		}
		else {
			// Optional params
			eventUrl = addOptionalParam(eventUrl, "ir", se.getRecipientUserId());
			eventUrl = addOptionalParam(eventUrl, "ia", se.getRecipientAddress());
			eventUrl = addOptionalParam(eventUrl, "im", se.getMethod());			
		}
		
		return sendToServer(eventUrl);
	}
	
	protected boolean sendToServer(TransactionEvent te) {
		
		// Set common params
		String eventUrl = PLAYNOMICS_BASE_URL
			+ te.getEventType()
			+ "?t=" + te.getEventTime().getTime()
			+ "&a=" + te.getApplicationId()
			+ "&u=" + te.getUserId()
			+ "&tt=" + te.getType();

		for (int i = 0; i < te.getCurrencyTypes().length; i++ ) {
			
			eventUrl += "&tc" + i + "=" + te.getCurrencyTypes()[i]
				+ "&tv" + i + "=" + te.getCurrencyValues()[i]
				+ "&ta" + i + "=" + te.getCurrencyCategories()[i];
		}
		
		// Optional params
		eventUrl = addOptionalParam(eventUrl, "i", te.getItemId());
		eventUrl = addOptionalParam(eventUrl, "tq", te.getQuantity());
		eventUrl = addOptionalParam(eventUrl, "to", te.getOtherUserId());
		
		return sendToServer(eventUrl);
	}
	
	protected boolean sendToServer(GameEvent ge) {
		
		// Set common params
		String eventUrl = PLAYNOMICS_BASE_URL
			+ ge.getEventType()
			+ "?t=" + ge.getEventTime().getTime()
			+ "&a=" + ge.getApplicationId()
			+ "&u=" + ge.getUserId();
		// sessionId is optional for game events
		if (ge.getEventType() == EventType.gameStart || ge.getEventType() == EventType.gameEnd)
			eventUrl = addOptionalParam(eventUrl, "s", ge.getSessionId());
		else
			eventUrl += "&s=" + ge.getSessionId();
		// Optional params
		eventUrl = addOptionalParam(eventUrl, "ss", ge.getSite());
		eventUrl = addOptionalParam(eventUrl, "r", ge.getReason());
		eventUrl = addOptionalParam(eventUrl, "g", ge.getInstanceId());
		eventUrl = addOptionalParam(eventUrl, "ss", ge.getSite());
		eventUrl = addOptionalParam(eventUrl, "gt", ge.getType());
		eventUrl = addOptionalParam(eventUrl, "gi", ge.getGameId());
		
		return sendToServer(eventUrl);
	}
	
	protected boolean sendToServer(UserInfoEvent uie) {
	
		// Set common params
		String eventUrl = PLAYNOMICS_BASE_URL
			+ uie.getEventType()
			+ "?t=" + uie.getEventTime().getTime()
			+ "&a=" + uie.getApplicationId()
			+ "&u=" + uie.getUserId()
			+ "&pt=" + uie.getType();
		// Optional params
		eventUrl = addOptionalParam(eventUrl, "pc", uie.getCountry());
		eventUrl = addOptionalParam(eventUrl, "ps", uie.getSubdivision());
		eventUrl = addOptionalParam(eventUrl, "px", uie.getSex());
	    SimpleDateFormat format = new SimpleDateFormat("MM-DD-yyyy");
		eventUrl = addOptionalParam(eventUrl, "pb", format.format(uie.getBirthday()));
		eventUrl = addOptionalParam(eventUrl, "po", uie.getSource());
		eventUrl = addOptionalParam(eventUrl, "pm", uie.getSourceCampaign());
		eventUrl = addOptionalParam(eventUrl, "pi", uie.getInstallTime().getTime());
		return sendToServer(eventUrl);
	}
	
	protected boolean sendToServer(BasicEvent be) {
	
		// Set common params
		String eventUrl = PLAYNOMICS_BASE_URL
			+ be.getEventType()
			+ "?t=" + be.getEventTime().getTime()
			+ "&a=" + be.getApplicationId()
			+ "&b=" + be.getCookieId()
			+ "&s=" + be.getSessionId()
			+ "&i=" + be.getInstanceId();
		
		switch (be.getEventType()) {
		
			case appStart:
			case appPage:
				eventUrl += "&z=" + be.getTimeZoneOffset();
				break;
			
			case appRunning:
				eventUrl += "&r=" + be.getSessionStartTime().getTime()
					+ "&q=" + be.getSequence()
					+ "&d=" + UPDATE_INTERVAL
					+ "&c=" + be.getClicks()
					+ "&e=" + be.getTotalClicks()
					+ "&k=" + be.getKeys()
					+ "&l=" + be.getTotalKeys()
					+ "&m=" + be.getCollectMode();
				break;
			
			case appResume:
				eventUrl += "&p=" + be.getPauseTime().getTime();
			case appPause:
				eventUrl += "&r=" + be.getSessionStartTime().getTime()
					+ "&q=" + be.getSequence();
				break;
		}
		return sendToServer(eventUrl);
	}
}

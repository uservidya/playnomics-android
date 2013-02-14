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
	
//	protected boolean sendToServer(String eventUrl) {
//	
//		try {
//			// Add version info
//			eventUrl += "&esrc=aj&ever=" + version;
//			
//			// Hack to use JUnit testing w/o emulator
////			if (useConsoleLogging)
//			System.out.println("Sending event to server : " + eventUrl);
////			else
////				Log.i(TAG, "Sending event to server : " + eventUrl);
//			
//			HttpURLConnection con = (HttpURLConnection) new URL(eventUrl).openConnection();
//			// con.setRequestMethod("HEAD");
//			con.setConnectTimeout(connectTimeout);
//			
//			System.out.println("Send Succeeded! : "+ con.getResponseMessage());
//			
//			return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
//			
//		}
//		catch (Exception e) {
//			
//			System.out.println("Send failed: " + e.getMessage() + " : " + e.getLocalizedMessage() + " : " + e.toString());
//						
//			return false;
//		}
//	}
	
	protected boolean sendToServer(PlaynomicsEvent pe) {
		return sendToServer(baseUrl + pe.toQueryString());
	}
	
	//Test for doing network calls on background thread
	protected boolean sendToServer(final String eventUrl){
		
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				try {
					// Add version info
					String url = eventUrl+"&esrc=aj&ever=" + version;
					System.out.println("Sending event to server : " + url);

					HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
					con.setConnectTimeout(connectTimeout);
					
					System.out.println("Send Succeeded! : "+ con.getResponseMessage());
					
//					return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
				}
				catch (Exception e) {
					System.out.println("Send failed: " + e.getMessage() + " : " 
							+ e.getLocalizedMessage() + " : " + e.toString());
//					return false;
				}
				return null;
			}
		};
//		
		task.execute((Void)null);
////		try {
////			return task.get();
////		} catch (InterruptedException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		} catch (ExecutionException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
		return true;
	}
}

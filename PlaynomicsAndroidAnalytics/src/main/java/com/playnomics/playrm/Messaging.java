package com.playnomics.playrm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.playnomics.playrm.ErrorDetail.PlaynomicsErrorType;


public class Messaging {
	private static Map<String,ConcurrentHashMap<String,Frame>> contextFrames 
		= new HashMap<String,ConcurrentHashMap<String,Frame>>();
	
	public static void setup(Context cont) {
		//possible do update code here
	}
	
	protected static void clearFramesInContext(Context context){
		String key = getKeyForContext(context);
		contextFrames.remove(key);
	}
	
	protected static void clearFrameFromContext(Context context, String frameId){
		String key = getKeyForContext(context);
		if(contextFrames.containsKey(key)){
			ConcurrentHashMap<String, Frame> framesById = contextFrames.get(key);
			framesById.remove(frameId);
		}
	}

	private static ConcurrentHashMap<String, Frame> getFramesForActivity(
			Context context){
		String key = getKeyForContext(context);
		if(contextFrames.containsKey(key)){
			return contextFrames.get(key);
		}
		return null;
	}
	
	protected static String getKeyForContext(Context context){
		return context.getClass().getName();
	}

	public static Frame initWithFrameID(String frameId, Context context) {

		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		StackTraceElement element = stElements[3];

		String caller = element.getMethodName();
		Frame frame = new Frame(frameId, context);
		
		ConcurrentHashMap<String, Frame> framesById = getFramesForActivity(context);
		boolean framesAreNew = framesById == null;
	
		if(framesAreNew){
			framesById = new ConcurrentHashMap<String, Frame>();
		}
		
		framesById.put(frameId, frame);
		
		String contextKey = getKeyForContext(context);
		
		if(framesAreNew){
			contextFrames.put(contextKey, framesById);
		}
		
		fetchDataAsync(context, contextKey, frameId, caller);
		return frame;
	}

	private static void fetchDataAsync(final Context context, final String contextKey, 
			final String frameId,  final String caller) {

		AsyncTask<Void, Void, AdResponse> task = new AsyncTask<Void, Void, AdResponse>() {

			@Override
			protected AdResponse doInBackground(Void... params) {	
				DisplayMetrics metrics = context.getApplicationContext()
						.getResources().getDisplayMetrics();
				
				int width = metrics.widthPixels;
				int height = metrics.heightPixels;

//				MessagingServiceClient client = new MessagingServiceClient(
//						serverUrl, PlaynomicsSession.getAppID(),
//						PlaynomicsSession.getUserID(),
//						PlaynomicsSession.getCookieID());
				
				MessagingServiceClientTest client = new MessagingServiceClientTest
						(PlaynomicsSession.getResourceBundle(),
						PlaynomicsSession.getBaseUrl(), 
						PlaynomicsSession.getAppID(), 
						PlaynomicsSession.getUserID(),
						PlaynomicsSession.getCookieID());
				
				return client.requestAd(frameId, caller, width,
						height);
			}

			protected void onPostExecute(AdResponse response) {

				if (response == null) {
					ErrorDetail detail = new ErrorDetail(PlaynomicsErrorType.errorTypeInvalidJson);
					PlaynomicsSession.errorReport(detail);
				} else {
					Frame frame = contextFrames.get(contextKey).get(frameId);
					frame.refreshData(response);
				}
			}
		};
		task.execute((Void) null);
	}

	public static void performActionForLabel(Context context, String actionLabel) {

		actionLabel = actionLabel.substring(3);
		Method method = null;

		try {
			method = context.getClass().getMethod(actionLabel, null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (method == null) {
			return;
		} else {
			try {
				method.invoke(context.getClass().cast(context));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void executeActionOnDelegate(Context context, String actionLabel) {
		actionLabel = actionLabel.substring(3);
		Method method = null;

		try {
			method = context.getClass().getMethod(actionLabel, null);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (method == null) {
			return;
		} else {
			try {
				method.invoke(context.getClass().cast(context));
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static void refreshWithId(Context context, String frameId) {
		String contextKey = getKeyForContext(context);
		fetchDataAsync(context, contextKey, frameId, "REFRESH CALLER" );
	}
}
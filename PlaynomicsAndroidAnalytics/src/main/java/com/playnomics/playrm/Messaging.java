package com.playnomics.playrm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Message;
import android.util.DisplayMetrics;

import com.playnomics.playrm.ErrorDetail.PlaynomicsErrorType;

public class Messaging {

//	private enum WorkerStatus {
//		NOT_STARTED, STARTED, PAUSED, STOPPED
//	}

//	private static WorkerStatus status = WorkerStatus.NOT_STARTED;

//	private static Thread backgroundWorker;

	private final static String TAG = Message.class.getSimpleName();

	private static Map<String, ConcurrentHashMap<String, Frame>> activityFrames 
			= new HashMap<String, ConcurrentHashMap<String,Frame>>();

	public static void setup(final Activity activity) {
		String key = getKeyForActivity(activity);
		if (activityFrames.containsKey(key)) {
			ConcurrentHashMap<String, Frame> framesById = activityFrames
					.get(key);
			for (String frameIdKey : framesById.keySet()) {
				Frame frame = framesById.get(frameIdKey);
				if (frame != null) {
					frame.updateContext(activity);
				}
			}
		}
	}

	protected static void clearFramesInActivity(Activity activity) {
		String key = getKeyForActivity(activity);
		activityFrames.remove(key);
	}

	protected static void clearFrameFromActivity(Activity activity,
			String frameId) {
		String key = getKeyForActivity(activity);
		if (activityFrames.containsKey(key)) {
			ConcurrentHashMap<String, Frame> framesById = activityFrames
					.get(key);
			framesById.remove(frameId);
		}
	}

	private static ConcurrentHashMap<String, Frame> getFramesForActivity(
			Activity context) {
		String key = getKeyForActivity(context);
		if (activityFrames.containsKey(key)) {
			return activityFrames.get(key);
		}
		return null;
	}

	protected static String getKeyForActivity(Activity context) {
		return context.getClass().getName();
	}

	private static String getCaller() {
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		StackTraceElement element = stElements[3];

		return element.getMethodName();
	}

	public static Frame initWithFrameID(String frameId, Activity context) {
		String caller = getCaller();
		Frame frame = new Frame(frameId, context);

		ConcurrentHashMap<String, Frame> framesById = getFramesForActivity(context);
		boolean framesAreNew = framesById == null;

		if (framesAreNew) {
			framesById = new ConcurrentHashMap<String, Frame>();
		}

		framesById.put(frameId, frame);

		String contextKey = getKeyForActivity(context);

		if (framesAreNew) {
			activityFrames.put(contextKey, framesById);
		}

		startDataFetchAsync(context, contextKey, frameId, caller);
		return frame;
	}

	private static void startDataFetchAsync(final Activity activity,
			final String contextKey, final String frameId, final String caller) {
		new Thread(new Runnable() {
			public void run() {
				DisplayMetrics metrics = activity.getApplicationContext()
						.getResources().getDisplayMetrics();

				int width = metrics.widthPixels;
				int height = metrics.heightPixels;

				// MessagingServiceClient client = new MessagingServiceClient(
				// serverUrl, PlaynomicsSession.getAppID(),
				// PlaynomicsSession.getUserID(),
				// PlaynomicsSession.getCookieID());

				MessagingServiceClientTest client = new MessagingServiceClientTest(
						PlaynomicsSession.getResourceBundle(),
						PlaynomicsSession.getBaseUrl(),
						PlaynomicsSession.getAppID(),
						PlaynomicsSession.getUserID(),
						PlaynomicsSession.getCookieID());

				AdResponse response = client.requestAd(frameId, caller, width,
						height);

				if (response == null) {
					ErrorDetail detail = new ErrorDetail(
							PlaynomicsErrorType.errorTypeInvalidJson);
					PlaynomicsSession.errorReport(detail);
				} else {
					final AdRenderData renderData = new AdRenderData(response);
					renderData.loadAllImages();

					final Frame frame = activityFrames.get(contextKey).get(
							frameId);
					activity.runOnUiThread(new Runnable() {
						public void run() {
							frame.refreshData(renderData);
						}
					});
				}
			}
		}).start();
	}

	public static void performActionForLabel(Activity activity,
			String actionLabel) {
		Method method = null;

		try {
			method = activity.getClass().getMethod(actionLabel, null);
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
				method.invoke(activity.getClass().cast(activity));
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

	public static void executeActionOnDelegate(Activity activity,
			String actionLabel) {
		performActionForLabel(activity, actionLabel);
	}

	public static void refreshWithId(Activity activity, String frameId) {
		PlaynomicsLogger.d(TAG, "Starting refresh data call for frameId: "
				+ frameId);
		String activityKey = getKeyForActivity(activity);
		startDataFetchAsync(activity, activityKey, frameId, "REFRESH CALLER");
	}

//	private static AtomicBoolean isStopping = new AtomicBoolean(false);
//
//	private final static Object startSync = new Object();
//
//	protected static void start() {
//		if (status == WorkerStatus.STARTED) {
//			return;
//		}
//
//		if (status == WorkerStatus.NOT_STARTED) {
//			activityFrames = new HashMap<String, ConcurrentHashMap<String, Frame>>();
//			backgroundWorker = new Thread(new Runnable() {
//				public void run() {
//
//				}
//			});
//			backgroundWorker.start();
//		}
//	}
//
//	protected static void stop() {
//		isStopping.set(true);
//	}
//
//	public static void pause() {
//
//	}
//
//	private static void produce() {
//		while (!isStopping.get()) {
//			// for each frame in the activity
//		}
//
//	}
}
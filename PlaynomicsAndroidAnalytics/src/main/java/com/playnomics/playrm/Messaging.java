package com.playnomics.playrm;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;

import com.playnomics.playrm.ErrorDetail.PlaynomicsErrorType;

public class Messaging {
	private static Map<String, Frame> frames = null;

	private static ResourceBundle resourceBundle;
	@SuppressWarnings("unused")
	private static int connectionTimeout;
	private static Context context;

	private static boolean refresh = false;

	private static String caller;
	private static String frameID;

	public static Frame frame;

	public static void setup(Context cont) {

		context = cont;

		if (frames == null) {
			resourceBundle = PlaynomicsSession.getResourceBundle();
			connectionTimeout = new Integer(
					resourceBundle.getString("connectTimeout"));
			frames = new HashMap<String, Frame>();
		} else {

			ArrayList<String> oldFrames = new ArrayList<String>();

			Iterator<?> it = frames.entrySet().iterator();
			while (it.hasNext()) {
				@SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry) it.next();
				Frame frame = (Frame) pairs.getValue();

				if (frame.getActivityName()
						.equals(context.getClass().getName())) {
					frame.updateContext(context);
				} else {
					oldFrames.add((String) pairs.getKey());
				}
			}

			// loop to remove old frames
			if (oldFrames.size() > 0) {
				for (String key : oldFrames) {
					frames.remove(frames.get(key));
				}
			}
		}
	}

	public static void frameRemoveFrame(String key) {
		frames.remove(key);
	}

	public static Frame initWithFrameID(String frameId) {

		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		StackTraceElement element = stElements[3];

		caller = element.getMethodName();
		frame = new Frame(frameId, context);
		frames.put(frameId, frame);

		createTask(frame, caller);
		return frame;
	}

	private static void createTask(final Frame frame, final String caller) {

		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				String serverUrl = PlaynomicsSession.getBaseUrl();
				
				DisplayMetrics metrics = frame.getContext()
						.getApplicationContext().getResources()
						.getDisplayMetrics();
				
				int width = metrics.widthPixels;
				int height = metrics.heightPixels;

//				MessagingServiceClient client = new MessagingServiceClient(
//						serverUrl, PlaynomicsSession.getAppID(),
//						PlaynomicsSession.getUserID(),
//						PlaynomicsSession.getCookieID());
				
				MessagingServiceClientTest client = new MessagingServiceClientTest(serverUrl, 
						PlaynomicsSession.getAppID(), 
						PlaynomicsSession.getUserID(),
						PlaynomicsSession.getCookieID());
				return client.requestAd(frame.getFrameID(), caller, width,
						height);
			}

			protected void onPostExecute(JSONObject jObj) {

				if (jObj == null) {
					ErrorDetail detail = new ErrorDetail(
							PlaynomicsErrorType.errorTypeInvalidJson);
					PlaynomicsSession.errorReport(detail);
				} else {
					if (refresh) {
						Frame refreshFrame = frames.get(frameID);
						if (refreshFrame != null) {
							refreshFrame.refreshProperties(jObj);
							refresh = false;
						}
					} else {
						frame.refreshProperties(jObj);
					}
				}
			}
		};
		task.execute((Void) null);
	}

	public static void performActionForLabel(String actionLabel) {

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

	public static void executeActionOnDelegate(String actionLabel) {
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

	public static void refreshWithId(String frameId) {
		Frame frame = frames.get(frameId);
		if (frame != null) {
			// referesh entire frame
			refresh = true;
			createTask(frame, caller);
		}
	}
}
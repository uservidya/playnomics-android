package com.playnomics.playrm;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.widget.ListView;

import org.json.JSONException;
import org.json.JSONObject;

import com.playnomics.playrm.ErrorDetail.PlaynomicsErrorType;

public class Messaging {

//	private static Map <String,PlaynomicsMessagingInterface> actionHandlers;
	private static Map <String,Frame> frames = null;
	
	private static ResourceBundle resourceBundle;
	@SuppressWarnings("unused")
	private static int connectionTimeout;
	private static InputStream is;
	private static String json = "";
	private static JSONObject jObj = null;
	private static Context context;
	
	private static boolean refresh = false;
	
	private static String caller;
	private static String frameID;
	
	public static Frame frame;
		
	public static void setup(Context cont){
		
		context = cont;
		
		if(frames==null){
			resourceBundle = ResourceBundle.getBundle("playnomicsAndroidAnalytics");
			connectionTimeout = new Integer(resourceBundle.getString("connectTimeout"));
			frames = new HashMap <String, Frame>();
		}
		else{
			
			ArrayList<String> oldFrames = new ArrayList<String>();
			
			Iterator<?> it = frames.entrySet().iterator();
		    while (it.hasNext()) {
		        @SuppressWarnings("rawtypes")
				Map.Entry pairs = (Map.Entry)it.next();
		        Frame frame = (Frame) pairs.getValue();
		        
		        if(frame.getActivityName().equals(context.getClass().getName())){
		        	frame.updateContext(context);
		        }
		        else{
		        	oldFrames.add((String) pairs.getKey());
		        }
		    }
		    
		    //loop to remove old frames
		    if(oldFrames.size()>0){
		    	for(String key: oldFrames){
		    		frames.remove(frames.get(key));
		    	}
		    }
		}
	}
	
	public static void frameRemoveFrame(String key){
		frames.remove(key);
	}
	
	public static Frame initWithFrameID(String frameId){
		
		StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
		StackTraceElement element = stElements[3];
	
		caller = element.getMethodName();
		frame = new Frame(frameId, context);
		frames.put(frameId, frame);
		
		createTask(frame, caller);
		return frame;
	}

	private static void createTask(final Frame frame, final String caller){
		
		AsyncTask<Void, Void, JSONObject> task = new AsyncTask<Void, Void, JSONObject>() {

			@Override
			protected JSONObject doInBackground(Void... params) {
				try{
					Date date=new Date(0);
					Long time = date.getTime();
					
					DisplayMetrics metrics = frame.getContext().getApplicationContext().getResources().getDisplayMetrics();
					int width = metrics.widthPixels;
					int height = metrics.heightPixels;
					
					String queryString = "?a="+PlaynomicsSession.getAppID()+"&u="+PlaynomicsSession.getUserID()+"&p="+caller+
					"&t="+time+"&b="+PlaynomicsSession.getCookieID()+"&f="+frame.getFrameID()+"&c="+height+"&d="+width+"&esrc=ios&ever=1";
									
					String serverUrl;
					
					if(PlaynomicsSession.getTestMode())
						serverUrl = resourceBundle.getString("messagingTestUrl");
					else
						serverUrl = resourceBundle.getString("messagingProdUrl");
					
					String url = ""+serverUrl+""+queryString;
															
					// defaultHttpClient
				    DefaultHttpClient httpClient = new DefaultHttpClient();
				    HttpPost httpPost = new HttpPost(url);
				    HttpResponse httpResponse = httpClient.execute(httpPost);
				    HttpEntity httpEntity = httpResponse.getEntity();
				    is = httpEntity.getContent();  
				    
				    //FIXME: byte buffer(java) and check to see if activity is killed
				    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
				
				    StringBuilder sb = new StringBuilder();
				    String line = null;
				    while ((line = reader.readLine()) != null) {
				        sb.append(line + "\n");
				    }
				    is.close();
				    
				    json = sb.toString();
				    
					jObj = new JSONObject(json);
					
				} catch (UnsupportedEncodingException e) {
				    e.printStackTrace();
				} catch (ClientProtocolException e) {
				    e.printStackTrace();
				} catch (IOException e) {
				    e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				return jObj;
			}
			
			protected void onPostExecute(JSONObject jObj) {
				
				if(jObj == null){
					ErrorDetail detail = new ErrorDetail(PlaynomicsErrorType.errorTypeInvalidJson);
					PlaynomicsSession.errorReport(detail);
				}
				else{
					if(refresh){
						Frame refreshFrame = frames.get(frameID);
						if(refreshFrame != null){
							refreshFrame.refreshProperties(jObj);
							refresh = false;
						}
					}
					else{
						frame.refreshProperties(jObj);
					}
				}
		    }
		};
		task.execute((Void)null);
	}
	
	public static void performActionForLabel(String actionLabel){

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
		
		if(method == null){
			return;
		}
		else{
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
	
	public static void executeActionOnDelegate(String actionLabel){
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
		
		if(method == null){
			return;
		}
		else{
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

	public static void refreshWithId(String frameId){
		Frame frame = frames.get(frameId);
		if(frame != null){
			// referesh entire frame
			refresh = true;
			createTask(frame, caller);
		}
	}
}
package com.playnomics.playrm;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;


import com.playnomics.playrm.BaseAdComponent.AdComponentStatus;
import com.playnomics.playrm.BaseAdComponent.ImageType;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

public class Frame implements BaseAdComponentInterface{

	public enum DisplayResult {
		displayResultNoInternetPermission, displayResultStartNotCalled, 
		displayResultUnableToConnect, displayResultFailUnkown, 
		displayResultDisplayPending, displayResultDisplayed
	};
	
	protected enum AdAction{
		AdActionHTTP, AdActionDefinedAction,
		AdActionExecuteCode, AdActionUnkown
	};
	
	private boolean display = false;
	public String frameID;
	private Timer expirationTimer;
	private JSONObject properties;
	private BaseAdComponent background;
	private BaseAdComponent adArea;
	private BaseAdComponent closeButton;
	private int expirationSeconds;
	private ResourceBundle resourceBundle;
	private Context context;
	private String activityName;
	private boolean adEnabledCode;
	private Map <String, Method> actions = null;
	
	public void registerAction(String name, Method method){
		if(this.actions == null){
			this.actions = new HashMap<String, Method>();
		}
		this.actions.put(name, method);
	}
	
	public Frame(String frmId, Context cont){
		this.frameID = frmId;
		this.resourceBundle = ResourceBundle.getBundle("playnomicsAndroidAnalytics");
		this.context = cont;		
		this.activityName = context.getClass().getName();
		this.adEnabledCode = false;
	}
	
	public void updateContext(Context cont){
		this.removeComponent();
		this.context = cont;
	    this.initAdComponents();	
	}
	
	public void setEnableAdCode(boolean enable){
		this.adEnabledCode = enable;
	}
	
	public Context getContext(){
		return this.context;
	}
	
	public String getActivityName(){
		return this.activityName;
	}
	
	public String getFrameID(){
		return this.frameID;
	}
	
	public void removeComponent(){
		Activity parent = (Activity)context;
		Window window = parent.getWindow();
		ViewGroup mainView = ((ViewGroup)window.findViewById(android.R.id.content).getParent());
		
		for(int i = 0; i < mainView.getChildCount(); i++){
			ViewGroup v = (ViewGroup) mainView.getChildAt(i);
			
			if(v.getChildCount()>0){
				
				for(int j = 0; j < v.getChildCount(); j++){
					if(v.getChildAt(j) != null && this.adArea != null){
						if(v.getChildAt(j).getTag() != null && v.getChildAt(j).getTag().equals(this.adArea.imageUrl+""+this.frameID)){
							v.removeView(v.getChildAt(j));
						}
					}
				}
			}
		}
	}
	
	public void refreshProperties(JSONObject properties){
		this.removeComponent();
		this.properties = properties;
		this.initAdComponents();
	}
	
	private void initAdComponents(){
		try {
			
			this.background = new BaseAdComponent(this.properties.getJSONObject(resourceBundle.getString("frameResponseBackgroundInfo")), this, context, ImageType.adBackground);
			this.adArea = new BaseAdComponent(this.mergedAdInfoProperties(), this, context, ImageType.adClicked);
			this.closeButton = new BaseAdComponent(this.properties.getJSONObject(resourceBundle.getString("frameResponseCloseButtonInfo")), this, context, ImageType.adClosed);
			
			this.expirationSeconds = this.properties.getInt(this.resourceBundle.getString("frameResponseExpiration"));
			
			this.background.layoutComponents(0, 0);
			this.adArea.layoutComponents(this.background.xOffset, this.background.yOffset);
			this.closeButton.layoutComponents(this.background.xOffset, this.background.yOffset);
						
			this.checkForNullInfo();
			
			this.background.layout.setTag(this.adArea.imageUrl+""+this.frameID);	
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void checkForNullInfo(){
		if(this.background.imageUrl == null || this.background.imageUrl.equals("null")){			
			this.background.setUpLayoutParameters();
			this.background.status = AdComponentStatus.adComponentStatusCompleted;
		}
		
		if(this.closeButton.imageUrl == null || this.closeButton.imageUrl.equals("null")){
			this.closeButton.status = AdComponentStatus.adComponentStatusCompleted;
		}
		this.baseAdComponentReady();
	}
	
	private JSONObject mergedAdInfoProperties(){
		
		JSONObject adInfo = this.determineAdInfoToUse();
		JSONObject adLocationInfo = new JSONObject();
		
		try {
			adLocationInfo = this.properties.getJSONObject(this.resourceBundle.getString("frameResponseAdLocationInfo"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		JSONObject merged = new JSONObject();
		JSONObject[] objs = new JSONObject[] { adInfo, adLocationInfo };
		for (JSONObject obj : objs) {
		    @SuppressWarnings("unchecked")
			Iterator<String> it = obj.keys();
		    while (it.hasNext()) {
		        String key = it.next();
		        try {
					merged.put(key, obj.get(key));
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		}
				
		return merged;
	}
	
	private JSONObject determineAdInfoToUse(){
		try {
			
			return this.properties.getJSONArray(this.resourceBundle.getString("frameResponseAds")).getJSONObject(0);
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public DisplayResult start(){		
		
		this.display = true;
        
		if(this.properties != null){
			try {
				this.submitImpressionToServer(this.adArea.properties.getString(this.resourceBundle.getString("frameResponseAd_ImpressionUrl")));
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if(this.allComponentsLoaded()){
			this.display();
			return DisplayResult.displayResultDisplayed;
		}
		else{
			return DisplayResult.displayResultDisplayPending;
		}
	}
	
	public boolean allComponentsLoaded(){
		if(this.properties == null){
			return false;
		}
		else{
			return (this.background.status==AdComponentStatus.adComponentStatusCompleted 
					&& this.adArea.status==AdComponentStatus.adComponentStatusCompleted
					&& this.closeButton.status==AdComponentStatus.adComponentStatusCompleted);
		}
	}
	
	public void startExpiryTimer(){
		this.stopExpiryTimer();
		this.expirationTimer = new Timer();
		this.expirationTimer.schedule(new TimerTask() {          
		    @Override
		    public void run() {
		        notifyDelegate();      
		    }
		}, this.expirationSeconds*1000);
	}
	
	public void stopExpiryTimer(){
		if(this.expirationTimer != null){
			this.expirationTimer.cancel();
		}
	}

	@Override
	public void baseAdComponentClose() {
		
		this.stopExpiryTimer();
		
		Activity parent = (Activity)context;
		Window window = parent.getWindow();
		ViewGroup mainView = ((ViewGroup)window.findViewById(android.R.id.content).getParent());
				
		//need to do checks and see if there are groups inside groups with views inside views
		for(int i = 0; i < mainView.getChildCount(); i++){
			ViewGroup v = (ViewGroup) mainView.getChildAt(i);
			
			if(v.getChildCount()>0){
				
				for(int j = 0; j < v.getChildCount(); j++){
					
					if(v.getChildAt(j).getTag() != null && v.getChildAt(j).getTag().equals(this.adArea.imageUrl+""+this.frameID)){
						v.removeViewAt(j);
						Messaging.frameRemoveFrame(this.frameID);
						return;
					}
				}
			}
		}
	}

	@Override
	public void baseAdComponentOpen(MotionEvent event) {
		this.adClicked(event);
	}

	public void adClicked(MotionEvent event){
		int x = (int)event.getX();
		int y = (int)event.getY();
		String coordParams = "&x="+x+"&y="+y;
		String preExecuteUrl = null;
		String postExecuteUrl = null;
		String clickTarget = null;
		
		try {
			preExecuteUrl = this.adArea.properties.getString(this.resourceBundle.getString("frameResponseAd_PreExecuteUrl"))+""+coordParams;
			postExecuteUrl = this.adArea.properties.getString(this.resourceBundle.getString("frameResponseAd_PostExecuteUrl"))+""+coordParams;
			clickTarget = this.adArea.properties.getString(this.resourceBundle.getString("frameResponseAd_ClickTarget"));
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if(clickTarget != null){
			
			String exc;
			int c;
			
			AdAction actionType = this.determineActionType(clickTarget);
			String actionLabel = this.determineActionLabel(clickTarget);
			
			if(actionType.equals(AdAction.AdActionHTTP)){
				Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(clickTarget));
				this.context.startActivity(browserIntent);
			}
			else if(actionType.equals(AdAction.AdActionDefinedAction)){
				
				preExecuteUrl = preExecuteUrl.concat("&x="+x+"&y="+y);
				this.submitImpressionToServer(preExecuteUrl);
				
				try{
					Messaging.performActionForLabel(actionLabel);
					c = 2;
					exc = "";
				}
				catch (Exception e) {
					c = -6;
					exc = e.toString()+"+"+e.getMessage();
				}
				postExecuteUrl = postExecuteUrl.concat("&c="+c+"&e="+exc);
				this.submitImpressionToServer(postExecuteUrl);
			}
			else if(actionType.equals(AdAction.AdActionExecuteCode)){
				
				preExecuteUrl = preExecuteUrl.concat("&x="+x+"&y="+y);
				this.submitImpressionToServer(preExecuteUrl);
				
				if(this.adEnabledCode){

					try{
						Messaging.executeActionOnDelegate(actionLabel);
						c = 1;
						exc = "";
					}
					catch (Exception e) {
						c = -4;
						exc = e.toString()+"+"+e.getMessage();
					}
				}
				else{
					c = -3;
					exc = "";
				}
				
				postExecuteUrl = postExecuteUrl.concat("&c="+c+"&e="+exc);
				this.submitImpressionToServer(postExecuteUrl);
			}
		}
	}
	
	private AdAction determineActionType(String clickTargetUrl){
		String protocol = clickTargetUrl.substring(0, clickTargetUrl.indexOf(":"));
		
		if(protocol.equals(this.resourceBundle.getString("HTTP_ACTION_PREFIX")) 
				|| protocol.equals(this.resourceBundle.getString("HTTPS_ACTION_PREFIX"))){
			return AdAction.AdActionHTTP;
		}
		else if(protocol.equals(this.resourceBundle.getString("PNACTION_ACTION_PREFIX"))){
			return AdAction.AdActionDefinedAction;
		}
		else if(protocol.equals(this.resourceBundle.getString("PNEXECUTE_ACTION_PREFIX"))){
			return AdAction.AdActionExecuteCode;
		}
		else{
			return AdAction.AdActionUnkown;
		}
	}
	
	private String determineActionLabel(String clickTargetUrl){
		String resource = clickTargetUrl.substring(clickTargetUrl.indexOf(":"));
		resource.replaceAll("/", "");
		return resource;
	}
	
	private void submitImpressionToServer(final String impressionUrl){
		AsyncTask<Void, Void, Void> task = new AsyncTask<Void, Void, Void>() {

    		@Override
    		protected Void doInBackground(Void... params) {
    			InputStream content = null;
    		    try {
    				HttpClient httpclient = new DefaultHttpClient();
    			 	HttpResponse response = httpclient.execute(new HttpGet(impressionUrl));
    			 	content = response.getEntity().getContent();
    			} catch (Exception e) {
    				System.out.println("Network exception" + e);
    			}
    		    return null;
    		}
    	};
    	task.execute((Void)null);
	}

	public void notifyDelegate(){
		Messaging.refreshWithId(this.frameID);
	}
	
	@Override
	public void baseAdComponentReady() {
		if(this.allComponentsLoaded()){
			this.background.addSubComponent(this.adArea);
			this.background.addSubComponent(this.closeButton);	
			
			if(this.display){
				this.display();
			}
		}
	}
	
	public void display() {
		this.startExpiryTimer();
		Activity parent = (Activity)this.context;
		Window window = parent.getWindow();
		window.addContentView(this.background.layout, new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));			
	}
}

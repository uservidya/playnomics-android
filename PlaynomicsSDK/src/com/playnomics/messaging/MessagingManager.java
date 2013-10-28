package com.playnomics.messaging;

import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;

import com.playnomics.client.PlacementDataClient;
import com.playnomics.messaging.Placement.IPlacementStateObserver;
import com.playnomics.messaging.ui.IPlayViewFactory;
import com.playnomics.messaging.ui.RenderTaskFactory;
import com.playnomics.sdk.IPlaynomicsPlacementDelegate;
import com.playnomics.session.ICallbackProcessor;
import com.playnomics.session.Session;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Util;

public class MessagingManager implements IPlacementStateObserver {	
	private Util util;
	private ConcurrentHashMap<String, Placement> placementsByName;
	private ConcurrentHashMap<String, Placement> placementsByActivityName;
	private PlacementDataClient placementDataClient;
	private Logger logger;
	private ICallbackProcessor callbackProcessor;
	private RenderTaskFactory renderTaskFactory;
	
	public void setSession(Session session){
		this.callbackProcessor = session;
		this.placementDataClient.setSession(session);
	}
	
	public MessagingManager(IConfig config, 
			PlacementDataClient frameDataClient, 
			Util util, Logger logger, IPlayViewFactory viewFactory){
		this.placementDataClient = frameDataClient;
		this.util = util;
		this.placementsByName = new ConcurrentHashMap<String, Placement>();
		this.placementsByActivityName = new ConcurrentHashMap<String, Placement>();
		this.logger = logger;
		this.renderTaskFactory = new RenderTaskFactory(viewFactory, logger);
	}

	public void preloadPlacements(String[] frameNames){
		for(String frameName : frameNames){
			getOrAddPlacement(frameName);
		}
	}
		
	public void showPlacement(String frameId, Activity activity, IPlaynomicsPlacementDelegate delegate){
		Placement placement = getOrAddPlacement(frameId);
		placement.show(activity, delegate);
	}
	
	private Placement getOrAddPlacement(String placementName){
		Placement placement;
		if(!placementsByName.containsKey(placementName)){
			placement = new Placement(placementName, callbackProcessor, util, logger, this, renderTaskFactory);
			placementDataClient.loadPlacementInBackground(placement);
			placementsByName.put(placementName, placement);
		} else {
			placement = placementsByName.get(placementName);
		}
		return placement;
	}
	
	public void hidePlacement(String placementName){
		if(placementsByName.containsKey(placementName)){
			Placement frame = placementsByName.get(placementName);
			frame.hide();
		}
	}
	
	public void onPlacementShown(Activity activity, Placement frame){
		placementsByActivityName.put(getKeyForActivity(activity), frame);
	}
	
	public void onPlacementDisposed(Activity activity){
		placementsByActivityName.remove(activity);
	}
	
	public void onActivityResumed(Activity activity){
		String key = getKeyForActivity(activity);
		if(placementsByActivityName.containsKey(key)){
			Placement placement = placementsByActivityName.get(key);
			placement.attachActivity(activity);
		}
	}

	public void onActivityPaused(Activity activity){
		String key = getKeyForActivity(activity);
		if(placementsByActivityName.containsKey(key)){
			Placement placement = placementsByActivityName.get(key);
			placement.detachActivity();
		}
	}
	
	private String getKeyForActivity(Activity activity){
		return activity.getClass().getName();
	}
}

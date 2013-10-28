package com.playnomics.messaging;

import java.util.concurrent.ConcurrentHashMap;

import android.app.Activity;

import com.playnomics.client.FrameDataClient;
import com.playnomics.messaging.Frame.IFrameStateObserver;
import com.playnomics.messaging.ui.IPlayViewFactory;
import com.playnomics.messaging.ui.RenderTaskFactory;
import com.playnomics.sdk.IPlaynomicsFrameDelegate;
import com.playnomics.session.ICallbackProcessor;
import com.playnomics.session.Session;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Util;

public class MessagingManager implements IFrameStateObserver {	
	private Util util;
	private ConcurrentHashMap<String, Frame> framesById;
	private ConcurrentHashMap<String, Frame> framesByActivityName;
	private FrameDataClient frameDataClient;
	private Logger logger;
	private ICallbackProcessor callbackProcessor;
	private RenderTaskFactory renderTaskFactory;
	
	public void setSession(Session session){
		this.callbackProcessor = session;
		this.frameDataClient.setSession(session);
	}
	
	public MessagingManager(IConfig config, 
			FrameDataClient frameDataClient, 
			Util util, Logger logger, IPlayViewFactory viewFactory){
		this.frameDataClient = frameDataClient;
		this.util = util;
		this.framesById = new ConcurrentHashMap<String, Frame>();
		this.framesByActivityName = new ConcurrentHashMap<String, Frame>();
		this.logger = logger;
		this.renderTaskFactory = new RenderTaskFactory(viewFactory, logger);
	}

	public void preloadFrames(String[] frameIds){
		for(String frameId : frameIds){
			getOrAddFrame(frameId);
		}
	}
		
	public void showFrame(String frameId, Activity activity, IPlaynomicsFrameDelegate delegate){
		Frame frame = getOrAddFrame(frameId);
		frame.show(activity, delegate);
	}
	
	private Frame getOrAddFrame(String frameId){
		Frame frame;
		if(!framesById.containsKey(frameId)){
			frame = new Frame(frameId, callbackProcessor, util, logger, this, renderTaskFactory);
			frameDataClient.loadFrameInBackground(frame);
			framesById.put(frameId, frame);
		} else {
			frame = framesById.get(frameId);
		}
		return frame;
	}
	
	public void hideFrame(String frameId){
		if(framesById.containsKey(frameId)){
			Frame frame = framesById.get(frameId);
			frame.hide();
		}
	}
	
	public void onFrameShown(Activity activity, Frame frame){
		framesByActivityName.put(getKeyForActivity(activity), frame);
	}
	
	public void onFrameDisposed(Activity activity){
		framesByActivityName.remove(activity);
	}
	
	public void onActivityResumed(Activity activity){
		String key = getKeyForActivity(activity);
		if(framesByActivityName.containsKey(key)){
			Frame frame = framesByActivityName.get(key);
			frame.attachActivity(activity);
		}
	}

	public void onActivityPaused(Activity activity){
		String key = getKeyForActivity(activity);
		if(framesByActivityName.containsKey(key)){
			Frame frame = framesByActivityName.get(key);
			frame.detachActivity();
		}
	}
	
	private String getKeyForActivity(Activity activity){
		return activity.getClass().getName();
	}
}

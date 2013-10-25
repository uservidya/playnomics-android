package com.playnomics.messaging;

import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import android.app.Activity;
import com.playnomics.client.AssetClient;
import com.playnomics.client.AssetClient.ResponseStatus;
import com.playnomics.client.IHttpConnectionFactory;
import com.playnomics.messaging.Frame.FrameState;
import com.playnomics.messaging.Frame.IFrameStateObserver;
import com.playnomics.sdk.IPlaynomicsFrameDelegate;
import com.playnomics.session.Session;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;
import com.playnomics.util.Util;

public class MessagingManager implements IFrameStateObserver {	
	private IConfig config;
	private Util util;
	private ConcurrentHashMap<String, Frame> framesById;
	private ConcurrentHashMap<String, Frame> framesByActivityName;
	private IHttpConnectionFactory connectionFactory;
	private Session session;
	private Logger logger;
	private AssetClient client;
	
	public void setSession(Session session){
		this.session = session;
	}
	
	public MessagingManager(IConfig config, 
			IHttpConnectionFactory connectionFactory, 
			Util util, Logger logger){
		this.config = config;
		this.connectionFactory = connectionFactory;
		this.util = util;
		this.framesById = new ConcurrentHashMap<String, Frame>();
		this.framesByActivityName = new ConcurrentHashMap<String, Frame>();
		this.client = new AssetClient(connectionFactory);
		this.logger = logger;
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
			frame = new Frame(frameId, session, util, logger, this);
			loadFrameInBackground(frame);
			framesById.put(frameId, frame);
		} else {
			frame = framesById.get(frameId);
		}
		return frame;
	}
	
	private void loadFrameInBackground(final Frame frame){
		Runnable task = new Runnable() {
			public void run() {
				frame.setState(FrameState.LOAD_STARTED);
				String messagingApiUrl = config.getMessagingUrl();
				
				String adsPath = config.getMessagingPathAds();
				
				TreeMap<String, Object> queryParams = new TreeMap<String, Object>();
				queryParams.put(config.getMessagingFrameIdKey(), frame.getFrameId());
				queryParams.put(config.getBreadcrumbIdKey(), session.getBreadcrumbId());
				queryParams.put(config.getMessagingDeviceIdKey(), session.getDeviceId());
				queryParams.put(config.getApplicationIdKey(), session.getApplicationId());
				queryParams.put(config.getUserIdKey(), session.getUserId());
				queryParams.put(config.getSdkNameKey(), config.getSdkName());
				queryParams.put(config.getSdkVersionKey(), config.getSdkVersion());
				
				String requestUrl = connectionFactory.buildUrl(messagingApiUrl, adsPath, queryParams);
				
				AssetClient.AssetResponse jsonResponse = client.requestAsset(requestUrl);

				if(jsonResponse.getStatus() == ResponseStatus.SUCCESS){
					try {
						HtmlAd htmlAd = HtmlAd.createFrameFromBytes(jsonResponse.getData());
			
						if(htmlAd.getCloseButton() instanceof NativeCloseButton){
							NativeCloseButton closeButton = (NativeCloseButton)htmlAd.getCloseButton();
							AssetClient.AssetResponse imageResponse = client.requestAsset(closeButton.getImageUrl());
							if(imageResponse.getStatus() == ResponseStatus.SUCCESS){
								closeButton.setImageData(imageResponse.getData());
								frame.updateFrameData(htmlAd);
							}
						} else {
							frame.updateFrameData(htmlAd);
						}
					} catch (Exception ex) {
						logger.log(LogLevel.WARNING, ex, "Could not fetch message for frame");
						frame.setState(FrameState.LOAD_FAILED);
					}
				}
			}
		};
		Thread thread = new Thread(task);
		thread.start();
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

package com.playnomics.messaging;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import org.json.JSONException;

import com.playnomics.client.AssetClient;
import com.playnomics.client.AssetClient.ResponseStatus;
import com.playnomics.client.IHttpConnectionFactory;
import com.playnomics.session.Session;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Util;
import com.playnomics.util.Logger.LogLevel;

public class MessagingManager {
	private IConfig config;
	private Util util;
	private ConcurrentHashMap<String, Frame> framesById;
	private IHttpConnectionFactory connectionFactory;
	private Session session;
	private Logger logger;
	private AssetClient client;
	
	public MessagingManager(Session session, IConfig config, 
			IHttpConnectionFactory connectionFactory, 
			Util util, Logger logger){
		this.session = session;
		this.config = config;
		this.connectionFactory = connectionFactory;
		this.util = util;
		this.framesById = new ConcurrentHashMap<String, Frame>();
		this.client = new AssetClient(connectionFactory);
		this.logger = logger;
	}

	public void preloadFrames(String[] frameIds){
		for(String frameId : frameIds){
			getOrAddFrame(frameId);
		}
	}
		
	public void showFrame(String frameId){
		Frame frame = getOrAddFrame(frameId);
		frame.show();
	}
	
	private Frame getOrAddFrame(String frameId){
		Frame frame;
		if(!framesById.containsKey(frameId)){
			frame = new Frame(frameId);
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
				
				AssetClient.AssetResponse response = client.requestAsset(requestUrl);
				if(response.getStatus() == ResponseStatus.SUCCESS){
					try {
						HtmlAd htmlAd = HtmlAd.createFrameFromBytes(response.getData());
						frame.updateFrameData(htmlAd);
					} catch (UnsupportedEncodingException ex) {
						logger.log(LogLevel.WARNING, ex, "Could not fetch ad for frame");
					} catch (JSONException ex) {
						logger.log(LogLevel.WARNING, ex, "Could not fetch ad for frame");
					} catch (Exception ex){
						logger.log(LogLevel.WARNING, ex, "Could not fetch ad for frame");
					}
				}
			}
		};
		Thread thread = new Thread(task);
		thread.start();
	}
	
	public void hideFrame(String frameId){
		
	}
}

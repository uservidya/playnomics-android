package com.playnomics.client;

import java.util.TreeMap;

import com.playnomics.messaging.Frame;
import com.playnomics.messaging.Frame.FrameState;
import com.playnomics.messaging.HtmlAd;
import com.playnomics.messaging.NativeCloseButton;
import com.playnomics.session.Session;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;

public class FrameAssetClient extends AssetClient {
	private IConfig config;
	private Session session;
	private IHttpConnectionFactory connectionFactory;
	private Logger logger;
	
	public void setSession(Session session){
		this.session = session;
	}

	public FrameAssetClient(IHttpConnectionFactory connectionFactory, IConfig config, Logger logger) {
		super(connectionFactory);
		this.logger = logger;
		this.config = config;
	}
		
	public Thread loadFrameInBackground(final Frame frame){
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
				
				AssetClient.AssetResponse jsonResponse = requestAsset(requestUrl);

				if(jsonResponse.getStatus() == ResponseStatus.SUCCESS){
					try {
						HtmlAd htmlAd = HtmlAd.createFrameFromBytes(jsonResponse.getData());
			
						if(htmlAd.getCloseButton() instanceof NativeCloseButton){
							NativeCloseButton closeButton = (NativeCloseButton)htmlAd.getCloseButton();
							AssetClient.AssetResponse imageResponse = requestAsset(closeButton.getImageUrl());
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
		return thread;
	}
}

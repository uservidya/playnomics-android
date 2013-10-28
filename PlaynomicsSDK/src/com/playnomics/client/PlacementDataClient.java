package com.playnomics.client;

import java.util.TreeMap;

import com.playnomics.client.AssetClient.ResponseStatus;
import com.playnomics.messaging.Placement;
import com.playnomics.messaging.Placement.PlacementState;
import com.playnomics.messaging.HtmlAd;
import com.playnomics.messaging.HtmlAdFactory;
import com.playnomics.messaging.NativeCloseButton;
import com.playnomics.session.Session;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;

public class PlacementDataClient {
	private IConfig config;
	private Session session;
	private Logger logger;
	private HtmlAdFactory adFactory;
	private AssetClient assetClient;
	
	public void setSession(Session session){
		this.session = session;
	}

	public PlacementDataClient(AssetClient assetClient, IConfig config, Logger logger, HtmlAdFactory adFactory) {
		this.logger = logger;
		this.config = config;
		this.adFactory = adFactory;
		this.assetClient = assetClient;
	}
		
	public Thread loadPlacementInBackground(final Placement frame){
		Runnable task = new Runnable() {
			public void run() {
				frame.setState(PlacementState.LOAD_STARTED);
				String messagingApiUrl = config.getMessagingUrl();
				
				String adsPath = config.getMessagingPathAds();
				
				TreeMap<String, Object> queryParams = new TreeMap<String, Object>();
				queryParams.put(config.getMessagingFrameIdKey(), frame.getPlacementName());
				queryParams.put(config.getBreadcrumbIdKey(), session.getBreadcrumbId());
				queryParams.put(config.getMessagingDeviceIdKey(), session.getDeviceId());
				queryParams.put(config.getApplicationIdKey(), session.getApplicationId());
				queryParams.put(config.getUserIdKey(), session.getUserId());
				queryParams.put(config.getSdkNameKey(), config.getSdkName());
				queryParams.put(config.getSdkVersionKey(), config.getSdkVersion());
				
				AssetClient.AssetResponse jsonResponse = assetClient.requestAsset(messagingApiUrl, adsPath, queryParams);

				if(jsonResponse.getStatus() == ResponseStatus.SUCCESS){
					try {
						HtmlAd htmlAd = adFactory.createDataFromBytes(jsonResponse.getData());
			
						if(htmlAd.getCloseButton() instanceof NativeCloseButton){
							NativeCloseButton closeButton = (NativeCloseButton)htmlAd.getCloseButton();
							AssetClient.AssetResponse imageResponse = assetClient.requestAsset(closeButton.getImageUrl());
							if(imageResponse.getStatus() == ResponseStatus.SUCCESS){
								closeButton.setImageData(imageResponse.getData());
								frame.updatePlacementData(htmlAd);
							} else {
								frame.setState(PlacementState.LOAD_FAILED);
							}
						} else {
							frame.updatePlacementData(htmlAd);
						}
					} catch (Exception ex) {
						logger.log(LogLevel.WARNING, ex, "Could not fetch message for frame");
						frame.setState(PlacementState.LOAD_FAILED);
					}
				} else {
					frame.setState(PlacementState.LOAD_FAILED);
				}
			}
		};
		Thread thread = new Thread(task);
		thread.start();
		return thread;
	}
}

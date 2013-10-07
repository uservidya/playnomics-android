package com.playnomics.events;

import java.util.TreeMap;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public abstract class PlaynomicsEvent {
	private TreeMap<String, Object> eventParameters;
	private EpochTime eventTime;
	
	private final String applicationIdKey = "a";
	private final String userIdKey = "u";
	private final String breadcrumbIdKey = "b";
	private final String eventTimeKey = "t";
	
	private final String sdkVersionKey = "ever";
	private final String sdkNameKey = "esrc";
	//appStart/appPage
	protected final String timeZoneOffsetKey = "z";
	//appRunning/appPause
	protected final String sequenceKey = "q";
	protected final String touchesKey  = "c";
	protected final String totalTouchesKey = "e";
	protected final String keysPressedKey = "k";
	protected final String totalKeysPressedKey = "l";
	protected final String sessionStartTimeKey = "r";
	protected final String intervalMillisecondsKey = "d";
	protected final String collectionModeKey = "m";
	//user info
	protected final String userInfoType = "pt";
	//protected final String PNEventParameterUserInfoPushToken = "pushTok";
	protected final String userInfoSourceKey = "po";
	protected final String userInfoCampaignKey = "pm";
	protected final String userInfoInstallDateKey = "pi";
	//transactions
	protected final String transactionIdKey = "r";
	protected final String transactionTypeKey = "tt";
	protected final String transactionItemIdKey = "i";
	protected final String transactionQuantityKey = "tq";
	protected final String transactionCurrencyTypeFormatKey = "tc%d";
	protected final String transactionCurrencyValueFormatKey = "tv%d";
	protected final String transactionCurrencyCategoryFormatKey = "ta%d";
	//milestones
	protected final String milestoneIdKey = "mi";
	protected final String milestoneNameKey = "mn";
	//push notifications
	protected final String pushToken = "pt";

	protected PlaynomicsEvent(GameSessionInfo sessionInfo){
		this.eventTime = EpochTime.getEpochTimeNow();
		
		final String source = "aj";
		
		this.eventParameters = new TreeMap<String, Object>();
		eventParameters.put(applicationIdKey, sessionInfo.getApplicationId());
		eventParameters.put(userIdKey, sessionInfo.getUserId());
		eventParameters.put(breadcrumbIdKey, sessionInfo.getBreadcrumbId());
		eventParameters.put(this.getSessionKey(), sessionInfo.getSessionId());
		eventParameters.put(sdkVersionKey, Util.getSdkVersion());
		eventParameters.put(sdkNameKey, source);
		eventParameters.put(eventTimeKey, this.eventTime);
	}
	
	public EpochTime getEventTime(){
		return eventTime;
	}
	
	protected abstract String getBaseUrl();
	
	protected abstract String getSessionKey();

	protected void appendParameter(String key, Object value){
		eventParameters.put(key, value);
	}
}

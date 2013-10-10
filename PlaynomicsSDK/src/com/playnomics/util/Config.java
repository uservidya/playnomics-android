package com.playnomics.util;

import java.util.ResourceBundle;

public class Config {
	private final String CONFIG_FILE = "PlaynomicsSDKConfig";
	
	private ResourceBundle bundle;
	
	public Config(){
		bundle = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public String getSdkVersion(){
		return bundle.getString("sdk.version");
	}
	
	public String getSdkName(){
		return bundle.getString("sdk.name");
	}
	
	public String getTestEventsUrl(){
		return bundle.getString("eventsUrl.test");
	}
	
	public String getProdEventsUrl(){
		return bundle.getString("eventsUrl.prod");
	}
	
	public String getTestMessagingUrl(){
		return bundle.getString("messagingUrl.test");
	}
	
	public String getProdMessagingUrl(){
		return bundle.getString("messagingUrl.prod");
	}
	
	public String getApplicationIdKey(){
		return bundle.getString("eventKeys.applicationIdKey");
	}
	
	public String getUserIdKey(){
		return bundle.getString("eventKeys.userIdKey");
	}
	
	public String getBreadcrumbIdKey(){
		return bundle.getString("eventKeys.breadcrumbIdKey");
	}
	
	public String getEventTimeKey(){
	    return bundle.getString("eventKeys.eventTimeKey");
	}

	public String getSdkVersionKey(){
	    return bundle.getString("eventKeys.sdkVersionKey");
	}

	public String getSdkNameKey(){
	    return bundle.getString("eventKeys.sdkNameKey");
	}

	public String getTimeZoneOffsetKey(){
	    return bundle.getString("eventKeys.timeZoneOffsetKey");
	}

	public String getSequenceKey(){
	    return bundle.getString("eventKeys.sequenceKey");
	}

	public String getTouchesKey(){
	    return bundle.getString("eventKeys.touchesKey");
	}

	public String getTotalTouchesKey(){
	    return bundle.getString("eventKeys.totalTouchesKey");
	}

	public String getKeysPressedKey(){
	    return bundle.getString("eventKeys.keysPressedKey");
	}

	public String getTotalKeysPressedKey(){
	    return bundle.getString("eventKeys.totalKeysPressedKey");
	}

	public String getSessionStartTimeKey(){
	    return bundle.getString("eventKeys.sessionStartTimeKey");
	}

	public String getIntervalMillisecondsKey(){
	    return bundle.getString("eventKeys.intervalMillisecondsKey");
	}

	public String getCollectionModeKey(){
	    return bundle.getString("eventKeys.collectionModeKey");
	}

	public String getSessionPauseTimeKey(){
	    return bundle.getString("eventKeys.sessionPauseTimeKey");
	}

	public String getUserInfoTypeKey(){
	    return bundle.getString("eventKeys.userInfoTypeKey");
	}

	public String getUserInfoSourceKey(){
	    return bundle.getString("eventKeys.userInfoSourceKey");
	}

	public String getUserInfoCampaignKey(){
	    return bundle.getString("eventKeys.userInfoCampaignKey");
	}

	public String getUserInfoInstallDateKey(){
	    return bundle.getString("eventKeys.userInfoInstallDateKey");
	}
	
	public String getUserInfoDeviceIdKey(){
		return bundle.getString("eventKeys.userInfoDeviceIdKey");
	}

	public String getUserInfoPushTokenKey(){
		return bundle.getString("eventKeys.userInfoPushTokenKey");
	}
	
	public String getTransactionIdKey(){
	    return bundle.getString("eventKeys.transactionIdKey");
	}

	public String getTransactionTypeKey(){
	    return bundle.getString("eventKeys.transactionTypeKey");
	}

	public String getTransactionItemIdKey(){
	    return bundle.getString("eventKeys.transactionItemIdKey");
	}

	public String getTransactionQuantityKey(){
	    return bundle.getString("eventKeys.transactionQuantityKey");
	}

	public String getTransactionCurrencyTypeFormatKey(){
	    return bundle.getString("eventKeys.transactionCurrencyTypeFormatKey");
	}

	public String getTransactionCurrencyValueFormatKey(){
	    return bundle.getString("eventKeys.transactionCurrencyValueFormatKey");
	}

	public String getTransactionCurrencyCategoryFormatKey(){
	    return bundle.getString("eventKeys.transactionCurrencyCategoryFormatKey");
	}

	public String getMilestoneIdKey(){
	    return bundle.getString("eventKeys.milestoneIdKey");
	}

	public String getMilestoneNameKey(){
	    return bundle.getString("eventKeys.milestoneNameKey");
	}
	
	public int getAppRunningIntervalSeconds(){
		return Integer.parseInt(bundle.getString("appRunningIntervalSeconds"));
	}
	
	public int getAppRunningIntervalMilliseconds(){
		return getAppRunningIntervalSeconds() * 1000;
	}
	
	public int getCollectionMode(){
		return Integer.parseInt(bundle.getString("collectionMode"));
	}
	
	public String getEventPathUserInfo(){
		return bundle.getString("eventNames.userInfo");
	}
	
	public String getEventPathMilestone(){
		return bundle.getString("eventNames.milestone");
	}
	
	public String getEventPathTransaction(){
		return bundle.getString("eventNames.transaction");
	}
	
	public String getEventPathAppRunning(){
		return bundle.getString("eventNames.appRunning");
	}

	public String getEventPathAppPage(){
		return bundle.getString("eventNames.appPage");
	}
	
	public String getEventPathAppResume(){
		return bundle.getString("eventNames.appResume");
	}
	
	public String getEventPathAppStart(){
		return bundle.getString("eventNames.appStart");
	}
	
	public String getEventPathAppPause(){
		return bundle.getString("eventNames.appPause");
	}
}

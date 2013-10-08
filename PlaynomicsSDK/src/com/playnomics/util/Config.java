package com.playnomics.util;

import java.util.ResourceBundle;

public class Config {
	private final String CONFIG_FILE = "PlaynomicsSDKConfig";
	
	private ResourceBundle bundle;
	
	public Config(){
		this.bundle = ResourceBundle.getBundle(CONFIG_FILE);
	}
	
	public String getSdkVersion(){
		return this.bundle.getString("sdk.version");
	}
	
	public String getSdkName(){
		return this.bundle.getString("sdk.name");
	}
	
	public String getTestEventsUrl(){
		return this.bundle.getString("eventsUrl.test");
	}
	
	public String getProdEventsUrl(){
		return this.bundle.getString("eventsUrl.prod");
	}
	
	public String getTestMessagingUrl(){
		return this.bundle.getString("messagingUrl.test");
	}
	
	public String getProdMessagingUrl(){
		return this.bundle.getString("messagingUrl.prod");
	}
	
	public String getApplicationIdKey(){
		return this.bundle.getString("eventKeys.applicationIdKey");
	}
	
	public String getUserIdKey(){
		return this.bundle.getString("eventKeys.userIdKey");
	}
	
	public String getBreadcrumbIdKey(){
		return this.bundle.getString("eventKeys.breadcrumbIdKey");
	}
	
	public String getEventTimeKey(){
	    return this.bundle.getString("eventKeys.eventTimeKey");
	}

	public String getSdkVersionKey(){
	    return this.bundle.getString("eventKeys.sdkVersionKey");
	}

	public String getSdkNameKey(){
	    return this.bundle.getString("eventKeys.sdkNameKey");
	}

	public String getTimeZoneOffsetKey(){
	    return this.bundle.getString("eventKeys.timeZoneOffsetKey");
	}

	public String getSequenceKey(){
	    return this.bundle.getString("eventKeys.sequenceKey");
	}

	public String getTouchesKey(){
	    return this.bundle.getString("eventKeys.touchesKey");
	}

	public String getTotalTouchesKey(){
	    return this.bundle.getString("eventKeys.totalTouchesKey");
	}

	public String getKeysPressedKey(){
	    return this.bundle.getString("eventKeys.keysPressedKey");
	}

	public String getTotalKeysPressedKey(){
	    return this.bundle.getString("eventKeys.totalKeysPressedKey");
	}

	public String getSessionStartTimeKey(){
	    return this.bundle.getString("eventKeys.sessionStartTimeKey");
	}

	public String getIntervalMillisecondsKey(){
	    return this.bundle.getString("eventKeys.intervalMillisecondsKey");
	}

	public String getCollectionModeKey(){
	    return this.bundle.getString("eventKeys.collectionModeKey");
	}

	public String getSessionPauseTimeKey(){
	    return this.bundle.getString("eventKeys.sessionPauseTimeKey");
	}

	public String getUserInfoTypeKey(){
	    return this.bundle.getString("eventKeys.userInfoTypeKey");
	}

	public String getUserInfoSourceKey(){
	    return this.bundle.getString("eventKeys.userInfoSourceKey");
	}

	public String getUserInfoCampaignKey(){
	    return this.bundle.getString("eventKeys.userInfoCampaignKey");
	}

	public String getUserInfoInstallDateKey(){
	    return this.bundle.getString("eventKeys.userInfoInstallDateKey");
	}

	public String getTransactionIdKey(){
	    return this.bundle.getString("eventKeys.transactionIdKey");
	}

	public String getTransactionTypeKey(){
	    return this.bundle.getString("eventKeys.transactionTypeKey");
	}

	public String getTransactionItemIdKey(){
	    return this.bundle.getString("eventKeys.transactionItemIdKey");
	}

	public String getTransactionQuantityKey(){
	    return this.bundle.getString("eventKeys.transactionQuantityKey");
	}

	public String getTransactionCurrencyTypeFormatKey(){
	    return this.bundle.getString("eventKeys.transactionCurrencyTypeFormatKey");
	}

	public String getTransactionCurrencyValueFormatKey(){
	    return this.bundle.getString("eventKeys.transactionCurrencyValueFormatKey");
	}

	public String getTransactionCurrencyCategoryFormatKey(){
	    return this.bundle.getString("eventKeys.transactionCurrencyCategoryFormatKey");
	}

	public String getMilestoneIdKey(){
	    return this.bundle.getString("eventKeys.milestoneIdKey");
	}

	public String getMilestoneNameKey(){
	    return this.bundle.getString("eventKeys.milestoneNameKey");
	}
	
	public int getAppRunningIntervalSeconds(){
		return Integer.parseInt(this.bundle.getString("appRunningIntervalSeconds"));
	}
	
	public int getAppRunningIntervalMilliseconds(){
		return this.getAppRunningIntervalSeconds() * 1000;
	}
	
	public int getCollectionMode(){
		return Integer.parseInt(this.bundle.getString("collectionMode"));
	}
}

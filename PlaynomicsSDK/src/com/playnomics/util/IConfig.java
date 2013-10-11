package com.playnomics.util;

public interface IConfig {
	
	public String getSdkVersion();

	public String getSdkName();

	public String getTestEventsUrl();

	public String getProdEventsUrl();

	public String getTestMessagingUrl();

	public String getProdMessagingUrl();

	public String getApplicationIdKey();

	public String getUserIdKey();

	public String getBreadcrumbIdKey();

	public String getEventTimeKey();

	public String getSdkVersionKey();

	public String getSdkNameKey();

	public String getTimeZoneOffsetKey();

	public String getSequenceKey();

	public String getTouchesKey();

	public String getTotalTouchesKey();

	public String getKeysPressedKey();

	public String getTotalKeysPressedKey();

	public String getSessionStartTimeKey();

	public String getIntervalMillisecondsKey();

	public String getCollectionModeKey();

	public String getSessionPauseTimeKey();

	public String getUserInfoTypeKey();

	public String getUserInfoSourceKey();

	public String getUserInfoCampaignKey();

	public String getUserInfoInstallDateKey();

	public String getUserInfoDeviceIdKey();

	public String getUserInfoPushTokenKey();

	public String getTransactionIdKey();

	public String getTransactionTypeKey();

	public String getTransactionItemIdKey();

	public String getTransactionQuantityKey();

	public String getTransactionCurrencyTypeFormatKey();

	public String getTransactionCurrencyValueFormatKey();

	public String getTransactionCurrencyCategoryFormatKey();

	public String getMilestoneIdKey();

	public String getMilestoneNameKey();

	public int getAppRunningIntervalSeconds();

	public int getAppRunningIntervalMilliseconds();

	public int getCollectionMode();

	public String getEventPathUserInfo();

	public String getEventPathMilestone();

	public String getEventPathTransaction();

	public String getEventPathAppRunning();

	public String getEventPathAppPage();

	public String getEventPathAppResume();

	public String getEventPathAppStart();

	public String getEventPathAppPause();
}
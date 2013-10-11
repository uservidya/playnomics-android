package com.playnomics.util;

import java.util.ResourceBundle;

public class Config implements IConfig {
	private final String CONFIG_FILE = "PlaynomicsSDKConfig";

	private ResourceBundle bundle;

	public Config() {
		bundle = ResourceBundle.getBundle(CONFIG_FILE);
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSdkVersion()
	 */
	public String getSdkVersion() {
		return bundle.getString("sdk.version");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSdkName()
	 */
	public String getSdkName() {
		return bundle.getString("sdk.name");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTestEventsUrl()
	 */
	public String getTestEventsUrl() {
		return bundle.getString("eventsUrl.test");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getProdEventsUrl()
	 */
	public String getProdEventsUrl() {
		return bundle.getString("eventsUrl.prod");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTestMessagingUrl()
	 */
	public String getTestMessagingUrl() {
		return bundle.getString("messagingUrl.test");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getProdMessagingUrl()
	 */
	public String getProdMessagingUrl() {
		return bundle.getString("messagingUrl.prod");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getApplicationIdKey()
	 */
	public String getApplicationIdKey() {
		return bundle.getString("eventKeys.applicationIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserIdKey()
	 */
	public String getUserIdKey() {
		return bundle.getString("eventKeys.userIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getBreadcrumbIdKey()
	 */
	public String getBreadcrumbIdKey() {
		return bundle.getString("eventKeys.breadcrumbIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventTimeKey()
	 */
	public String getEventTimeKey() {
		return bundle.getString("eventKeys.eventTimeKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSdkVersionKey()
	 */
	public String getSdkVersionKey() {
		return bundle.getString("eventKeys.sdkVersionKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSdkNameKey()
	 */
	public String getSdkNameKey() {
		return bundle.getString("eventKeys.sdkNameKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTimeZoneOffsetKey()
	 */
	public String getTimeZoneOffsetKey() {
		return bundle.getString("eventKeys.timeZoneOffsetKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSequenceKey()
	 */
	public String getSequenceKey() {
		return bundle.getString("eventKeys.sequenceKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTouchesKey()
	 */
	public String getTouchesKey() {
		return bundle.getString("eventKeys.touchesKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTotalTouchesKey()
	 */
	public String getTotalTouchesKey() {
		return bundle.getString("eventKeys.totalTouchesKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getKeysPressedKey()
	 */
	public String getKeysPressedKey() {
		return bundle.getString("eventKeys.keysPressedKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTotalKeysPressedKey()
	 */
	public String getTotalKeysPressedKey() {
		return bundle.getString("eventKeys.totalKeysPressedKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSessionStartTimeKey()
	 */
	public String getSessionStartTimeKey() {
		return bundle.getString("eventKeys.sessionStartTimeKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getIntervalMillisecondsKey()
	 */
	public String getIntervalMillisecondsKey() {
		return bundle.getString("eventKeys.intervalMillisecondsKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getCollectionModeKey()
	 */
	public String getCollectionModeKey() {
		return bundle.getString("eventKeys.collectionModeKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getSessionPauseTimeKey()
	 */
	public String getSessionPauseTimeKey() {
		return bundle.getString("eventKeys.sessionPauseTimeKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserInfoTypeKey()
	 */
	public String getUserInfoTypeKey() {
		return bundle.getString("eventKeys.userInfoTypeKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserInfoSourceKey()
	 */
	public String getUserInfoSourceKey() {
		return bundle.getString("eventKeys.userInfoSourceKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserInfoCampaignKey()
	 */
	public String getUserInfoCampaignKey() {
		return bundle.getString("eventKeys.userInfoCampaignKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserInfoInstallDateKey()
	 */
	public String getUserInfoInstallDateKey() {
		return bundle.getString("eventKeys.userInfoInstallDateKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserInfoDeviceIdKey()
	 */
	public String getUserInfoDeviceIdKey() {
		return bundle.getString("eventKeys.userInfoDeviceIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getUserInfoPushTokenKey()
	 */
	public String getUserInfoPushTokenKey() {
		return bundle.getString("eventKeys.userInfoPushTokenKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionIdKey()
	 */
	public String getTransactionIdKey() {
		return bundle.getString("eventKeys.transactionIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionTypeKey()
	 */
	public String getTransactionTypeKey() {
		return bundle.getString("eventKeys.transactionTypeKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionItemIdKey()
	 */
	public String getTransactionItemIdKey() {
		return bundle.getString("eventKeys.transactionItemIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionQuantityKey()
	 */
	public String getTransactionQuantityKey() {
		return bundle.getString("eventKeys.transactionQuantityKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionCurrencyTypeFormatKey()
	 */
	public String getTransactionCurrencyTypeFormatKey() {
		return bundle.getString("eventKeys.transactionCurrencyTypeFormatKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionCurrencyValueFormatKey()
	 */
	public String getTransactionCurrencyValueFormatKey() {
		return bundle.getString("eventKeys.transactionCurrencyValueFormatKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getTransactionCurrencyCategoryFormatKey()
	 */
	public String getTransactionCurrencyCategoryFormatKey() {
		return bundle
				.getString("eventKeys.transactionCurrencyCategoryFormatKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getMilestoneIdKey()
	 */
	public String getMilestoneIdKey() {
		return bundle.getString("eventKeys.milestoneIdKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getMilestoneNameKey()
	 */
	public String getMilestoneNameKey() {
		return bundle.getString("eventKeys.milestoneNameKey");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getAppRunningIntervalSeconds()
	 */
	public int getAppRunningIntervalSeconds() {
		return Integer.parseInt(bundle.getString("appRunningIntervalSeconds"));
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getAppRunningIntervalMilliseconds()
	 */
	public int getAppRunningIntervalMilliseconds() {
		return getAppRunningIntervalSeconds() * 1000;
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getCollectionMode()
	 */
	public int getCollectionMode() {
		return Integer.parseInt(bundle.getString("collectionMode"));
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathUserInfo()
	 */
	public String getEventPathUserInfo() {
		return bundle.getString("eventNames.userInfo");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathMilestone()
	 */
	public String getEventPathMilestone() {
		return bundle.getString("eventNames.milestone");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathTransaction()
	 */
	public String getEventPathTransaction() {
		return bundle.getString("eventNames.transaction");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathAppRunning()
	 */
	public String getEventPathAppRunning() {
		return bundle.getString("eventNames.appRunning");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathAppPage()
	 */
	public String getEventPathAppPage() {
		return bundle.getString("eventNames.appPage");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathAppResume()
	 */
	public String getEventPathAppResume() {
		return bundle.getString("eventNames.appResume");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathAppStart()
	 */
	public String getEventPathAppStart() {
		return bundle.getString("eventNames.appStart");
	}

	/* (non-Javadoc)
	 * @see com.playnomics.util.IConfigLookup#getEventPathAppPause()
	 */
	public String getEventPathAppPause() {
		return bundle.getString("eventNames.appPause");
	}
}

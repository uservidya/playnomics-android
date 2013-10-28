package com.playnomics.events;

import java.util.Date;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.IConfig;

public class UserInfoEvent extends ExplicitEvent {

	public UserInfoEvent(IConfig config, GameSessionInfo sessionInfo,
			String source, String campaign, Date installDate) {
		super(config, sessionInfo);
		appendParameter(config.getUserInfoSourceKey(), source);
		appendParameter(config.getUserInfoCampaignKey(), campaign);
		// date should be in EPOCH format

		if (installDate != null) {
			appendParameter(config.getUserInfoInstallDateKey(),
					installDate.getTime());
		}
		appendParameter(config.getUserInfoTypeKey(), "update");
	}

	public UserInfoEvent(IConfig config, GameSessionInfo sessionInfo,
			String pushRegistrationId, String deviceId) {
		super(config, sessionInfo);
		appendParameter(config.getUserInfoPushTokenKey(), pushRegistrationId);
		appendParameter(config.getUserInfoDeviceIdKey(), deviceId);
		appendParameter(config.getUserInfoTypeKey(), "update");
	}

	@Override
	public String getUrlPath() {
		return config.getEventPathUserInfo();
	}
}

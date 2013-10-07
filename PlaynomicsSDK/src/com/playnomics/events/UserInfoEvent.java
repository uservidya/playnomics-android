package com.playnomics.events;

import java.util.Date;

import com.playnomics.session.GameSessionInfo;

public class UserInfoEvent extends ExplicitEvent {

	public UserInfoEvent(GameSessionInfo sessionInfo, String source, String campaign, Date installDate){
		super(sessionInfo);	
		appendParameter(userInfoSourceKey, source);
		appendParameter(userInfoCampaignKey, campaign);
		appendParameter(userInfoInstallDateKey, installDate);
		appendParameter(userInfoType, "update");
	}
	
	@Override
	protected String getBaseUrl() {
		return "userInfo";
	}
}

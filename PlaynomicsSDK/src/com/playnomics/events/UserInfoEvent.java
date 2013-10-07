package com.playnomics.events;

import java.util.Date;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Util;

public class UserInfoEvent extends ExplicitEvent {

	public UserInfoEvent(Util util, GameSessionInfo sessionInfo, String source, String campaign, Date installDate){
		super(util, sessionInfo);	
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

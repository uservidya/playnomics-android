package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppPageEvent extends ImplicitEvent {
	
	public AppPageEvent(Config config, GameSessionInfo sessionInfo, LargeGeneratedId instanceId){
		super(config, sessionInfo, instanceId);
		appendParameter(config.getTimeZoneOffsetKey(), EventTime.getMinutesTimezoneOffset());
	}

	@Override
	public String getUrlPath() {
		return "appPage";
	}
}

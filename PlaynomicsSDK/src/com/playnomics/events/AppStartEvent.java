package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppStartEvent extends ImplicitEvent {
	
	public AppStartEvent(IConfig config, GameSessionInfo sessionInfo, LargeGeneratedId instanceId){
		super(config, sessionInfo, instanceId);
		appendParameter(config.getTimeZoneOffsetKey(), EventTime.getMinutesTimezoneOffset());
	}
	
	@Override
	public String getUrlPath() {
		return config.getEventPathAppStart();
	}
}

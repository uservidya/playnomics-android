package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppStartEvent extends ImplicitEvent {
	
	public AppStartEvent(GameSessionInfo sessionInfo, LargeGeneratedId instanceId){
		super(sessionInfo, instanceId);
		appendParameter(timeZoneOffsetKey, Util.getMinutesTimezoneOffset());
	}
	
	@Override
	protected String getBaseUrl() {
		return "appStart";
	}
}

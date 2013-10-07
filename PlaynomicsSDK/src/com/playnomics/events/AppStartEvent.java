package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppStartEvent extends ImplicitEvent {
	
	public AppStartEvent(Util util, GameSessionInfo sessionInfo, LargeGeneratedId instanceId){
		super(util, sessionInfo, instanceId);
		appendParameter(timeZoneOffsetKey, util.getMinutesTimezoneOffset());
	}
	
	@Override
	protected String getBaseUrl() {
		return "appStart";
	}
}

package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppResumeEvent extends ImplicitEvent {

	public AppResumeEvent(Util util, GameSessionInfo sessionInfo, LargeGeneratedId instanceId, EventTime sessionStartTime, 
			EventTime sessionPauseTime, int sequenceNumber)
	{
		super(util, sessionInfo, instanceId);
		
		appendParameter(sequenceKey, sequenceNumber);
		appendParameter(sessionStartTimeKey, sessionStartTime);
		appendParameter(sessionPauseTimeKey, sessionPauseTime);
	}
	
	@Override
	protected String getBaseUrl() {
		return "appResume";
	}
}

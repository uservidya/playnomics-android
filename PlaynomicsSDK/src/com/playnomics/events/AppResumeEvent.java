package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppResumeEvent extends ImplicitEvent {

	public AppResumeEvent(GameSessionInfo sessionInfo, LargeGeneratedId instanceId, EventTime sessionStartTime, 
			EventTime sessionPauseTime, int sequenceNumber)
	{
		super(sessionInfo, instanceId);
		
		appendParameter(sequenceKey, sequenceNumber);
		appendParameter(sessionStartTimeKey, sessionStartTime);
	}
	
	@Override
	protected String getBaseUrl() {
		return "appResume";
	}
}

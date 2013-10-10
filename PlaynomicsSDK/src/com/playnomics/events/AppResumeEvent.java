package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppResumeEvent extends ImplicitEvent {

	public AppResumeEvent(Config config, GameSessionInfo sessionInfo,
			LargeGeneratedId instanceId, EventTime sessionStartTime,
			EventTime sessionPauseTime, int sequenceNumber) {
		super(config, sessionInfo, instanceId);

		appendParameter(config.getSequenceKey(), sequenceNumber);
		appendParameter(config.getSessionStartTimeKey(), sessionStartTime);
		appendParameter(config.getSessionPauseTimeKey(), sessionPauseTime);
	}

	@Override
	public String getUrlPath() {
		return config.getEventPathAppResume();
	}
}

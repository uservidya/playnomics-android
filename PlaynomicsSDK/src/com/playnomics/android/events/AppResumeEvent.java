package com.playnomics.android.events;

import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.*;

public class AppResumeEvent extends ImplicitEvent {

	public AppResumeEvent(IConfig config, GameSessionInfo sessionInfo,
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

package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppPauseEvent extends ImplicitEvent {
	
	public AppPauseEvent(GameSessionInfo sessionInfo, LargeGeneratedId instanceId, 
			EventTime sessionStartTime, int sequenceNumber, int touches, int totalTouches) 
	{
		super(sessionInfo, instanceId);
	
		final int updateTimeIntervalMilliseconds = Util.getAppRunningIntervalSeconds() * 1000;
		final int keysPressed = 0;
		final int totalKeysPressed = 0;
		final int collectionMode = Util.getCollectionMode();
		
		appendParameter(sequenceKey, sequenceNumber);
		appendParameter(touchesKey, touches);
		appendParameter(totalTouchesKey, totalTouches);
		appendParameter(sessionStartTimeKey, sessionStartTime);
		appendParameter(keysPressedKey, keysPressed);
		appendParameter(totalKeysPressedKey, totalKeysPressed);
		appendParameter(intervalMillisecondsKey, updateTimeIntervalMilliseconds);
		appendParameter(collectionModeKey, collectionMode);
	}
		
	@Override
	protected String getBaseUrl() {
		return "appPause";
	}
}

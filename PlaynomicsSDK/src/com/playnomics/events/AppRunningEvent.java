package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppRunningEvent extends ImplicitEvent {
	
	public AppRunningEvent(Util util, GameSessionInfo sessionInfo, LargeGeneratedId instanceId, 
			EventTime sessionStartTime, int sequenceNumber, int touches, int totalTouches) 
	{
		super(util, sessionInfo, instanceId);
	
		final int updateTimeIntervalMilliseconds = util.getAppRunningIntervalSeconds() * 1000;
		final int keysPressed = 0;
		final int totalKeysPressed = 0;
		final int collectionMode = util.getCollectionMode();
		
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
	public String getBaseUrl() {
		return "appRunning";
	}
}

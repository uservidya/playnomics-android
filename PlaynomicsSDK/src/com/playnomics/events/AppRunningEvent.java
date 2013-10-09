package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public class AppRunningEvent extends ImplicitEvent {
	
	public AppRunningEvent(Config config, GameSessionInfo sessionInfo, LargeGeneratedId instanceId, 
			EventTime sessionStartTime, int sequenceNumber, int touches, int totalTouches) 
	{
		super(config, sessionInfo, instanceId);
	
		final int updateTimeIntervalMilliseconds = config.getAppRunningIntervalMilliseconds();
		final int keysPressed = 0;
		final int totalKeysPressed = 0;
		final int collectionMode = config.getCollectionMode();
		
		appendParameter(config.getSequenceKey(), sequenceNumber);
		appendParameter(config.getTouchesKey(), touches);
		appendParameter(config.getTotalTouchesKey(), totalTouches);
		appendParameter(config.getSessionStartTimeKey(), sessionStartTime);
		appendParameter(config.getKeysPressedKey(), keysPressed);
		appendParameter(config.getTotalKeysPressedKey(), totalKeysPressed);
		appendParameter(config.getIntervalMillisecondsKey(), updateTimeIntervalMilliseconds);
		appendParameter(config.getCollectionModeKey(), collectionMode);
	}
		
	@Override
	public String getUrlPath() {
		return this.config.getEventPathAppRunning();
	}
}

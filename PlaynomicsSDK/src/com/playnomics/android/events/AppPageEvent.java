package com.playnomics.android.events;

import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.*;

public class AppPageEvent extends ImplicitEvent {

	public AppPageEvent(IConfig config, GameSessionInfo sessionInfo,
			LargeGeneratedId instanceId) {
		super(config, sessionInfo, instanceId);
		appendParameter(config.getTimeZoneOffsetKey(),
				EventTime.getMinutesTimezoneOffset());
	}

	@Override
	public String getUrlPath() {
		return config.getEventPathAppPage();
	}
}

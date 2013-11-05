package com.playnomics.android.events;

import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.IConfig;
import com.playnomics.android.util.IRandomGenerator;

public class CustomEvent extends ExplicitEvent {
	
	public CustomEvent(IConfig config, IRandomGenerator generator,
			GameSessionInfo sessionInfo, String customEventName) {
		super(config, sessionInfo);

		long milestoneId = generator.generatePositiveRandomLong();
		appendParameter(config.getMilestoneIdKey(), milestoneId);
		appendParameter(config.getMilestoneNameKey(), customEventName);
	}

	@Override
	public String getUrlPath() {
		return config.getEventPathMilestone();
	}
}

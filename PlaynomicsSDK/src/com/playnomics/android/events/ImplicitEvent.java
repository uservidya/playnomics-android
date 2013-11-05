package com.playnomics.android.events;

import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.IConfig;
import com.playnomics.android.util.LargeGeneratedId;

public abstract class ImplicitEvent extends PlaynomicsEvent {

	private final String implicitEventSessionKey = "s";
	private final String instanceKey = "i";

	public ImplicitEvent(IConfig config, GameSessionInfo sessionInfo,
			LargeGeneratedId instanceId) {
		super(config, sessionInfo);
		appendParameter(instanceKey, instanceId);
	}

	@Override
	protected String getSessionKey() {
		return implicitEventSessionKey;
	}
}

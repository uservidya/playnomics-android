package com.playnomics.android.events;

import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.IConfig;

public abstract class ExplicitEvent extends PlaynomicsEvent {
	private final String explicitEventSessionKey = "jsh";

	public ExplicitEvent(IConfig config, GameSessionInfo sessionInfo) {
		super(config, sessionInfo);
	}

	@Override
	protected String getSessionKey() {
		return explicitEventSessionKey;
	}
}

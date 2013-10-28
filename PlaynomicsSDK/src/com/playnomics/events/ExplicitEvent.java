package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.IConfig;

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

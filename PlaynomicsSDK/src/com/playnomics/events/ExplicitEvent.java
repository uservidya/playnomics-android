package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Config;

public abstract class ExplicitEvent extends PlaynomicsEvent {
	private final String explicitEventSessionKey = "jsh";
	
	public ExplicitEvent(Config config, GameSessionInfo sessionInfo){
		super(config, sessionInfo);
	}
	
	@Override
	protected String getSessionKey() {
		return this.explicitEventSessionKey;
	}
}

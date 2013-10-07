package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;

public abstract class ExplicitEvent extends PlaynomicsEvent {
	private final String explicitEventSessionKey = "jsh";
	
	public ExplicitEvent(GameSessionInfo sessionInfo){
		super(sessionInfo);
	}
	
	@Override
	protected String getSessionKey() {
		return this.explicitEventSessionKey;
	}
}

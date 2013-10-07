package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Util;

public abstract class ExplicitEvent extends PlaynomicsEvent {
	private final String explicitEventSessionKey = "jsh";
	
	public ExplicitEvent(Util util, GameSessionInfo sessionInfo){
		super(util, sessionInfo);
	}
	
	@Override
	protected String getSessionKey() {
		return this.explicitEventSessionKey;
	}
}

package com.playnomics.events;

import com.playnomics.util.IConfig;
import com.playnomics.util.LargeGeneratedId;
import com.playnomics.session.GameSessionInfo;

public abstract class ImplicitEvent extends PlaynomicsEvent {
	
	private final String implicitEventSessionKey = "s";
	private final String instanceKey = "i";

	public ImplicitEvent(IConfig config, GameSessionInfo sessionInfo, LargeGeneratedId instanceId)
	{
		super(config, sessionInfo);
		appendParameter(instanceKey, instanceId);
	}

	@Override
	protected String getSessionKey(){
		return implicitEventSessionKey;
	}
}

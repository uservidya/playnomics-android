package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public abstract class ImplicitEvent extends PlaynomicsEvent {
	
	private final String implicitEventSessionKey = "s";
	private final String instanceKey = "i";
	public ImplicitEvent(GameSessionInfo sessionInfo, LargeGeneratedId instanceId)
	{
		super(sessionInfo);
		appendParameter(instanceKey, instanceId);
	}

	@Override
	protected String getSessionKey(){
		return this.implicitEventSessionKey;
	}
}

package com.playnomics.events;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public abstract class ImplicitEvent extends PlaynomicsEvent {
	
	private final String implicitEventSessionKey = "s";
	private final String instanceKey = "i";
	public ImplicitEvent(Util util, GameSessionInfo sessionInfo, LargeGeneratedId instanceId)
	{
		super(util, sessionInfo);
		appendParameter(instanceKey, instanceId);
	}

	@Override
	protected String getSessionKey(){
		return this.implicitEventSessionKey;
	}
}

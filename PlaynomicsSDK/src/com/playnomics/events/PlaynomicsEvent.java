package com.playnomics.events;

import java.util.Map;
import java.util.TreeMap;

import com.playnomics.util.*;
import com.playnomics.session.GameSessionInfo;

public abstract class PlaynomicsEvent {
	private TreeMap<String, Object> eventParameters;
	private EventTime eventTime;

	protected IConfig config;

	protected PlaynomicsEvent(IConfig config, GameSessionInfo sessionInfo) {
		eventTime = new EventTime();
		eventParameters = new TreeMap<String, Object>();
		this.config = config;
		eventParameters.put(config.getApplicationIdKey(),
				sessionInfo.getApplicationId());
		eventParameters.put(config.getUserIdKey(), sessionInfo.getUserId());
		eventParameters.put(config.getBreadcrumbIdKey(),
				sessionInfo.getBreadcrumbId());
		eventParameters.put(getSessionKey(), sessionInfo.getSessionId());
		eventParameters.put(config.getSdkVersionKey(), config.getSdkVersion());
		eventParameters.put(config.getSdkNameKey(), config.getSdkName());
		eventParameters.put(config.getEventTimeKey(), eventTime);
	}

	public EventTime getEventTime() {
		return eventTime;
	}

	public abstract String getUrlPath();

	protected abstract String getSessionKey();

	protected void appendParameter(String key, Object value) {
		eventParameters.put(key, value);
	}

	public Map<String, Object> getEventParameters() {
		return eventParameters;
	}
}

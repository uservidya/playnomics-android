package com.playnomics.messaging;

import java.util.Map;

public class Target {
	public enum TargetType {
		URL, EXTERNAL, DATA
	}

	private TargetType targetType;
	private String targetUrl;
	private Map<String, Object> targetData;

	public String getTargetUrl() {
		return targetUrl;
	}

	public void setTargetUrl(String targetUrl) {
		this.targetUrl = targetUrl;
	}

	public TargetType getTargetType() {
		return targetType;
	}

	public void setTargetType(TargetType targetType) {
		this.targetType = targetType;
	}

	public Map<String, Object> getTargetData() {
		return targetData;
	}

	public void setTargetData(Map<String, Object> targetData) {
		this.targetData = targetData;
	}

	public Target(TargetType targetType) {
		this.targetType = targetType;
	}

	public Target(TargetType targetType, Map<String, Object> targetData) {
		this.targetType = targetType;
		this.targetData = targetData;
	}

	public Target(TargetType targetType, String targetUrl) {
		this.targetType = targetType;
		this.targetUrl = targetUrl;
	}
}

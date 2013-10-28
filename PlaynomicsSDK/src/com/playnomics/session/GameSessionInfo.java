package com.playnomics.session;

import com.playnomics.util.*;

/**
 * @author jaredjenkins Encapsulates general information about the session for
 *         PlaynomicsEvents.
 */
public class GameSessionInfo {
	private Long applicationId;
	private String userId;
	private String breadcrumbId;
	private LargeGeneratedId sessionId;

	public GameSessionInfo(Long applicationId, String userId,
			String breadcrumbId, LargeGeneratedId sessionId) {
		this.applicationId = applicationId;
		this.userId = userId;
		this.breadcrumbId = breadcrumbId;
		this.sessionId = sessionId;
	}

	public Long getApplicationId() {
		return applicationId;
	}

	public String getUserId() {
		return userId;
	}

	public String getBreadcrumbId() {
		return breadcrumbId;
	}

	public LargeGeneratedId getSessionId() {
		return sessionId;
	}
}

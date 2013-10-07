package com.playnomics.session;

import com.playnomics.util.*;

public class GameSessionInfo {
	private Long applicationId;
	private String userId;
	private String breadcrumbId;
	private LargeGeneratedId sessionId;
	
	public GameSessionInfo(Long applicationId, String userId, String breadcrumbId, LargeGeneratedId sessionId){
		this.applicationId = applicationId;
		this.userId = userId;
		this.breadcrumbId = breadcrumbId;
		this.sessionId = sessionId;
	}
	
	public Long getApplicationId(){
		return this.applicationId;
	}
	
	public String getUserId(){
		return this.userId;
	}
	
	public String getBreadcrumbId(){
		return this.breadcrumbId;
	}
	
	public LargeGeneratedId getSessionId(){
		return this.sessionId;
	}
}

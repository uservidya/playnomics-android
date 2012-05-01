package com.playnomics.analytics;

class SocialEvent extends PlaynomicsEvent {
	
	public enum ResponseType {
		accepted
	};
	
	private String invitationId;
	private String recipientUserId;
	private String recipientAddress;
	private String method;
	private ResponseType response;
	
	public String getInvitationId() {
	
		return invitationId;
	}
	
	public void setInvitationId(String invitationId) {
	
		this.invitationId = invitationId;
	}
	
	public String getRecipientUserId() {
	
		return recipientUserId;
	}
	
	public void setRecipientUserId(String recipientUserId) {
	
		this.recipientUserId = recipientUserId;
	}
	
	public String getRecipientAddress() {
	
		return recipientAddress;
	}
	
	public void setRecipientAddress(String recipientAddress) {
	
		this.recipientAddress = recipientAddress;
	}
	
	public String getMethod() {
	
		return method;
	}
	
	public void setMethod(String method) {
	
		this.method = method;
	}
	
	public ResponseType getResponse() {
	
		return response;
	}
	
	public void setResponse(ResponseType response) {
	
		this.response = response;
	}
	
	public SocialEvent(EventType eventType, String applicationId, String userId, String invitationId,
		String recipientUserId, String recipientAddress, String method, ResponseType response) {
	
		super(eventType, applicationId, userId);
		this.invitationId = invitationId;
		this.recipientUserId = recipientUserId;
		this.recipientAddress = recipientAddress;
		this.method = method;
		this.response = response;
	}
}

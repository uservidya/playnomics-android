package com.playnomics.playrm;

import com.playnomics.playrm.PlaynomicsConstants.ResponseType;

class SocialEvent extends PlaynomicsEvent {

	private static final long serialVersionUID = 1L;

	private Long invitationId;
	private String recipientUserId;
	private String recipientAddress;
	private String method;
	private ResponseType response;

	public SocialEvent(EventType eventType, String internalSessionId,
			Long applicationId, String userId, Long invitationId,
			String recipientUserId, String recipientAddress, String method,
			ResponseType response) {

		super(eventType, internalSessionId, applicationId, userId);
		this.invitationId = invitationId;
		this.recipientUserId = recipientUserId;
		this.recipientAddress = recipientAddress;
		this.method = method;
		this.response = response;
	}

	public Long getInvitationId() {

		return invitationId;
	}

	public void setInvitationId(Long invitationId) {

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

	@Override
	public String toQueryString() {

		// Set common params
		String queryString = getEventType() + "?t=" + getEventTime().getTime()
				+ "&a=" + getApplicationId() + "&u=" + getUserId() + "&ii="
				+ getInvitationId() + "&jsh=" + getSessionId();

		if (getEventType() == EventType.invitationResponse) {
			queryString += "&ie=" + getResponse() + "&ir="
					+ getRecipientUserId();
		} else {
			// Optional params
			queryString = addOptionalParam(queryString, "ir",
					getRecipientUserId());
			queryString = addOptionalParam(queryString, "ia",
					getRecipientAddress());
			queryString = addOptionalParam(queryString, "im", getMethod());
		}

		return queryString;
	}
}

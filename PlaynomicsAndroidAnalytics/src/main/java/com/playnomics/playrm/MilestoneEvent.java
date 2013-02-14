package com.playnomics.playrm;

public class MilestoneEvent extends PlaynomicsEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long milestoneId;
	public String milestoneName;

	public MilestoneEvent(EventType eventType, String internalSessionId,
			long applicationId, String userId, String cookieId, long mileStone,
			String mileStoneName) {

		super(eventType, internalSessionId, applicationId, userId);

		this.milestoneId = mileStone;
		this.milestoneName = mileStoneName;
	}

	private long getMilestoneId() {
		return this.milestoneId;
	}

	private String getMilestoneName() {
		return this.milestoneName;
	}

	@Override
	public String toQueryString() {
		String queryString = getEventType() + "?t=" + getEventTime().getTime()
				+ "&a=" + getApplicationId() + "&u=" + getUserId() + "&jsh="
				+ getSessionId() + "&mi=" + getMilestoneId() + "&mn="
				+ getMilestoneName() + "&jsh=" + getInternalSessionId();

		return queryString;
	}
}

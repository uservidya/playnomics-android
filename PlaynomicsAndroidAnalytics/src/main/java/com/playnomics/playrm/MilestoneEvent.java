package com.playnomics.playrm;

class MilestoneEvent extends PlaynomicsEvent {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public Long milestoneId;
	public String milestoneName;

	public MilestoneEvent(EventType eventType, String internalSessionId,
			long applicationId, String userId, String deviceId, long mileStone,
			String mileStoneName) {

		super(eventType, internalSessionId, applicationId, userId, deviceId);

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
	protected String toQueryString() {
		String queryString = getEventType() + "?t=" + getEventTime().getTime()
				+ "&a=" + getApplicationId() + "&u=" + getUserId() + "&b="+ getDeviceId()
				+ "&jsh=" + getSessionId() + "&mi=" + getMilestoneId() + "&mn="
				+ getMilestoneName();

		return queryString;
	}
}

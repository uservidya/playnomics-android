package com.playnomics.playrm;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.playnomics.playrm.PlaynomicsConstants.UserInfoSex;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoType;

class UserInfoEvent extends PlaynomicsEvent {

	private static final long serialVersionUID = 1L;

	private UserInfoType type;
	private String country;
	private String subdivision;
	private UserInfoSex sex;
	private Date birthday;
	private String source;
	private String sourceCampaign;
	private Date installTime;

	public UserInfoEvent(String internalSessionId, Long applicationId,
			String userId, UserInfoType type) {

		super(EventType.userInfo, internalSessionId, applicationId, userId);
		this.type = type;
	}

	public UserInfoEvent(String sessionId, Long applicationId, String userId,
			UserInfoType type, String country, String subdivision,
			UserInfoSex sex, Date birthday, String source,
			String sourceCampaign, Date installTime) {

		this(sessionId, applicationId, userId, type);
		this.country = country;
		this.subdivision = subdivision;
		this.sex = sex;
		this.birthday = birthday;
		this.source = source;
		this.sourceCampaign = sourceCampaign;
		this.installTime = installTime;
	}

	public UserInfoType getType() {
		return type;
	}

	public void setType(UserInfoType type) {
		this.type = type;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getSubdivision() {
		return subdivision;
	}

	public void setSubdivision(String subdivision) {
		this.subdivision = subdivision;
	}

	public UserInfoSex getSex() {
		return sex;
	}

	public void setSex(UserInfoSex sex) {
		this.sex = sex;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public String getSource() {

		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSourceCampaign() {
		return sourceCampaign;
	}

	public void setSourceCampaign(String sourceCampaign) {
		this.sourceCampaign = sourceCampaign;
	}

	public Date getInstallTime() {
		return installTime;
	}

	public void setInstallTime(Date installTime) {

		this.installTime = installTime;
	}

	@Override
	protected String toQueryString() {

		// Set common params
		String queryString = getEventType() + "?t=" + getEventTime().getTime()
				+ "&a=" + getApplicationId() + "&u=" + getUserId() + "&pt="
				+ getType() + "&jsh=" + getSessionId();

		// Optional params
		queryString = addOptionalParam(queryString, "pc", getCountry());
		queryString = addOptionalParam(queryString, "ps", getSubdivision());
		queryString = addOptionalParam(queryString, "px", getSex());
		SimpleDateFormat format = new SimpleDateFormat("MM-DD-yyyy");
		queryString = addOptionalParam(queryString, "pb",
				format.format(getBirthday()));
		queryString = addOptionalParam(queryString, "po", getSource());
		queryString = addOptionalParam(queryString, "pm", getSourceCampaign());
		queryString = addOptionalParam(queryString, "pi", getInstallTime()
				.getTime());

		return queryString;
	}
}

package com.playnomics.api;

import java.util.Date;

public class UserInfoEvent extends PlaynomicsEvent {
	
	// Enums for UserInfo Events
	public static enum UserInfoType {
		update
	};
	
	public static enum UserInfoSex {
		M, F, U
	};
	
	public static enum UserInfoSource {
		Adwords, DoubleClick, YahooAds, MSNAds, AOLAds, Adbrite, FacebookAds,
		GoogleSearch, YahooSearch, BingSearch, FacebookSearch,
		Applifier, AppStrip, VIPGamesNetwork, UserReferral, InterGame, Other
	}
	
	private UserInfoType type;
	private String country;
	private String subdivision;
	private UserInfoSex sex;
	private Date birthday;
	private UserInfoSource source;
	private String sourceCampaign;
	private Date installTime;
	
	public UserInfoEvent(String applicationId, String userId, UserInfoType type) {
		
		super(EventType.userInfo, applicationId, userId);
		this.type = type;
	}
	
	public UserInfoEvent(String applicationId, String userId, UserInfoType type, String country,
		String subdivision, UserInfoSex sex, Date birthday, UserInfoSource source, String sourceCampaign,
		Date installTime) {
	
		this(applicationId, userId, type);
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
	
	public UserInfoSource getSource() {
	
		return source;
	}
	
	public void setSource(UserInfoSource source) {
	
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
}

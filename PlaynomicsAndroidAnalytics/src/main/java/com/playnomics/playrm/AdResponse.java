package com.playnomics.playrm;

import java.util.ArrayList;
import java.util.List;

class AdResponse {
	private List<Ad> ads;
	private CloseButton closeButton;
	private Background background;
	private Location location;
	private int expirationSeconds;
	private String status;
	private String message;

	public AdResponse(CloseButton close, Background background,
			Location location, int expirationSeconds, String status,
			String message) {
		this.location = location;
		this.background = background;
		this.ads = new ArrayList<Ad>();
		this.closeButton = close;
		this.expirationSeconds = expirationSeconds;
		this.status = status;
		this.message = message;
	}
	
	public AdResponse(List<Ad> ads, CloseButton close, Background background,
			Location location, int expirationSeconds, String status,
			String message) {
		this(close, background, location, expirationSeconds, status, message);
		this.ads = ads;
	}
	
	public CloseButton getCloseButton() {
		return this.closeButton;
	}

	public boolean hasCloseButtonImage(){
		return this.closeButton != null &&
				this.closeButton.getImageUrl() != null;
	}
	
	public Background getBackground() {
		return this.background;
	}

	public boolean hasBackgroundImage(){
		return this.background != null &&
			this.background.getImageUrl() != null;
	}
	
	public Location getLocation() {
		return this.location;
	}

	public void insertAd(Ad ad) {
		ads.add(ad);
	}
	
	public Iterable<Ad> getAds(){
		return ads;
	}
	
	public Ad getFirstAd(){
		return ads.size() == 0
				? null : ads.get(0);
	}

	public int getExpirationSeconds() {
		return this.expirationSeconds;
	}

	public String getStatus() {
		return this.status;
	}

	public String getMessage() {
		return this.message;
	}
}

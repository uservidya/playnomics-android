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
	
	public CloseButton getCloseButton() {
		return this.closeButton;
	}

	public Background getBackground() {
		return this.background;
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

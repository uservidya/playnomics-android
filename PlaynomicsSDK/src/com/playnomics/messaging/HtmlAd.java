package com.playnomics.messaging;

public class HtmlAd {
	private Position position;

	public Position getPosition() {
		return position;
	}

	private CloseButton closeButton;

	public CloseButton getCloseButton() {
		return closeButton;
	}

	private Target target;

	public Target getTarget() {
		return target;
	}

	// url for reporting click interaction
	private String clickLink;

	public String getClickLink() {
		return clickLink;
	}

	// url for reporting click interaction
	private String clickUrl;

	public String getClickUrl() {
		return clickUrl;
	}

	// url for reporting impression of message
	private String impressionUrl;

	public String getImpressionUrl() {
		return impressionUrl;
	}

	// url for close interaction
	private String closeUrl;

	public String getCloseUrl() {
		return closeUrl;
	}

	private String contentBaseUrl;

	public String getContentBaseUrl() {
		return contentBaseUrl;
	}

	private String htmlContent;

	public String getHtmlContent() {
		return htmlContent;
	}

	public HtmlAd(Position position, CloseButton closeButton, Target target,
			String clickLink, String clickUrl, String impressionUrl,
			String closeUrl, String contentBaseUrl, String htmlContent) {
		this.position = position;
		this.target = target;
		this.clickLink = clickLink;
		this.clickUrl = clickUrl;
		this.impressionUrl = impressionUrl;
		this.closeUrl = closeUrl;
		this.contentBaseUrl = contentBaseUrl;
		this.htmlContent = htmlContent;
		this.closeButton = closeButton;
	}
}

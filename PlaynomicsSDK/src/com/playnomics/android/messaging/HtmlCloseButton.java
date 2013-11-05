package com.playnomics.android.messaging;

public class HtmlCloseButton extends CloseButton {
	private String closeLink;

	public String getCloseLink() {
		return closeLink;
	}

	public void setCloseLink(String closeLink) {
		this.closeLink = closeLink;
	}

	public HtmlCloseButton(String closeLink) {
		this.closeLink = closeLink;
	}
}
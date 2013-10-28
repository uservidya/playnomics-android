package com.playnomics.messaging;

public class NativeCloseButton extends CloseButton {
	private Integer height;
	private Integer width;
	private String closeImageUrl;
	private byte[] imageData;

	public Integer getHeight() {
		return height;
	}

	public void setHeight(Integer height) {
		this.height = height;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

	public String getImageUrl() {
		return closeImageUrl;
	}

	public void setCloseImageUrl(String closeImageUrl) {
		this.closeImageUrl = closeImageUrl;
	}

	public byte[] getImageData() {
		return imageData;
	}

	public void setImageData(byte[] imageData) {
		this.imageData = imageData;
	}

	public NativeCloseButton(Integer height, Integer width, String closeImageUrl) {
		this.height = height;
		this.width = width;
		this.closeImageUrl = closeImageUrl;
	}
}
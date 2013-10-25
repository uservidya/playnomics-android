package com.playnomics.messaging;

public class NativeCloseButton extends CloseButton {
	private Float height;
	private Float width;
	private String closeImageUrl;
	private byte[] imageData;
	
	public Float getHeight(){
		return height;
	}
	
	public void setHeight(Float height){
		this.height = height;
	}

	public Float getWidth(){
		return width;
	}
	
	public void setWidth(Float width){
		this.width = width;
	}
	
	public String getImageUrl(){
		return closeImageUrl;
	}
	
	public void setCloseImageUrl(String closeImageUrl){
		this.closeImageUrl = closeImageUrl;
	}
	
	public byte[] getImageData(){
		return imageData;
	}
	
	public void setImageData(byte[] imageData){
		this.imageData = imageData;
	}
}
package com.playnomics.playrm;

class CloseButton extends Location {
	private String imageUrl;

	public String getImage() {
		return imageUrl;
	}
	
	public boolean hasImage(){
		return this.imageUrl != null;
	}

	public CloseButton(int x, int y, int width, int height, String imageUrl) {
		super(x, y, width, height);
		this.imageUrl = imageUrl;
	}
}

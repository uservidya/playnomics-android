package com.playnomics.playrm;

class Background {

	public enum Orientation{
		DETECT, LANDSCAPE, PORTRAIT
	}
	
	public Background(String image, Orientation orientation, int height,
			int width, int landscapeX, int landscapeY, int portraitX,
			int portraitY) {
		
		this.height = height;
		this.width = width;
		this.imageUrl = image;
		this.orientation = orientation;
		this.landscape = new Coordinates(landscapeX, landscapeY);
		this.portrait = new Coordinates(portraitX, portraitY);
	}

	private Coordinates landscape;
	private Coordinates portrait;
	private Orientation orientation;
	private String imageUrl;
	private int height;
	private int width;

	public Coordinates getLandscape() {
		return landscape;
	}

	public Coordinates getPortrait() {
		return portrait;
	}

	public int getHeight() {
		return height;
	}

	public int getWidth() {
		return width;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public Orientation getOrientation() {
		return orientation;
	}
}
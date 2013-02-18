package com.playnomics.playrm;

class Location extends Coordinates {
	private int width;
	private int height;

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Location(int x, int y, int width, int height) {
		super(x, y);
		this.width = width;
		this.height = height;
	}
}

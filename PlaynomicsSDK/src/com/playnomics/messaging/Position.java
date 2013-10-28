package com.playnomics.messaging;

public class Position {
	public enum PositionType {
		FULLSCREEN,
	}

	private PositionType positionType;

	public PositionType getPositionType() {
		return positionType;
	}

	public void setPositionType(PositionType positionType) {
		this.positionType = positionType;
	}

	public Position(PositionType positionType) {
		this.positionType = positionType;
	}
}
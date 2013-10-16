package com.playnomics.messaging;

public class Position {
	public enum PositionType{
		FULLSCREEN,
		BANNER
	}
	
	public enum HorizontalPosition{
		LEFT,
		CENTER,
		RIGHT
	}
	
	public enum VerticalPosition{
		TOP,
		CENTER,
		BOTTOM
	}
	
	private PositionType positionType;
	private HorizontalPosition horizontal;
	private VerticalPosition vertical;
	private Float height;
	private Float width;

	public PositionType getPositionType(){
		return positionType;
	}
	
	public void setPositionType(PositionType positionType){
		this.positionType = positionType;
	}
	
	public HorizontalPosition getHorizontalPosition(){
		return horizontal; 
	}
	
	public void setHorizontalPosition(HorizontalPosition horizontal){
		this.horizontal = horizontal;
	}
	
	public VerticalPosition getVerticalPosition(){
		return vertical; 
	}
	
	public void setVerticalPosition(VerticalPosition vertical){
		this.vertical = vertical;
	}
	
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
}
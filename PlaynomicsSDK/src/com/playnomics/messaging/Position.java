package com.playnomics.messaging;

public class Position {
	public enum PositionType{
		FULLSCREEN,
		BANNER
	}
	
	public enum HorizontalGravity{
		LEFT,
		CENTER,
		RIGHT
	}
	
	public enum VerticalGravity{
		TOP,
		CENTER,
		BOTTOM
	}
	
	private PositionType positionType;
	private HorizontalGravity horizontal;
	private VerticalGravity vertical;
	private Float height;
	private Float width;

	public PositionType getPositionType(){
		return positionType;
	}
	
	public void setPositionType(PositionType positionType){
		this.positionType = positionType;
	}
	
	public HorizontalGravity getHorizontalGravity(){
		return horizontal; 
	}
	
	public void setHorizontalGravity(HorizontalGravity horizontal){
		this.horizontal = horizontal;
	}
	
	public VerticalGravity getVerticalGravity(){
		return vertical; 
	}
	
	public void setVerticalGravity(VerticalGravity vertical){
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
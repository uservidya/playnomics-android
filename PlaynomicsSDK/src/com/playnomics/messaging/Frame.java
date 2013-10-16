package com.playnomics.messaging;

public class Frame {
	
	public enum FrameState{
		NOT_LOADED,
		LOAD_STARTED,
		LOAD_COMPLETE,
		LOAD_FAILED
	}
	
	private boolean shouldRender;
	
	private String frameId;
	public String getFrameId(){
		return frameId;
	}
	
	private FrameState state;
	public FrameState getState(){
		return state;
	}
	
	public void setState(FrameState state){
		this.state = state;
	}
	
	private HtmlAd htmlAd;

	public Frame(String frameId){
		this.frameId = frameId;
		this.state = FrameState.NOT_LOADED;
	}

	public void updateFrameData(HtmlAd ad){
		
	}
	
	public void show(){
		
	}
	
	public void hide(){
		
	}
}

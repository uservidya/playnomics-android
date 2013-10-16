package com.playnomics.messaging;

public class HtmlAd {		
	private Position position;
	public Position getPosition(){
		return position;
	}
	
	public void setPosition(Position position){
		this.position = position;
	}
	
	private CloseButton closeButton;
	public CloseButton getCloseButton(){
		return closeButton;
	}
	
	public void setCloseButton(CloseButton closeButton){
		this.closeButton = closeButton;
	}
	
	private Target target;
	public Target getTarget(){
		return target;
	}
	
	public void setTarget(Target target){
		this.target = target;
	}
	
	//url for reporting click interaction
	private String clickLink;
	public String getClickLink(){
		return clickLink;
	}
	
	public void setClickLink(String clickLink){
		this.clickLink = clickLink;
	}
	

	//url for reporting click interaction
	private String clickUrl;
	public String getClickUrl(){
		return clickUrl;
	}
	
	public void setClickUrl(String clickUrl){
		this.clickUrl = clickUrl;
	}

	//url for reporting impression of message
	private String impressionUrl;
	public String getImpressionUrl(){
		return impressionUrl;
	}
	
	public void setImpressionUrl(String impressionUrl){
		this.impressionUrl = impressionUrl;
	}
	
	//url for close interaction
	private String closeUrl;
	public String getCloseUrl(){
		return closeUrl;
	}
	
	public void setCloseUrl(String closeUrl){
		this.closeUrl = closeUrl;
	}
	
	private String htmlContent;
	public String getHtmlContent(){
		return htmlContent;
	}
	
	public void setHtmlContent(String htmlContent){
		this.htmlContent = htmlContent;
	}
}



package com.playnomics.playrm;

class ImpressionEvent extends PlaynomicsEvent {
	
	private static final long serialVersionUID = 3947370695587377533L;

	public ImpressionEvent(String impressionUrl){
		super(impressionUrl);
	}
	
	@Override 
	public boolean appendSourceInformation(){
		return false;
	}
	
	@Override
	protected String toQueryString() {
		return "";
	}
}

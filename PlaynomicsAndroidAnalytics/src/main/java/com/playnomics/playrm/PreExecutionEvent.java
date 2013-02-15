package com.playnomics.playrm;

class PreExecutionEvent extends PlaynomicsEvent {
	
	private int x;
	private int y;
	
	public PreExecutionEvent(String preExecutionUrl, int x, int y){
		super(preExecutionUrl);
		this.x = x;
		this.y = y;
	}
	
	@Override
	public boolean appendSourceInformation() {
		return false;
	}
	
	@Override
	protected String toQueryString() {
		String queryString = "x=" + this.x + "&y=" + y;
		if(!this.baseUrl.contains("?")){
			queryString = "?" + queryString;
		} else {
			queryString = "&" + queryString;
		}
		return queryString;
	}
}

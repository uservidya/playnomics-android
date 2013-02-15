package com.playnomics.playrm;

class PostExecutionEvent extends PlaynomicsEvent {
	
	private static final long serialVersionUID = 8636227653849645489L;
	private int statusCode;
	private Exception ex;
	
	public static class Status{
		public static final int PNX_SUCCESS = 1;
		public static final int PNX_EXCEPTION = -4;
		public static final int PNX_ACTIONS_NOT_ENABLED = -3;
		
		public static final int PNA_SUCCESS = 2;
		public static final int PNA_EXCEPTION = -6;
		public static final int UNKNOWN_ERROR = -1;
	}
	
	public PostExecutionEvent(String postExecutionUrl, int statusCode, 
			Exception ex){	
		super(postExecutionUrl);
		this.statusCode = statusCode;
		this.ex = ex;
	}
	
	@Override
	public boolean appendSourceInformation() {
		return false;
	}
	
	@Override
	protected String toQueryString() {
		String queryString = "c=" + this.statusCode; 
		if(this.ex != null){
			queryString += "&e=" + this.ex.toString() + "+" + this.ex.getMessage();
		}
		
		if(!this.baseUrl.contains("?")){
			queryString = "?" + queryString;
		} else {
			queryString = "&" + queryString;
		}
		return queryString;
	}
	
}

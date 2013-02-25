package com.playnomics.playrm;

class CloseFrameEvent extends PlaynomicsEvent {
	
	private static final long serialVersionUID = -2878694731829386058L;

	public CloseFrameEvent(String closeEventUrl)
	{
		super(closeEventUrl);
	}
	
	@Override
	public boolean appendSourceInformation() {
		return false;
	}
	
	@Override
	protected String toQueryString() {
		return "";
	}

}

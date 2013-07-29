package com.playnomics.playrm;
import org.json.JSONException;
import org.json.JSONObject;

class Ad {
	private final String PNX_PREFIX = "pnx://";
	private final String PNA_PREFIX = "pna://";
	private final String HTTP_PREFIX = "http://";
	private final String HTTPS_PREFIX = "https://";

	public enum AdTargetUrlType {
		WEB, PNX, PNA, UNKNOWN, NULL
	}
	
	public enum AdTargetType{
		URL, DATA
	}

	private String imageUrl;
	
	private String targetUrl;
	private JSONObject targetData;
	
	private AdTargetUrlType targetUrlType;
	private AdTargetType targetType;
	
	private String preExecutionUrl;
	private String postExecutionUrl;
	private String impressionUrl;
	private String closeUrl;
	
	public Ad(String imageUrl, AdTargetType targetType, String target, String impression,
			String preExecute, String postExecute, String closeUrl) throws JSONException {
		this.imageUrl = imageUrl;
		
		this.targetType = targetType;
		
		if(this.targetType == AdTargetType.URL){
			this.targetUrl = target;
			
			if(this.targetUrl == null){
				this.targetUrlType = AdTargetUrlType.NULL;
			} else if (targetUrl.startsWith(HTTP_PREFIX)
					|| targetUrl.startsWith(HTTPS_PREFIX)) {
				this.targetUrlType = AdTargetUrlType.WEB;
			} else if (targetUrl.startsWith(PNA_PREFIX)) {
				this.targetUrlType = AdTargetUrlType.PNA;
			} else if (targetUrl.startsWith(PNX_PREFIX)) {
				this.targetUrlType = AdTargetUrlType.PNX;
			} else {
				this.targetUrlType = AdTargetUrlType.UNKNOWN;
			}
		} else {
			if(target != null){
				targetData = new JSONObject(target);
			}
		}
		
		this.impressionUrl = impression;
		this.preExecutionUrl = preExecute;
		this.postExecutionUrl = postExecute;
		this.closeUrl = closeUrl;
	}

	public AdTargetType getTargetType(){
		return targetType;
	}
	
	public AdTargetUrlType getTargetUrlType() {
		return targetUrlType;
	}

	public String getTargetUrl() {
		return this.targetUrl;
	}

	public String getTargetUrlForClick() {
		if (targetUrlType == AdTargetUrlType.WEB) {
			return this.targetUrl;
		}

		if (targetUrlType == AdTargetUrlType.PNA) {
			return this.targetUrl.replace(PNA_PREFIX, "");
		}

		if (targetUrlType == AdTargetUrlType.PNX) {
			return this.targetUrl.replace(PNX_PREFIX, "");
		}
		return null;
	}
	
	public JSONObject getTargetData(){
		return targetData;
	}

	public String getImageUrl() {
		return this.imageUrl;
	}

	public boolean hasPostExecutionUrl() {
		return this.postExecutionUrl != null;
	}

	public String getPostExecutionUrl() {
		return this.postExecutionUrl;
	}

	public boolean hasPreExecutionUrl() {
		return this.preExecutionUrl != null;
	}

	public String getPreExecutionUrl() {
		return this.preExecutionUrl;
	}

	public String getImpressionUrl() {
		return this.impressionUrl;
	}

	public String getCloseUrl() {
		return this.closeUrl;
	}

	public boolean hasCloseUrl() {
		return this.closeUrl != null;
	}
}
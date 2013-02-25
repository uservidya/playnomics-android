package com.playnomics.playrm;

class Ad {
	private final String PNX_PREFIX = "pnx://";
	private final String PNA_PREFIX = "pna://";
	private final String HTTP_PREFIX = "http://";
	private final String HTTPS_PREFIX = "https://";

	public enum AdTargetType {
		WEB, PNX, PNA, UNKNOWN
	}

	private String imageUrl;
	private String targetUrl;
	private AdTargetType targetType;
	private String preExecutionUrl;
	private String postExecutionUrl;
	private String impressionUrl;
	private String closeUrl;
	
	public Ad(String imageUrl, String target, String impression,
			String preExecute, String postExecute, String closeUrl) {
		this.imageUrl = imageUrl;
		this.targetUrl = target;
		this.impressionUrl = impression;
		this.preExecutionUrl = preExecute;
		this.postExecutionUrl = postExecute;
		this.closeUrl = closeUrl;
		
		if (targetUrl.startsWith(HTTP_PREFIX)
				|| targetUrl.startsWith(HTTPS_PREFIX)) {
			this.targetType = AdTargetType.WEB;
		} else if (targetUrl.startsWith(PNA_PREFIX)) {
			this.targetType = AdTargetType.PNA;
		} else if (targetUrl.startsWith(PNX_PREFIX)) {
			this.targetType = AdTargetType.PNX;
		} else {
			this.targetType = AdTargetType.UNKNOWN;
		}
	}

	public AdTargetType getTargetType() {
		return targetType;
	}

	public String getTargetUrl() {
		return this.targetUrl;
	}

	public String getTargetUrlForClick() {
		if (targetType == AdTargetType.WEB) {
			return this.targetUrl;
		}

		if (targetType == AdTargetType.PNA) {
			return this.targetUrl.replace(PNA_PREFIX, "");
		}

		if (targetType == AdTargetType.PNX) {
			return this.targetUrl.replace(PNX_PREFIX, "");
		}
		return null;
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
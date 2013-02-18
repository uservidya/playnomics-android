package com.playnomics.playrm;

class Ad {
	private final String pnxPrefix = "pnx://";
	private final String pnaPrefix = "pna://";
	private final String httpPrefix = "http://";
	private final String httpsPrefix = "https://";

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

		if (targetUrl.startsWith(httpPrefix)
				|| targetUrl.startsWith(httpsPrefix)) {
			this.targetType = AdTargetType.WEB;
		} else if (targetUrl.startsWith(pnaPrefix)) {
			this.targetType = AdTargetType.PNA;
		} else if (targetUrl.startsWith(pnxPrefix)) {
			this.targetType = AdTargetType.PNX;
		} else {
			this.targetType = AdTargetType.UNKNOWN;
		}
		this.closeUrl = closeUrl;
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
			return this.targetUrl.replace(pnaPrefix, "");
		}

		if (targetType == AdTargetType.PNX) {
			return this.targetUrl.replace(pnxPrefix, "");
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
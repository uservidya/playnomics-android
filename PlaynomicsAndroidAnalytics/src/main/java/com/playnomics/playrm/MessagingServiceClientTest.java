package com.playnomics.playrm;

import java.util.ResourceBundle;

import com.playnomics.playrm.Background.Orientation;

class MessagingServiceClientTest extends MessagingServiceClient {

	private static int requestCount = 0;

	public MessagingServiceClientTest(ResourceBundle resource, String baseUrl,
			long appId, String userId, String cookieId) {
		super(resource, baseUrl, appId, userId, cookieId);
	}

	private class testCases {
		public static final String NoCloseButton = "NoCloseButton";
		public static final String CloseButton = "CloseButton";
		public static final String NoBackground = "NoBackground";
		public static final String BadData = "BadData";
		public static final String BadBackgroundImage = "BadBackgroundImage";
		public static final String NoReception = "NoReception";
		public static final String BadAdImage = "BadAdImage";
		public static final String BadCloseButtonImage = "BadCloseButtonImage";
		public static final String GoodPNA = "GoodPNA";
		public static final String BadPNA = "BadPNA";
		public static final String GoodPNX = "GoodPNX";
		public static final String BadPNX = "BadPNX";
	}

	@Override
	public AdResponse requestAd(String frameId, String caller, int width,
			int height) {

		final String impressionUrl = "https://ads.b.playnomics.net/v1/impression?a=3&b=4491464577382170140&d=20121019&i=7861628744956845208&r=201210190001&s=GusysoT0lOfJXp-Bp-d6QcSegzVa-OYa3dFtpmyzvxE%3D&u=testUserId";
		final String closeUrl = "https://ads.b.playnomics.net/v1/closeImpression?a=3&b=4491464577382170140&d=20121019&i=7861628744956845208&r=201210190001&s=ixDNt0akWB1M0ctorrKh1HsCfvje4UURN9ct8KThM-s%3D&u=testUserId";
		final String targetWebUrl = "http://www.facebook.com";
		final String goodPNA = "pna://myGoodMethod";
		final String goodPNX = "pnx://myGoodMethod";
		final String badPNA = "pna://myBadMethod";
		final String badPNX = "pnx://myBadMethod";

		final String adImage = getRotatedAdImage(true);
		final String preExecuteUrl = "http://www.google.com";
		final String postExecureUrl = "http://www.nytimes.com"; 
		
		Background background = getBackground(Orientation.DETECT, true);	
		CloseButton button = getCloseButton(true);
		// should never shown
		Ad secondAd = new Ad("", "http://www.google.com", impressionUrl, null,
				null, null);

		Location location = new Location(10, 10, 300, 250);


		Ad adToShow;
		adToShow = new Ad(adImage, targetWebUrl, impressionUrl, null, null,
				closeUrl);
		
		if (frameId == testCases.NoCloseButton) {
			button = getCloseButtonNoImage();
		} else if (frameId == testCases.NoBackground) {
			background = getBackgroundNoImage(Orientation.DETECT);
		} else if (frameId == testCases.BadAdImage) {
			adToShow = new Ad(getRotatedAdImage(false), targetWebUrl,
					impressionUrl, null, null, closeUrl);
		} else if (frameId == testCases.BadBackgroundImage) {
			background = getBackground(Orientation.DETECT, false);
		} else if (frameId == testCases.BadCloseButtonImage) {
			button = getCloseButton(false); 
		} else if (frameId == testCases.BadData) {
			//not sure how to test this just yet
		} else if (frameId == testCases.BadPNA) {
			
		} else if (frameId == testCases.GoodPNA) {
			adToShow = new Ad(getRotatedAdImage(true), goodPNA, impressionUrl,
					preExecuteUrl, postExecureUrl, closeUrl); 
		} else if (frameId == testCases.BadPNA) {
			adToShow = new Ad(getRotatedAdImage(true), badPNA, impressionUrl,
					preExecuteUrl, postExecureUrl, closeUrl); 
		} else if (frameId == testCases.GoodPNX) {
			adToShow = new Ad(getRotatedAdImage(true), goodPNX, impressionUrl,
					preExecuteUrl, postExecureUrl, closeUrl);
		} else if (frameId == testCases.BadPNX) {
			adToShow = new Ad(getRotatedAdImage(true), badPNX, impressionUrl,
					preExecuteUrl, postExecureUrl, closeUrl);
		}

		AdResponse responseData = new AdResponse(button, background, location,
				60, "Ok", null);
		responseData.insertAd(adToShow);
		responseData.insertAd(secondAd);
		return responseData;
	}

	private Background getBackground(Orientation orientation, boolean validImage) {
		final String backgroundImage = validImage ? "http://pn-assets-development.s3.amazonaws.com/manual/mBack.png"
				: "http://pn-assets-development.s3.amazonaws.com/manual/invalidImage.png";

		return new Background(backgroundImage, orientation, 270, 320, 10, 10,
				40, 20);
	}

	private Background getBackgroundNoImage(Orientation orientation) {
		return new Background(null, orientation, 270, 320, 10, 10, 40, 20);
	}

	private String getRotatedAdImage(boolean validImage) {
		if (!validImage) {
			return "https://pn-assets-development.s3.amazonaws.com/manual/badImage.gif";
		}
		return (requestCount++) % 2 == 0 ? "https://pn-assets-development.s3.amazonaws.com/manual/m3p.gif"
				: "https://pn-assets-development.s3.amazonaws.com/manual/m3r.gif";
	}
	
	private CloseButton getCloseButton(boolean validImage){
		String imageUrl = validImage ? 
				"http://pn-assets-development.s3.amazonaws.com/manual/mClose.png" : 
				"http://pn-assets-development.s3.amazonaws.com/manual/brokenClose.png";
		
		return new CloseButton(210, 10, 10, 10, imageUrl);
	}
	
	private CloseButton getCloseButtonNoImage(){
		return new CloseButton(210, 10, 10, 10, null);
	}
}
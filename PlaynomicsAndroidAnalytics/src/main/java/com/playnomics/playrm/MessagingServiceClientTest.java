package com.playnomics.playrm;

import java.util.ResourceBundle;

import org.json.JSONException;

import com.playnomics.playrm.Ad.AdTargetType;
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
		public static final String BadAdImage = "BadAdImage";
		public static final String BadCloseButtonImage = "BadCloseButtonImage";
		public static final String GoodPNA = "GoodPNA";
		public static final String BadPNA = "BadPNA";
		public static final String GoodPNX = "GoodPNX";
		public static final String BadPNX = "BadPNX";

		public static final String FixedPortrait = "FixedPortrait";
		public static final String FixedLandscape = "FixedLandscape";
	}

	@Override
	public AdResponse requestAd(String frameId, String caller, int width,
			int height) {
		try
		{
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

			Background background = getBackground(Orientation.DETECT, true, width, height);
			CloseButton button = getCloseButton(true);
			// should never shown
			Ad secondAd;
			secondAd = new Ad("", AdTargetType.URL, "http://www.google.com", impressionUrl, null,
						null, null);

			Location location = new Location(10, 20, 300, 240);

			Ad adToShow;
			adToShow = new Ad(adImage, AdTargetType.URL, targetWebUrl, impressionUrl, null, null,
					closeUrl);

			if (frameId == testCases.FixedPortrait) {
				background = getBackground(Orientation.PORTRAIT, true, width, height);
			} else if (frameId == testCases.FixedLandscape) {
				background = getBackground(Orientation.LANDSCAPE, true, width, height);
			} else if (frameId == testCases.NoCloseButton) {
				button = getCloseButtonNoImage();
			} else if (frameId == testCases.NoBackground) {
				background = getBackgroundNoImage(Orientation.DETECT);
			} else if (frameId == testCases.BadAdImage) {
				adToShow = new Ad(getRotatedAdImage(false), AdTargetType.URL, targetWebUrl,
						impressionUrl, null, null, closeUrl);
			} else if (frameId == testCases.BadBackgroundImage) {
				background = getBackground(Orientation.DETECT, false, width, height);
			} else if (frameId == testCases.BadCloseButtonImage) {
				button = getCloseButton(false);
			} else if (frameId == testCases.BadData) {
				// not sure how to test this just yet
			} else if (frameId == testCases.GoodPNA) {
				adToShow = new Ad(getRotatedAdImage(true), AdTargetType.URL, goodPNA, impressionUrl,
						preExecuteUrl, postExecureUrl, closeUrl);
			} else if (frameId == testCases.BadPNA) {
				adToShow = new Ad(getRotatedAdImage(true), AdTargetType.URL, badPNA, impressionUrl,
						preExecuteUrl, postExecureUrl, closeUrl);
			} else if (frameId == testCases.GoodPNX) {
				adToShow = new Ad(getRotatedAdImage(true), AdTargetType.URL, goodPNX, impressionUrl,
						preExecuteUrl, postExecureUrl, closeUrl);
			} else if (frameId == testCases.BadPNX) {
				adToShow = new Ad(getRotatedAdImage(true), AdTargetType.URL, badPNX, impressionUrl,
						preExecuteUrl, postExecureUrl, closeUrl);
			}

			AdResponse responseData = new AdResponse(button, background, location,
					30, "Ok", null);
			responseData.insertAd(adToShow);
			responseData.insertAd(secondAd);
			return responseData;
		} catch(JSONException ex){
			return null;
		}
	}

	private Background getBackground(Orientation orientation,
			boolean validImage, int screenWidth, int screenHeight) {
		final String backgroundImage = validImage ? "http://pn-assets-development.s3.amazonaws.com/manual/mBack.png"
				: "http://pn-assets-development.s3.amazonaws.com/manual/invalidImage.png";

		int width = 320;
		int height = 270;
		int landscapeX = 10; // (int) (Math.random() * (screenWidth - width));
		int landscapteY = 10; //(int) (Math.random() * (screenHeight - height));
		int portraitX = 50; // (int) (Math.random() * (screenWidth - width));
		int portraitY = 50; //(int) (Math.random() * (screenHeight - height));
		return new Background(backgroundImage, orientation, height, width,
				landscapeX, landscapteY, portraitX, portraitY);
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

	private CloseButton getCloseButton(boolean validImage) {
		String imageUrl = validImage ? "http://pn-assets-development.s3.amazonaws.com/manual/mClose.png"
				: "http://pn-assets-development.s3.amazonaws.com/manual/brokenClose.png";

		return new CloseButton(260, 5, 20, 20, imageUrl);
	}

	private CloseButton getCloseButtonNoImage() {
		return new CloseButton(260, 5, 20, 20, null);
	}
}
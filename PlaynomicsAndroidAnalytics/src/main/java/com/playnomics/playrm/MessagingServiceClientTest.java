package com.playnomics.playrm;

import java.util.ResourceBundle;

import com.playnomics.playrm.Background.Orientation;

public class MessagingServiceClientTest extends MessagingServiceClient {

	public MessagingServiceClientTest(ResourceBundle resource, String baseUrl,
			long appId, String userId, String cookieId) {
		super(resource, baseUrl, appId, userId, cookieId);
	}

	@Override
	public AdResponse requestAd(String frameId, String caller, int width,
			int height) {
		try {
			// introduce some latency
			Thread.sleep(1000);

			final String impressionUrl = "https://ads.b.playnomics.net/v1/impression?a=3&b=4491464577382170140&d=20121019&i=7861628744956845208&r=201210190001&s=GusysoT0lOfJXp-Bp-d6QcSegzVa-OYa3dFtpmyzvxE%3D&u=testUserId";
			final String closeUrl = "https://ads.b.playnomics.net/v1/closeImpression?a=3&b=4491464577382170140&d=20121019&i=7861628744956845208&r=201210190001&s=ixDNt0akWB1M0ctorrKh1HsCfvje4UURN9ct8KThM-s%3D&u=testUserId";
			final String targetWebUrl = "http://www.facebook.com";
			final String goodPNA = "pna://myMethod";
			final String goodPNX = "pna://myMethod";
			final String badPNA = "pnx://myBadMethod";
			final String badPNX = "pnx://myBadMethod";
			final String backgroundImage = "http://pn-assets-development.s3.amazonaws.com/manual/mBack.png";

			final Orientation orientationDetect = Orientation.DETECT;
			final Orientation orientationPortrait = Orientation.PORTRAIT;
			final Orientation orientationLandscape = Orientation.LANDSCAPE;

			final String adImage = "https://pn-assets-development.s3.amazonaws.com/manual/m3p.gif";

			final String closeButtonImage = "http://pn-assets-development.s3.amazonaws.com/manual/mClose.png";

			Background background = new Background(backgroundImage, orientationDetect, 270, 320, 10, 10, 40, 20);

			CloseButton buttonNoImage = new CloseButton(210, 10, 10, 10, null);
			CloseButton button = new CloseButton(210, 10, 10, 10,
					closeButtonImage);

			// should never shown
			Ad secondAd = new Ad("", "http://www.google.com", impressionUrl,
					null, null, null);
			AdResponse responseData = null;

			if (frameId == "noCloseButton") {
				Location location = new Location(0, 0, 270, 320);

				Ad adToShow = new Ad(adImage, targetWebUrl, impressionUrl,
						null, null, null);
				responseData = new AdResponse(buttonNoImage, background, location,
						60, "Ok", null);
				responseData.insertAd(adToShow);
				responseData.insertAd(secondAd);
			} else if (frameId == "closeButton") {
				Location location = new Location(10, 10, 200, 100);

				Ad adToShow = new Ad(adImage, targetWebUrl, impressionUrl,
						null, null, closeUrl);

				responseData = new AdResponse(button, background, location, 60,
						"Ok", null);
				responseData.insertAd(adToShow);
				responseData.insertAd(secondAd);
			} else if (frameId == "noBackgroundImage") {
				background = new Background(null, orientationDetect, 270, 320,
						10, 10, 40, 20);

				Location location = new Location(10, 10, 200, 100);

				Ad adToShow = new Ad(adImage, targetWebUrl, impressionUrl,
						null, null, closeUrl);
				responseData = new AdResponse(button, background, location, 60,
						"Ok", null);
				responseData.insertAd(adToShow);
				responseData.insertAd(secondAd);
			} else if (frameId == "fixedLandscape") {
				background = new Background(null, orientationLandscape, 270,
						320, 10, 10, 40, 20);
			} else if (frameId == "fixedPortrait") {
				background = new Background(null, orientationLandscape, 270,
						320, 10, 10, 40, 20);
			} else if (frameId == "brokenBackgroundImage") {
				background = new Background(null, orientationLandscape, 270,
						320, 10, 10, 40, 20);
			} else if (frameId == "clickPnx") {
				background = new Background(null, orientationLandscape, 270,
						320, 10, 10, 40, 20);
			} else if (frameId == "clickPna") {
				background = new Background(null, orientationLandscape, 270,
						320, 10, 10, 40, 20);
			}
			return responseData;
		} catch (InterruptedException ex) {
			return null;
		}
	}
}
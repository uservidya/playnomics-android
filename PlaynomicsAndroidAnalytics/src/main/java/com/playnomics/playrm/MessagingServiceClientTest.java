package com.playnomics.playrm;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class MessagingServiceClientTest extends MessagingServiceClient {

	public MessagingServiceClientTest(String baseUrl, long appId,
			String userId, String cookieId) {
		super(baseUrl, appId, userId, cookieId);
	}

	public class AdResponse {
		private List<Ad> a;
		private CloseButton c;
		private Background b;
		private Location l;
		private int e = 60;
		private String s = "Ok";
		private String m = null;
		
		public List<Ad> getA() {
			return a;
		}
		
		public AdResponse(CloseButton close, Background background, Location location)
		{
			this.l = location;
			this.b = background;
			this.a = new ArrayList<Ad>();
			this.c = close;
		}
	}

	public class Ad {
		private String i;
		private String t;
		private String u;
		private String v;
		private String s;
		private String d;

		public Ad(String image, String targetWeb, String impression) {
			this.i = image;
			this.t = targetWeb;
			this.s = impression;
		}
		
		public Ad(String image, String targetWeb, String impression, String closeUrl) {
			this(image, targetWeb, impression);
			this.d = closeUrl;
		}

		public Ad(String image, String target, String impression,
				String preExecute, String postExecute, String closeUrl) {
			this(image, target, impression, preExecute, postExecute);
			this.d = closeUrl;
		}
		
		public Ad(String image, String target, String impression,
				String preExecute, String postExecute) {
			this(image, target, impression);
			this.v = postExecute;
			this.u = preExecute;
		}
	}

	public class Location extends Coordinates {
		private int w;
		private int h;

		public int getW() {
			return w;
		}

		public int getH() {
			return h;
		}

		public Location(int x, int y, int width, int height) {
			super(x, y);
			this.w = width;
			this.h = height;
		}

	}

	public class CloseButton extends Location {
		private String i;

		public String getI() {
			return i;
		}

		public CloseButton(int x, int y, int width, int height, String image) {
			super(x, y, width, height);
			this.i = image;
		}
	}

	public class Coordinates {
		private int x;
		private int y;

		public Coordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}

		public int getX() {
			return x;
		}

		public int getY() {
			return y;
		}
	}

	public class Background {
		public Background(String image, String orientation, int height,
				int width, int landscapeX, int landscapeY, int portraitX,
				int portraitY) {
			this.h = height;
			this.w = width;
			this.i = image;
			this.o = orientation;
			this.l = new Coordinates(landscapeX, landscapeY);
			this.p = new Coordinates(landscapeX, landscapeY);
		}

		private Coordinates l;
		private Coordinates p;
		private String o;
		private String i;
		private int h;
		private int w;

		public Coordinates getL() {
			return l;
		}

		public Coordinates getP() {
			return p;
		}

		public int getH() {
			return h;
		}

		public int getW() {
			return w;
		}

		public String getI() {
			return i;
		}

		public String getO() {
			return o;
		}
		
	}

	@Override
	public JSONObject requestAd(String frameId, String caller, int width,
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
			
			final String orientationDetect = "detect";
			final String orientationPortrait = "portrait";
			final String orientationLandscape = "landscape";
			
			final String adImage = "https://pn-assets-development.s3.amazonaws.com/manual/m3p.gif";
			
			final String closeButtonImage = "http://pn-assets-development.s3.amazonaws.com/manual/mClose.png"; 
			
			Background background = new Background(backgroundImage, 
					orientationDetect,
					270, 320, 
					10, 10,
					40, 20);
			
			CloseButton buttonNoImage = new CloseButton(210, 10 ,10, 10, null);
			CloseButton button = new CloseButton(210, 10 ,10, 10, closeButtonImage);
			
			//should never shown
			Ad secondAd = new Ad("", "http://www.google.com", impressionUrl);
			AdResponse jsonData = null;
			
			if (frameId == "noCloseButton") {
				Location location = new Location(10, 10, 200, 100);
			
				Ad adToShow = new Ad(adImage, targetWebUrl, impressionUrl);
				jsonData = new AdResponse(buttonNoImage, background,location);
				jsonData.getA().add(adToShow);
				jsonData.getA().add(secondAd);
			} else if (frameId == "closeButton") {
				Location location = new Location(10, 10, 200, 100);
				
				Ad adToShow = new Ad(adImage, targetWebUrl, impressionUrl, closeUrl);
				
				jsonData = new AdResponse(button, background, location);
				jsonData.getA().add(adToShow);
				jsonData.getA().add(secondAd);
			} else if (frameId == "noBackgroundImage") {
				background = new Background(null, 
						orientationDetect,
						270, 320, 
						10, 10,
						40, 20);
				
				Location location = new Location(10, 10, 200, 100);
				
				Ad adToShow = new Ad(adImage, targetWebUrl, impressionUrl, closeUrl);
				jsonData = new AdResponse(button, background, location);
				jsonData.getA().add(adToShow);
				jsonData.getA().add(secondAd);				
			} else if (frameId == "fixedLandscape") {
				background = new Background(null, 
						orientationLandscape,
						270, 320, 
						10, 10,
						40, 20);
			} else if (frameId == "fixedPortrait") {
				background = new Background(null, 
						orientationLandscape,
						270, 320, 
						10, 10,
						40, 20);
			} else if (frameId == "brokenBackgroundImage") {
				background = new Background(null, 
						orientationLandscape,
						270, 320, 
						10, 10,
						40, 20);
			} else if (frameId == "clickPnx") {
				background = new Background(null, 
						orientationLandscape,
						270, 320, 
						10, 10,
						40, 20);
			} else if (frameId == "clickPna") {
				background = new Background(null, 
						orientationLandscape,
						270, 320, 
						10, 10,
						40, 20);
			}
			Gson gson = new GsonBuilder().serializeNulls().create();
			String gsonString = gson.toJson(jsonData);
			
			Log.d("tag",gsonString);
			
			return new JSONObject(gsonString);
		} catch (InterruptedException ex) {
		} catch (JSONException e) {
			
		}
		return null;
	}
}

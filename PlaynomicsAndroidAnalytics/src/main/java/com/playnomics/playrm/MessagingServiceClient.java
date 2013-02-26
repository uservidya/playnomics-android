package com.playnomics.playrm;

import java.util.List;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.playnomics.playrm.Background.Orientation;

class MessagingServiceClient {

	private final String baseUrl;
	private final long appId;
	private final String userId;
	private final String cookieId;


	private final String TAG = MessagingServiceClient.class.getSimpleName();

	public MessagingServiceClient(ResourceBundle bundle, String baseUrl,
			long appId, String userId, String cookieId) {
		this.baseUrl = baseUrl;
		this.appId = appId;
		this.userId = userId;
		this.cookieId = cookieId;
	}

	public AdResponse requestAd(String frameId, String caller, int width,
			int height) {

		JSONObject jObj = null;
		try {
			Date date = new Date(0);
			Long time = date.getTime();

			String url = baseUrl + "?a=" + this.appId + "&u=" + this.userId
					+ "&p=" + caller + "&t=" + time + "&b=" + this.cookieId
					+ "&f=" + frameId + "&c=" + height + "&d=" + width
					+ "&esrc=aj&ever=" + PlaynomicsSession.getVersion();
			url = URLEncoder.encode(url, PlaynomicsSession.getEncoding());
			
			PlaynomicsLogger.d(TAG, "Fetching ad...");
			PlaynomicsLogger.d(TAG, url);

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(url);
			HttpResponse httpResponse = httpClient.execute(httpPost);
			HttpEntity httpEntity = httpResponse.getEntity();
			InputStream is = httpEntity.getContent();

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					is, PlaynomicsSession.getEncoding()), 8);

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			is.close();

			String jsonString = sb.toString();
			jObj = new JSONObject(jsonString);
			return parseResponse(jObj);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private AdResponse parseResponse(JSONObject responseData) {
		final String expirationKey = "e";
		final String messageKey = "m";
		final String statusKey = "s";

		try {
			String status = cleanJSONString(responseData, statusKey);
			int expirationSeconds = responseData.getInt(expirationKey);
			String message = cleanJSONString(responseData, messageKey);

			Background background = getBackgroundFromData(responseData);
			Location location = getLocationFromData(responseData);
			CloseButton button = getCloseButtonFromData(responseData);

			List<Ad> ads = getAdFromData(responseData);
			if (ads == null || ads.size() == 0) {
				return null;
			}

			return new AdResponse(ads, button, background, location,
					expirationSeconds, status, message);
		} catch (JSONException e) {
			PlaynomicsLogger.d(TAG, "Failed to parse ad response: ", e);
		}
		return null;
	}

	private Background getBackgroundFromData(JSONObject responseData)
			throws JSONException {
		final String backgroundKey = "b";
		final String landscapeKey = "l";
		final String portraitKey = "p";
		final String xKey = "x";
		final String yKey = "y";
		final String widthKey = "w";
		final String heightKey = "h";
		final String orientationKey = "o";
		final String imageKey = "i";

		JSONObject backgroundData = responseData.getJSONObject(backgroundKey);

		String imageUrl = cleanJSONString(backgroundData, imageKey);
		int height = backgroundData.getInt(heightKey);
		int width = backgroundData.getInt(widthKey);

		Orientation orientation = getOrientation(backgroundData
				.getString(orientationKey));

		JSONObject landscapeData = backgroundData.getJSONObject(landscapeKey);
		int landscapeX = landscapeData.getInt(xKey);
		int landscapeY = landscapeData.getInt(yKey);

		JSONObject portraitData = backgroundData.getJSONObject(portraitKey);
		int portraitX = portraitData.getInt(xKey);
		int portraitY = portraitData.getInt(yKey);

		return new Background(imageUrl, orientation, height, width, landscapeX,
				landscapeY, portraitX, portraitY);

	}

	private Orientation getOrientation(String orientation) {
		if (orientation == "landscape") {
			return Orientation.LANDSCAPE;
		}
		if (orientation == "portrait") {
			return Orientation.PORTRAIT;
		}
		return Orientation.DETECT;
	}

	private Location getLocationFromData(JSONObject responseData)
			throws JSONException {
		final String xKey = "x";
		final String yKey = "y";
		final String widthKey = "w";
		final String heightKey = "h";
		final String locationKey = "l";

		JSONObject locationData = responseData.getJSONObject(locationKey);
		int x = locationData.getInt(xKey);
		int y = locationData.getInt(yKey);
		int width = locationData.getInt(widthKey);
		int height = locationData.getInt(heightKey);
		return new Location(x, y, width, height);
	}

	private CloseButton getCloseButtonFromData(JSONObject responseData)
			throws JSONException {
		final String xKey = "x";
		final String yKey = "y";
		final String widthKey = "w";
		final String heightKey = "h";
		final String imageKey = "i";
		final String closeButtonKey = "c";

		JSONObject closeButtonData = responseData.getJSONObject(closeButtonKey);
		int x = closeButtonData.getInt(xKey);
		int y = closeButtonData.getInt(yKey);
		int width = closeButtonData.getInt(widthKey);
		int height = closeButtonData.getInt(heightKey);
		String imageUrl = cleanJSONString(closeButtonData, imageKey);
		return new CloseButton(x, y, width, height, imageUrl);
	}

	private List<Ad> getAdFromData(JSONObject responseData)
			throws JSONException {
		final String imageKey = "i";
		final String targetKey = "t";
		final String impressionKey = "s";
		final String preExecuteKey = "u";
		final String postExecuteKey = "v";
		final String closeUrlKey = "d";
		final String adsKey = "a";

		List<Ad> ads = new ArrayList<Ad>();

		JSONArray adsData = responseData.getJSONArray(adsKey);
		if (adsData.length() == 0) {
			return null;
		}

		for (int i = 0; i < adsData.length(); i++) {
			JSONObject adData = adsData.getJSONObject(i);
			String imageUrl = cleanJSONString(adData, imageKey);
			String targetUrl = cleanJSONString(adData, targetKey);
			String impressionUrl = cleanJSONString(adData, impressionKey);
			String preExecute = cleanJSONString(adData, preExecuteKey);
			String postExecute = cleanJSONString(adData, postExecuteKey);
			String closeUrl = cleanJSONString(adData, closeUrlKey);
			Ad ad = new Ad(imageUrl, targetUrl, impressionUrl, preExecute,
					postExecute, closeUrl);

			ads.add(ad);
		}
		return ads;
	}

	private String cleanJSONString(JSONObject obj, String key)
			throws JSONException {
		return (!obj.has(key) || obj.getString(key).equals("null")) ? null
				: obj.getString(key);
	}
}

package com.playnomics.messaging;

import java.io.UnsupportedEncodingException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.playnomics.messaging.Position.HorizontalGravity;
import com.playnomics.messaging.Position.PositionType;
import com.playnomics.messaging.Position.VerticalGravity;
import com.playnomics.messaging.Target.TargetType;
import com.playnomics.util.Util;

public class HtmlAd {		
	private Position position;
	public Position getPosition(){
		return position;
	}
	
	private CloseButton closeButton;
	CloseButton getCloseButton(){
		return closeButton;
	}
	
	private Target target;
	public Target getTarget(){
		return target;
	}
	
	//url for reporting click interaction
	private String clickLink;
	public String getClickLink(){
		return clickLink;
	}
	

	//url for reporting click interaction
	private String clickUrl;
	public String getClickUrl(){
		return clickUrl;
	}

	//url for reporting impression of message
	private String impressionUrl;
	public String getImpressionUrl(){
		return impressionUrl;
	}

	//url for close interaction
	private String closeUrl;
	public String getCloseUrl(){
		return closeUrl;
	}
	
	private String htmlContent;
	public String getHtmlContent(){
		return htmlContent;
	}

	private HtmlAd(){
	}
	
	public static HtmlAd createFrameFromBytes(byte[] data) throws UnsupportedEncodingException, JSONException{
		if(data == null || data.length == 0){
			return null;
		}
		
		String jsonData = new String(data, Util.UT8_ENCODING);
		JSONObject json = new JSONObject(jsonData);
		
		JSONArray adArray = json.getJSONArray("ads");

		if(adArray == null || adArray.length() == 0){
			return null;
		}
		
		JSONObject firstAdData = adArray.getJSONObject(0);
	
		HtmlAd ad = new HtmlAd();
		ad.clickLink = firstAdData.optString("clickLink", null);
		ad.clickUrl = firstAdData.getString("clickUrl");
		ad.impressionUrl = firstAdData.getString("impressionUrl");
		ad.closeUrl = firstAdData.getString("closeUrl");
		ad.htmlContent = firstAdData.getString("htmlContent");
		
		ad.position = getPositionFromResponse(firstAdData);
		ad.target = getTargetFromResponse(firstAdData);
		ad.closeButton = getCloseButtonFromResponse(firstAdData);
		return ad;
	}
	
	static Position getPositionFromResponse(JSONObject response) throws JSONException{
		JSONObject positionData = response.getJSONObject("position");
		
		String positionTypeString = positionData.getString("positionType");
		
		Position position = new Position();
		if(positionTypeString.equals("fullscreen")){
			position.setPositionType(PositionType.FULLSCREEN);
		} else if(positionTypeString.equals("banner")){
			position.setPositionType(PositionType.BANNER);
			position.setHeight(((float) positionData.getDouble("height")));
			position.setWidth(((float) positionData.getDouble("width")));
			
			String horizontalGravityString = positionData.optString("horizontalGravity", null);
			position.setHorizontalGravity(toHorizontalGravity(horizontalGravityString));
			
			String verticalGravityString = positionData.optString("verticalGravity", null);
			position.setVerticalGravity(toVerticalGravity(verticalGravityString));
			
			Float height = (float)positionData.getDouble("height");
			Float width = (float)positionData.getDouble("width");
			position.setHeight(height);
			position.setWidth(width);
		} else {
			throw new JSONException(String.format("Unsupported positionType: %s", positionTypeString));
		}
		return position;
	}
	
	static Target getTargetFromResponse(JSONObject response) throws JSONException {
		JSONObject targetJSONData = response.getJSONObject("target");
		Target target = new Target();
		
		String targetTypeString = targetJSONData.optString("targetType", null);
		target.setTargetType(toTargetType(targetTypeString));
		
		if(target.getTargetType() == TargetType.URL){
			target.setTargetUrl(targetJSONData.optString("targetUrl", null));
		} else if(target.getTargetType() == TargetType.EXTERNAL) {
			String targetDataJSON = targetJSONData.optString("targetData", null);
			if(targetDataJSON != null){
				JSONObject targetData = new JSONObject(targetDataJSON);
				target.setTargetData(Util.toMap(targetData));
			}
		}
		return target;
	}
	
	static CloseButton getCloseButtonFromResponse(JSONObject response) throws JSONException{
		
		JSONObject closeButtonJSONData = response.getJSONObject("closeButton");
	
		String closeButtonType = closeButtonJSONData.getString("closeButtonType");
		
		if(closeButtonType.equals("native")){
			NativeCloseButton nativeCloseButton = new NativeCloseButton();
			nativeCloseButton.setCloseImageUrl(closeButtonJSONData.getString("closeImageUrl"));
			nativeCloseButton.setHeight((float)closeButtonJSONData.getDouble("height"));
			nativeCloseButton.setWidth((float)closeButtonJSONData.getDouble("width"));
			return nativeCloseButton;
		} else if(closeButtonType.equals("html")){
			HtmlCloseButton htmlCloseButton = new HtmlCloseButton();
			htmlCloseButton.setCloseLink(closeButtonJSONData.getString("closeLink"));
		} 
		throw new JSONException(String.format("Unsupported closeButtonType: %s", closeButtonType));
	}
	
	static HorizontalGravity toHorizontalGravity(String gravityString) throws JSONException{
		if(gravityString == null){
			throw new JSONException("horizontalGravity is null");
		}
		
		gravityString = gravityString.toLowerCase();
		if(gravityString.equals("left")){
			return HorizontalGravity.LEFT;
		}
		
		if(gravityString.equals("center")){
			return HorizontalGravity.CENTER;
		}
		
		if(gravityString.equals("right")){
			return HorizontalGravity.RIGHT;
		}
		throw new JSONException(String.format("Unsupported horizontalGravity: %s", gravityString));
	}
	
	static VerticalGravity toVerticalGravity(String gravityString) throws JSONException{
		if(gravityString == null){
			throw new JSONException("verticalGravity is null");
		}
		
		gravityString = gravityString.toLowerCase();
		
		if(gravityString.equals("top")){
			return VerticalGravity.TOP;
		}
		if(gravityString.equals("center")){
			return VerticalGravity.CENTER;
		}
		if(gravityString.equals("bottom")){
			return VerticalGravity.BOTTOM;
		}
		throw new JSONException(String.format("Unsupported verticalGravity: %s", gravityString));
	}
	
	static TargetType toTargetType(String targetTypeString) throws JSONException{
		if(targetTypeString == null){
			throw new JSONException("targetTypeString is null");
		}
		
		targetTypeString = targetTypeString.toLowerCase();
		
		if(targetTypeString.equals("url")){
			return TargetType.URL;
		} else if(targetTypeString.equals("external")){
			return TargetType.EXTERNAL;
		} else if(targetTypeString.equals("data")){
			return TargetType.DATA;
		}
		throw new JSONException(String.format("Unsupported targetType: %s", targetTypeString));
	}
}



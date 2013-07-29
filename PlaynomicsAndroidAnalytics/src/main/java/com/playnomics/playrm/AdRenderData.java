package com.playnomics.playrm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import android.graphics.drawable.Drawable;

class AdRenderData {
	private AdResponse adResponse;
	private Map<String, Drawable> imageAssets;
	
	public AdRenderData(AdResponse adResponse){
		this.adResponse = adResponse;
		this.imageAssets = new HashMap<String, Drawable>();
	}
	
	public AdResponse getAdResponse(){
		return adResponse;
	}
	
	public Drawable getBackgroundImage(){
		return getOrAddDrawableForImage(adResponse.getBackground().getImageUrl());
	}
	
	public Drawable getAdImage(){
		return getOrAddDrawableForImage(adResponse.getFirstAd().getImageUrl());
	}
	
	public Drawable getCloseButtonImage(){
		return getOrAddDrawableForImage(adResponse.getCloseButton().getImageUrl());
	}
	
	public void loadAllImages(){
		if(adResponse.getFirstAd() != null){
			getOrAddDrawableForImage(adResponse.getBackground().getImageUrl());
			getOrAddDrawableForImage(adResponse.getFirstAd().getImageUrl());
			getOrAddDrawableForImage(adResponse.getCloseButton().getImageUrl());
		}
	}
	
	private Drawable getOrAddDrawableForImage(String imageUrl){
		if(imageUrl == null){
			return null;
		}
		
		Drawable image = imageAssets.get(imageUrl);
		if(image != null){
			return image;
		}
		
		image = downloadDrawable(imageUrl);
		imageAssets.put(imageUrl, image);
		return image;
	}
	
	private Drawable downloadDrawable(String imageUrl){
		try {
			URL url = new URL(imageUrl);
			InputStream stream = (InputStream) url.getContent();
			Drawable drawable = Drawable.createFromStream(stream, "src");
			return drawable;
		} catch (IOException e) {
			return null;
		}
	}
}

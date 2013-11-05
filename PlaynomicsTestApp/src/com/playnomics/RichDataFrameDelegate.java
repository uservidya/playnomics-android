package com.playnomics;

import java.util.Map;

import android.util.Log;

import com.playnomics.android.sdk.IPlaynomicsPlacementDelegate;

public class RichDataFrameDelegate implements IPlaynomicsPlacementDelegate {

	private final String TAG = RichDataFrameDelegate.class.getSimpleName();
	
	private final String placementName;
	
	public RichDataFrameDelegate(String placementName){
		this.placementName = placementName;
	}

	@Override
	public void onShow(Map<String, Object> jsonData) {
		Log.d(TAG, "onShow data for this placement: "+ placementName);
		Log.d(TAG, jsonData.toString());
	}

	@Override
	public void onTouch(Map<String, Object> jsonData) {
		Log.d(TAG, "onTouch data for this placement: "+ placementName);
		Log.d(TAG, jsonData.toString());
	}

	@Override
	public void onClose(Map<String, Object> jsonData) {
		Log.d(TAG, "onClose data for this placement: "+ placementName);
		Log.d(TAG, jsonData.toString());
	}

	@Override
	public void onRenderFailed() {
		Log.d(TAG, "Placement could not be rendered: "+ placementName);
	}
}

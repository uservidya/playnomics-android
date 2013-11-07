package com.playnomics;

import java.util.Map;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.playnomics.android.sdk.IPlaynomicsPlacementDelegate;

public class RichDataFrameDelegate implements IPlaynomicsPlacementDelegate {
	private final String placementName;
	private Context context;
	public RichDataFrameDelegate(String placementName, Context context){
		this.placementName = placementName;
		this.context = context;
	}

	@Override
	public void onShow(Map<String, Object> jsonData) {
		postToastMessage(String.format("onShow:\n %s", jsonData == null ? "No data" : jsonData.toString()));
	}

	@Override
	public void onTouch(Map<String, Object> jsonData) {
		postToastMessage(String.format("onTouch:\n %s", jsonData == null ? "No data" : jsonData.toString()));
	}

	@Override
	public void onClose(Map<String, Object> jsonData) {
		postToastMessage(String.format("onClose:\n %s", jsonData == null ? "No data" : jsonData.toString()));
	}

	@Override
	public void onRenderFailed() {
		postToastMessage(String.format("Placement could not be rendered: %s", placementName));
	}
	
	private void postToastMessage(String message){
		int duration = Toast.LENGTH_LONG;
		Toast toast = Toast.makeText(context, message, duration);
		toast.show();
	}
}

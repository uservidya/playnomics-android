package com.playnomics;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.playnomics.playrm.FrameDelegate;

public class RichDataFrameDelegate implements FrameDelegate {

	private final String TAG = RichDataFrameDelegate.class.getSimpleName();
	
	private final String frameId;
	
	public RichDataFrameDelegate(String frameId){
		this.frameId = frameId;
	}
	
	@Override
	public void onClick(JSONObject jsonData) {
		Log.d(TAG, "Received data for this frame: "+ frameId);
		try {
			Log.d(TAG, jsonData.toString(4));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}
}

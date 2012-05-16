package com.playnomics.analytics;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenReceiver extends BroadcastReceiver {
	
	private static String TAG = ScreenReceiver.class.getSimpleName();
	public boolean screenOff;
	
	@Override
	public void onReceive(Context context, Intent intent) {
	
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			screenOff = true;
			PlaynomicsSession.pause();
			Log.i(TAG, "SCREEN TURNED OFF on BroadcastReceiver");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			screenOff = false;
			PlaynomicsSession.resume();
			Log.i(TAG, "SCREEN TURNED ON on BroadcastReceiver");
		}
	}
}
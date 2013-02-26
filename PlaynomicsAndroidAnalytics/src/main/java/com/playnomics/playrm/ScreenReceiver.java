package com.playnomics.playrm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

class ScreenReceiver extends BroadcastReceiver {
	private static String TAG = ScreenReceiver.class.getSimpleName();
	
	@Override
	public void onReceive(Context context, Intent intent) {
		if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
			PlaynomicsSession.pause();
			PlaynomicsLogger.i(TAG, "SCREEN TURNED OFF");
		} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
			PlaynomicsSession.resume();
			PlaynomicsLogger.i(TAG, "SCREEN TURNED ON");
		}
	}
}
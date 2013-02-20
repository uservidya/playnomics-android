package com.playnomics.playrm;

import android.view.MotionEvent;

public interface AdEventHandler {
	
	public void onAdViewClose();

	public void onAdViewClicked(MotionEvent event);
}

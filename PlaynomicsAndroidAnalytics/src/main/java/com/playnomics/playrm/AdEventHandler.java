package com.playnomics.playrm;

import android.view.MotionEvent;

public interface AdEventHandler {

	public void adViewClose();

	public void adViewClicked(MotionEvent event);

	public void onAdViewLoaded();

}

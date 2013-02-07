package com.playnomics.playrm;

import android.view.MotionEvent;

public interface BaseAdComponentInterface {
	
	public void baseAdComponentClose();
	public void baseAdComponentOpen(MotionEvent event);
	public void baseAdComponentReady();
		
}

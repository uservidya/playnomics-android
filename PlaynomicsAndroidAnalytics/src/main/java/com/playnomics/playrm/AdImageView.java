package com.playnomics.playrm;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;

class AdImageView extends ImageView {

	public enum ImageType {
		CREATIVE, CLOSE_BUTTON, BACKGROUND
	};
	
	private ImageType imageType;

	public ImageType getImageType() {
		return this.imageType;
	}
	
	private Frame frame;

	public AdImageView(int x, int y, int height, int width, Drawable image, 
			Frame frame, Context cont, ImageType imageType) {
		super(cont);
		this.frame = frame;
		this.imageType = imageType;
		if (image != null) {
			this.setupImage(image, x, y, width, height);
		}
	}
	
//	private void handleGifDownload() {
//		LayoutParams layoutParams = new LayoutParams(this.width, this.height);
//		this.gifWebView.setHorizontalScrollBarEnabled(false);
//		this.gifWebView.setVerticalScrollBarEnabled(false);
//		this.gifWebView.setOnTouchListener(new WebView.OnTouchListener() {
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					delegate.baseAdComponentOpen(event);
//				}
//
//				return false;
//			}
//		});
//		this.gifWebView.setLayoutParams(layoutParams);
//		this.layout.addView(this.gifWebView);
//		this.setStatus(ViewStatus.COMPLETED);
//	}

	private void setupImage(Drawable result, int x, int y, 
			int width, int height) {
		
//			setMaxHeight(height);
//			setMinimumHeight(height);
//			setMaxWidth(width);
//			setMinimumWidth(width);
		LayoutParams layoutParams = new LayoutParams(width, height);
		layoutParams.leftMargin = x;
		layoutParams.topMargin = y;
		setLayoutParams(layoutParams);
		
//just for debugging
//			if(this.imageType == ImageType.BACKGROUND){
//				setBackgroundColor(Color.BLUE);
//			} else if (this.imageType == ImageType.CREATIVE) {
//				setBackgroundColor(Color.WHITE);
//			} else {
//				setBackgroundColor(Color.RED);
//			}
		
		setImageDrawable(result);
		
		if (imageType == ImageType.CLOSE_BUTTON) {
			setOnClickListener(new ImageView.OnClickListener() {
				public void onClick(View v) {
					// Close button has been clicked, should remove the ad
					// here
					frame.onAdViewClose();
				}
			});
		}
		
		if(imageType == ImageType.CREATIVE){
			setOnTouchListener(new OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent e) {
					frame.onAdViewClicked(e);
					return true;
				}
			});
		}
	}
}

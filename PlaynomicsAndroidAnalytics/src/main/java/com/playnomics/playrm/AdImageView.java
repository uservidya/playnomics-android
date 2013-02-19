package com.playnomics.playrm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AdImageView extends ImageView {
	public enum ViewStatus {
		PENDING, COMPLETED, ERROR
	};

	public enum ImageType {
		CREATIVE, CLOSE_BUTTON, BACKGROUND
	};

	private String imageUrl;
	private AdImageView.ViewStatus status = ViewStatus.PENDING;

	public AdImageView.ViewStatus getStatus() {
		return this.status;
	}

	private void setStatus(ViewStatus value) {
		this.status = value;
		if (this.status == ViewStatus.COMPLETED) {
			this.delegate.onAdViewLoaded();
		}
	}

	private ImageType imageType;

	public ImageType getImageType() {
		return this.imageType;
	}
	
	private AdEventHandler delegate;

	public AdImageView(int x, int y, int height, int width, String imageUrl, 
			AdEventHandler handler, Context cont, ImageType imageType) {
		super(cont);
		this.imageType = imageType;
		this.delegate = handler;
		this.imageUrl = imageUrl;
		if (this.imageUrl == null || this.imageUrl.length() < 0 || 
				!(width > 0 && height > 0)) {
			// no image to load so we'll just that say
			// that is ready to go
			setStatus(ViewStatus.COMPLETED);
		} else {
			this.startImageDownload(x, y, width, height);
		}
	}
	
	private void startImageDownload(final int x, final int y, final int width, final int height) {
		AsyncTask<Void, Void, Drawable> task = new AsyncTask<Void, Void, Drawable>() {
			@Override
			protected Drawable doInBackground(Void... params) {
				try {
					URL url = new URL(imageUrl);
					InputStream stream = (InputStream) url.getContent();
					Drawable drawable = Drawable.createFromStream(stream, "src");
					return drawable;
				} catch (IOException e) {
					return null;
				}
			}

			protected void onPostExecute(Drawable result) {
				handleImageDownload(result, x, y, width, height);
			}
		};
		task.execute((Void) null);
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

	private void handleImageDownload(Drawable result, int x, int y, 
			int width, int height) {
		if (result != null) {
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
						delegate.adViewClose();
					}
				});
			}
			
			if(imageType == ImageType.CREATIVE){
				setOnTouchListener(new OnTouchListener() {
					@Override
					public boolean onTouch(View view, MotionEvent e) {
						delegate.adViewClicked(e);
						return false;
					}
				});
			}
			
			this.setStatus(ViewStatus.COMPLETED);
		} else {
			this.setStatus(ViewStatus.ERROR);
		}
	}
}

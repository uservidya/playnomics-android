package com.playnomics.playrm;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class AdView {
	public enum ViewStatus {
		PENDING, COMPLETED, ERROR
	};

	public enum ImageType {
		AD_CLICKED, AD_CLOSED, AD_BACKGROUND
	};

	private FrameLayout layout;
	public FrameLayout getLayout() {
		return layout;
	}

	public View view;

	private String imageUrl = null;
	private AdView.ViewStatus status = ViewStatus.PENDING;

	public AdView.ViewStatus getStatus() {
		return this.status;
	}

	private void setStatus(ViewStatus value) {
		this.status = value;
		if (this.status == ViewStatus.COMPLETED) {
			this.delegate.baseAdComponentReady();
		}
	}
	
	private int height;
	private int width;

	private ImageType imageType;

	public ImageType getImageType() {
		return this.imageType;
	}
	
	private AdEventHandler delegate;
	private ImageView imageView;
	
	public ImageView getImageView(){
		return this.imageView;
	}
	
	private Context context;

	public AdView(int topX, int topY, int height, int width, String imageUrl,
			Frame parentFrame, Context cont, ImageType imageType) {
		
		this.context = cont;
		this.height = height;
		this.width = width;
		this.layout = new FrameLayout(this.context);	
		this.imageType = imageType;
		
		//set the height and width all of the time
		setLayout(topX, topY);
		this.delegate = parentFrame;
		this.imageUrl = imageUrl;
		if (this.imageUrl == null && this.imageUrl.length() > 0 && 
				this.width > 0 && this.height > 0) {
			// no image to load so we'll just that say
			// that is ready to go
			setStatus(ViewStatus.COMPLETED);
		} else {
			this.startImageDownload();
		}
	}
	
	public void setLayout(int x, int y){
		LayoutParams params = new LayoutParams(width, height);
		params.setMargins(x, y, 0, 0);
		this.layout.setLayoutParams(params);
	}

	
	private void startImageDownload() {
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
				handleImageDownload(result);
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

	private void handleImageDownload(Drawable result) {
		if (result != null) {
			this.imageView = new ImageView(this.context);
			this.imageView.setImageDrawable(result);
			this.imageView.setMaxHeight(height);
			this.imageView.setMinimumHeight(height);
			this.imageView.setMaxWidth(width);
			this.imageView.setMinimumWidth(width);
			
			this.imageView.setOnClickListener(new ImageView.OnClickListener() {
				public void onClick(View v) {
					if (imageType == ImageType.AD_CLOSED) {
						// Close button has been clicked, should remove the ad
						// here
						delegate.baseAdComponentClose();
					}
				}
			});

			LayoutParams layoutParams = new LayoutParams(
					RelativeLayout.LayoutParams.WRAP_CONTENT,
					RelativeLayout.LayoutParams.WRAP_CONTENT);

			this.imageView.setLayoutParams(layoutParams);
			this.layout.addView(imageView);

			this.setStatus(ViewStatus.COMPLETED);
		} else {
			this.setStatus(ViewStatus.ERROR);
		}
	}

	public void addChildView(AdView cildView) {
		this.layout.addView(cildView.getLayout());
	}
}

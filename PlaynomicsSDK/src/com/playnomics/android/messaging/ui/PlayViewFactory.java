package com.playnomics.android.messaging.ui;

import android.app.Activity;
import android.content.Context;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.playnomics.android.messaging.Placement.IPlacementStateObserver;
import com.playnomics.android.messaging.ui.PlayWebView.IPlayWebViewHandler;
import com.playnomics.android.util.Logger;

public class PlayViewFactory implements IPlayViewFactory {

	public interface IImageViewHandler{
		void onTouch();
	}
	
	public PlayDialog createPlayDialog(Context context, PlayWebView webView, IPlacementStateObserver observer, Activity activity) {
		return new PlayDialog(context, webView, observer, activity);
	}
	
	public PlayDialog createPlayDialog(Context context, PlayWebView webView, IPlacementStateObserver observer, Activity activity, ImageView nativeCloseButton) {
		return new PlayDialog(context, webView, observer, activity, nativeCloseButton);
	}

	public PlayWebView createPlayWebView(Context context, String htmlContent, final IPlayWebViewHandler handler,
			final Logger logger) throws Exception {
		return new PlayWebView(context, htmlContent, handler, logger);
	}

	public ImageView createImageView(Context context, final IImageViewHandler imageViewHandler) {
		return new ImageView(context){
			@Override
			public boolean onTouchEvent(MotionEvent event) {
				if(event.getActionMasked() == MotionEvent.ACTION_DOWN){
					imageViewHandler.onTouch();
				}
				return true;
			}
		};
	}
}

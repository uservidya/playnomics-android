package com.playnomics.android.messaging.ui;

import android.app.Activity;
import android.content.Context;
import android.widget.ImageView;

import com.playnomics.android.messaging.Placement.IPlacementStateObserver;
import com.playnomics.android.messaging.ui.PlayViewFactory.IImageViewHandler;
import com.playnomics.android.messaging.ui.PlayWebView.IPlayWebViewHandler;
import com.playnomics.android.util.Logger;

public interface IPlayViewFactory {
	
	public PlayDialog createPlayDialog(Context context, PlayWebView webView, IPlacementStateObserver observer, Activity activity);
	
	public PlayDialog createPlayDialog(Context context, PlayWebView webView, IPlacementStateObserver observer, Activity activity, ImageView nativeCloseButton);
	
	public PlayWebView createPlayWebView(Context context, String htmlContent, String baseUrl,
			final IPlayWebViewHandler handler, final Logger logger) throws Exception;
	
	public ImageView createImageView(Context context, final IImageViewHandler imageViewHandler);
}

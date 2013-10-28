package com.playnomics.messaging.ui;

import android.content.Context;
import android.widget.ImageView;

import com.playnomics.messaging.ui.PlayWebView.IPlayWebViewHandler;
import com.playnomics.util.Logger;

public class PlayViewFactory implements IPlayViewFactory {
	
	public PlayDialog createPlayDialog(Context context, PlayWebView webView){
		return new PlayDialog(context, webView);
	}
	
	public PlayWebView createPlayWebView(Context context, String htmlContent, String baseUrl,
			final IPlayWebViewHandler handler, final Logger logger) throws Exception{
		return new PlayWebView(context, htmlContent, baseUrl, handler, logger);
	}
	
	public ImageView createImageView(Context context){
		return new ImageView(context);
	}
}

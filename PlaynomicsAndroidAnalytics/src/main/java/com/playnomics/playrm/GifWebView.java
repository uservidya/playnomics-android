package com.playnomics.playrm;

import android.content.Context;
import android.webkit.WebView;

class GifWebView extends WebView {
	public GifWebView(Context context, String path) {
		super(context);
		loadUrl(path);
	}
}

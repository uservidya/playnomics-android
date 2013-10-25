package com.playnomics.messaging.ui;

import android.content.Context;
import android.graphics.Color;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.playnomics.util.Logger;
import com.playnomics.util.Util;
import com.playnomics.util.Logger.LogLevel;

public class PlayWebView extends WebView {

	public interface IPlayWebViewHandler {
		void onLoadFailure(int errorCode);

		void onLoadComplete();

		void onUrlLoading(String url);
	}

	public PlayWebView(Context context, String htmlContent, String baseUrl,
			final IPlayWebViewHandler handler, final Logger logger)
			throws Exception {
		super(context);
		// transparent
		setBackgroundColor(Color.TRANSPARENT);

		setHorizontalScrollBarEnabled(false);
		setHorizontalScrollbarOverlay(false);
		setVerticalScrollBarEnabled(false);
		setVerticalScrollbarOverlay(false);

		WebSettings settings = getSettings();
		settings.setSupportZoom(false);
		settings.setJavaScriptEnabled(true);

		setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				handler.onLoadComplete();
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				handler.onUrlLoading(url);
				//always prevent the webview from redirecting another pages
				return true;
			}

			@Override
			public void onReceivedError(WebView view, int errorCode,
					String description, String failingUrl) {
				handler.onLoadFailure(errorCode);
			}
		});

		setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
				String message = String.format("%s -- line  %d",
						consoleMessage.message(), consoleMessage.lineNumber());
				switch (consoleMessage.messageLevel()) {
				case ERROR:
					logErrorMessage(message);
					break;
				default:
					logInfoMessage(message);
					break;
				}
				return true;
			}

			private void logInfoMessage(String message) {
				logger.log(LogLevel.DEBUG, message);
			}

			private void logErrorMessage(String message) {
				logger.log(LogLevel.WARNING, message);
			}
		});

		// load this data in the background
		loadDataWithBaseURL(baseUrl, htmlContent, Util.CONTENT_TYPE_HTML, Util.UT8_ENCODING, null);
	}
}

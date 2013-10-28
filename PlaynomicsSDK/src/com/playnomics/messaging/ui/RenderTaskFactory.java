package com.playnomics.messaging.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;

import com.playnomics.messaging.Placement;
import com.playnomics.messaging.Placement.IPlacementStateObserver;
import com.playnomics.messaging.HtmlAd;
import com.playnomics.messaging.NativeCloseButton;
import com.playnomics.messaging.ui.PlayWebView.IPlayWebViewHandler;
import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;

public class RenderTaskFactory {

	private IPlayViewFactory viewFactory;
	private Logger logger;

	public RenderTaskFactory(IPlayViewFactory viewFactory, Logger logger) {
		this.viewFactory = viewFactory;
		this.logger = logger;
	}

	public Runnable createShowPlacementTask(final Placement placement,
			final HtmlAd htmlAd, final Activity activity,
			final IPlayWebViewHandler handler,
			final IPlacementStateObserver observer) {

		return new Runnable() {
			public void run() {
				try {
					PlayWebView webView = viewFactory.createPlayWebView(
							activity, htmlAd.getHtmlContent(),
							htmlAd.getContentBaseUrl(), handler, logger);
					PlayDialog dialog = viewFactory.createPlayDialog(activity,
							webView);

					placement.setDialog(dialog);

					if (htmlAd.getCloseButton() instanceof NativeCloseButton) {
						NativeCloseButton closeButton = (NativeCloseButton) htmlAd
								.getCloseButton();

						ImageView imageView = viewFactory
								.createImageView(activity);

						byte[] imageData = closeButton.getImageData();
						Bitmap bitmap = BitmapFactory.decodeByteArray(
								imageData, 0, imageData.length);
						imageView.setImageBitmap(bitmap);

						dialog.showWebView(webView, imageView);
					} else {
						dialog.showWebView(webView);
					}
					observer.onPlacementShown(activity, placement);
				} catch (Exception ex) {
					logger.log(LogLevel.WARNING,
							"The placement %s cannot be rendered",
							placement.getPlacementName());
					logger.log(LogLevel.WARNING, ex);
				}
			}
		};
	}

	public Runnable createHidePlacementTask(final PlayDialog dialog) {
		return new Runnable() {
			public void run() {
				dialog.dismiss();
			}
		};
	}
}

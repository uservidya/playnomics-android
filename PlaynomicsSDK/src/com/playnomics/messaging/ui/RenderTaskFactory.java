package com.playnomics.messaging.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

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

	public Runnable createLayoutPlacementTask(final Placement placement,
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

					RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 
							LayoutParams.FILL_PARENT);
					dialog.addContentView(webView, layoutParams);
					
					if (htmlAd.getCloseButton() instanceof NativeCloseButton) {
						NativeCloseButton closeButton = (NativeCloseButton) htmlAd
								.getCloseButton();

						ImageView closeButtonView = viewFactory
								.createImageView(activity);

						byte[] imageData = closeButton.getImageData();
						Bitmap bitmap = BitmapFactory.decodeByteArray(
								imageData, 0, imageData.length);
						closeButtonView.setImageBitmap(bitmap);
						
						RelativeLayout.LayoutParams params = new LayoutParams(closeButton.getWidth(), closeButton.getHeight());
						params.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.ALIGN_PARENT_RIGHT);
						dialog.addContentView(closeButtonView, params);
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
	
	public Runnable createShowPlacementTask(final PlayDialog dialog){
		return new Runnable() {
			public void run() {
				dialog.show();
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

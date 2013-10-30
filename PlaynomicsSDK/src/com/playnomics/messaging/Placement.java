package com.playnomics.messaging;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.playnomics.messaging.Position.PositionType;
import com.playnomics.messaging.Target.TargetType;
import com.playnomics.messaging.ui.IPlayViewFactory;
import com.playnomics.messaging.ui.PlayDialog;
import com.playnomics.messaging.ui.PlayViewFactory.IImageViewHandler;
import com.playnomics.messaging.ui.PlayWebView;
import com.playnomics.messaging.ui.RenderTaskFactory;
import com.playnomics.sdk.IPlaynomicsPlacementDelegate;
import com.playnomics.session.ICallbackProcessor;
import com.playnomics.util.Logger;
import com.playnomics.util.Util;
import com.playnomics.util.Logger.LogLevel;

public class Placement implements PlayWebView.IPlayWebViewHandler, IImageViewHandler {

	public interface IPlacementStateObserver {
		void onPlacementShown(Activity activity, Placement placement);

		void onPlacementDisposed(Activity activity);
	}

	public enum PlacementState {
		NOT_LOADED, LOAD_STARTED, LOAD_COMPLETE, LOAD_FAILED
	}

	private boolean shouldRender;
	private boolean impressionLogged;

	private Util util;
	private IPlaynomicsPlacementDelegate delegate;
	private Activity activity;
	private ICallbackProcessor callbackProcessor;
	private Logger logger;
	private IPlacementStateObserver observer;
	private RenderTaskFactory renderTaskFactory;

	private PlayDialog dialog;

	private String placementName;

	public String getPlacementName() {
		return placementName;
	}

	private PlacementState state;

	public PlacementState getState() {
		return state;
	}

	public void setState(PlacementState state) {
		this.state = state;
	}

	public void setDialog(PlayDialog dialog) {
		this.dialog = dialog;
	}

	private boolean hasNativeCloseButton() {
		return htmlAd != null
				&& htmlAd.getCloseButton() instanceof NativeCloseButton;
	}

	private HtmlAd htmlAd;

	public Placement(String placementName,
			ICallbackProcessor callbackProcessor, Util util, Logger logger,
			IPlacementStateObserver observer,
			RenderTaskFactory renderTaskFactory) {
		this.observer = observer;
		this.placementName = placementName;
		this.state = PlacementState.NOT_LOADED;
		this.callbackProcessor = callbackProcessor;
		this.util = util;
		this.logger = logger;
		this.renderTaskFactory = renderTaskFactory;
	}

	public void updatePlacementData(HtmlAd htmlAd) {
		this.htmlAd = htmlAd;
		state = PlacementState.LOAD_COMPLETE;
		impressionLogged = false;
		if (shouldRender) {
			loadWebView();
		}
	}

	public void show(final Activity activity,
			IPlaynomicsPlacementDelegate delegate) {
		this.delegate = delegate;
		this.activity = activity;

		shouldRender = true;

		if (state == PlacementState.LOAD_COMPLETE) {
			loadWebView();
		} else if (state == PlacementState.LOAD_FAILED) {
			if (delegate != null) {
				delegate.onRenderFailed();
			}
		}
	}

	private void loadWebView() {
		if (!(shouldRender && state == PlacementState.LOAD_COMPLETE)) {
			return;
		}

		Runnable renderTask = renderTaskFactory.createLayoutPlacementTask(this,
				htmlAd, activity, this, this, observer);
		// make sure we run this task on the UI thread
		util.runTaskOnActivityUIThread(renderTask, activity);
	}

	public void hide() {
		if (dialog != null) {
			Runnable hideTask = renderTaskFactory
					.createHidePlacementTask(dialog);
			util.runTaskOnActivityUIThread(hideTask, activity);
		}
	}

	public void onLoadFailure(int errorCode) {
		onLoadFailure();
	}

	private void onLoadFailure() {
		state = PlacementState.LOAD_FAILED;
		if (delegate != null) {
			delegate.onRenderFailed();
		}
	}

	public void onLoadComplete() {
		state = PlacementState.LOAD_COMPLETE;
		
		Runnable showTask = renderTaskFactory.createShowPlacementTask(dialog);
		util.runTaskOnActivityUIThread(showTask, activity);
		
		if (!impressionLogged) {
			impressionLogged = true;
			callbackProcessor.processUrlCallback(htmlAd.getImpressionUrl());
		
			if (delegate != null) {
				delegate.onShow(htmlAd.getTarget().getTargetData());
			}
		}
	}

	public void onUrlLoading(String url) {
		hide();
		if (!Util.stringIsNullOrEmpty(url)) {
			if (!hasNativeCloseButton()) {
				HtmlCloseButton htmlClose = (HtmlCloseButton) htmlAd
						.getCloseButton();
				if (url.equals(htmlClose.getCloseLink())) {
					onAdClosed(true);
					return;
				}
			}

			onAdTouched();
			if (!url.equals(htmlAd.getClickLink())) {
				util.openUrlInPhoneBrowser(url, activity);
			}
		}
	}

	private void onAdTouched() {
		callbackProcessor.processUrlCallback(htmlAd.getClickUrl());
		Target target = htmlAd.getTarget();

		if (target.getTargetType() == TargetType.URL
				&& !Util.stringIsNullOrEmpty(target.getTargetUrl())) {
			util.openUrlInPhoneBrowser(target.getTargetUrl(), activity);
		}

		if (delegate != null) {
			delegate.onTouch(htmlAd.getTarget().getTargetData());
		}
	}

	private void onAdClosed(boolean closedByUser) {
		observer.onPlacementDisposed(activity);
		if (closedByUser) {
			callbackProcessor.processUrlCallback(htmlAd.getCloseUrl());
		}
		if (delegate != null) {
			delegate.onClose(htmlAd.getTarget().getTargetData());
		}
	}

	public void attachActivity(Activity activity) {
		this.activity = activity;
		loadWebView();
	}

	public void detachActivity() {
		if (dialog != null) {
			Runnable hideTask = renderTaskFactory
					.createHidePlacementTask(dialog);
			util.runTaskOnActivityUIThread(hideTask, activity);
		}
		this.activity = null;
	}

	public void onTouch() {
		hide();
		onAdClosed(true);
	}
}
package com.playnomics.playrm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.playnomics.playrm.AdImageView.ImageType;
import com.playnomics.playrm.Background.Orientation;

public class Frame implements AdEventHandler {
	public enum DisplayResult {
		NO_INTERNET_PERMISSION, START_NOT_CALLED, UNABLE_TO_CONNECT, FAIL_UNKNOWN, DISPLAY_PENDING, DISPLAYED
	};

	// should we show the frame?
	private boolean shouldDisplay = false;
	// have we already drawn this frame?
	private boolean shown = false;
	// have we received data for this frame
	private boolean receivedData = false;
	// are we currently updating this frame?
	private AtomicBoolean updatingComponent = new AtomicBoolean(false);
	// are we rendering this component?
	private AtomicBoolean renderingComponent = new AtomicBoolean(false);

	private final String frameID;

	private Timer expirationTimer;

	private Background background;
	private Ad ad;
	private Location location;
	private CloseButton button;

	private RelativeLayout frameWrapper;
	private RelativeLayout imageContainer;
	private AdImageView backgroundView;
	private AdImageView adAreaView;
	private AdImageView closeButtonView;

	private int expirationSeconds;
	private Activity activity;
	private boolean adEnabledCode = false;
	private Map<String, Method> actions = null;

	private Map<String, Drawable> imagesByUrl = new HashMap<String, Drawable>();

	private static final String TAG = Frame.class.getSimpleName();

	public void registerAction(String name, Method method) {
		if (this.actions == null) {
			this.actions = new HashMap<String, Method>();
		}
		this.actions.put(name, method);
	}

	public Frame(String frameId, Activity context) {
		this.frameID = frameId;
		this.activity = context;
	}

	public void setEnableAdCode(boolean enable) {
		this.adEnabledCode = enable;
	}

	public Context getContext() {
		return this.activity;
	}

	public String getActivityName() {
		return this.activity.getClass().getName();
	}

	public String getFrameID() {
		return this.frameID;
	}

	private String getTagId() {
		return this.ad.getImageUrl() + this.frameID;
	}

	public void removeComponent() {
		if (this.imageContainer == null || this.frameWrapper == null
				|| this.adAreaView == null) {
			return;
		}

		this.imageContainer.removeAllViews();
		this.frameWrapper.removeAllViews();

		if (this.backgroundView != null && this.adAreaView != null) {

			this.backgroundView = null;
			this.adAreaView = null;
			this.closeButtonView = null;

			Activity parent = (Activity) activity;
			Window window = parent.getWindow();

			ViewGroup mainView = ((ViewGroup) window.findViewById(
					android.R.id.content).getParent());

			for (int i = 0; i < mainView.getChildCount(); i++) {
				ViewGroup view = (ViewGroup) mainView.getChildAt(i);

				if (view.getChildCount() > 0) {
					for (int j = 0; j < view.getChildCount(); j++) {

						if (view.getChildAt(j) != null) {

							if (view.getChildAt(j).getTag() != null
									&& view.getChildAt(j).getTag()
											.equals(this.getTagId())) {

								view.removeView(view.getChildAt(j));
								return;
							}
						}
					}
				}
			}
		}
	}

	public void refreshData(AdResponse data, Activity activity) {
		if (updatingComponent.compareAndSet(false, true)) {
			//update the activity
			this.activity = activity;
			this.removeComponent();
			this.background = data.getBackground();
			this.button = data.getCloseButton();
			this.location = data.getLocation();
			this.ad = data.getAds().iterator().next();
			this.expirationSeconds = data.getExpirationSeconds();
			this.receivedData = true;
			this.initAdComponents();
			updatingComponent.set(false);
		}
	}

	private void createImageContainers() {
		frameWrapper = new RelativeLayout(activity);
		RelativeLayout.LayoutParams frameParams = new RelativeLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		frameWrapper.setLayoutParams(frameParams);
		frameWrapper.setTag(this.getTagId());

		imageContainer = new RelativeLayout(activity);
		Coordinates coordinates = getBackgroundOrientation(background);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
				background.getWidth(), background.getHeight());
		params.leftMargin = coordinates.getX();
		params.topMargin = coordinates.getY();
		imageContainer.setLayoutParams(params);

		frameWrapper.addView(imageContainer);
	}

	private void initAdComponents() {
		this.backgroundView = new AdImageView(0, 0, background.getHeight(),
				background.getWidth(), background.getImageUrl(), this,
				activity, ImageType.BACKGROUND);
		int adX = this.location.getX();
		int adY = this.location.getY();

		this.adAreaView = new AdImageView(adX, adY, location.getHeight(),
				location.getWidth(), ad.getImageUrl(), this, activity,
				ImageType.CREATIVE);

		if (this.closeButtonView == null && button.hasImage()) {
			int closeX = button.getX();
			int closeY = button.getY();

			this.closeButtonView = new AdImageView(closeX, closeY,
					button.getHeight(), button.getWidth(), button.getImage(),
					this, activity, ImageType.CLOSE_BUTTON);
		} else {
			this.closeButtonView = null;
		}
	}

	private Coordinates getBackgroundOrientation(Background background) {
		if (background.getOrientation() == Orientation.LANDSCAPE)
			return background.getLandscape();

		if (background.getOrientation() == Orientation.PORTRAIT)
			return background.getPortrait();

		Configuration config = activity.getResources().getConfiguration();
		if (config.orientation == Configuration.ORIENTATION_LANDSCAPE)
			return background.getLandscape();

		return background.getPortrait();
	}

	public DisplayResult start() {
		this.shouldDisplay = true;
		if (this.allComponentsLoaded()) {
			this.render();
			return DisplayResult.DISPLAYED;
		} else {
			return DisplayResult.DISPLAY_PENDING;
		}
	}

	public boolean allComponentsLoaded() {
		if (!this.receivedData) {
			return false;
		}

		boolean backgroundReady = this.backgroundView != null
				&& this.backgroundView.getStatus() == AdImageView.ViewStatus.COMPLETED;

		boolean closeReady = !this.button.hasImage()
				|| (this.closeButtonView != null && this.closeButtonView
						.getStatus() == AdImageView.ViewStatus.COMPLETED);

		boolean adReady = this.adAreaView != null
				&& this.adAreaView.getStatus() == AdImageView.ViewStatus.COMPLETED;

		return backgroundReady && adReady && closeReady;
	}

	public void startExpiryTimer() {
		this.stopExpiryTimer();
		this.expirationTimer = new Timer();
		this.expirationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				notifyDelegate();
			}
		}, this.expirationSeconds * 1000);
	}

	public void stopExpiryTimer() {
		if (this.expirationTimer != null) {
			this.expirationTimer.cancel();
		}
	}

	@Override
	public void adViewClose() {
		this.stopExpiryTimer();
		removeComponent();
		Messaging.clearFrameFromActivity(activity, frameID);
	}

	@Override
	public void adViewClicked(MotionEvent event) {
		this.adClicked(event);
	}

	private void adClicked(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();
		String preExecuteUrl = this.ad.getPreExecutionUrl();
		String postExecuteUrl = this.ad.getPostExecutionUrl();
		String clickTarget = this.ad.getTargetUrlForClick();
		Ad.AdTargetType targetType = this.ad.getTargetType();

		if (clickTarget == null) {
			return;
		}

		Exception exec = null;
		int statusCode;

		if (targetType == Ad.AdTargetType.WEB) {
			Intent browserIntent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(clickTarget));
			this.activity.startActivity(browserIntent);

		} else if (targetType == Ad.AdTargetType.PNA
				|| targetType == Ad.AdTargetType.PNX) {

			if (preExecuteUrl != null)
				PlaynomicsSession.preExecution(preExecuteUrl, x, y);

			if (targetType == Ad.AdTargetType.PNA) {
				try {
					Messaging.performActionForLabel(activity, clickTarget);
					statusCode = PostExecutionEvent.Status.PNA_SUCCESS;
				} catch (Exception e) {
					statusCode = PostExecutionEvent.Status.PNA_EXCEPTION;
					exec = e;
				}
			} else {
				if (this.adEnabledCode) {
					try {
						Messaging
								.executeActionOnDelegate(activity, clickTarget);
						statusCode = PostExecutionEvent.Status.PNA_SUCCESS;
					} catch (Exception e) {
						statusCode = PostExecutionEvent.Status.PNX_EXCEPTION;
						exec = e;
					}
				} else {
					statusCode = PostExecutionEvent.Status.PNX_ACTIONS_NOT_ENABLED;
				}
			}

			if (postExecuteUrl != null) {
				PlaynomicsSession.postExecution(postExecuteUrl, statusCode,
						exec);
			}
		} else {
			// log error
		}
	}

	public void notifyDelegate() {
		Messaging.refreshWithId(activity, this.frameID);
	}

	@Override
	public void onAdViewLoaded() {
		if (renderingComponent.compareAndSet(false, true)) {
			// only let the render call happen once
			render();
			renderingComponent.set(false);
		}
	}

	private void render() {
		if (!(this.allComponentsLoaded() && this.shouldDisplay)) {
			return;
		}

		createImageContainers();
		this.imageContainer.addView(this.backgroundView);
		this.imageContainer.addView(this.adAreaView);

		if (closeButtonView != null) {
			this.imageContainer.addView(this.closeButtonView);
		}

		if (!this.shown) {
			// only log an impression once
			PlaynomicsSession.impression(this.ad.getImpressionUrl());
			this.shown = true;
		}

		this.startExpiryTimer();

		Activity parent = (Activity) this.activity;
		Window window = parent.getWindow();
		window.addContentView(this.frameWrapper, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	public void refreshView() {
		if (!updatingComponent.compareAndSet(false, true)) {
			updatingComponent.set(true);
			removeComponent();
			render();
			updatingComponent.set(false);
		}
	}
}

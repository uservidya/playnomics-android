package com.playnomics.playrm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
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
import com.playnomics.playrm.PlaynomicsSession.SessionState;

public class Frame {
	public enum DisplayResult {
		NO_INTERNET_PERMISSION, START_NOT_CALLED, UNABLE_TO_CONNECT, FAIL_UNKNOWN, DISPLAY_PENDING, DISPLAYED
	};

	private UUID frameUUID = UUID.randomUUID();

	// should we show the frame?
	private boolean shouldDisplay = false;
	// have we already drawn this frame?
	private boolean shown = false;
	// have we received data for this frame
	private boolean receivedData = false;
	// are we currently updating this frame?
	private AtomicBoolean updatingComponent = new AtomicBoolean(false);

	private final String frameID;

	private Timer expirationTimer;

	private AdRenderData renderData;

	private RelativeLayout frameWrapper;
	private RelativeLayout imageContainer;
	private AdImageView backgroundView;
	private AdImageView adAreaView;
	private AdImageView closeButtonView;

	private int expirationSeconds;
	private Activity activity;
	private boolean adEnabledCode = false;
	private Map<String, Method> actions = null;

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

	protected Context getContext() {
		return this.activity;
	}

	protected String getActivityName() {
		return this.activity.getClass().getName();
	}

	public String getFrameID() {
		return this.frameID;
	}

	private String getTagId() {
		return this.frameUUID.toString();
	}

	protected UUID getFrameCacheKey() {
		return this.frameUUID;
	}

	protected void removeComponent() {
		if (this.imageContainer == null || this.frameWrapper == null
				|| this.adAreaView == null) {
			// nothing to remove so just exit
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
							}
						}
					}
				}
			}
		}
	}

	protected void refreshData(AdRenderData data) {
		if (updatingComponent.compareAndSet(false, true)) {
			// only update the activity
			// when the data is valid
			if (isDataValid(data)) {
				this.renderData = data;
				this.expirationSeconds = data.getAdResponse()
						.getExpirationSeconds();
				this.removeComponent();
				this.receivedData = true;
				this.initAdComponents();
				this.shown = false;
				this.render(true);
			}
			updatingComponent.set(false);
		}
	}

	protected void updateContext(Activity activity) {
		if (updatingComponent.compareAndSet(false, true)) {
			removeComponent();
			this.activity = activity;
			initAdComponents();
			render(false);
			updatingComponent.set(false);
		}
	}

	private void createImageContainers() {
		Background background = renderData.getAdResponse().getBackground();

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

	private boolean isDataValid(AdRenderData data) {
		Background background = data.getAdResponse().getBackground();
		Drawable backgroundImage = data.getBackgroundImage();

		boolean backgroundIsValid = background.getImageUrl() == null
				|| backgroundImage != null;

		Ad ad = data.getAdResponse().getFirstAd();
		Drawable adImage = data.getAdImage();

		boolean adIsValid = ad != null && adImage != null;

		CloseButton button = data.getAdResponse().getCloseButton();
		Drawable buttonImage = data.getCloseButtonImage();

		boolean buttonIsValid = (button.getImageUrl() == null
				&& button.getHeight() > 0 && button.getWidth() > 0)
				|| buttonImage != null;

		return buttonIsValid && backgroundIsValid && adIsValid;
	}

	private void initAdComponents() {
		Background background = renderData.getAdResponse().getBackground();
		Drawable backgroundImage = renderData.getBackgroundImage();
		this.backgroundView = new AdImageView(0, 0, background.getHeight(),
				background.getWidth(), backgroundImage, this, activity,
				ImageType.BACKGROUND);

		Location location = renderData.getAdResponse().getLocation();
		Drawable adImage = renderData.getAdImage();

		this.adAreaView = new AdImageView(location.getX(), location.getY(),
				location.getHeight(), location.getWidth(), adImage, this,
				activity, ImageType.CREATIVE);

		Drawable buttonImage = renderData.getCloseButtonImage();
		if (buttonImage != null) {
			CloseButton button = renderData.getAdResponse().getCloseButton();
			this.closeButtonView = new AdImageView(button.getX(),
					button.getY(), button.getHeight(), button.getWidth(),
					buttonImage, this, activity, ImageType.CLOSE_BUTTON);
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
		if(!PlaynomicsSession.hasInternetPermission()){
			return DisplayResult.NO_INTERNET_PERMISSION;
		}
		
		if(PlaynomicsSession.getSessionState() != SessionState.STARTED){
			return DisplayResult.START_NOT_CALLED;
		}
	
		this.shouldDisplay = true;
		
		if (this.allComponentsLoaded()) {
			this.render(true);
			return DisplayResult.DISPLAYED;
		} else {
			return DisplayResult.DISPLAY_PENDING;
		}
	}

	private boolean allComponentsLoaded() {
		if (!this.receivedData) {
			return false;
		}

		boolean backgroundReady = this.backgroundView != null;
		boolean closeReady = !this.renderData.getAdResponse().getCloseButton()
				.hasImage()
				|| (this.closeButtonView != null);
		boolean adReady = this.adAreaView != null;
		return backgroundReady && adReady && closeReady;
	}

	private void startExpiryTimer() {
		this.stopExpiryTimer();
		this.expirationTimer = new Timer();
		this.expirationTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				notifyDelegate();
			}
		}, this.expirationSeconds * 1000);
	}

	private void stopExpiryTimer() {
		if (this.expirationTimer != null) {
			this.expirationTimer.cancel();
		}
	}

	protected void onAdViewClose() {
		closeFrame(true);
	}

	private void closeFrame(boolean logCloseAction) {
		this.stopExpiryTimer();

		if (logCloseAction) {
			String closeUrl = renderData.getAdResponse().getFirstAd()
					.getCloseUrl();
			if (closeUrl != null) {
				PlaynomicsSession.closeFrame(closeUrl);
			}
		}
		removeComponent();
		Messaging.clearFrameFromActivity(activity, frameID);
	}

	protected void onAdViewClicked(MotionEvent event) {
		this.adClicked(event);
	}

	private void adClicked(MotionEvent event) {
		int x = (int) event.getX();
		int y = (int) event.getY();

		Ad ad = renderData.getAdResponse().getFirstAd();

		String preExecuteUrl = ad.getPreExecutionUrl();
		String postExecuteUrl = ad.getPostExecutionUrl();
		String clickTarget = ad.getTargetUrlForClick();
		Ad.AdTargetType targetType = ad.getTargetType();

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

			if (preExecuteUrl != null) {
				PlaynomicsSession.preExecution(preExecuteUrl, x, y);
			}

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

		if (closeButtonView != null) {
			closeFrame(false);
		}
	}

	protected void notifyDelegate() {
		Messaging.refreshWithId(activity, this.frameID);
	}

	private void render(boolean resetUpdateTimer) {
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
			Ad ad = renderData.getAdResponse().getFirstAd();
			// only log an impression once
			PlaynomicsSession.impression(ad.getImpressionUrl());
			this.shown = true;
		}

		if (resetUpdateTimer) {
			this.startExpiryTimer();
		}

		Activity parent = this.activity;
		Window window = parent.getWindow();
		window.addContentView(this.frameWrapper, new LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}
}

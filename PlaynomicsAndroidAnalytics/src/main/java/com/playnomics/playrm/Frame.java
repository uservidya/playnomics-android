package com.playnomics.playrm;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.RelativeLayout;

import com.playnomics.playrm.AdView.ImageType;
import com.playnomics.playrm.AdView.ViewStatus;
import com.playnomics.playrm.Background.Orientation;

public class Frame implements AdEventHandler {
	
	
	public enum DisplayResult {
		NO_INTERNET_PERMISSION, START_NOT_CALLED, UNABLE_TO_CONNECT, FAIL_UNKNOWN, DISPLAY_PENDING, DISPLAYED
	};

	// should we draw/re-draw the frame?
	private boolean shouldDisplay = false;
	// have we already drawn this frame?
	private boolean shown = false;

	private boolean receivedData = false;

	private final String frameID;

	private Timer expirationTimer;

	private Background background;
	private Ad ad;
	private Location location;
	private CloseButton button;

	private AdView backgroundView;
	private AdView adAreaView;
	private AdView closeButtonView;

	private int expirationSeconds;
	private Context context;
	private String activityName;

	private boolean adEnabledCode = false;
	private int currentOrientation; 
	private Map<String, Method> actions = null;

	private static final String TAG = Frame.class.getSimpleName();

	public void registerAction(String name, Method method) {
		if (this.actions == null) {
			this.actions = new HashMap<String, Method>();
		}
		this.actions.put(name, method);
	}

	public Frame(String frameId, Context context) {
		this.frameID = frameId;
		this.context = context;
		this.activityName = this.context.getClass().getName();
	}

	public void updateContext(Context cont) {
		this.removeComponent();
		this.context = cont;
		this.initAdComponents();
	}

	public void setEnableAdCode(boolean enable) {
		this.adEnabledCode = enable;
	}

	public Context getContext() {
		return this.context;
	}

	public String getActivityName() {
		return this.activityName;
	}

	public String getFrameID() {
		return this.frameID;
	}

	private String getTagId() {
		return this.ad.getImageUrl() + this.frameID;
	}

	public void removeComponent() {
		Activity parent = (Activity) context;
		Window window = parent.getWindow();

		ViewGroup mainView = ((ViewGroup) window.findViewById(
				android.R.id.content).getParent());

		for (int i = 0; i < mainView.getChildCount(); i++) {
			ViewGroup view = (ViewGroup) mainView.getChildAt(i);

			if (view.getChildCount() > 0) {
				for (int j = 0; j < view.getChildCount(); j++) {

					if (view.getChildAt(j) != null && this.adAreaView != null) {

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

	public void refreshData(AdResponse data) {
		this.removeComponent();
		this.background = data.getBackground();
		this.button = data.getCloseButton();
		this.location = data.getLocation();
		this.ad = data.getAds().iterator().next();
		this.expirationSeconds = data.getExpirationSeconds();

		this.receivedData = true;

		this.initAdComponents();
	}

	private void initAdComponents() {
		Coordinates coordinates = getBackgroundOrientation(background);
		
		this.backgroundView = new AdView(coordinates.getX(),
				coordinates.getY(), background.getHeight(),
				background.getWidth(), background.getImageUrl(), this, context,
				ImageType.AD_BACKGROUND);

		int adX = coordinates.getX() + this.location.getX();
		int adY = coordinates.getY() + this.location.getY();

		this.adAreaView = new AdView(adX, adY, location.getHeight(),
				location.getWidth(), ad.getImageUrl(), this, context,
				ImageType.AD_CLICKED);

		if (button.hasImage()) {
			int closeX = coordinates.getX() + button.getX();
			int closeY = coordinates.getY() + button.getY();

			this.closeButtonView = new AdView(closeX, closeY,
					button.getHeight(), button.getWidth(), button.getImage(),
					this, context, ImageType.AD_CLOSED);
		} else {
			this.closeButtonView = null;
		}

		this.backgroundView.getLayout().setTag(this.getTagId());
	}

	private Coordinates getBackgroundOrientation(Background background) {
		
		if(background.getOrientation() == Orientation.LANDSCAPE)
			return background.getLandscape();
		
		if(background.getOrientation() == Orientation.PORTRAIT)
			return background.getPortrait();
		
		Configuration config = context.getResources().getConfiguration();
		if(config.orientation == Configuration.ORIENTATION_LANDSCAPE)
			return background.getLandscape();
		return background.getPortrait();
	}

	public DisplayResult start() {
		this.shouldDisplay = true;
		if (this.allComponentsLoaded()) {
			this.display();
			return DisplayResult.DISPLAYED;
		} else {
			return DisplayResult.DISPLAY_PENDING;
		}
	}

	public boolean allComponentsLoaded() {
		if(!this.receivedData) {
			return false;
		}
		
		boolean backgroundReady = this.backgroundView != null
				&& this.backgroundView.getStatus() == ViewStatus.COMPLETED;
		
		boolean closeReady =  !this.button.hasImage()
				|| (this.closeButtonView != null && this.closeButtonView
						.getStatus() == ViewStatus.COMPLETED);
		
		boolean adReady = this.adAreaView != null
				&& this.adAreaView.getStatus() == ViewStatus.COMPLETED;

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
	public void baseAdComponentClose() {

		this.stopExpiryTimer();

		Activity parent = (Activity) context;
		Window window = parent.getWindow();
		ViewGroup mainView = ((ViewGroup) window.findViewById(
				android.R.id.content).getParent());

		// need to do checks and see if there are groups inside groups with
		// views inside views
		for (int i = 0; i < mainView.getChildCount(); i++) {
			ViewGroup v = (ViewGroup) mainView.getChildAt(i);
			if (v.getChildCount() > 0) {
				for (int j = 0; j < v.getChildCount(); j++) {

					if (v.getChildAt(j).getTag() != null
							&& v.getChildAt(j).getTag().equals(this.getTagId())) {

						v.removeViewAt(j);
						Messaging.clearFrameFromContext(context, frameID);
						return;

					}
				}
			}
		}
	}

	@Override
	public void baseAdComponentOpen(MotionEvent event) {
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
			this.context.startActivity(browserIntent);

		} else if (targetType == Ad.AdTargetType.PNA
				|| targetType == Ad.AdTargetType.PNX) {

			if (preExecuteUrl != null)
				PlaynomicsSession.preExecution(preExecuteUrl, x, y);

			if (targetType == Ad.AdTargetType.PNA) {
				try {
					Messaging.performActionForLabel(context, clickTarget);
					statusCode = PostExecutionEvent.Status.PNA_SUCCESS;
				} catch (Exception e) {
					statusCode = PostExecutionEvent.Status.PNA_EXCEPTION;
					exec = e;
				}
			} else {
				if (this.adEnabledCode) {
					try {
						Messaging.executeActionOnDelegate(context, clickTarget);
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
		Messaging.refreshWithId(context, this.frameID);
	}

	@Override
	public void baseAdComponentReady() {
		if (this.allComponentsLoaded()) {
			this.backgroundView.addChildView(this.adAreaView);
			if (closeButtonView != null) {
				this.backgroundView.addChildView(this.closeButtonView);
			}

			if (this.shouldDisplay) {
				this.display();
			}
		}
	}

	public void display() {
		if (!this.shown) {
			PlaynomicsSession.impression(this.ad.getImpressionUrl());
			this.shown = true;
		}

		this.startExpiryTimer();
		
		Activity parent = (Activity) this.context;
		Window window = parent.getWindow();
		window.addContentView(this.backgroundView.getLayout(),
				new LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,
						RelativeLayout.LayoutParams.WRAP_CONTENT));
	}
}

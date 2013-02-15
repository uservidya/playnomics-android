package com.playnomics.playrm;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.RelativeLayout;

public class BaseAdComponent {

	protected enum AdComponentStatus {
		adComponentStatusPending, adComponentStatusCompleted, adComponentStatusError
	};

	protected enum ImageType {
		adClicked, adClosed, adBackground
	};

	public RelativeLayout layout;
	public View view;

	public JSONObject properties = null;
	public String imageUrl = null;
	public ImageView imageUI = null;
	public BaseAdComponent parentComponent = null;
	public Frame frame = null;

	public AdComponentStatus status;
	public int xOffset;
	public int yOffset;
	public int height;
	public int width;

	public ImageType imageType;

	private BaseAdComponentInterface delegate;
	private Bitmap image;

	private GifWebView gifWebView;

	public ArrayList<BaseAdComponent> subComponents;
	private ResourceBundle resourceBundle;
	private Context context;
	private int orient;

	public BaseAdComponent(JSONObject jObj, Frame frm, Context cont,
			ImageType imageType) {
		this.properties = jObj;
		this.frame = frm;

		this.subComponents = new ArrayList<BaseAdComponent>();
		this.resourceBundle = PlaynomicsSession.getResourceBundle();
		this.context = cont;
		this.imageType = imageType;
		this.delegate = frm;
	}

	public void layoutComponents(int x, int y) {
		this.initCoordinateValues(x, y);
		this.createComponentView();

		// make sure image url is not null
		if (this.imageUrl != null && this.imageUrl.length() > 0
				&& this.image == null && !this.imageUrl.equals("null"))
			this.startImageDownload();
	}

	private void initCoordinateValues(int x, int y) {
		try {
			this.imageUrl = this.properties.getString(this.resourceBundle
					.getString("frameResponseImageUrl"));
			this.height = this.properties.getInt(this.resourceBundle
					.getString("frameResponseHeight"));
			this.width = this.properties.getInt(this.resourceBundle
					.getString("frameResponseWidth"));

			// no sense getting image if it has 0 height or width
			if (this.width == 0 || this.height == 0)
				this.imageUrl = null;

			JSONObject coOrdinateProps = this.extractCoordinateProps();
			this.xOffset = coOrdinateProps.getInt(this.resourceBundle
					.getString("frameResponseXOffset"));
			this.yOffset = coOrdinateProps.getInt(this.resourceBundle
					.getString("frameResponseYOffset"));
			this.xOffset += x;
			this.yOffset += y;

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private JSONObject extractCoordinateProps() {
		JSONObject jObj = this.properties;

		try {

			if (this.properties.getJSONObject(this.resourceBundle
					.getString("frameResponseBackground_Landscape")) == null) {
				return jObj;
			}

			if (orient != context.getResources().getConfiguration().orientation) {
				orient = context.getResources().getConfiguration().orientation;
			}

			switch (orient) {
			case Configuration.ORIENTATION_LANDSCAPE:
				return this.properties.getJSONObject(this.resourceBundle
						.getString("frameResponseBackground_Landscape"));
			case Configuration.ORIENTATION_PORTRAIT:
				return this.properties.getJSONObject(this.resourceBundle
						.getString("frameResponseBackground_Portrait"));
			default:
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return jObj;
	}

	private void createComponentView() {
		if (this.imageUI == null) {
			this.imageUI = new ImageView(this.context);
			this.layout = new RelativeLayout(this.context);
			this.layout.setLayoutParams(new LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		}
	}

	private void startImageDownload() {

		if (this.imageUrl != null) {
			if (this.imageUrl.endsWith(".gif")) {
				this.gifWebView = new GifWebView(this.context, this.imageUrl);
				this.handleGifDownload(true);
			} else {

				AsyncTask<Void, Void, Boolean> task = new AsyncTask<Void, Void, Boolean>() {

					@Override
					protected Boolean doInBackground(Void... params) {
						try {
							URL url = new URL(imageUrl);
							URLConnection conn = url.openConnection();
							image = BitmapFactory.decodeStream(conn
									.getInputStream());
							return true;
						} catch (IOException e) {
							e.printStackTrace();
							return false;
						}
					}

					protected void onPostExecute(Boolean result) {
						handleImageDownload(result);
					}
				};
				task.execute((Void) null);
			}
		}
	}

	private void handleGifDownload(boolean result) {
		if (result) {
			LayoutParams layoutParams = new LayoutParams(this.width,
					this.height);
			layoutParams.setMargins(this.xOffset, this.yOffset, 0, 0);
			this.gifWebView.setHorizontalScrollBarEnabled(false);
			this.gifWebView.setVerticalScrollBarEnabled(false);
			this.gifWebView.setOnTouchListener(new WebView.OnTouchListener() {

				@Override
				public boolean onTouch(View v, MotionEvent event) {

					if (event.getAction() == MotionEvent.ACTION_UP) {
						delegate.baseAdComponentOpen(event);
					}

					return false;
				}
			});

			this.status = AdComponentStatus.adComponentStatusCompleted;
			this.gifWebView.setLayoutParams(layoutParams);
			this.layout.addView(this.gifWebView);
			this.delegate.baseAdComponentReady();
		}
	}

	private void handleImageDownload(boolean result) {
		if (result) {
			this.imageUI.setImageBitmap(this.image);
			this.finishImageSetup();
		}
	}

	private void finishImageSetup() {

		this.imageUI.setOnClickListener(new ImageView.OnClickListener() {
			public void onClick(View v) {
				if (imageType == ImageType.adClosed) {
					// Close button has been clicked, should remove the add here
					delegate.baseAdComponentClose();
				}
			}
		});

		LayoutParams layoutParams = new LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		layoutParams.setMargins(this.xOffset, this.yOffset, 0, 0);
		this.imageUI.setLayoutParams(layoutParams);
		this.layout.addView(this.imageUI);
		this.status = AdComponentStatus.adComponentStatusCompleted;
		this.delegate.baseAdComponentReady();
	}

	public void setUpLayoutParameters() {
		this.layout = new RelativeLayout(this.context);
		this.layout.setLayoutParams(new LayoutParams(this.width, this.height));
	}

	public void addSubComponent(BaseAdComponent subComponent) {
		subComponent.parentComponent = this;
		this.subComponents.add(subComponent);
		this.layout.addView(subComponent.layout);
	}
}

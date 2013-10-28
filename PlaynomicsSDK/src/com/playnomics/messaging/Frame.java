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
import com.playnomics.messaging.ui.PlayWebView;
import com.playnomics.messaging.ui.RenderTaskFactory;
import com.playnomics.sdk.IPlaynomicsFrameDelegate;
import com.playnomics.session.ICallbackProcessor;
import com.playnomics.util.Logger;
import com.playnomics.util.Util;
import com.playnomics.util.Logger.LogLevel;

public class Frame implements PlayWebView.IPlayWebViewHandler{
	
	public interface IFrameStateObserver{
		void onFrameShown(Activity activity, Frame frame);
		
		void onFrameDisposed(Activity activity);
	}
	
	public enum FrameState{
		NOT_LOADED,
		LOAD_STARTED,
		LOAD_COMPLETE,
		LOAD_FAILED
	}
	
	private boolean shouldRender;
	private boolean impressionLogged;
	
	private Util util;
	private IPlaynomicsFrameDelegate delegate;
	private Activity activity;
	private ICallbackProcessor callbackProcessor;
	private Logger logger;
	private IFrameStateObserver observer;
	private RenderTaskFactory renderTaskFactory;
	
	private PlayDialog dialog;
	private PlayWebView webView;
	private ImageView imageView;

	private String frameId;
	public String getFrameId(){
		return frameId;
	}
	
	private FrameState state;
	public FrameState getState(){
		return state;
	}
	
	public void setState(FrameState state){
		this.state = state;
	}
	
	private boolean hasNativeCloseButton(){
		return htmlAd != null && htmlAd.getCloseButton() instanceof NativeCloseButton;
	}
	
	private HtmlAd htmlAd;
	
	public Frame(String frameId, ICallbackProcessor callbackProcessor, Util util, Logger logger, IFrameStateObserver observer, RenderTaskFactory renderTaskFactory){
		this.observer = observer;
		this.frameId = frameId;
		this.state = FrameState.NOT_LOADED;
		this.callbackProcessor = callbackProcessor;
		this.util = util;
		this.logger = logger;
		this.renderTaskFactory = renderTaskFactory;
	}

	public void updateFrameData(HtmlAd htmlAd){
		this.htmlAd = htmlAd;
		state = FrameState.LOAD_COMPLETE;
		impressionLogged = false;
		if(shouldRender){
			loadWebView();
		}
	}
	
	public void show(final Activity activity, IPlaynomicsFrameDelegate delegate){
		this.delegate = delegate;
		this.activity = activity;

		shouldRender = true;
		
		if(state == FrameState.LOAD_COMPLETE){
			loadWebView();
		} else if (state == FrameState.LOAD_FAILED) {
			if(delegate != null){
				delegate.onRenderFailed();
			}
		}
	}
	
	private void loadWebView(){
		if(!(shouldRender && state == FrameState.LOAD_COMPLETE)){ return; }
		
		Runnable renderTask = renderTaskFactory.createRenderTask(this, htmlAd, activity, this, observer);
		//make sure we run this task on the UI thread
		activity.runOnUiThread(renderTask);
	}
	
	public void hide(){
		onAdClosed(false);
	}
	
	public void onLoadFailure(int errorCode) {
		onLoadFailure();
	}
	
	private void onLoadFailure(){
		state = FrameState.LOAD_FAILED;
		if(delegate != null){
			delegate.onRenderFailed();
		}
	}

	public void onLoadComplete() {
		state = FrameState.LOAD_COMPLETE;
		if(htmlAd.getPosition().getPositionType() == PositionType.FULLSCREEN){
			PlayDialog dialog = new PlayDialog(activity, webView);
			dialog.show();
		}
		
		if(!impressionLogged){
			impressionLogged = false;
			callbackProcessor.processUrlCallback(htmlAd.getImpressionUrl());
		}
		
		if(delegate != null){
			delegate.onShow(htmlAd.getTarget().getTargetData());
		}
	}

	public void onUrlLoading(String url) {
		hide();
		if(!Util.stringIsNullOrEmpty(url)){
			if(!hasNativeCloseButton()){
				HtmlCloseButton htmlClose = (HtmlCloseButton)htmlAd.getCloseButton();
				if(url.equals(htmlClose.getCloseLink())){
					onAdClosed(true);
					return;
				}
			}
			
			if(url.equals(htmlAd.getClickLink())){
				onAdTouched();
				return;
			}
			util.openUrlInPhoneBrowser(url, activity);
		}	
	}

	private void onAdTouched() {
		callbackProcessor.processUrlCallback(htmlAd.getClickUrl());
		Target target = htmlAd.getTarget();
		
		if(target.getTargetType() == TargetType.URL && Util.stringIsNullOrEmpty(target.getTargetUrl())){
			util.openUrlInPhoneBrowser(target.getTargetUrl(), activity);
		}
	 
		if(delegate != null){
			delegate.onTouch(htmlAd.getTarget().getTargetData());
		}
	}

	private void onAdClosed(boolean closedByUser) {
		if(dialog != null){
			dialog.dismiss();
		}
		
		observer.onFrameDisposed(activity);
		if(closedByUser){
			callbackProcessor.processUrlCallback(htmlAd.getCloseUrl());
		}
		if(delegate != null){
			delegate.onClose(htmlAd.getTarget().getTargetData());
		}
	}
	
	public void attachActivity(Activity activity){
		this.activity = activity;
		loadWebView();
	}
	
	public void detachActivity(){
		if(dialog != null){
			dialog.dismiss();
		}
		this.activity = null;
	}
}
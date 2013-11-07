package com.playnomics.android.messaging.ui;
import com.playnomics.android.messaging.Placement.IPlacementStateObserver;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class PlayDialog extends Dialog {
	private PlayWebView webView;
	private ImageView nativeCloseButton;
	private Activity activity;
	private IPlacementStateObserver observer;
	
	public PlayDialog(Context context, PlayWebView webView, final IPlacementStateObserver observer, Activity activity) {
		super(context);
		
		this.webView = webView;
		this.activity = activity;
		this.observer = observer;
	}
	
	public PlayDialog(Context context, PlayWebView webView, final IPlacementStateObserver observer, Activity activity, ImageView nativeCloseButton) {
		this(context, webView, observer, activity);
		this.nativeCloseButton = nativeCloseButton;
	}
	
	final int PADDING = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		Window window = getWindow();
		window.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
		//make the dialog window fullscreen
		window.setLayout(WindowManager.LayoutParams.FILL_PARENT, WindowManager.LayoutParams.FILL_PARENT);
		setCanceledOnTouchOutside(false);
		
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.FILL_PARENT);
		addContentView(webView, layoutParams);
		
		if(nativeCloseButton != null){
			RelativeLayout.LayoutParams params = new LayoutParams(nativeCloseButton.getWidth(), nativeCloseButton.getHeight());
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.ALIGN_PARENT_RIGHT);
			addContentView(nativeCloseButton, params);
		}
	}
}

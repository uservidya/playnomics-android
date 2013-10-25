package com.playnomics.messaging.ui;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class PlayDialog extends Dialog {
	public PlayDialog(Context context, PlayWebView webView) {
		super(context);
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
	}
	
	public void showWebView(PlayWebView webView){
		showWebView(webView, null);
	}
	
	public void showWebView(PlayWebView webView, ImageView closeButton){
		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.FILL_PARENT, 
				LayoutParams.FILL_PARENT);
		addContentView(webView, layoutParams);
		
		if(closeButton != null){
			RelativeLayout.LayoutParams params = new LayoutParams(closeButton.getWidth(), closeButton.getHeight());
			params.addRule(RelativeLayout.ALIGN_PARENT_TOP | RelativeLayout.ALIGN_PARENT_RIGHT);
			addContentView(closeButton, params);
		}
		show();
	}
}

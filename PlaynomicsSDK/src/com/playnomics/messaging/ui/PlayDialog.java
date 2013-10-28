package com.playnomics.messaging.ui;
import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

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
}

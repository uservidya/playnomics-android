package com.playnomics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.playnomics.api.PlaynomicsSession;


public class PlaynomicsTestAppActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
	}

	@Override
	protected void onStart() {
	
		super.onStart();
		Toast.makeText(this, "SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this), Toast.LENGTH_LONG).show();
	};
	
	public void onSwitchActivityClick(View view) {
		
        finish();
	}
}

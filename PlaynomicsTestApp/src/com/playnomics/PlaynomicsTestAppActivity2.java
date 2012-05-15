package com.playnomics;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.playnomics.analytics.PlaynomicsSession;


public class PlaynomicsTestAppActivity2 extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);
	}

	@Override
	protected void onStart() {
	
		super.onStart();
		PlaynomicsSession.switchActivity(this);
	};
	
	public void onSwitchActivityClick(View view) {
        Intent myIntent = new Intent(view.getContext(), PlaynomicsTestAppActivity.class);
        startActivityForResult(myIntent, 0);		
	}
}

package com.playnomics;

import java.util.Date;
import java.util.GregorianCalendar;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.playnomics.android.sdk.Playnomics;
import com.playnomics.android.util.Logger.LogLevel;

public class PlaynomicsTestAppActivity extends Activity {
	
	private static boolean preloaded = false;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		final long applicationId = 2143315484923938870L;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Playnomics.setLogLevel(LogLevel.VERBOSE);
		//Playnomics.setTestMode(false);
		Playnomics.start(this, applicationId);
		
		if(!preloaded){
			//only preload once
			Playnomics.preloadPlacements("44841d6a2bcec8c9", "a40893b36c6ddb32", "67dbfcad37eccbf9", "5bc049bb66ffc121", "e45c59f627043701");
			preloaded = true;
		}
	}
	
	@Override
	protected void onResume() {
		Playnomics.onActivityResumed(this);
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		Playnomics.onActivityPaused(this);
		super.onPause();
	}

	public void onUserInfoClick(View view) {
		String source = "source";
		String campaign = "campaign";
		GregorianCalendar cal = new GregorianCalendar();
		Date installDate = cal.getTime();
		
		Playnomics.attributeInstall(source, campaign, installDate);
	}
	
	public void onTransactionClick(View view) {
		float price = 0.99f;
		int quantity = 1;
		Playnomics.transactionInUSD(price, quantity);
	}

	public void onMilestoneClick(View view) {
		String eventName = "my event";
		Playnomics.customEvent(eventName);
	}

	public void onHttpClick(View view){
		setupPlacement("44841d6a2bcec8c9");
	}
	
	public void onJsonClick(View view){
		setupPlacement("a40893b36c6ddb32");
	}
	
	public void onNullTargetClick(View view){
		setupPlacement("67dbfcad37eccbf9");
	}
	
	public void onNoAdsClick(View view){
		setupPlacement("5bc049bb66ffc121");
	}
	
	public void onThirdPartyAdClick(View view){
 		setupPlacement("e45c59f627043701");
	}
	
	private void setupPlacement (String placementName){
		RichDataFrameDelegate delegate = new RichDataFrameDelegate(placementName, getApplicationContext());
		Playnomics.showPlacement(placementName, this, delegate);
	}
}
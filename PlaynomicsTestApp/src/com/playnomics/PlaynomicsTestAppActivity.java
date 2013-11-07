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
	
	private String logTag = PlaynomicsTestAppActivity.class.getSimpleName();
	private final long applicationId = 2143315484923938870L;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		Playnomics.setLogLevel(LogLevel.VERBOSE);
		Playnomics.setOverrideEventsUrl("https://e.c.playnomics.net/v1/");
		Playnomics.setOverrideMessagingUrl("https://ads.c.playnomics.net/v3/");
		Playnomics.start(this, applicationId);
		Playnomics.preloadPlacements("44841d6a2bcec8c9", "a40893b36c6ddb32", "67dbfcad37eccbf9", "5bc049bb66ffc121");
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
	
	private void setupPlacement (String placementName){
		RichDataFrameDelegate delegate = new RichDataFrameDelegate(placementName);
		Playnomics.showPlacement(placementName, this, delegate);
	}
}
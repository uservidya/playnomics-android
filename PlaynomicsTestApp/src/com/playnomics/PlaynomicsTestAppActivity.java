package com.playnomics;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.playnomics.playrm.PlaynomicsConstants.CurrencyCategory;
import com.playnomics.playrm.PlaynomicsConstants.CurrencyType;
import com.playnomics.playrm.PlaynomicsConstants.ResponseType;
import com.playnomics.playrm.PlaynomicsConstants.TransactionType;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoSex;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoSource;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoType;
import com.playnomics.playrm.Frame;
import com.playnomics.playrm.Messaging;
import com.playnomics.playrm.PlaynomicsSession;

public class PlaynomicsTestAppActivity extends Activity {
	
	private String logTag = PlaynomicsTestAppActivity.class.getSimpleName();
	private final long applicationId = 2143315484923938870L;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		PlaynomicsSession.setOverrideEventsUrl("https://e.c.playnomics.net/v1/");
		PlaynomicsSession.setOverrideMessagingUrl("https://ads.c.playnomics.net/v1/ads");
		
		Messaging.setup(this);
		Log.d(logTag, "START: " + PlaynomicsSession.start(this, applicationId).toString());
	}
	
	@Override
	protected void onStart() {
		Log.d(logTag, "SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this));
		super.onStart();
	};
	
	@Override
	protected void onDestroy() {
		PlaynomicsSession.stop();
		super.onDestroy();
	}

	
	public void onUserInfoClick(View view) {
		Log.d(logTag, "USER INFO: " +PlaynomicsSession.userInfo(UserInfoType.update, "USA", "test", UserInfoSex.Male, new Date("1/1/1999"),
				UserInfoSource.Other, "test", new Date()).toString());
	}
	
	public void onTransactionClick(View view) {
		String[] currencyTypes = { CurrencyType.USD.toString(), CurrencyType.OFF.toString() };
		double[] currencyValues = { 1, 2 };
		CurrencyCategory[] currencyCategories = { CurrencyCategory.Real, CurrencyCategory.Virtual };
	
		Log.d(logTag, "TRANSACTION: " +
				PlaynomicsSession.transaction(1234567890L, "TEST_ITEM_ID", null, TransactionType.BuyItem, "TEST_USER_ID",
						currencyTypes, currencyValues, currencyCategories).toString());
	}

	public void onMilestoneClick(View view) {
		Log.d(logTag, "TEST_MILESTONE: "+ PlaynomicsSession.milestone(1L, "CUSTOM1").toString());
		Log.d(logTag, "TEST_MILESTONE: "+ PlaynomicsSession.milestone(2L, "CUSTOM2").toString());
		Log.d(logTag, "TEST_MILESTONE: "+ PlaynomicsSession.milestone(3L, "CUSTOM3").toString());
		Log.d(logTag, "TEST_MILESTONE: "+ PlaynomicsSession.milestone(4L, "CUSTOM4").toString());
		Log.d(logTag, "TEST_MILESTONE: "+ PlaynomicsSession.milestone(5L, "CUSTOM5").toString());
	}

	public void onHttpClick(View view){
		setupFrame("44841d6a2bcec8c9");
	}
	
	public void onJsonClick(View view){
		setupFrame("a40893b36c6ddb32");
	}
	
	public void onNullTargetClick(View view){
		setupFrame("67dbfcad37eccbf9");
	}
	
	public void onNoAdsClick(View view){
		setupFrame("5bc049bb66ffc121");
	}
	
	public void onPnxClick(View view){
		setupFrame("e45c59f627043701");
	}
	
	public void onPnx(){
		//callback for pnx
		Log.d(logTag, "PNX Called!");
	}
	
	private void setupFrame (String frameId){
		RichDataFrameDelegate delegate = new RichDataFrameDelegate(frameId);
		Frame frame = Messaging.initWithFrameID(frameId, this, delegate);
		frame.setEnableAdCode(true);
		frame.start();
	}
}
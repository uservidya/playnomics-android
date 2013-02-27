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
import com.playnomics.playrm.PlaynomicsSession;

public class PlaynomicsTestAppActivity extends Activity {
	
	private String logTag = PlaynomicsTestAppActivity.class.getSimpleName();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		PlaynomicsSession.setTestMode(true);
		
//		Toast.makeText(this, "START: " + PlaynomicsSession.start(this, 3L, "testUserId").toString(),
//		Toast.LENGTH_LONG)
//		.show();
		System.out.println("START: " + PlaynomicsSession.start(this, 3L, "testUserId").toString());
	}
	
	@Override
	protected void onStart() {
		super.onStart();
//		Toast.makeText(this, "SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this), Toast.LENGTH_LONG).show();
		System.out.println("SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this));
	};
	
	@Override
	protected void onDestroy() {
		PlaynomicsSession.stop();
		super.onDestroy();
	}
	
	public void onChangeUserClick(View view) {
//		Toast.makeText(this, "CHANGE USER: " +
//			PlaynomicsSession.changeUser("testChangeUserId").toString(), Toast.LENGTH_LONG).show();
		System.out.println("CHANGE USER: " +PlaynomicsSession.changeUser("testChangeUserId").toString());
	}
	
	public void onUserInfoClick(View view) {
//		Toast.makeText(this, "USER INFO: " +
//			PlaynomicsSession.userInfo(UserInfoType.update, "USA", "test", UserInfoSex.Male, new Date("1/1/1999"),
//				UserInfoSource.Other, "test", new Date()).toString(), Toast.LENGTH_LONG).show();
		System.out.println("USER INFO: " +PlaynomicsSession.userInfo(UserInfoType.update, "USA", "test", UserInfoSex.Male, new Date("1/1/1999"),
				UserInfoSource.Other, "test", new Date()).toString());
	}
	
	public void onSessionStartClick(View view) {
//		Toast.makeText(this,
//			"SESSION START: " + PlaynomicsSession.sessionStart(3L, "TEST_SITE").toString(),
//			Toast.LENGTH_LONG).show();
		System.out.println("SESSION START: " + PlaynomicsSession.sessionStart(3L, "TEST_SITE").toString());
	}
	
	public void onSessionEndClick(View view) {
//		Toast.makeText(this, "SESSION END: " + PlaynomicsSession.sessionEnd(3L, "QUIT").toString(),
//			Toast.LENGTH_LONG)
//			.show();
		System.out.println("SESSION END: " + PlaynomicsSession.sessionEnd(3L, "QUIT").toString());
	}
	
	public void onGameStartClick(View view) {
//		Toast.makeText(this, "GAME START: "+PlaynomicsSession.gameStart(3L, 3L, "TEST_SITE", "TEST_TYPE","TEST_GAME").toString(), Toast.LENGTH_LONG).show();
		System.out.println("GAME START: "+PlaynomicsSession.gameStart(3L, 3L, "TEST_SITE", "TEST_TYPE", "TEST_GAME").toString());
	}
	
	public void onGameEndClick(View view) {
//		Toast.makeText(this,
//			"GAME END: " + PlaynomicsSession.gameEnd(3L, 3L, "LOSE").toString(),
//			Toast.LENGTH_LONG).show();
		System.out.println("GAME END: " + PlaynomicsSession.gameEnd(3L, 3L, "LOSE").toString());
	}
	
	public void onTransactionClick(View view) {
		String[] currencyTypes = { CurrencyType.USD.toString(), CurrencyType.OFF.toString() };
		double[] currencyValues = { 1, 2 };
		CurrencyCategory[] currencyCategories = { CurrencyCategory.Real, CurrencyCategory.Virtual };
		
//		Toast.makeText(this, "TRANSACTION: " +
//			PlaynomicsSession.transaction(1234567890L, "TEST_ITEM_ID", 1, TransactionType.BuyItem, "TEST_USER_ID",
//				currencyTypes, currencyValues, currencyCategories).toString(),
//			Toast.LENGTH_LONG).show();
		System.out.println("TRANSACTION: " +
				PlaynomicsSession.transaction(1234567890L, "TEST_ITEM_ID", 1, TransactionType.BuyItem, "TEST_USER_ID",
						currencyTypes, currencyValues, currencyCategories).toString());
	}
	
	public void onInvitationSentClick(View view) {
//		Toast.makeText(
//			this,
//			"INVITATION SENT: "
//				+ PlaynomicsSession.invitationSent(3L, "TEST_RECIPIENT", "TEST_ADDRESS", "TEST_METHOD")
//					.toString(),
//			Toast.LENGTH_LONG).show();
		Log.d(logTag, "INVITATION SENT: "
				+ PlaynomicsSession.invitationSent(3L, "TEST_RECIPIENT", "TEST_ADDRESS", "TEST_METHOD")
				.toString());
	}
	
	public void onInvitationResponseClick(View view) {
//		Toast.makeText(
//			this,
//			"INVITATION RESPONSE: "
//				+ PlaynomicsSession.invitationResponse(3L, "TEST_RECIPIENT",ResponseType.accepted).toString(),
//			Toast.LENGTH_LONG).show();
		Log.d(logTag, "INVITATION RESPONSE: "
				+ PlaynomicsSession.invitationResponse(3L, "TEST_RECIPIENT",ResponseType.accepted).toString());
	}
	
	public void onMilestoneClick(View view) {
//		Toast.makeText(
//			this,
//			"TEST_MILESTONE: "
//				+ PlaynomicsSession.milestone(3L, "TEST_MILESTONE").toString(),
//			Toast.LENGTH_LONG).show();
		Log.d(logTag, "INVITATION RESPONSE: "
				+ PlaynomicsSession.invitationResponse(3L, "TEST_RECIPIENT",ResponseType.accepted).toString());
		
		System.out.println("TEST_MILESTONE: "+ PlaynomicsSession.milestone(3L, "TEST_MILESTONE").toString());
	}
	
	public void onSwitchActivityClick(View view) {
		Intent myIntent = new Intent(this, PlaynomicsTestAppActivity2.class);
		startActivity(myIntent);
	}
	
	public void onCloseClick(View view) {
		finish();
	}
	
}
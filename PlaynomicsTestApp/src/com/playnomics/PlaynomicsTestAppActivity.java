package com.playnomics;

import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.playnomics.api.PlaynomicsConstants.CurrencyCategory;
import com.playnomics.api.PlaynomicsConstants.CurrencyType;
import com.playnomics.api.PlaynomicsConstants.ResponseType;
import com.playnomics.api.PlaynomicsConstants.TransactionType;
import com.playnomics.api.PlaynomicsConstants.UserInfoSex;
import com.playnomics.api.PlaynomicsConstants.UserInfoSource;
import com.playnomics.api.PlaynomicsConstants.UserInfoType;
import com.playnomics.api.PlaynomicsSession;

public class PlaynomicsTestAppActivity extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
	
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		PlaynomicsSession.setTestMode(true);
		Toast.makeText(this, "START: " + PlaynomicsSession.start(this, 3L, "testUserId").toString(),
			Toast.LENGTH_LONG)
			.show();
	}
	
	@Override
	protected void onStart() {
	
		super.onStart();
		Toast.makeText(this, "SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this), Toast.LENGTH_LONG).show();
	};
	
	@Override
	protected void onDestroy() {
	
		PlaynomicsSession.stop();
		super.onDestroy();
	}
	
	public void onUserInfoClick(View view) {
	
		Toast.makeText(this, "USER INFO: " +
			PlaynomicsSession.userInfo(UserInfoType.update, "USA", "test", UserInfoSex.Male, new Date("1/1/1999"),
				UserInfoSource.Other, "test", new Date()).toString(), Toast.LENGTH_LONG).show();
	}
	
	public void onSessionStartClick(View view) {
	
		Toast.makeText(this,
			"SESSION START: " + PlaynomicsSession.sessionStart(3L, "TEST_SITE").toString(),
			Toast.LENGTH_LONG).show();
	}
	
	public void onSessionEndClick(View view) {
	
		Toast.makeText(this, "SESSION END: " + PlaynomicsSession.sessionEnd(3L, "QUIT").toString(),
			Toast.LENGTH_LONG)
			.show();
	}
	
	public void onGameStartClick(View view) {
	
		Toast.makeText(
			this,
			"GAME START: "
				+
				PlaynomicsSession.gameStart(3L, 3L, "TEST_SITE", "TEST_TYPE",
					"TEST_GAME")
					.toString(), Toast.LENGTH_LONG).show();
	}
	
	public void onGameEndClick(View view) {
	
		Toast.makeText(this,
			"GAME END: " + PlaynomicsSession.gameEnd(3L, 3L, "LOSE").toString(),
			Toast.LENGTH_LONG).show();
	}
	
	public void onTransactionClick(View view) {
	
		String[] currencyTypes = { CurrencyType.USD.toString(), CurrencyType.OFF.toString() };
		double[] currencyValues = { 1, 2 };
		CurrencyCategory[] currencyCategories = { CurrencyCategory.Real, CurrencyCategory.Virtual };
		
		Toast.makeText(this, "TRANSACTION: " +
			PlaynomicsSession.transaction(1234567890L, "TEST_ITEM_ID", 1, TransactionType.BuyItem, "TEST_USER_ID",
				currencyTypes, currencyValues, currencyCategories).toString(),
			Toast.LENGTH_LONG).show();
	}
	
	public void onInvitationSentClick(View view) {
	
		Toast.makeText(
			this,
			"INVITATION SENT: "
				+ PlaynomicsSession.invitationSent(3L, "TEST_USER_ID", "TEST_ADDRESS", "TEST_METHOD")
					.toString(),
			Toast.LENGTH_LONG).show();
	}
	
	public void onInvitationResponseClick(View view) {
	
		Toast.makeText(
			this,
			"INVITATION RESPONSE: "
				+ PlaynomicsSession.invitationResponse(3L, ResponseType.accepted).toString(),
			Toast.LENGTH_LONG).show();
	}
	
	public void onSwitchActivityClick(View view) {
	
		Intent myIntent = new Intent(this, PlaynomicsTestAppActivity2.class);
		startActivity(myIntent);
	}
	
	public void onCloseClick(View view) {
		
		finish();
	}
}
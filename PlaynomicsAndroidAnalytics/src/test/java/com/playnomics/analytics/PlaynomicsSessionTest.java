package com.playnomics.analytics;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.playnomics.analytics.PlaynomicsEvent.EventType;
import com.playnomics.analytics.TransactionEvent.CurrencyCategory;
import com.playnomics.analytics.TransactionEvent.CurrencyType;
import com.playnomics.analytics.TransactionEvent.TransactionType;
import com.playnomics.analytics.UserInfoEvent.UserInfoSex;
import com.playnomics.analytics.UserInfoEvent.UserInfoSource;
import com.playnomics.analytics.UserInfoEvent.UserInfoType;

public class PlaynomicsSessionTest {
	
	@Test
	public void testEvents() {
	
		List<BasicEvent> basicEventList = new ArrayList<BasicEvent>();
		EventType eventType = EventType.appStart;
		String applicationId = "testAppId";
		String userId = "testUserId";
		BasicEvent startEvent = new BasicEvent(eventType, applicationId, userId, "testCookieId",
			RandomGenerator.createRandomHex(), RandomGenerator.createRandomHex(), 480);
		basicEventList.add(startEvent);
		
		EventSender es = new EventSender(true);
		
		for (BasicEvent be : basicEventList) {
			assertTrue(es.sendToServer(be));
		}
		
		List<UserInfoEvent> userInfoEventList = new ArrayList<UserInfoEvent>();
		@SuppressWarnings("deprecation")
		UserInfoEvent userInfoEvent = new UserInfoEvent(applicationId, userId, UserInfoType.update, "US", "CA",
			UserInfoSex.M, new Date("01/01/1999"), UserInfoSource.Other, "testCampaign", new Date());
		userInfoEventList.add(userInfoEvent);
		
		for (UserInfoEvent uie : userInfoEventList) {
			assertTrue(es.sendToServer(uie));
		}
		
		List<GameEvent> gameEventList = new ArrayList<GameEvent>();
		GameEvent gameEvent = new GameEvent(EventType.gameStart, applicationId, userId,
			RandomGenerator.createRandomHex(), "testSite", RandomGenerator.createRandomHex(), "testType",
			"testGameId", null);
		gameEventList.add(gameEvent);
		
		for (GameEvent ge : gameEventList) {
			assertTrue(es.sendToServer(ge));
		}
		
		List<TransactionEvent> transactionEventList = new ArrayList<TransactionEvent>();
		
		String[] currencyTypes = {CurrencyType.USD.toString(), CurrencyType.OFF.toString()};
		double[] currencyValues = {1,2};
		CurrencyCategory[] currencyCategories = {CurrencyCategory.r, CurrencyCategory.v};
		TransactionEvent transactionEvent = new TransactionEvent(EventType.transaction, applicationId, userId, 123456,
			"testItemId", 1, TransactionType.SellItem, null, currencyTypes, currencyValues, currencyCategories);

		transactionEventList.add(transactionEvent);
		
		for (TransactionEvent te : transactionEventList) {
			assertTrue(es.sendToServer(te));
		}
		
		List<SocialEvent> socialEventList = new ArrayList<SocialEvent>();

		SocialEvent socialEvent = new SocialEvent(EventType.invitationSent, applicationId, userId, "testInvitationId",
			"testRecipientId", "test@test.com", "email",null);

		socialEventList.add(socialEvent);
		
		for (SocialEvent se : socialEventList) {
			assertTrue(es.sendToServer(se));
		}
		
	}
}

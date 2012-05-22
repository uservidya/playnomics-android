package com.playnomics.api;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.playnomics.api.PlaynomicsConstants.CurrencyCategory;
import com.playnomics.api.PlaynomicsConstants.CurrencyType;
import com.playnomics.api.PlaynomicsConstants.TransactionType;
import com.playnomics.api.PlaynomicsConstants.UserInfoSex;
import com.playnomics.api.PlaynomicsConstants.UserInfoSource;
import com.playnomics.api.PlaynomicsConstants.UserInfoType;
import com.playnomics.api.PlaynomicsEvent.EventType;

public class PlaynomicsSessionTest {
	
	@Test
	public void testEvents() {
	
		EventSender es = new EventSender(true);
		List<PlaynomicsEvent> eventList = new ArrayList<PlaynomicsEvent>();
		EventType eventType = EventType.appStart;
		Long applicationId = 3L;
		String userId = "testUserId";
		BasicEvent startEvent = new BasicEvent(eventType, applicationId, userId, "testCookieId",
			RandomGenerator.createRandomHex(), RandomGenerator.createRandomHex(), 480);
		eventList.add(startEvent);
		
		@SuppressWarnings("deprecation")
		UserInfoEvent userInfoEvent = new UserInfoEvent(applicationId, userId, UserInfoType.update, "US", "CA",
			UserInfoSex.Male, new Date("01/01/1999"), UserInfoSource.Other.toString(), "testCampaign", new Date());
		eventList.add(userInfoEvent);
		
		GameEvent gameEvent = new GameEvent(EventType.gameStart, applicationId, userId,
			RandomGenerator.createRandomHex(), "testSite", RandomGenerator.createRandomHex(), "testType",
			"testGameId", null);
		eventList.add(gameEvent);
	
		String[] currencyTypes = {CurrencyType.USD.toString(), CurrencyType.OFF.toString()};
		double[] currencyValues = {1,2};
		CurrencyCategory[] currencyCategories = {CurrencyCategory.Real, CurrencyCategory.Virtual};
		TransactionEvent transactionEvent = new TransactionEvent(EventType.transaction, applicationId, userId, 123456,
			"testItemId", 1, TransactionType.SellItem, null, currencyTypes, currencyValues, currencyCategories);

		eventList.add(transactionEvent);
		
		SocialEvent socialEvent = new SocialEvent(EventType.invitationSent, applicationId, userId, "testInvitationId",
			"testRecipientId", "test@test.com", "email",null);

		eventList.add(socialEvent);
		
		ErrorEvent ee = new ErrorEvent(new Exception("test exception"));
		eventList.add(ee);
		
		for (PlaynomicsEvent pe : eventList) {
			assertTrue(es.sendToServer(pe));
		}	
	}
}

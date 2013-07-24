package com.playnomics.playrm;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import com.playnomics.playrm.PlaynomicsConstants.CurrencyCategory;
import com.playnomics.playrm.PlaynomicsConstants.CurrencyType;
import com.playnomics.playrm.PlaynomicsConstants.TransactionType;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoSex;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoSource;
import com.playnomics.playrm.PlaynomicsConstants.UserInfoType;
import com.playnomics.playrm.PlaynomicsEvent.EventType;

public class PlaynomicsSessionTest {
	
	@BeforeClass 
	public static void setUp(){
		PlaynomicsSession.setTestMode(true);
		PlaynomicsLogger.setEnabled(false);
	}
	
	@Test
	public void testEvents() throws IOException {
		
		EventSender es = new EventSender();
		List<PlaynomicsEvent> eventList = new ArrayList<PlaynomicsEvent>();
		EventType eventType = EventType.appStart;
		Long applicationId = 3L;
		String userId = "testUserId";
		String sessionId = RandomGenerator.createRandomHex();
		
		String deviceId  = "testDeviceId";
		
		BasicEvent startEvent = new BasicEvent(eventType, applicationId, userId, deviceId,
			sessionId, RandomGenerator.createRandomHex(), 480);
		eventList.add(startEvent);
		
		@SuppressWarnings("deprecation")
		UserInfoEvent userInfoEvent = new UserInfoEvent(sessionId, applicationId, userId, UserInfoType.update, "US", "CA",
			UserInfoSex.Male, new Date("01/01/1999"), UserInfoSource.Other.toString(), "testCampaign", new Date(), deviceId);
		eventList.add(userInfoEvent);
		
		GameEvent gameEvent = new GameEvent(EventType.gameStart, sessionId, applicationId, userId,
			3L, "testSite", 3L, "testType",
			"testGameId", null, deviceId);
		eventList.add(gameEvent);
	
		String[] currencyTypes = {CurrencyType.USD.toString(), CurrencyType.OFF.toString()};
		double[] currencyValues = {1,2};
		CurrencyCategory[] currencyCategories = {CurrencyCategory.Real, CurrencyCategory.Virtual};
		
		Double quantity = new Double(1);
		TransactionEvent transactionEvent = new TransactionEvent(EventType.transaction, sessionId, applicationId, userId, 123456,
			"testItemId", quantity, TransactionType.SellItem, null, currencyTypes, currencyValues, currencyCategories, deviceId);

		eventList.add(transactionEvent);
		
		SocialEvent socialEvent = new SocialEvent(EventType.invitationSent, sessionId, applicationId, userId, 3L,
			"testRecipientId", "test@test.com", "email",null, deviceId);

		eventList.add(socialEvent);
		
		ErrorEvent ee = new ErrorEvent(new Exception("test exception"));
		eventList.add(ee);
		
		for (PlaynomicsEvent pe : eventList) {
			assertTrue(es.sendToServer(pe));
		}	
	}
}

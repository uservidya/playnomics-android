package com.playnomics.analytics;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.playnomics.analytics.PlaynomicsEvent.EventType;
import com.playnomics.analytics.UserInfoEvent.UserInfoSex;
import com.playnomics.analytics.UserInfoEvent.UserInfoSource;
import com.playnomics.analytics.UserInfoEvent.UserInfoType;

public class PlaynomicsSessionTest {
	
	@Test
	public void testUserInfoEvent() {
	
		List<PlaynomicsEvent> eventList = new ArrayList<PlaynomicsEvent>();
		EventType eventType = EventType.appStart;
		String applicationId = "testAppId";
		String userId = "testUserId";
//		PlaynomicsEvent startEvent = new PlaynomicsEvent(eventType, applicationId, userId);
//		eventList.add(startEvent);
		
		UserInfoEvent uie = new UserInfoEvent(applicationId, userId, UserInfoType.update, "US", "CA", UserInfoSex.M,
			new Date("01/01/1999"), UserInfoSource.Other, "testCampaign", new Date());
		eventList.add(uie);
		
		EventSender es = new EventSender(true);
		
		for (PlaynomicsEvent pe : eventList) {
			
			boolean sent;
			// Send the event and remove if successful
			if (pe instanceof UserInfoEvent)
				sent = es.sendToServer((UserInfoEvent) pe);
			else
				sent = es.sendToServer((BasicEvent) pe);
			
			if (sent)
				eventList.remove(pe);
		}
		
	}
}

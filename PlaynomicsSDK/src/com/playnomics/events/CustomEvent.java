package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.IConfig;
import com.playnomics.util.IRandomGenerator;

public class CustomEvent extends ExplicitEvent {

	public enum CustomEventType {
		CUSTOM_EVENT_1(1), CUSTOM_EVENT_2(2), CUSTOM_EVENT_3(3), CUSTOM_EVENT_4(
				4), CUSTOM_EVENT_5(5), CUSTOM_EVENT_6(6), CUSTOM_EVENT_7(7), CUSTOM_EVENT_8(
				8), CUSTOM_EVENT_9(9), CUSTOM_EVENT_10(10), CUSTOM_EVENT_11(11), CUSTOM_EVENT_12(
				12), CUSTOM_EVENT_13(13), CUSTOM_EVENT_14(14), CUSTOM_EVENT_15(
				15), CUSTOM_EVENT_16(16), CUSTOM_EVENT_17(17), CUSTOM_EVENT_18(
				18), CUSTOM_EVENT_19(19), CUSTOM_EVENT_20(20), CUSTOM_EVENT_21(
				21), CUSTOM_EVENT_22(22), CUSTOM_EVENT_23(23), CUSTOM_EVENT_24(
				24), CUSTOM_EVENT_25(25);

		private int milestoneNum;

		CustomEventType(int milestoneNum) {
			this.milestoneNum = milestoneNum;
		}

		@Override
		public String toString() {
			return String.format("CUSTOM%d", milestoneNum);
		}
	}

	public CustomEvent(IConfig config, IRandomGenerator generator,
			GameSessionInfo sessionInfo, CustomEventType milestoneType) {
		super(config, sessionInfo);

		long milestoneId = generator.generatePositiveRandomLong();
		appendParameter(config.getMilestoneIdKey(), milestoneId);
		appendParameter(config.getMilestoneNameKey(), milestoneType);
	}

	@Override
	public String getUrlPath() {
		return config.getEventPathMilestone();
	}
}

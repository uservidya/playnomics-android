package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Util;

public class MilestoneEvent extends ExplicitEvent{

	public enum MilestoneType{
		MilestoneCustom1(1),
		MilestoneCustom2(2),
		MilestoneCustom3(3),
		MilestoneCustom4(4),
		MilestoneCustom5(5),
		MilestoneCustom6(6),
		MilestoneCustom7(7),
		MilestoneCustom8(8),
		MilestoneCustom9(9),
		MilestoneCustom10(10),
		MilestoneCustom11(11),
		MilestoneCustom12(12),
		MilestoneCustom13(13),
		MilestoneCustom14(14),
		MilestoneCustom15(15),
		MilestoneCustom16(16),
		MilestoneCustom17(17),
		MilestoneCustom18(18),
		MilestoneCustom19(19),
		MilestoneCustom20(20),
		MilestoneCustom21(21),
		MilestoneCustom22(22),
		MilestoneCustom23(23),
		MilestoneCustom24(24),
		MilestoneCustom25(25);
		
		private int milestoneNum;
		MilestoneType(int milestoneNum){
			this.milestoneNum = milestoneNum;
		}
		
		public String getMilestoneName(){
			return String.format("CUSTOM%d", this.milestoneNum);
		}
	}
	
	public MilestoneEvent(GameSessionInfo sessionInfo, MilestoneType milestoneType){
		super(sessionInfo);
		
		long milestoneId = Util.generatePositiveRandomLong();
		String milestoneName = milestoneType.getMilestoneName();
		appendParameter(milestoneIdKey, milestoneId);
		appendParameter(milestoneNameKey, milestoneName);
	}
	
	@Override
	protected String getBaseUrl() {
		return "milestone";
	}
}

package com.playnomics.util;

public class EpochTime {
	
	private long timeInMilliseconds;
	private EpochTime(long timeInMilliseconds){
		this.timeInMilliseconds = timeInMilliseconds;
	}
	
	public static EpochTime getEpochTimeNow(){
		long time = System.currentTimeMillis();
		return new EpochTime(time);
	}
	
	public long getTimeInMilliseconds(){
		return this.timeInMilliseconds;
	}
	
	public long getTimeInSeconds(){
		return this.timeInMilliseconds/1000;
	}
	
	@Override
	public String toString(){
		return String.format("%l", this.getTimeInMilliseconds());
	}
}

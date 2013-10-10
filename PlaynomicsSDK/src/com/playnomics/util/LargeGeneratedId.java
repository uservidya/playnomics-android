package com.playnomics.util;

public class LargeGeneratedId {
	private long generatedId;
	
	public long getId(){
		return generatedId;
	}
	
	public LargeGeneratedId(Util util){
		generatedId = util.generatePositiveRandomLong();
	}
	
	public LargeGeneratedId(long value){
		generatedId = value;
	}
	
	@Override
	public String toString(){
		return String.format("%X", generatedId);
	}
}

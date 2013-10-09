package com.playnomics.util;

public class LargeGeneratedId {
	private long generatedId;
	
	public long getId(){
		return this.generatedId;
	}
	
	public LargeGeneratedId(Util util){
		this.generatedId = util.generatePositiveRandomLong();
	}
	
	public LargeGeneratedId(long value){
		this.generatedId = value;
	}
	
	@Override
	public String toString(){
		return String.format("%X", this.generatedId);
	}
}

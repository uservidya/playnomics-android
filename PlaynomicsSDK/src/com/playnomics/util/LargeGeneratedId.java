package com.playnomics.util;

public class LargeGeneratedId {
	private long generatedId;
	public LargeGeneratedId(long value){
		this.generatedId = value;
	}
	
	public static LargeGeneratedId generateNextValue(){
		long id = Util.generatePositiveRandomLong();
		return new LargeGeneratedId(id);
	}
	
	@Override
	public String toString(){
		return String.format("%X", this.generatedId);
	}
}

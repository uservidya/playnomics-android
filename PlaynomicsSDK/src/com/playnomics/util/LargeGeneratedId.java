package com.playnomics.util;

public class LargeGeneratedId {
	private long generatedId;
	public LargeGeneratedId(long value){
		this.generatedId = value;
	}
	
	static LargeGeneratedId generateNextValue(){
		long id = Util.generateRandomLong();
		return new LargeGeneratedId(id);
	}
	
	@Override
	public String toString(){
		return String.format("%X", this.generatedId);
	}
}

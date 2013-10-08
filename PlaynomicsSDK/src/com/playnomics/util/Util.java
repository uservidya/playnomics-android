package com.playnomics.util;

import java.util.Random;
import java.util.TimeZone;

public class Util {	
	public long generatePositiveRandomLong(){
		Random rand = new Random();
		return Math.abs(rand.nextLong());
	}

	public boolean stringIsNullOrEmpty(String value){
		return (value == null || value.isEmpty());
	}
}

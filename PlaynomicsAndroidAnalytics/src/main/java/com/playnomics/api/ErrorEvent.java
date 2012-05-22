package com.playnomics.api;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import android.util.Log;

public class ErrorEvent extends PlaynomicsEvent {
	
	private static final long serialVersionUID = 1L;
	
	private Exception e;
	
	public ErrorEvent(Exception e) {
	
		super();
		this.e = e;
	}
	
	@Override
	public String toQueryString() {
	
		String queryString = null;
		try {
			queryString = "ajlog?m=" + URLEncoder.encode(Log.getStackTraceString(e), "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		return queryString;
	}
}

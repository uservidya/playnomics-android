package com.playnomics.playrm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


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
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			e.printStackTrace(pw);	
			
			queryString = "jslog?m=" +  URLEncoder.encode(sw.toString(), "US-ASCII");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return queryString;
	}
	
}

package com.playnomics.client;

import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

public interface IUrlBuilder {
	public String buildUrl(String url, String path,
			TreeMap<String, Object> queryParameters)
			throws UnsupportedEncodingException;
}

package com.playnomics.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.util.TreeMap;

public interface IHttpConnectionFactory {
	HttpURLConnection startConnectionForUrl(String urlString)
			throws IOException;

	public String buildUrl(String url, String path,
			TreeMap<String, Object> queryParameters);
}

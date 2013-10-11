package com.playnomics.client;

import java.io.IOException;
import java.net.HttpURLConnection;

public interface IHttpConnectionFactory {
	HttpURLConnection startConnectionForUrl(String urlString) throws IOException;
}

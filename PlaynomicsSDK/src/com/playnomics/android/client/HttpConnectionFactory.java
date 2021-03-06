package com.playnomics.android.client;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.TreeMap;

import com.playnomics.android.util.Logger;
import com.playnomics.android.util.Util;
import com.playnomics.android.util.Logger.LogLevel;

public class HttpConnectionFactory implements IHttpConnectionFactory {
	private Logger logger;

	public HttpConnectionFactory(Logger logger) {
		this.logger = logger;
	}

	public HttpURLConnection startConnectionForUrl(String urlString)
			throws IOException {
		try {
			URL url = new URL(urlString);
			return ((HttpURLConnection) url.openConnection());
		} catch (MalformedURLException ex) {
			logger.log(LogLevel.WARNING, ex,
					"Could not generate a valid URL from this String %s",
					urlString);
			return null;
		}
	}

	public String buildUrl(String url, String path,
			TreeMap<String, Object> queryParameters) {
		if (Util.stringIsNullOrEmpty(url)) {
			return null;
		}

		StringBuilder builder = new StringBuilder(url);

		if (!Util.stringIsNullOrEmpty(path)) {
			builder.append(url.endsWith("/") ? path : String
					.format("/%s", path));
		}

		try {
			if (queryParameters != null) {
				boolean hasQueryString = builder.toString().contains("?");
				boolean firstParam = true;

				for (String key : queryParameters.keySet()) {
					if (Util.stringIsNullOrEmpty(key)) {
						continue;
					}

					Object value = queryParameters.get(key);
					if (value == null) {
						continue;
					}

					builder.append((!hasQueryString && firstParam) ? String
							.format("?%s=%s", key, URLEncoder.encode(
									value.toString(), Util.UT8_ENCODING))
							: String.format("&%s=%s", key, URLEncoder.encode(
									value.toString(), Util.UT8_ENCODING)));

					firstParam = false;
				}
			}
		} catch (UnsupportedEncodingException ex) {
			logger.log(LogLevel.WARNING, ex, "Could not build URL");
			return null;
		}
		return builder.toString();
	}

}

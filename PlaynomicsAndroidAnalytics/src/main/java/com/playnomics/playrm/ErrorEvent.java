package com.playnomics.playrm;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import com.playnomics.playrm.ErrorDetail.PlaynomicsErrorType;

class ErrorEvent extends PlaynomicsEvent {

	private static final long serialVersionUID = 1L;

	public ErrorDetail detail = null;

	private Exception e;

	public ErrorEvent(Exception e) {
		super();
		this.e = e;
	}

	public ErrorEvent(ErrorDetail detail) {
		super();
		this.detail = detail;
	}

	@Override
	public String toQueryString() {

		String queryString = null;

		try {

			if (this.detail != null) {
				if (this.detail.errorType == PlaynomicsErrorType.errorTypeInvalidJson) {
					queryString = "jslog?m=invalid_json";
				} else {
					queryString = "jslog?m=unknown";
				}
			} else {
				StringWriter sw = new StringWriter();
				PrintWriter pw = new PrintWriter(sw);
				e.printStackTrace(pw);

				queryString = "jslog?m="
						+ URLEncoder.encode(sw.toString(), PlaynomicsSession.getEncoding());
			}

		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return queryString;
	}

}

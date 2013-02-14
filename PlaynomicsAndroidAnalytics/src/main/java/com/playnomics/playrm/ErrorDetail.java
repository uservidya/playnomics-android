package com.playnomics.playrm;

public class ErrorDetail {

	protected enum PlaynomicsErrorType {
		errorTypeUndefined, errorTypeInvalidJson
	};

	public PlaynomicsErrorType errorType;

	public ErrorDetail(PlaynomicsErrorType errorType) {
		this.errorType = errorType;
	}

}

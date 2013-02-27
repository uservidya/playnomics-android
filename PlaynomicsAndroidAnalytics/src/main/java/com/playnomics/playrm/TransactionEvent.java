package com.playnomics.playrm;

import com.playnomics.playrm.PlaynomicsConstants.CurrencyCategory;
import com.playnomics.playrm.PlaynomicsConstants.TransactionType;

class TransactionEvent extends PlaynomicsEvent {

	private static final long serialVersionUID = 1L;

	private long transactionId;
	private String itemId;
	private double quantity;
	private TransactionType type;
	private String otherUserId;
	private String[] currencyTypes;
	private double[] currencyValues;
	private CurrencyCategory[] currencyCategories;

	public TransactionEvent(EventType eventType, String internalSessionId,
			Long applicationId, String userId, long transactionId,
			String itemId, double quantity, TransactionType type,
			String otherUserId, String[] currencyTypes,
			double[] currencyValues, CurrencyCategory[] currencyCategories) {

		super(eventType, internalSessionId, applicationId, userId);
		this.transactionId = transactionId;
		this.itemId = itemId;
		this.quantity = quantity;
		this.type = type;
		this.otherUserId = otherUserId;
		this.currencyTypes = currencyTypes;
		this.currencyValues = currencyValues;
		this.currencyCategories = currencyCategories;
	}

	public long getTransactionId() {

		return transactionId;
	}

	public void setTransactionId(long transactionId) {

		this.transactionId = transactionId;
	}

	public String getItemId() {

		return itemId;
	}

	public void setItemId(String itemId) {

		this.itemId = itemId;
	}

	public double getQuantity() {

		return quantity;
	}

	public void setQuantity(double quantity) {

		this.quantity = quantity;
	}

	public TransactionType getType() {

		return type;
	}

	public void setType(TransactionType type) {

		this.type = type;
	}

	public String getOtherUserId() {

		return otherUserId;
	}

	public void setOtherUserId(String otherUserId) {

		this.otherUserId = otherUserId;
	}

	public String[] getCurrencyTypes() {

		return currencyTypes;
	}

	public void setCurrencyTypes(String[] currencyTypes) {

		this.currencyTypes = currencyTypes;
	}

	public double[] getCurrencyValues() {

		return currencyValues;
	}

	public void setCurrencyValues(double[] currencyValues) {

		this.currencyValues = currencyValues;
	}

	public CurrencyCategory[] getCurrencyCategories() {

		return currencyCategories;
	}

	public void setCurrencyCategories(CurrencyCategory[] currencyCategories) {

		this.currencyCategories = currencyCategories;
	}

	@Override
	protected String toQueryString() {

		// Set common params
		String queryString = getEventType() + "?t=" + getEventTime().getTime()
				+ "&a=" + getApplicationId() + "&u=" + getUserId() + "&tt="
				+ getType() + "&jsh=" + getSessionId();

		for (int i = 0; i < getCurrencyTypes().length; i++) {

			queryString += "&tc" + i + "=" + getCurrencyTypes()[i] + "&tv" + i
					+ "=" + getCurrencyValues()[i] + "&ta" + i + "="
					+ getCurrencyCategories()[i];
		}

		// Optional params
		queryString = addOptionalParam(queryString, "i", getItemId());
		queryString = addOptionalParam(queryString, "tq", getQuantity());
		queryString = addOptionalParam(queryString, "to", getOtherUserId());

		return queryString;
	}

}

package com.playnomics.api;

public class TransactionEvent extends PlaynomicsEvent {
	
	public enum TransactionType {
		BuyItem, SellItem, ReturnItem, BuyService, SellService, ReturnService,
		CurrencyConvert, Initial, Free, Reward, GiftSend, GiftReceive
	};
	
	public enum CurrencyCategory {
		r, v
	};
	
	public enum CurrencyType {
		USD, FBC, OFD, OFF
	}
	
	private long transactionId;
	private String itemId;
	private double quantity;
	private TransactionType type;
	private String otherUserId;
	private String[] currencyTypes;
	private double[] currencyValues;
	private CurrencyCategory[] currencyCategories;
	
	public TransactionEvent(EventType eventType, Long applicationId, String userId, long transactionId,
		String itemId, double quantity, TransactionType type, String otherUserId, String[] currencyTypes,
		double[] currencyValues, CurrencyCategory[] currencyCategories) {
	
		super(eventType, applicationId, userId);
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
	
}

package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Util;

public class TransactionEvent extends ExplicitEvent {
	
	public TransactionEvent(Util util, GameSessionInfo sessionInfo, int quantity, float price){
		super(util, sessionInfo);
		
		long transactionId = util.generatePositiveRandomLong();
		//only currency allowed
		int currencyIndex = 0;
		//hard code these values since we are simplifying transactions
		String transactionType = "BuyItem";
		String realCurrencyCategory = "r";
		String usdCurrencyType = "USD";
		String itemId = "monetized";
		
		appendParameter(transactionIdKey, transactionId);
		appendParameter(transactionTypeKey, transactionType);
		appendParameter(String.format(transactionCurrencyCategoryFormatKey, currencyIndex), realCurrencyCategory);
		appendParameter(String.format(transactionCurrencyTypeFormatKey, currencyIndex), usdCurrencyType);
		appendParameter(String.format(transactionCurrencyValueFormatKey, currencyIndex), price);
		appendParameter(transactionQuantityKey, quantity);
		appendParameter(transactionItemIdKey, itemId);
	}
	
	@Override
	public String getBaseUrl() {
		return "transaction";
	}

}

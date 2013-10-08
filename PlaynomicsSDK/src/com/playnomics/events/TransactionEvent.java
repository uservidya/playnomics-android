package com.playnomics.events;

import com.playnomics.session.GameSessionInfo;
import com.playnomics.util.Config;
import com.playnomics.util.Util;

public class TransactionEvent extends ExplicitEvent {
	
	public TransactionEvent(Config config, Util util, GameSessionInfo sessionInfo, int quantity, float price){
		super(config, sessionInfo);
		
		long transactionId = util.generatePositiveRandomLong();
		//only currency allowed
		int currencyIndex = 0;
		//hard code these values since we are simplifying transactions
		String transactionType = "BuyItem";
		String realCurrencyCategory = "r";
		String usdCurrencyType = "USD";
		String itemId = "monetized";
		
		appendParameter(config.getTransactionIdKey(), transactionId);
		appendParameter(config.getTransactionTypeKey(), transactionType);
		appendParameter(String.format(config.getTransactionCurrencyCategoryFormatKey(), currencyIndex), realCurrencyCategory);
		appendParameter(String.format(config.getTransactionCurrencyTypeFormatKey(), currencyIndex), usdCurrencyType);
		appendParameter(String.format(config.getTransactionCurrencyValueFormatKey(), currencyIndex), price);
		appendParameter(config.getTransactionQuantityKey(), quantity);
		appendParameter(config.getTransactionItemIdKey(), itemId);
	}
	
	@Override
	public String getUrlPath() {
		return "transaction";
	}

}

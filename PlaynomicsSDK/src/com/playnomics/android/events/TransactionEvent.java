package com.playnomics.android.events;

import com.playnomics.android.session.GameSessionInfo;
import com.playnomics.android.util.IConfig;
import com.playnomics.android.util.IRandomGenerator;

public class TransactionEvent extends ExplicitEvent {

	public TransactionEvent(IConfig config, IRandomGenerator generator,
			GameSessionInfo sessionInfo, int quantity, float price) {
		super(config, sessionInfo);

		long transactionId = generator.generatePositiveRandomLong();
		// only currency allowed
		int currencyIndex = 0;
		// hard code these values since we are simplifying transactions
		String transactionType = "BuyItem";
		String realCurrencyCategory = "r";
		String usdCurrencyType = "USD";
		String itemId = "monetized";

		appendParameter(config.getTransactionIdKey(), transactionId);
		appendParameter(config.getTransactionTypeKey(), transactionType);
		appendParameter(
				String.format(config.getTransactionCurrencyCategoryFormatKey(),
						currencyIndex), realCurrencyCategory);
		appendParameter(String.format(
				config.getTransactionCurrencyTypeFormatKey(), currencyIndex),
				usdCurrencyType);
		appendParameter(String.format(
				config.getTransactionCurrencyValueFormatKey(), currencyIndex),
				price);
		appendParameter(config.getTransactionQuantityKey(), quantity);
		appendParameter(config.getTransactionItemIdKey(), itemId);
	}

	@Override
	public String getUrlPath() {
		return config.getEventPathTransaction();
	}

}

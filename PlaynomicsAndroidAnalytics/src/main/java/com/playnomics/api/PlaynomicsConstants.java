package com.playnomics.api;


public class PlaynomicsConstants {
	
	public enum ResponseType {
		accepted
	};

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
	
	// Enums for UserInfo Events
	public static enum UserInfoType {
		update
	};
	
	public static enum UserInfoSex {
		M, F, U
	};
	
	public static enum UserInfoSource {
		Adwords, DoubleClick, YahooAds, MSNAds, AOLAds, Adbrite, FacebookAds,
		GoogleSearch, YahooSearch, BingSearch, FacebookSearch,
		Applifier, AppStrip, VIPGamesNetwork, UserReferral, InterGame, Other
	}	
}

package com.playnomics.api;

public class PlaynomicsConstants {
	
	public enum ResponseType {
		accepted
	};
	
	public enum TransactionType {
		BuyItem, SellItem, ReturnItem, BuyService, SellService, ReturnService,
		CurrencyConvert, Initial, Free, Reward, GiftSend, GiftReceive
	}
	
	public enum CurrencyCategory {
		Real("r"), Virtual("v");
		
		private CurrencyCategory(String name) {
		
			this.name = name;
		}
		
		private final String name;
		
		public String toString() {
		
			return name;
		}
	}
	
	public enum CurrencyType {
		USD, FBC, OFD, OFF
	}
	
	// Enums for UserInfo Events
	public static enum UserInfoType {
		update
	};
	
	public static enum UserInfoSex {
		Male("M"), Femaile("F"), Unknown("U");
		
		private UserInfoSex(String name) {
		
			this.name = name;
		}
		
		private final String name;
		
		public String toString() {
		
			return name;
		}
	}
	
	public static enum UserInfoSource {
		Adwords, DoubleClick, YahooAds, MSNAds, AOLAds, Adbrite, FacebookAds,
		GoogleSearch, YahooSearch, BingSearch, FacebookSearch,
		Applifier, AppStrip, VIPGamesNetwork, UserReferral, InterGame, Other
	}
}

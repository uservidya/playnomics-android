package com.playnomics.sdk;

import java.util.Date;

import android.app.Activity;
import android.content.Context;

import com.playnomics.client.HttpConnectionFactory;
import com.playnomics.events.MilestoneEvent.MilestoneType;
import com.playnomics.session.Session;
import com.playnomics.util.Config;
import com.playnomics.util.Util;

public class Playnomics {
	private static final Object syncLock = new Object();
	private static Session instance;
	private static Session getInstance() {
		synchronized (Playnomics.syncLock) {
			if (instance == null) {
				HttpConnectionFactory connectionFactory = new HttpConnectionFactory();
				Config config = new Config();
				Util util = new Util();
				instance = new Session(config, util, connectionFactory);
			}
			return instance;
		}
	}
	
	public static void start(Context context, long applicationId, String userId){
		Session session = getInstance();
		session.start(applicationId, userId, context);
	}
	
	public static void start(Context context, long applicationId){
		Session session = getInstance();
		session.start(applicationId, context);
	}
	
	public static void attachActivity(Activity activity){
		Session session = getInstance();
		session.attachActivity(activity);
	}
	
	public static void detachActivity(){
		Session session = getInstance();
		session.detachActivity();
	}
	
	public static void transactionInUSD(float priceInUSD, int quantity){
		Session session = getInstance();
		session.transactionInUSD(priceInUSD, quantity);
	}
	
	public static void milestone(MilestoneType milestoneType){
		Session session = getInstance();
		session.milestone(milestoneType);
	}
	
	public static void attributeInstall(String source, String campaign, Date installDateUtc){
		Session session = getInstance();
		session.attributeInstall(source, campaign, installDateUtc);
	}
	
	public static void attributeInstall(String source, String campaign){
		Session session = getInstance();
		session.attributeInstall(source, campaign, null);
	}
	
	public static void attributeInstall(String source){
		Session session = getInstance();
		session.attributeInstall(source, null, null);
	}
}

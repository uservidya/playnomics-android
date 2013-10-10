package com.playnomics.sdk;

import java.util.Date;

import android.app.Activity;
import android.content.Context;

import com.playnomics.events.MilestoneEvent.MilestoneType;
import com.playnomics.session.Session;

public class Playnomics {
	
	public static void start(Context context, long applicationId, String userId){
		Session session = Session.getInstance();
		session.start(applicationId, userId, context);
	}
	
	public static void start(Context context, long applicationId){
		Session session = Session.getInstance();
		session.start(applicationId, context);
	}
	
	public static void attachActivity(Activity activity){
		Session session = Session.getInstance();
		session.attachActivity(activity);
	}
	
	public static void detachActivity(){
		Session session = Session.getInstance();
		session.detachActivity();
	}
	
	public static void transactionInUSD(float priceInUSD, int quantity){
		Session session = Session.getInstance();
		session.transactionInUSD(priceInUSD, quantity);
	}
	
	public static void milestone(MilestoneType milestoneType){
		Session session = Session.getInstance();
		session.milestone(milestoneType);
	}
	
	public static void attributeInstall(String source, String campaign, Date installDateUtc){
		Session session = Session.getInstance();
		session.attributeInstall(source, campaign, installDateUtc);
	}
	
	public static void attributeInstall(String source, String campaign){
		Session session = Session.getInstance();
		session.attributeInstall(source, campaign, null);
	}
	
	public static void attributeInstall(String source){
		Session session = Session.getInstance();
		session.attributeInstall(source, null, null);
	}
}

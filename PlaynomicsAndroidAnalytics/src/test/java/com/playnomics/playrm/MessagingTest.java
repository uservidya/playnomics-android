package com.playnomics.playrm;

import org.junit.Test;

import junit.framework.Assert;

public class MessagingTest {

	@Test
	public void testCreateAd() {
		final String imageUrl ="http://playnomics.com/ad.gif";
		final String httpTargetUrl = "http://playnomics.com/target";
		final String pnxTargetUrl = "pnx://targetMethodPnx";
		final String pnaTargetUrl = "pna://targetMethodPna";
		final String impressionUrl = "http://playnomics.com/impression";
		final String closeUrl = "http://playnomics.com/close";
		final String preExecuteUrl = "http://playnomics.com/pre";
		final String postExecuteUrl = "http://playnomics.com/post";
		
		Ad ad = new Ad(imageUrl, httpTargetUrl,	impressionUrl, null, null, 
				closeUrl);

		Assert.assertEquals(impressionUrl,	ad.getImpressionUrl());
		Assert.assertEquals(closeUrl, ad.getCloseUrl());
		Assert.assertEquals(imageUrl, ad.getImageUrl());
		Assert.assertEquals(null, ad.getPostExecutionUrl());
		Assert.assertEquals(null, ad.getPreExecutionUrl());
		Assert.assertEquals(httpTargetUrl, ad.getTargetUrlForClick());
		Assert.assertEquals(httpTargetUrl, ad.getTargetUrl());
		Assert.assertEquals(Ad.AdTargetType.WEB, ad.getTargetType());

		ad = new Ad(imageUrl, pnxTargetUrl,	
				impressionUrl, preExecuteUrl, 
				postExecuteUrl, 
				closeUrl);

		Assert.assertEquals(impressionUrl,	ad.getImpressionUrl());
		Assert.assertEquals(closeUrl, ad.getCloseUrl());
		Assert.assertEquals(imageUrl, ad.getImageUrl());
		Assert.assertEquals(postExecuteUrl, ad.getPostExecutionUrl());
		Assert.assertEquals(preExecuteUrl, ad.getPreExecutionUrl());
		Assert.assertEquals("targetMethodPnx", ad.getTargetUrlForClick());
		Assert.assertEquals(pnaTargetUrl, ad.getTargetUrl());
		Assert.assertEquals(Ad.AdTargetType.PNX, ad.getTargetType());	
		
		ad = new Ad(imageUrl, pnxTargetUrl,	
				impressionUrl, preExecuteUrl, 
				postExecuteUrl, 
				closeUrl);

		Assert.assertEquals(impressionUrl,	ad.getImpressionUrl());
		Assert.assertEquals(closeUrl, ad.getCloseUrl());
		Assert.assertEquals(imageUrl, ad.getImageUrl());
		Assert.assertEquals(postExecuteUrl, ad.getPostExecutionUrl());
		Assert.assertEquals(preExecuteUrl, ad.getPreExecutionUrl());
		Assert.assertEquals("targetMethodPna", ad.getTargetUrlForClick());
		Assert.assertEquals(pnaTargetUrl, ad.getTargetUrl());
		Assert.assertEquals(Ad.AdTargetType.PNA, ad.getTargetType());	
	}
}

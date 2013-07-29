package com.playnomics.playrm;

import org.json.JSONException;
import org.junit.Test;

import com.playnomics.playrm.Ad.AdTargetType;

import junit.framework.Assert;

public class MessagingTest {

	@Test
	public void testCreateAd() throws JSONException {
		final String imageUrl = "http://playnomics.com/ad.gif";
		final String httpTargetUrl = "http://playnomics.com/target";
		final String pnxTargetUrl = "pnx://targetMethodPnx";
		final String pnaTargetUrl = "pna://targetMethodPna";
		final String impressionUrl = "http://playnomics.com/impression";
		final String closeUrl = "http://playnomics.com/close";
		final String preExecuteUrl = "http://playnomics.com/pre";
		final String postExecuteUrl = "http://playnomics.com/post";
		
		Ad ad;
		try
		{
			ad = new Ad(imageUrl, AdTargetType.URL, httpTargetUrl, impressionUrl, null, null,
					closeUrl);	
			Assert.assertEquals(impressionUrl, ad.getImpressionUrl());
			Assert.assertEquals(closeUrl, ad.getCloseUrl());
			Assert.assertEquals(imageUrl, ad.getImageUrl());
			Assert.assertEquals(null, ad.getPostExecutionUrl());
			Assert.assertEquals(null, ad.getPreExecutionUrl());
			Assert.assertEquals(httpTargetUrl, ad.getTargetUrlForClick());
			Assert.assertEquals(httpTargetUrl, ad.getTargetUrl());
			Assert.assertEquals(Ad.AdTargetUrlType.WEB, ad.getTargetUrlType());
		} catch (JSONException jex){
			Assert.fail("Unhanled JSON Exception");
		}
		
		
		try
		{
			ad = new Ad(imageUrl, AdTargetType.URL, pnxTargetUrl, impressionUrl, preExecuteUrl,
				postExecuteUrl, closeUrl);
			Assert.assertEquals(impressionUrl, ad.getImpressionUrl());
			Assert.assertEquals(closeUrl, ad.getCloseUrl());
			Assert.assertEquals(imageUrl, ad.getImageUrl());
			Assert.assertEquals(postExecuteUrl, ad.getPostExecutionUrl());
			Assert.assertEquals(preExecuteUrl, ad.getPreExecutionUrl());
			Assert.assertEquals("targetMethodPnx", ad.getTargetUrlForClick());
			Assert.assertEquals(pnxTargetUrl, ad.getTargetUrl());
			Assert.assertEquals(Ad.AdTargetUrlType.PNX, ad.getTargetUrlType());
		} catch (JSONException jex){
			Assert.fail("Unhanled JSON Exception");
		}

		try
		{
			ad = new Ad(imageUrl, AdTargetType.URL, pnaTargetUrl, impressionUrl, preExecuteUrl,
					postExecuteUrl, closeUrl);

			Assert.assertEquals(impressionUrl, ad.getImpressionUrl());
			Assert.assertEquals(closeUrl, ad.getCloseUrl());
			Assert.assertEquals(imageUrl, ad.getImageUrl());
			Assert.assertEquals(postExecuteUrl, ad.getPostExecutionUrl());
			Assert.assertEquals(preExecuteUrl, ad.getPreExecutionUrl());
			Assert.assertEquals("targetMethodPna", ad.getTargetUrlForClick());
			Assert.assertEquals(pnaTargetUrl, ad.getTargetUrl());
			Assert.assertEquals(Ad.AdTargetUrlType.PNA, ad.getTargetUrlType());
		} catch(JSONException jex){
			Assert.fail("Unhanled JSON Exception");
		}
	}
	
}

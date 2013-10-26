package com.playnomics.messaging;

import static org.junit.Assert.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;

import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.playnomics.messaging.Position.PositionType;
import com.playnomics.messaging.Target.TargetType;

public class HtmlAdFactoryTest {
	private HtmlAdFactory adFactory;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		adFactory = new HtmlAdFactory();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeserializeFullscreenAd_HtmlClose_TargetData() throws IOException, JSONException {
		byte[] jsonData = getTestJsonResource("sample-internal-ad-all-html-target-data.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData);
		
		assertTrue("HTML content is set", ad.getHtmlContent().equals("<html></html>"));
		assertTrue("Click link is set", ad.getClickLink().equals("pn://click"));
		assertTrue("Click URL is set", ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set", ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set", ad.getImpressionUrl().equals("http://impressionUrl"));
		assertTrue("Content Base URL is set", ad.getContentBaseUrl().equals("https://ads.a.playnomics.net"));

		Target target = ad.getTarget();
		assertNotNull("Target is set", target); 
		assertEquals("Target type is data", TargetType.DATA, target.getTargetType());
		assertTrue("Target Data is set", target.getTargetData().get("key").equals("value"));
		assertNull("Target URL is null", target.getTargetUrl());
		
		Position position = ad.getPosition();
		assertNotNull("Position is set", position); 
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN, position.getPositionType());
		
		HtmlCloseButton closeButton = (HtmlCloseButton)ad.getCloseButton();
		assertTrue("Close link is set", closeButton.getCloseLink().equals("pn://close"));
	}
	
	@Test 
	public void testDeserializeFullscreenAd_HtmlClose_TargetUrl() throws IOException, JSONException {
		byte[] jsonData = getTestJsonResource("sample-internal-ad-all-html-target-url.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData);
		
		assertTrue("HTML content is set", ad.getHtmlContent().equals("<html></html>"));
		assertTrue("Click link is set", ad.getClickLink().equals("pn://click"));
		assertTrue("Click URL is set", ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set", ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set", ad.getImpressionUrl().equals("http://impressionUrl"));
		assertTrue("Content Base URL is set", ad.getContentBaseUrl().equals("https://ads.a.playnomics.net"));

		Target target = ad.getTarget();
		assertNotNull("Target is set", target); 
		assertEquals("Target type is url", TargetType.URL, target.getTargetType());
		assertNull("Target Data is null", target.getTargetData());
		assertTrue("Target URL is set", target.getTargetUrl().equals("http://targetUrl"));
		
		Position position = ad.getPosition();
		assertNotNull("Position is set", position); 
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN, position.getPositionType());
		
		HtmlCloseButton closeButton = (HtmlCloseButton)ad.getCloseButton();
		assertTrue("Close link is set", closeButton.getCloseLink().equals("pn://close"));
	}
	
	@Test 
	public void testDeserializeFullscreenAd_HtmlClose_NullTarget() throws IOException, JSONException {
		byte[] jsonData = getTestJsonResource("sample-internal-ad-all-html-null-target.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData);
		
		assertTrue("HTML content is set", ad.getHtmlContent().equals("<html></html>"));
		assertTrue("Click link is set", ad.getClickLink().equals("pn://click"));
		assertTrue("Click URL is set", ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set", ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set", ad.getImpressionUrl().equals("http://impressionUrl"));
		assertTrue("Content Base URL is set", ad.getContentBaseUrl().equals("https://ads.a.playnomics.net"));

		Target target = ad.getTarget();
		assertNotNull("Target is set", target); 
		assertEquals("Target type is url", TargetType.URL, target.getTargetType());
		assertNull("Target Data is null", target.getTargetData());
		assertNull("Target URL is null", target.getTargetUrl());
		
		Position position = ad.getPosition();
		assertNotNull("Position is set", position); 
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN, position.getPositionType());
		
		HtmlCloseButton closeButton = (HtmlCloseButton)ad.getCloseButton();
		assertTrue("Close link is set", closeButton.getCloseLink().equals("pn://close"));
	}
	
	private byte[] getTestJsonResource(String fileName) throws IOException{
		URL url = this.getClass().getResource(String.format("/%s", fileName));
		File sampleJsonFile = new File(url.getFile());
		FileInputStream stream = new FileInputStream(sampleJsonFile);
		
		BufferedInputStream bufferedIn = new BufferedInputStream(stream);
		ByteArrayOutputStream bufferedOut = new ByteArrayOutputStream();
		int read;
		while((read = bufferedIn.read()) != -1){
			bufferedOut.write(read);
		}
		bufferedIn.close();
		
		return bufferedOut.toByteArray();
	}

}

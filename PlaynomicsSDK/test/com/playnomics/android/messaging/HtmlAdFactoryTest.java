package com.playnomics.android.messaging;

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

import com.playnomics.android.messaging.HtmlAd;
import com.playnomics.android.messaging.HtmlAdFactory;
import com.playnomics.android.messaging.HtmlCloseButton;
import com.playnomics.android.messaging.NativeCloseButton;
import com.playnomics.android.messaging.Position;
import com.playnomics.android.messaging.Target;
import com.playnomics.android.messaging.Position.PositionType;
import com.playnomics.android.messaging.Target.TargetType;
import com.playnomics.android.util.Logger;
import com.playnomics.android.util.UnitTestLogWriter;

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
		Logger logger = new Logger(new UnitTestLogWriter());
		adFactory = new HtmlAdFactory(logger);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testDeserializeFullscreenAd_HtmlClose_TargetData()
			throws IOException, JSONException {
		byte[] jsonData = getTestJsonResource("sample-internal-ad-all-html-target-data.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData, "placement");

		assertTrue("HTML content is set",
				ad.getHtmlContent().equals("<html></html>"));
		assertTrue("Click link is set", ad.getClickLink().equals("pn://click"));
		assertTrue("Click URL is set",
				ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set",
				ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set",
				ad.getImpressionUrl().equals("http://impressionUrl"));

		Target target = ad.getTarget();
		assertNotNull("Target is set", target);
		assertEquals("Target type is data", TargetType.DATA,
				target.getTargetType());
		assertTrue("Target Data is set", target.getTargetData().get("key")
				.equals("value"));
		assertNull("Target URL is null", target.getTargetUrl());

		Position position = ad.getPosition();
		assertNotNull("Position is set", position);
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN,
				position.getPositionType());

		HtmlCloseButton closeButton = (HtmlCloseButton) ad.getCloseButton();
		assertTrue("Close link is set",
				closeButton.getCloseLink().equals("pn://close"));
	}

	@Test
	public void testDeserializeFullscreenAd_HtmlClose_TargetUrl()
			throws IOException, JSONException {
		byte[] jsonData = getTestJsonResource("sample-internal-ad-all-html-target-url.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData, "placementname");

		assertTrue("HTML content is set",
				ad.getHtmlContent().equals("<html></html>"));
		assertTrue("Click link is set", ad.getClickLink().equals("pn://click"));
		assertTrue("Click URL is set",
				ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set",
				ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set",
				ad.getImpressionUrl().equals("http://impressionUrl"));
		
		Target target = ad.getTarget();
		assertNotNull("Target is set", target);
		assertEquals("Target type is url", TargetType.URL,
				target.getTargetType());
		assertNull("Target Data is null", target.getTargetData());
		assertTrue("Target URL is set",
				target.getTargetUrl().equals("http://targetUrl"));

		Position position = ad.getPosition();
		assertNotNull("Position is set", position);
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN,
				position.getPositionType());

		HtmlCloseButton closeButton = (HtmlCloseButton) ad.getCloseButton();
		assertTrue("Close link is set",
				closeButton.getCloseLink().equals("pn://close"));
	}

	@Test
	public void testDeserializeFullscreenAd_HtmlClose_NullTarget()
			throws IOException, JSONException {
		byte[] jsonData = getTestJsonResource("sample-internal-ad-all-html-null-target.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData, "placementName");

		assertTrue("HTML content is set",
				ad.getHtmlContent().equals("<html></html>"));
		assertTrue("Click link is set", ad.getClickLink().equals("pn://click"));
		assertTrue("Click URL is set",
				ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set",
				ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set",
				ad.getImpressionUrl().equals("http://impressionUrl"));
		
		Target target = ad.getTarget();
		assertNotNull("Target is set", target);
		assertEquals("Target type is url", TargetType.URL,
				target.getTargetType());
		assertNull("Target Data is null", target.getTargetData());
		assertNull("Target URL is null", target.getTargetUrl());

		Position position = ad.getPosition();
		assertNotNull("Position is set", position);
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN,
				position.getPositionType());

		HtmlCloseButton closeButton = (HtmlCloseButton) ad.getCloseButton();
		assertTrue("Close link is set",
				closeButton.getCloseLink().equals("pn://close"));
	}

	@Test
	public void testDeserializeFullscreenAd_ThirdPartyAd() throws IOException,
			JSONException {
		byte[] jsonData = getTestJsonResource("sample-third-party-ad.json");
		HtmlAd ad = adFactory.createDataFromBytes(jsonData, "placementName");

		assertTrue("HTML content is set",
				ad.getHtmlContent().equals("<html>Third party ad here </html>"));
		assertNull("Click link is null", ad.getClickLink());
		assertTrue("Click URL is set",
				ad.getClickUrl().equals("http://clickUrl"));
		assertTrue("Close URL is set",
				ad.getCloseUrl().equals("http://closeUrl"));
		assertTrue("Impression URL is set",
				ad.getImpressionUrl().equals("http://impressionUrl"));
		
		Target target = ad.getTarget();
		assertNotNull("Target is set", target);
		assertEquals("Target type is external", TargetType.EXTERNAL,
				target.getTargetType());
		assertNull("Target Data is null", target.getTargetData());
		assertNull("Target URL is null", target.getTargetUrl());

		Position position = ad.getPosition();
		assertNotNull("Position is set", position);
		assertEquals("Position is fullscreen", PositionType.FULLSCREEN,
				position.getPositionType());

		NativeCloseButton closeButton = (NativeCloseButton) ad.getCloseButton();
		assertTrue("Height is set", closeButton.getHeight().equals(20));
		assertTrue("Width is set", closeButton.getWidth().equals(30));
		assertTrue("Image url is set",
				closeButton.getImageUrl().equals("http://closeImageUrl"));
	}

	private byte[] getTestJsonResource(String fileName) throws IOException {
		URL url = this.getClass().getResource(String.format("/%s", fileName));
		File sampleJsonFile = new File(url.getFile());
		FileInputStream stream = new FileInputStream(sampleJsonFile);

		BufferedInputStream bufferedIn = new BufferedInputStream(stream);
		ByteArrayOutputStream bufferedOut = new ByteArrayOutputStream();
		int read;
		while ((read = bufferedIn.read()) != -1) {
			bufferedOut.write(read);
		}
		bufferedIn.close();

		return bufferedOut.toByteArray();
	}

}

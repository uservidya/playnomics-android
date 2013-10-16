package com.playnomics.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.playnomics.util.Logger;

public class HttpConnectionFactoryTest {

	@Mock
	private Logger loggerMock;
	
	private IUrlBuilder builder;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		builder = new HttpConnectionFactory(loggerMock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBuildUrlNullParams() throws UnsupportedEncodingException {
		String url = null;
		String path = null;
		TreeMap<String,Object> parameters = null;
		String result = builder.buildUrl(url, path, parameters);
		assertNull("Should be null", result);
	}
	
	@Test
	public void testBuildUrlNoPathOrParams() throws UnsupportedEncodingException {
		String url = "http://google.com";
		String path = null;
		TreeMap <String, Object> parameters = null;
		String result = builder.buildUrl(url, path, parameters);
		assertEquals(url, result);
	}

	@Test
	public void testBuildUrlPathNoSlash() throws UnsupportedEncodingException {
		String url = "http://google.com";
		String path = "apple";
		TreeMap <String, Object> parameters = null;
		String result = builder.buildUrl(url, path, parameters);
		assertEquals("http://google.com/apple", result);
	}
	
	@Test
	public void testBuildUrlPathSlash() throws UnsupportedEncodingException {
		String url = "http://google.com/";
		String path = "apple";
		TreeMap <String, Object> parameters = null;
		String result = builder.buildUrl(url, path, parameters);
		assertEquals("http://google.com/apple", result);
	}
	
	@Test
	public void testBuildUrlAllFields() throws UnsupportedEncodingException {
		String url = "http://google.com";
		String path = "apple";
		TreeMap <String, Object> parameters = new TreeMap<String, Object>();
		parameters.put("x", 1);
		parameters.put("y", "This is my name");
		String result = builder.buildUrl(url, path, parameters);
		assertEquals("http://google.com/apple?x=1&y=This+is+my+name", result);
	}
	
	public void testBuildUrlPrexistingQueryString() throws UnsupportedEncodingException {
		String url = "http://google.com";
		String path = "apple?z=1";
		TreeMap <String, Object> parameters = new TreeMap<String, Object>();
		parameters.put("x", 1);
		parameters.put("y", "This is my name");
		String result = builder.buildUrl(url, path, parameters);
		assertEquals("http://google.com/apple?z=1&x=1&y=This+is+my+name", result);
	}
}

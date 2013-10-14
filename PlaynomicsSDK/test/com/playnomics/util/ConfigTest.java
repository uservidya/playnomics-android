package com.playnomics.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class ConfigTest {
	IConfig config;
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		config = new Config();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testProductionEventsUrl() {
		assertEquals("Production Events URL", "https://e.a.playnomics.net/v1/", config.getEventsUrl());
	}
	
	@Test
	public void testTestEventsUrl() {
		config.setTestMode(true);
		assertEquals("Production Events URL", "https://e.b.playnomics.net/v1/", config.getEventsUrl());
	}
	
	@Test
	public void testOverrideEventsUrl() {
		config.setTestMode(true);
		config.setOverrideEventsUrl("https://e.c.playnomics.net/v1/");
		assertEquals("Production Events URL", "https://e.c.playnomics.net/v1/", config.getEventsUrl());
		
		config.setTestMode(false);
		assertEquals("Production Events URL", "https://e.c.playnomics.net/v1/", config.getEventsUrl());
	}
	
	@Test
	public void testProductionMessagingUrl() {
		assertEquals("Production Messaging URL", "https://ads.a.playnomics.net/v2/", config.getMessagingUrl());
	}
	
	@Test 
	public void testTestMessagingUrl() {
		config.setTestMode(true);
		assertEquals("Production Messaging URL", "https://ads.b.playnomics.net/v2/", config.getMessagingUrl());
	}
	
	@Test
	public void testOverrideMessagingUrl() {
		config.setTestMode(true);
		config.setOverrideMessagingUrl("https://ads.c.playnomics.net/v1/");
		assertEquals("Production Events URL", "https://ads.c.playnomics.net/v1/", config.getMessagingUrl());
		
		config.setTestMode(false);
		assertEquals("Production Events URL", "https://ads.c.playnomics.net/v1/", config.getMessagingUrl());
	}
}

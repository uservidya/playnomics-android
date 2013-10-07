package com.playnomics.util;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class UtilTests {
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGeneratedLongsAlwaysPositive() {
		for(int i = 0; i < 1000; i ++){
			long num = Util.generatePositiveRandomLong();
			assertTrue(num >= 0);
		}
	}
	
	@Test
	public void testCorrectCollectionMode(){
		//collection mode for Android is 7
		assertEquals(7, Util.getCollectionMode());
	}
	
	@Test
	public void testCorrectAppRunningInterval(){
		//appRunning Interval is 60 seconds
		assertEquals(60, Util.getAppRunningIntervalSeconds());
	}
}

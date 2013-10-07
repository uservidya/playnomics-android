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
		Util util = new Util();
		for(int i = 0; i < 1000; i ++){
			long num = util.generatePositiveRandomLong();
			assertTrue(num >= 0);
		}
	}
	
	@Test
	public void testCorrectCollectionMode(){
		//collection mode for Android is 7
		Util util = new Util();
		assertEquals(7, util.getCollectionMode());
	}
	
	@Test
	public void testCorrectAppRunningInterval(){
		//appRunning Interval is 60 seconds
		Util util = new Util();
		assertEquals(60, util.getAppRunningIntervalSeconds());
	}
}

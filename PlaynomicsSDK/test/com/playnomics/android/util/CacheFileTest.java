package com.playnomics.android.util;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.playnomics.android.util.CacheFile.ICacheFileHandler;

import android.content.Context;

public class CacheFileTest {

	@Mock
	private Util utilMock;
	
	@Mock
	private Context contextMock;
	
	private CacheFile cacheFile;
	
	private String fileName = "testfile";
	
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		
		File file = new File(System.getProperty("java.io.tmpdir"), fileName);
		
		Config config =  new Config();
		
		when(utilMock.getContextCacheFile(contextMock, config.getCacheFileName())).thenReturn(file);
		
		cacheFile = new CacheFile(utilMock, config);
		cacheFile.setContext(contextMock);
	}

	@After
	public void tearDown() throws Exception {
		File file = new File(System.getProperty("java.io.tmpdir"), fileName);
		file.delete();
	}

	@Test
	public void testReadsWritesFile() throws InterruptedException {
		Set<String> urls = new HashSet<String>();
		urls.add("url1");
		urls.add("url2");
		urls.add("url3");
		
		Thread thread;
		
		Runnable writeTask = cacheFile.writeSetToFile(urls);
		thread = new Thread(writeTask);
		thread.start();
		thread.join();
		
		final Set<String> result = new HashSet<String>();
		ICacheFileHandler handler = new ICacheFileHandler(){
			public void onReadSetComplete(Set<String> data) {
				result.addAll(data);
			}
		};
		
		Runnable readTask =cacheFile.readSetFromFile(handler);
		thread = new Thread(readTask);
		thread.start();
		thread.join();
		
		assertArrayEquals(urls.toArray(), result.toArray());
	}

}

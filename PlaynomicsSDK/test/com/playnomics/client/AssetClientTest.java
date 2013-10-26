package com.playnomics.client;

import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.TreeMap;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.playnomics.client.AssetClient.AssetResponse;
import com.playnomics.client.AssetClient.ResponseStatus;

public class AssetClientTest {

	@Mock
	private IHttpConnectionFactory httpFactoryMock;
	@Mock
	private HttpURLConnection connectionMock;
	
	private AssetClient client;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}
	
	@SuppressWarnings("unchecked")
	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		when(httpFactoryMock.buildUrl(any(String.class), any(String.class), any(TreeMap.class))).thenReturn("http://google.com");
		
		client = new AssetClient(httpFactoryMock);
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testSuccess() throws IOException{
		when(httpFactoryMock.startConnectionForUrl("http://google.com")).thenReturn(connectionMock);
		
		URL url = this.getClass().getResource("/sample-internal-ad-all-html-target-data.json");
		File sampleJsonFile = new File(url.getFile());
		FileInputStream stream = new FileInputStream(sampleJsonFile);
		when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
		when(connectionMock.getInputStream()).thenReturn(stream);
	
		AssetResponse response = client.requestAsset("http://google.com", null, null);
		assertEquals("Request is successful", ResponseStatus.SUCCESS, response.getStatus());

		stream = new FileInputStream(sampleJsonFile); 
		
		BufferedInputStream bufferedIn = new BufferedInputStream(stream);
		ByteArrayOutputStream bufferedOut = new ByteArrayOutputStream();
		int read;
		while((read = bufferedIn.read()) != -1){
			bufferedOut.write(read);
		}
		bufferedIn.close();
		
		byte[] expected = bufferedOut.toByteArray();
		assertArrayEquals("Data is loaded as expected", expected, response.getData());
	}
	
	@Test
	public void testFailureConnectionFailed() throws IOException{
		when(httpFactoryMock.startConnectionForUrl("http://google.com")).thenThrow(new IOException());
		AssetResponse response = client.requestAsset("http://google.com", null, null);
		assertEquals("Request fails", ResponseStatus.FAILURE, response.getStatus());
	}

	@Test
	public void testFailureBadResponseCode() throws IOException{
		when(httpFactoryMock.startConnectionForUrl("http://google.com")).thenReturn(connectionMock);
		when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_BAD_REQUEST);
		AssetResponse response = client.requestAsset("http://google.com", null, null);
		assertEquals("Request fails", ResponseStatus.FAILURE, response.getStatus());
	}
	
	@Test
	public void testFailureStreamRead() throws IOException{
		when(httpFactoryMock.startConnectionForUrl("http://google.com")).thenReturn(connectionMock);
		when(connectionMock.getResponseCode()).thenReturn(HttpURLConnection.HTTP_OK);
		when(connectionMock.getInputStream()).thenThrow(new IOException());
		
		AssetResponse response = client.requestAsset("http://google.com", null, null);
		assertEquals("Request fails", ResponseStatus.FAILURE, response.getStatus());
	}
}

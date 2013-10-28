package com.playnomics.client;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.TreeMap;

import org.json.JSONException;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.playnomics.client.AssetClient.AssetResponse;
import com.playnomics.client.AssetClient.ResponseStatus;
import com.playnomics.messaging.Placement;
import com.playnomics.messaging.Placement.PlacementState;
import com.playnomics.messaging.Placement.IPlacementStateObserver;
import com.playnomics.messaging.HtmlAd;
import com.playnomics.messaging.HtmlAdFactory;
import com.playnomics.messaging.HtmlCloseButton;
import com.playnomics.messaging.NativeCloseButton;
import com.playnomics.messaging.ui.IPlayViewFactory;
import com.playnomics.messaging.ui.RenderTaskFactory;
import com.playnomics.session.ICallbackProcessor;
import com.playnomics.session.Session;
import com.playnomics.util.Config;
import com.playnomics.util.IConfig;
import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;
import com.playnomics.util.UnitTestLogWriter;
import com.playnomics.util.Util;

public class FrameTest {

	@Mock
	private Util utilMock;
	@Mock
	private ICallbackProcessor processorMock;
	@Mock
	private Session sessionMock;
	@Mock
	private IPlacementStateObserver observerMock;
	@Mock 
	private HtmlAdFactory htmlAdFactory;
	@Mock
	private HtmlAd adMock;
	@Mock
	private AssetClient assetClientMock;
	@Mock
	private AssetResponse jsonAssetResponseMock;
	@Mock
	private AssetResponse imageAssetResponseMock;
	@Mock
	private RenderTaskFactory renderTaskFactoryMock;
	
	@Mock 
	private NativeCloseButton nativeCloseMock;
	
	@Mock 
	private HtmlCloseButton htmlCloseMock;
		
	private PlacementDataClient dataClient;
	private Placement placement;
	private Logger logger;
	
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
		
		when(sessionMock.getApplicationId()).thenReturn(1L);
		when(sessionMock.getBreadcrumbId()).thenReturn("breadcrumb");
		when(sessionMock.getDeviceId()).thenReturn("deviceId");
		when(sessionMock.getUserId()).thenReturn("userId");

		IConfig config = new Config();
	
		logger = new Logger(new UnitTestLogWriter());
		logger.setLogLevel(LogLevel.VERBOSE);
		
		when(assetClientMock.requestAsset(any(String.class), any(String.class),
				any(TreeMap.class))).thenReturn(jsonAssetResponseMock);
		
		dataClient = new PlacementDataClient(assetClientMock, config, logger, htmlAdFactory);
		dataClient.setSession(sessionMock);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testSuccessJsonWithHtmlCloseButton() throws IOException, InterruptedException, JSONException {
		byte[] data = new byte[1];
		when(jsonAssetResponseMock.getData()).thenReturn(data);
		when(jsonAssetResponseMock.getStatus()).thenReturn(ResponseStatus.SUCCESS);
		
		when(htmlAdFactory.createDataFromBytes(data)).thenReturn(adMock);

		when(adMock.getCloseButton()).thenReturn(htmlCloseMock);
		
		placement = new Placement("placementName", processorMock, utilMock, logger, observerMock, renderTaskFactoryMock);
		Thread thread = dataClient.loadPlacementInBackground(placement);
		thread.join();
		
		assertEquals("Placement loaded", PlacementState.LOAD_COMPLETE, placement.getState());
	}
	
	@Test
	public void testSuccessJsonWithNativeCloseButton() throws IOException, InterruptedException, JSONException {
		when(assetClientMock.requestAsset(any(String.class))).thenReturn(imageAssetResponseMock);
		
		byte[] data = new byte[1];
		when(jsonAssetResponseMock.getData()).thenReturn(data);
		when(jsonAssetResponseMock.getStatus()).thenReturn(ResponseStatus.SUCCESS);
		
		when(imageAssetResponseMock.getData()).thenReturn(data);
		when(imageAssetResponseMock.getStatus()).thenReturn(ResponseStatus.SUCCESS);
		
		when(htmlAdFactory.createDataFromBytes(data)).thenReturn(adMock);

		when(adMock.getCloseButton()).thenReturn(nativeCloseMock);
		
		placement = new Placement("placementName", processorMock, utilMock, logger, observerMock, renderTaskFactoryMock);
		Thread thread = dataClient.loadPlacementInBackground(placement);
		thread.join();
		
		assertEquals("Placement loaded", PlacementState.LOAD_COMPLETE, placement.getState());
	}
	
	@Test
	public void testSuccessJsonRequestWithNativeFailure() throws IOException, InterruptedException, JSONException{
		when(assetClientMock.requestAsset(any(String.class))).thenReturn(imageAssetResponseMock);
		
		byte[] data = new byte[1];
		when(jsonAssetResponseMock.getData()).thenReturn(data);
		when(jsonAssetResponseMock.getStatus()).thenReturn(ResponseStatus.SUCCESS);
		
		when(imageAssetResponseMock.getStatus()).thenReturn(ResponseStatus.FAILURE);
		
		when(htmlAdFactory.createDataFromBytes(data)).thenReturn(adMock);

		when(adMock.getCloseButton()).thenReturn(nativeCloseMock);
		
		placement = new Placement("placementName", processorMock, utilMock, logger, observerMock, renderTaskFactoryMock);
		Thread thread = dataClient.loadPlacementInBackground(placement);
		thread.join();
		
		assertEquals("Placement not loaded", PlacementState.LOAD_FAILED, placement.getState());
	}
	
	@Test
	public void testFailedJsonRequest() throws IOException, InterruptedException{
		when(jsonAssetResponseMock.getStatus()).thenReturn(ResponseStatus.FAILURE);
		
		placement = new Placement("placementName", processorMock, utilMock, logger, observerMock, renderTaskFactoryMock);
		Thread thread = dataClient.loadPlacementInBackground(placement);
		thread.join();
		
		assertEquals("Placement not loaded", PlacementState.LOAD_FAILED, placement.getState());
	}
	
	@Test
	public void testFailEncodingException() throws IOException, InterruptedException, UnsupportedEncodingException, JSONException{
		byte[] data = new byte[1];
		when(jsonAssetResponseMock.getData()).thenReturn(data);
		when(jsonAssetResponseMock.getStatus()).thenReturn(ResponseStatus.SUCCESS);
		
		when(htmlAdFactory.createDataFromBytes(data)).thenThrow(new UnsupportedEncodingException("Failed"));
		
		placement = new Placement("placementName", processorMock, utilMock, logger, observerMock, renderTaskFactoryMock);
		Thread thread = dataClient.loadPlacementInBackground(placement);
		thread.join();
		
		assertEquals("Placement not loaded", PlacementState.LOAD_FAILED, placement.getState());
	}
	
	@Test
	public void testFailJSONException() throws IOException, InterruptedException, UnsupportedEncodingException, JSONException{
		byte[] data = new byte[1];
		when(jsonAssetResponseMock.getData()).thenReturn(data);
		when(jsonAssetResponseMock.getStatus()).thenReturn(ResponseStatus.SUCCESS);
		
		when(htmlAdFactory.createDataFromBytes(data)).thenThrow(new JSONException("Failed"));
		
		placement = new Placement("placementName", processorMock, utilMock, logger, observerMock, renderTaskFactoryMock);
		Thread thread = dataClient.loadPlacementInBackground(placement);
		thread.join();
		
		assertEquals("Placement not loaded", PlacementState.LOAD_FAILED, placement.getState());
	}
}

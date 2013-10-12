package com.playnomics.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.net.HttpURLConnection;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.playnomics.client.HttpConnectionFactory;
import com.playnomics.util.Config;
import com.playnomics.util.Logger;
import com.playnomics.util.UnitTestLogWriter;
import com.playnomics.util.Util;


public class SessionTest {

	private long appId = 10;
	private String userId = "testUser";
	private String deviceId = "deviceId";
	
	@Mock 
	private HttpConnectionFactory factoryMock;
	@Mock 
	private HttpURLConnection connectionMock;
	
	@Mock 
	private Util utilMock;
	
	@Mock
	private Context contextMock;
	
	@Mock 
	private ContentResolver contentResolverMock;
	
	@Mock
	private SharedPreferences preferencesMock;
	@Mock
	private SharedPreferences.Editor editorMock;
	@Mock
	private PackageManager packageManagerMock;
	@Mock 
	private PackageInfo packageInfoMock;
	
	private Session session;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
				
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		MockitoAnnotations.initMocks(this);
		//data for the application version
		when(contextMock.getPackageName()).thenReturn("com.playnomics.test");
		when(contextMock.getPackageManager()).thenReturn(packageManagerMock);
		when(packageManagerMock.getPackageInfo("com.playnomics.test", 0)).thenReturn(packageInfoMock);
		
		//device ID setup
		when(utilMock.getDeviceIdFromContext(any(Context.class))).thenReturn(deviceId);
		//cache mock setup
		when(contextMock.getSharedPreferences("com.playnomics.cache", Context.MODE_PRIVATE)).thenReturn(preferencesMock);
		//the connection just works
		when(factoryMock.startConnectionForUrl(any(String.class))).thenReturn(connectionMock);
		when(connectionMock.getResponseCode()).thenReturn(200);
		
		Config config = new Config();
		Logger logger = new Logger(new UnitTestLogWriter());
		session = new Session(config, utilMock, factoryMock, logger);
	}

	@After
	public void tearDown() throws Exception {
	}
	


	@Test
	/**
	 * Should queue one event, appStart
	 */
	public void testStartLapse(){
		
		
	}
	
	@Test
	public void testStartNewDevice() throws IOException{
		when(packageInfoMock.versionCode).thenReturn(1);
		
		when(preferencesMock.getString("pushId", null)).thenReturn(null);
		when(preferencesMock.getInt("sessionId", -1)).thenReturn(-1);
		when(preferencesMock.getInt("appVersion", 0)).thenReturn(0);
		when(preferencesMock.getInt("lastEventTime", 0)).thenReturn(0);
		when(preferencesMock.getInt("sessionStartTime", 0)).thenReturn(0);
		
		session.start(appId, userId, contextMock);
		
		assertEquals("Application ID is set", appId, session.getApplicationId());
		assertEquals("User ID is set", userId, session.getUserId());
		assertEquals("Breadcrumb ID is set", deviceId);
		assertEquals("Session state is started", SessionStateMachine.SessionState.STARTED, session.getSessionState());
		
		//an event for start is sent
		verify(connectionMock).getResponseCode();
		//cleans up all of the session resources
		session.pause();
	}	
	
	@Test
	/**
	 * Should queue one event, appPage
	 */
	public void testStartNoLapse(){
		fail("Not implemented");
	}
	
	@Test 
	public void testMilestone(){
		fail("Not implemented");
	}
	
	@Test 
	public void testMilestoneNoStart(){
		fail("Not implemented");
	}
	

	@Test
	public void testTransaction(){
		fail("Not implemented");
	}
	
	@Test 
	public void testTransactionNoStart(){
		fail("Not implemented");
	}
	
}

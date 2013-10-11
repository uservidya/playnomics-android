package com.playnomics.session;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.when;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.audiofx.BassBoost.Settings;
import android.provider.Settings.Secure;

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
	private int httpStatus;
	
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
	@Mock
	private Settings settingsMock;
	@Mock
	private Secure secureMock;
	
	private Session session;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		
				
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		
	}

	@Before
	public void setUp() throws Exception {
		//data for the application version
		when(contextMock.getPackageName()).thenReturn("com.playnomics.test");
		when(contextMock.getPackageManager()).thenReturn(packageManagerMock);
		when(packageManagerMock.getPackageInfo("com.playnomics.test", 0)).thenReturn(packageInfoMock);
		
		//device ID setup
		when(contextMock.)
		
		//cache mock setup
		when(contextMock.getSharedPreferences("com.playnomics.cache", Context.MODE_PRIVATE)).thenReturn(preferencesMock);
		
		//in general we don't care about the editor calls
		when(preferencesMock.edit()).thenReturn(editorMock);
		Mockito.doNothing().when(editorMock.putInt(any(String.class), anyInt()));
		Mockito.doNothing().when(editorMock.putString(any(String.class),any(String.class)));
		Mockito.doNothing().when(editorMock.commit());
		
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
	public void testStartNewDevice(){
		
		
		when(packageInfoMock.versionCode).thenReturn(1);
		
		when(preferencesMock.getString("pushId", null)).thenReturn(null);
		when(preferencesMock.getInt("sessionId", -1)).thenReturn(-1);
		when(preferencesMock.getInt("appVersion", 0)).thenReturn(-1);
		when(preferencesMock.getInt("lastEventTime", 0)).thenReturn(0);
		when(preferencesMock.getInt("sessionStartTime", 0)).thenReturn(0);
		
		session.start(appId, userId, contextMock);
		
		assertEquals("Application ID is set", appId, session.getApplicationId());
		assertEquals("User ID is set", userId, session.getUserId());
		assertEquals("Breadcrumb ID is set", );
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

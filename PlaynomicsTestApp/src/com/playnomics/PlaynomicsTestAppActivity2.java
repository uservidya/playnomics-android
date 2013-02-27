package com.playnomics;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.playnomics.playrm.Frame;
import com.playnomics.playrm.Frame.DisplayResult;
import com.playnomics.playrm.Messaging;
import com.playnomics.playrm.PlaynomicsSession;

public class PlaynomicsTestAppActivity2 extends Activity {

	Frame frame;
	Spinner spinnerIntegrationTests;
	Spinner spinnerCodedTests;
	
	Button btnRunIntegrationTests;
	Button btnTest;

	private final static String logTag = PlaynomicsTestAppActivity2.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);

		Messaging.setup(this);
		spinnerCodedTests = (Spinner) findViewById(R.id.spnTests);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item);
		adapter.add("NoCloseButton");
		adapter.add("CloseButton");
		adapter.add("NoBackground");
		adapter.add("BadData");
		adapter.add("BadBackgroundImage");
		adapter.add("GoodPNA");
		adapter.add("BadPNA");
		adapter.add("GoodPNX");
		adapter.add("BadPNX");
		adapter.add("PNXDisabled");
		adapter.add("FixedLandscape");
		adapter.add("FixedPortrait");
		adapter.add("BadFrameId");

		spinnerCodedTests.setAdapter(adapter);
		
		btnTest = (Button) findViewById(R.id.btnRunTest);
		btnTest.setOnClickListener(btnTestClickListener);
		
		spinnerIntegrationTests = (Spinner) findViewById(R.id.spnIntegrationTests);
		
		ArrayAdapter<String> adapterIntegration = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item);
		adapterIntegration.add("testTL");
		adapterIntegration.add("testCC");
		adapterIntegration.add("testBR");
		adapterIntegration.add("testNoClose");
		adapterIntegration.add("testMessOnly");
		
		spinnerIntegrationTests.setAdapter(adapterIntegration);
		btnRunIntegrationTests = (Button) findViewById(R.id.btnRunIntegrationTest);
		btnRunIntegrationTests.setOnClickListener(btnTestClickListener);
		
		ImageView image = (ImageView) findViewById(R.id.imageFiller);
		image.setBackgroundColor(Color.CYAN);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this,
				"SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this),
				Toast.LENGTH_LONG).show();
	};

	public void onSwitchActivityClick(View view) {
		finish();
	}

	private OnClickListener btnTestClickListener = new OnClickListener() {
		public void onClick(View v) {
			String selectedFrame = v.getId() == btnTest.getId() ? 
					String.valueOf(spinnerCodedTests.getSelectedItem()) :
					String.valueOf(spinnerIntegrationTests.getSelectedItem());
			initMsgFrame(selectedFrame);
		}
	};

	public void initMsgFrame(String frameId) {
		// Retrieve the ad frame you need using the provided Frame ID and start
		// it. Once all of the assets are loaded
		// the frame will display itself.
		
		if("PNXDisabled" == frameId){
			frame = Messaging.initWithFrameID("GoodPNX", this);
			frame.setEnableAdCode(false);
		} else {
			frame = Messaging.initWithFrameID(frameId, this);
			frame.setEnableAdCode(true);
		}
	
		Log.d(logTag, "Initializing frame: " + frameId);
		DisplayResult result = frame.start();
	}

	public void myGoodMethod() {
		Toast.makeText(this, "PNX/PNA called!", Toast.LENGTH_LONG).show();
	}
}

package com.playnomics;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.playnomics.playrm.Frame;
import com.playnomics.playrm.Frame.DisplayResult;
import com.playnomics.playrm.Messaging;
import com.playnomics.playrm.PlaynomicsSession;

public class PlaynomicsTestAppActivity2 extends Activity {

	Frame frame;
	Spinner spin;
	Button btnTest;

	private final static String logTag = PlaynomicsTestAppActivity2.class
			.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main2);

		Messaging.setup(this);
		spin = (Spinner) findViewById(R.id.spnTests);

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item);
		adapter.add("NoCloseButton");
		adapter.add("CloseButton");
		adapter.add("NoBackground");
		adapter.add("BadData");
		adapter.add("NoReception");
		adapter.add("BadBackgroundImage");
		adapter.add("GoodPNA");
		adapter.add("BadPNA");
		adapter.add("GoodPNX");
		adapter.add("BadPNX");

		spin.setAdapter(adapter);
		Button btnTest = (Button) findViewById(R.id.btnRunTest);
		btnTest.setOnClickListener(btnTestClickListener);
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

			String selectedFrame = String.valueOf(spin.getSelectedItem());
			initMsgFrame(selectedFrame);
		}
	};

	public void initMsgFrame(String frameId) {
		// Retrieve the ad frame you need using the provided Frame ID and start
		// it. Once all of the assets are loaded
		// the frame will display itself.
		frame = Messaging.initWithFrameID(frameId, this);
		frame.setEnableAdCode(true);

		DisplayResult result = frame.start();
	}

	public void someRandomExecution() {
		Toast.makeText(this, "PNX called!", Toast.LENGTH_LONG).show();
		Log.d(logTag, "PNX:// someRandomExecution() is being performed!!!");
	}

	public static void sampleMethod() {
		Log.d(logTag, "PNA:// someRandomExecution() is being performed!!!");
	}
	
	public void myGoodMethod(){
		Toast.makeText(this, "PNX called!", Toast.LENGTH_LONG).show();
	}
}

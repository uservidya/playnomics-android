package com.playnomics;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.playnomics.playrm.Frame;
import com.playnomics.playrm.Frame.DisplayResult;
import com.playnomics.playrm.Messaging;
import com.playnomics.playrm.PlaynomicsSession;

public class PlaynomicsTestAppActivity2 extends Activity {
	
	Frame frame;
	EditText text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main2);
		text = (EditText)findViewById(R.id.Input_Text);
		Messaging.setup(this);
	}

	@Override
	protected void onStart() {
		super.onStart();
		Toast.makeText(this, "SWITCH ACTIVITY: " + PlaynomicsSession.switchActivity(this), Toast.LENGTH_LONG).show();
	};
	
	public void onSwitchActivityClick(View view) {
        finish();
	}	
	
	public void onMessageTLClick(View view){
		this.initMsgFrame("testTL");
	}

	public void onMessageCCClick(View view){
		this.initMsgFrame("testCC");
	}
	
	public void onMessageBRClick(View view){
		this.initMsgFrame("testBR");
	}
	
	public void onMessageNoCloseClick(View view){
		this.initMsgFrame("noCloseButton");
	}
	
	public void onMessageMessOnlyClick(View view){
		this.initMsgFrame("testMessOnly");
	}
	
	public void onMessageClick(View view){
		String inputText = text.getText().toString();
		this.initMsgFrame(inputText);
	}
	
	public void initMsgFrame(String frameId){
		// Retrieve the ad frame you need using the provided Frame ID and start it.  Once all of the assets are loaded
	    // the frame will display itself.
		frame = Messaging.initWithFrameID(frameId, this);
		frame.setEnableAdCode(true);
		
		DisplayResult result = frame.start();
	}
	
	public void someRandomExecution(){
		System.out.println("PNX:// someRandomExecution() is being performed!!!");
	}
	
	public static void sampleMethod(){
		System.out.println("PNA:// sampleMethod() is being performed!!!");
	}
}

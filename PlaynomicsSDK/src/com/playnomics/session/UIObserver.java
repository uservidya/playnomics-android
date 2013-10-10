package com.playnomics.session;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.view.Window;

public class UIObserver {
	private SessionStateMachine stateMachine;
	private ConcurrentLinkedQueue<Activity> activities;
	private TouchEventHandler handler;

	public UIObserver(SessionStateMachine stateMachine,
			TouchEventHandler eventHandler) {
		this.stateMachine = stateMachine;
		this.activities = new ConcurrentLinkedQueue<Activity>();
	}

	public void observeNewActivity(Activity activity) {
		Window.Callback currentCallback = activity.getWindow().getCallback();
		activity.getWindow().setCallback(
				CallbackProxy.newCallbackProxyForActivity(currentCallback, handler));
		activities.add(activity);
		stateMachine.resume();
	}

	public void forgetLastActivity() {
		activities.remove();
		if (activities.isEmpty()) {
			stateMachine.pause();
		}
	}
}

package com.playnomics.session;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;

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
		activity.getWindow().setCallback(
				CallbackProxy.newCallbackProxyForActivity(activity,
						this.handler));
		activities.add(activity);
		this.stateMachine.resume();
	}

	public void forgetLastActivity() {
		this.activities.remove();
		if (activities.isEmpty()) {
			this.stateMachine.pause();
		}
	}
}

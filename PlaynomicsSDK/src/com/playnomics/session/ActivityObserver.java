package com.playnomics.session;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.view.Window;

/**
 * @author jaredjenkins
 * Tracks the life-cycle of Activities as a proxy for session state machine, and user
 * interaction with the Activity.
 */
public class ActivityObserver {
	private SessionStateMachine stateMachine;
	private ConcurrentLinkedQueue<Activity> activities;
	private TouchEventHandler handler;
	
	public ActivityObserver(SessionStateMachine stateMachine,
			TouchEventHandler eventHandler) {
		this.stateMachine = stateMachine;
		this.activities = new ConcurrentLinkedQueue<Activity>();
	}
	
	/**
	 * @param activity
	 * Enqueues the activity and attaches a proxy object on the callback so that the SDK can observe and
	 * track user interaction.
	 */
	public void observeNewActivity(Activity activity) {
		Window.Callback currentCallback = activity.getWindow().getCallback();
		activity.getWindow().setCallback(
				WindowCallbackProxy.newCallbackProxyForActivity(currentCallback, handler));
		activities.add(activity);
		stateMachine.resume();
	}

	/**
	 * Dequeues the previously observed activity. When there are no longer any more activities to observe
	 * we assume that the application has been paused, and can be killed or resumed at some future state.
	 */
	public void forgetLastActivity() {
		Activity activity = activities.remove();
		WindowCallbackProxy proxy = (WindowCallbackProxy)activity.getWindow().getCallback();
		activity.getWindow().setCallback(proxy.getOriginalCallback());
		
		if (activities.isEmpty()) {
			stateMachine.pause();
		}
	}
}

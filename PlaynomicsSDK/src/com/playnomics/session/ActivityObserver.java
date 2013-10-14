package com.playnomics.session;

import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.view.Window;

/**
 * @author jaredjenkins
 * Tracks the life-cycle of Activities as a proxy for session state machine, and user
 * interaction with the Activity.
 */
public class ActivityObserver implements IActivityObserver {
	private SessionStateMachine stateMachine;
	private ConcurrentLinkedQueue<Activity> activities;
	
	public ActivityObserver() {
		this.activities = new ConcurrentLinkedQueue<Activity>();
	}
	
	
	public void setStateMachine(SessionStateMachine stateMachine){
		this.stateMachine = stateMachine;
	}
	
	/**
	 * @param activity
	 * Enqueues the activity and attaches a proxy object on the callback so that the SDK can observe and
	 * track user interaction.
	 */
	public void observeNewActivity(Activity activity, final TouchEventHandler handler) {
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

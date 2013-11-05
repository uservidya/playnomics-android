package com.playnomics.android.session;

import android.app.Activity;

public interface IActivityObserver {

	public void setStateMachine(SessionStateMachine stateMachine);

	/**
	 * @param activity
	 *            Enqueues the activity and attaches a proxy object on the
	 *            callback so that the SDK can observe and track user
	 *            interaction.
	 */
	public void observeNewActivity(Activity activity,
			final TouchEventHandler handler);

	/**
	 * Dequeues the previously observed activity. When there are no longer any
	 * more activities to observe we assume that the application has been
	 * paused, and can be killed or resumed at some future state.
	 */
	public void forgetLastActivity();
}
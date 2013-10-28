package com.playnomics.session;

public interface SessionStateMachine {

	public enum SessionState {
		NOT_STARTED, STARTED, PAUSED
	};

	public SessionState getSessionState();

	public void pause();

	public void resume();
}

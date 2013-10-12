package com.playnomics.session;

public interface SessionStateMachine {

	public enum SessionState {
		NOT_STARTED, STARTED, PAUSED
	};

	public SessionState getSessionState();
	
	void pause();

	void resume();
}

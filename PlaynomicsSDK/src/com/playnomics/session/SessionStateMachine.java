package com.playnomics.session;

public interface SessionStateMachine {
	
	public enum SessionState {
		NOT_STARTED, STARTED, PAUSED
	};
	
	void pause();
	void resume();
}

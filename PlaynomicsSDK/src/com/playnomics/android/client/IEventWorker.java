package com.playnomics.android.client;

import java.util.Set;

public interface IEventWorker {

	public void start();

	public void stop();

	public Set<String> getAllUnprocessedEvents();
	
}
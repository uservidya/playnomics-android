package com.playnomics.client;

import java.io.UnsupportedEncodingException;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.playnomics.events.PlaynomicsEvent;

public class StubEventQueue implements IEventQueue {

	public ConcurrentLinkedQueue<Object> queue;
	public StubEventQueue(){
		this.queue = new ConcurrentLinkedQueue<Object>();
	}
	
	public void enqueueEvent(PlaynomicsEvent event)
			throws UnsupportedEncodingException {
		this.queue.add(event);
	}

	public void enqueueEventUrl(String eventUrl) {
		this.queue.add(eventUrl);
	}

	public boolean isEmpty() {
		return this.queue.isEmpty();
	}

	public String dequeueEventUrl() {
		throw new UnsupportedOperationException();
	}
}

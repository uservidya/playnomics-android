package com.playnomics.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;

public class EventWorker implements IEventWorker {

	private IHttpConnectionFactory connectionFactory;
	private IEventQueue eventQueue;
	private Logger logger;
	private AtomicBoolean running;

	private Thread thread;

	public EventWorker(IEventQueue eventQueue, IHttpConnectionFactory factory,
			Logger logger) {
		this.running = new AtomicBoolean(false);
		this.connectionFactory = factory;
		this.eventQueue = eventQueue;
		this.logger = logger;
	}

	public void start() {
		if (running.getAndSet(true)) {
			// return if we were already running
			return;
		}

		thread = new Thread(new Runnable() {
			public void run() {
				doWork();
			}
		});
		thread.start();
	}

	public void stop() {
		if (!running.getAndSet(false)) {
			return;
		}

		try {
			thread.join();
		} catch (InterruptedException ex) {
			logger.log(LogLevel.WARNING, ex);
		}
	}

	private void doWork() {
		while (running.get()) {
			while (!eventQueue.isEmpty() && running.get()) {
				String url = eventQueue.dequeueEventUrl();

				boolean successful = false;
				HttpURLConnection connection = null;
				try {
					connection = connectionFactory.startConnectionForUrl(url);
					successful = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
				} catch (IOException ex) {
					logger.log(LogLevel.WARNING, ex,
							"Event URL Request failed for URL: %s", url);
				} finally {
					if (connection != null) {
						connection.disconnect();
					}
				}

				if (!successful) {
					eventQueue.enqueueEventUrl(url);
				}
			}

			try {
				// put this thread to sleep
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				return;
			}
		}
	}
}

package com.playnomics.client;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.concurrent.atomic.AtomicBoolean;

import com.playnomics.util.Logger;
import com.playnomics.util.Logger.LogLevel;

public class EventWorker {
	
	private HttpConnectionFactory connectionFactory;
	private EventQueue eventQueue;
	
	private AtomicBoolean running;
	
	public EventWorker(EventQueue eventQueue, HttpConnectionFactory factory){
		connectionFactory = factory;
		this.eventQueue = eventQueue;
	}
	
	public void start(){
		if(running.getAndSet(true)){
			//return if we were already running
			return;
		}
		
		new Thread(new Runnable() {
			public void run() {
				doWork();
			}
		}).start();
	}
	
	public void stop(){
		running.getAndSet(false);		
	}
	
	private void doWork(){
		while(running.get()){
			while(!eventQueue.isEmpty()){
				String url = eventQueue.dequeueEventUrl();
				
				boolean successful = false;
				HttpURLConnection connection = null;
				try {
					connection = connectionFactory.startConnectionForUrl(url);
					successful = (connection.getResponseCode() == HttpURLConnection.HTTP_OK);
				} catch (IOException e) {
					Logger.log(LogLevel.WARNING, e, "Event URL Request failed for URL: %s", url);
				} finally{
					if(connection != null){
						connection.disconnect();
					}
				}
				
				if(!successful){
					eventQueue.enqueueEventUrl(url);
				}
			}
			
			try {
				//put this thread to sleep
				Thread.sleep(1000);
			} catch (InterruptedException e) { 
				return;
			}
		}
	}
}

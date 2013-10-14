package com.playnomics.session;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import com.playnomics.util.IConfig;

public class HeartBeatProducer implements IHeartBeatProducer {
	private ScheduledThreadPoolExecutor hearbeatSchedule;
	private IConfig config;

	private AtomicBoolean started;
	
	public HeartBeatProducer(IConfig config) {
		this.started = new AtomicBoolean(false);
		hearbeatSchedule = new ScheduledThreadPoolExecutor(1);
	}

	public void start(final HeartBeatHandler handler) {
		if(started.getAndSet(true)){
			return;
		}
		
		hearbeatSchedule.scheduleAtFixedRate(new Runnable() {
			public void run() {
				handler.onHeartBeat(config.getAppRunningIntervalSeconds());
			}
		}, config.getAppRunningIntervalSeconds(),
				config.getAppRunningIntervalSeconds(), TimeUnit.SECONDS);
	}

	public void stop() {
		if(!started.getAndSet(false)){
			return;
		}
		hearbeatSchedule.shutdown();
	}
}

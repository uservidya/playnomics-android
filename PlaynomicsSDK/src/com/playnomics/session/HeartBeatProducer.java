package com.playnomics.session;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.playnomics.util.IConfig;

public class HeartBeatProducer {
	private HeartBeatHandler handler;
	private ScheduledThreadPoolExecutor hearbeatSchedule;
	private IConfig config;

	public HeartBeatProducer(HeartBeatHandler handler, IConfig config) {
		this.handler = handler;
		hearbeatSchedule = new ScheduledThreadPoolExecutor(1);
	}

	public void start() {
		hearbeatSchedule.scheduleAtFixedRate(new Runnable() {
			public void run() {
				handler.onHeartBeat(config.getAppRunningIntervalSeconds());
			}
		}, config.getAppRunningIntervalSeconds(),
				config.getAppRunningIntervalSeconds(), TimeUnit.SECONDS);
	}

	public void stop() {
		hearbeatSchedule.shutdown();
	}
}

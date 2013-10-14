package com.playnomics.session;

public interface IHeartBeatProducer {

	public void start(final HeartBeatHandler handler);

	public void stop();

}
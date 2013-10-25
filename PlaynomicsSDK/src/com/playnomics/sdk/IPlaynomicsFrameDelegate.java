package com.playnomics.sdk;

import java.util.Map;

public interface IPlaynomicsFrameDelegate {
	void onShow(Map<String, Object> jsonData);
	void onTouch(Map<String, Object> jsonData);
	void onClose(Map<String, Object> jsonData);
	void onRenderFailed();
}

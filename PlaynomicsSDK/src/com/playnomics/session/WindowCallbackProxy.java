package com.playnomics.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.view.MotionEvent;
import android.view.Window;

/**
 * @author jaredjenkins Uses a dynamic proxy to safely intercept the UI events
 *         for an Activity's Window Callback.
 */
public class WindowCallbackProxy implements InvocationHandler {

	private Window.Callback callback;
	private TouchEventHandler eventHandler;

	public Window.Callback getOriginalCallback() {
		return callback;
	}

	public static Window.Callback newCallbackProxyForActivity(
			Window.Callback callback, TouchEventHandler eventHandler) {
		return (Window.Callback) java.lang.reflect.Proxy.newProxyInstance(
				callback.getClass().getClassLoader(), callback.getClass()
						.getInterfaces(), new WindowCallbackProxy(callback,
						eventHandler));
	}

	private WindowCallbackProxy(Window.Callback callback,
			TouchEventHandler eventHandler) {
		this.callback = callback;
		this.eventHandler = eventHandler;
	}

	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {

		Object result;
		if (method.getName() == "dispatchTouchEvent") {
			if (args != null && args.length > 0) {
				Object event = args[0];

				if (event != null
						&& event instanceof MotionEvent
						&& (((MotionEvent) event).getActionMasked() == MotionEvent.ACTION_DOWN || ((MotionEvent) event)
								.getActionMasked() == MotionEvent.ACTION_POINTER_DOWN)) {
					// if the motion event was for
					eventHandler.onTouchEventReceived();
				}
			}
		}
		// invoke the method as normal
		result = method.invoke(callback, args);
		return result;
	}

}

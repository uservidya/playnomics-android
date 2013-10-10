package com.playnomics.session;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

import android.app.Activity;
import android.view.MotionEvent;
import android.view.Window;

public class CallbackProxy implements InvocationHandler {

	private Window.Callback callback;
	private TouchEventHandler eventHandler;

	public static Window.Callback newCallbackProxyForActivity(
			Activity activity, TouchEventHandler eventHandler) {
		Window.Callback callback = activity.getWindow().getCallback();
		return (Window.Callback) java.lang.reflect.Proxy.newProxyInstance(
				callback.getClass().getClassLoader(), callback.getClass()
						.getInterfaces(), new CallbackProxy(callback,
						eventHandler));
	}

	private CallbackProxy(Window.Callback callback,
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
						&& ((MotionEvent) event).getAction() == MotionEvent.ACTION_DOWN) {
					this.eventHandler.onTouchEventReceived();
				}
			}
		}
		// invoke the method as normal
		result = method.invoke(this.callback, args);
		return result;
	}

}

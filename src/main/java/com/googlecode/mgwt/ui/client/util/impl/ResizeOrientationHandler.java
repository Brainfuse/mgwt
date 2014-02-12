package com.googlecode.mgwt.ui.client.util.impl;

import com.gargoylesoftware.htmlunit.javascript.host.Screen;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

public class ResizeOrientationHandler extends BaseOrientationHandler implements
		OrientationHandler {

	@Override
	public void doSetupOrientation() {

		if (!orientationEventSupported()) {
			Window.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(ResizeEvent event) {
					ORIENTATION orientation = getOrientation();
					if (orientation != currentOrientation) {
						currentOrientation = orientation;
						fireOrientationChangedEvent(orientation);
					}
				}
			});
		} else {
			setupNativeBrowerOrientationHandler();
		}

	}

	private native static boolean orientationEventSupported()/*-{
		return "onorientationchange" in $wnd;
	}-*/;
	
	private native static int getScreenWidth()/*-{
		return screen.width;
	}-*/;
	
	private native static int getScreenHeight()/*-{
		return screen.height;
	}-*/;
	
	

	/**
	 * Get the current orientation of the device
	 * 
	 * @return the current orientation of the device
	 */
	public ORIENTATION getOrientation() {

		/**
		 * Android devices ASUS, Samsung report a value that is shifted 90
		 * degrees on various devices. This is why I changed to use the
		 * screen.height instead of the value of window.orientation.
		 */
		int height = getScreenHeight();

		int width = getScreenWidth();

		if (width > height) {
			return ORIENTATION.LANDSCAPE;
		} else {
			return ORIENTATION.PORTRAIT;
		}

	}

	private static native boolean orientationSupport() /*-{
		return "orientation" in $wnd;
	}-*/;

}

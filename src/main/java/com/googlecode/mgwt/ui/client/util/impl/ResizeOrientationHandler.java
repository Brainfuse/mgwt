package com.googlecode.mgwt.ui.client.util.impl;

import static java.lang.Math.abs;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

public class ResizeOrientationHandler extends BaseOrientationHandler implements
		OrientationHandler {
	private int initialAngle;
	private int delta;
	@Override
	public void doSetupOrientation() {

		if (!orientationEventSupported()) {
			Window.addResizeHandler(new ResizeHandler() {
				@Override
				public void onResize(ResizeEvent event) {
					ORIENTATION orientation = getOrientation();
					if (orientation != currentOrientation) {
						fireOrientationChangedEvent(orientation);
					}
				}
			});
		} else {
			setupNativeBrowerOrientationHandler();
		}
		ORIENTATION currentOrientationByScreenSize = getOrientationByScreenSize();
		initialAngle = abs(getOrientation0());
		int[] standardAngel = getStandardAngleByOrientation(currentOrientationByScreenSize);
		if ( (inArr(initialAngle , standardAngel) ) ){
			//Good implementation
			delta = 0;
			
		}else{
			delta = 90;
		}
		

	}
	
	private boolean inArr(int val, int[] arr){
		for (int i=0;i<arr.length;i++){
			if ( arr[i]== val)
				return true;
		}
		return false;
	}
	
	/**
	 * Get the current orientation of the device
	 * 
	 * @return the current orientation of the device
	 */
	public ORIENTATION getOrientationByScreenSize() {

		/**
		 * Android devices ASUS, Samsung report a value that is shifted 90
		 * degrees on various devices. This is why I changed to use the
		 * screen.height instead of the value of window.orientation.
		 */
		int height =Window.getClientHeight();
		int width = Window.getClientWidth();

		if (width > height) {
			return ORIENTATION.LANDSCAPE;
		} else {
			return ORIENTATION.PORTRAIT;
		}

	}

	private native static boolean orientationEventSupported()/*-{
		return "onorientationchange" in $wnd;
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
		return super.getBrowserOrientationByAngle(abs(getOrientation0())+ delta);

	}

	private static native boolean orientationSupport() /*-{
		return "orientation" in $wnd;
	}-*/;

	protected static int[] getStandardAngleByOrientation(ORIENTATION o){
		switch(o){
		case PORTRAIT:
			return new int[]{180, 0};
		case LANDSCAPE:
			return new int[]{90}; 
		 default:
	          throw new IllegalStateException("this should not happen!?");
		}
	}
}

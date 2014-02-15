package com.googlecode.mgwt.ui.client.util.impl;

import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

public class IOSOrientationHandler extends BaseOrientationHandler implements
		OrientationHandler {

	@Override
	public void doSetupOrientation() {
		setupNativeBrowerOrientationHandler();
	}

	@Override
	public ORIENTATION getOrientation() {
		return getBrowserOrientation();
	}

	

	protected static native int getOrientation0()/*-{
		if (typeof ($wnd.orientation) == 'undefined') {
			return 0;
		}

		return $wnd.orientation;
	}-*/;

	protected static ORIENTATION getBrowserOrientation() {
		int orientation = getOrientation0();
	
	      ORIENTATION o;
	      switch (orientation) {
	        case 0:
	        case 180:
	          o = ORIENTATION.PORTRAIT;
	          break;
	
	        case 90:
	        case -90:
	          o = ORIENTATION.LANDSCAPE;
	          break;
	
	        default:
	          throw new IllegalStateException("this should not happen!?");
	      }
	
	      return o;
	}

	
}

package com.googlecode.mgwt.ui.client.util.impl;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.Window;
import com.google.web.bindery.event.shared.EventBus;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.UtilCss;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

public abstract class BaseOrientationHandler implements OrientationHandler {
	protected static ORIENTATION currentOrientation;
	protected static boolean orientationInitialized;
	private EventBus manager;

	protected JavaScriptObject nativeJsFunction;

	@Override
	public final void maybeSetupOrientation(EventBus manager) {
		this.manager = manager;
		if (orientationInitialized)
			return;
		initializeOrientation();
	}

	protected void initializeOrientation() {
		if (!GWT.isClient()) {
			return;
		}
		doSetupOrientation();
		
		currentOrientation = getOrientation();
		orientationInitialized = true;
	}
	
	protected abstract void doSetupOrientation();
	public void onorientationChange(int orientation) {

		ORIENTATION o = getOrientation();
		fireOrientationChangedEvent(o);

	}

	void fireOrientationChangedEvent(ORIENTATION orientation) {
		setClasses(orientation);
		currentOrientation = orientation;
		manager.fireEvent(new OrientationChangeEvent(orientation));
	}

	// update styles on body
	private static void setClasses(ORIENTATION o) {
		UtilCss utilCss = MGWTStyle.getTheme().getMGWTClientBundle()
				.getUtilCss();
		switch (o) {

		case PORTRAIT:
			Document.get().getBody().addClassName(utilCss.portrait());
			Document.get().getBody().removeClassName(utilCss.landscape());
			break;
		case LANDSCAPE:
			Document.get().getBody().addClassName(utilCss.landscape());
			Document.get().getBody().removeClassName(utilCss.portrait());
			break;

		default:
			break;
		}
	}

	protected void setupNativeBrowerOrientationHandler() {
		nativeJsFunction = setupOrientation0(this);
		Window.addCloseHandler(new CloseHandler<Window>() {

			@Override
			public void onClose(CloseEvent<Window> event) {
				destroyOrientation(nativeJsFunction);

			}
		});
	}

	private static native JavaScriptObject setupOrientation0(
			BaseOrientationHandler handler)/*-{

		var func = $entry(function() {
			handler.@com.googlecode.mgwt.ui.client.util.impl.BaseOrientationHandler::onorientationChange(I)($wnd.orientation);
		});
		var w = $wnd.top || $wnd;
		w.onorientationchange = func;
		w.addEventListener("orientationChanged", func);
		return func;
	}-*/;

	private static native void destroyOrientation(JavaScriptObject o)/*-{
		var w = $wnd.top || $wnd;
		w.onorientationchange = null;
		w.removeEventListener("orientationChanged", o);
	}-*/;


	protected static native int getOrientation0()/*-{
		var w = $wnd.top || $wnd;
		if (typeof (w.orientation) == 'undefined') {
			return 0;
		}

		return w.orientation;
	}-*/;
	

	protected static ORIENTATION getBrowserOrientation() {
		int orientation = getOrientation0();
	
	      return getBrowserOrientationByAngle(orientation);
	}

	protected static ORIENTATION getBrowserOrientationByAngle(int orientation) {
		ORIENTATION o;
	      switch (orientation) {
	        case 0:
	        case 180:
	          o = ORIENTATION.PORTRAIT;
	          break;
	
	        case 90:
	        case -90:
	        case 270: //added.  if delta is 90, and orientation is 180.
	          o = ORIENTATION.LANDSCAPE; 
	          break;
	
	        default:
	          throw new IllegalStateException("this should not happen!?");
	      }
	
	      return o;
	}
	

}

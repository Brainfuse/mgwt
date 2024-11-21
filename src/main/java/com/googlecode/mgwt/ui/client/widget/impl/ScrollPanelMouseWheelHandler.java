package com.googlecode.mgwt.ui.client.widget.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;

import jsinterop.annotations.JsPackage;
import jsinterop.annotations.JsType;

public final class ScrollPanelMouseWheelHandler implements MouseWheelHandler {
	private ScrollPanelWithMouseWheelHandler scrollPanel;

	public ScrollPanelMouseWheelHandler(ScrollPanelWithMouseWheelHandler scrollPanel) {
		super();
		this.scrollPanel = scrollPanel;
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		WheelData wheelData = normalizeWheel(event.getNativeEvent());
		scrollPanel.wheel(wheelData.pixelX, wheelData.pixelY,
				event.getClientX(), event.getClientY());
		event.preventDefault();
	}

	private native int getMouseWheelVelocityX(NativeEvent evt)/*-{
		return Math.round(-evt.wheelDeltaX) || 0;
	}-*/;

	private native int getMouseWheelVelocityY(NativeEvent evt)/*-{
		// IE only sends wheelDelta
		var val = (evt.detail * 40) || -evt.wheelDeltaY
				|| (evt.wheelDelta * 10) || 0;
		return Math.round(val);
	}-*/;
	
	@JsType(isNative =  true, name ="Object", namespace = JsPackage.GLOBAL)
	public static class WheelData {
		int spinX;
		int spinY;
		int pixelX;
		int pixelY;
	}

	/**
	 * https://stackoverflow.com/questions/5527601/normalizing-mousewheel-speed-across-browsers/30134826#30134826
	 */
    public static native WheelData normalizeWheel(JavaScriptObject event) /*-{
        var PIXEL_STEP = 10;
        var LINE_HEIGHT = 40;
        var PAGE_HEIGHT = 800;

        var sX = 0, sY = 0, pX = 0, pY = 0;

        if ('detail' in event) { sY = event.detail; }
        if ('wheelDelta' in event) { sY = -event.wheelDelta / 120; }
        if ('wheelDeltaY' in event) { sY = -event.wheelDeltaY / 120; }
        if ('wheelDeltaX' in event) { sX = -event.wheelDeltaX / 120; }

        if ('axis' in event && event.axis === event.HORIZONTAL_AXIS) {
            sX = sY;
            sY = 0;
        }

        pX = sX * PIXEL_STEP;
        pY = sY * PIXEL_STEP;

        if ('deltaY' in event) { pY = event.deltaY; }
        if ('deltaX' in event) { pX = event.deltaX; }

        if ((pX || pY) && event.deltaMode) {
            if (event.deltaMode == 1) {
                pX *= LINE_HEIGHT;
                pY *= LINE_HEIGHT;
            } else {
                pX *= PAGE_HEIGHT;
                pY *= PAGE_HEIGHT;
            }
        }

        if (pX && !sX) { sX = (pX < 1) ? -1 : 1; }
        if (pY && !sY) { sY = (pY < 1) ? -1 : 1; }

        return {
            spinX: sX,
            spinY: sY,
            pixelX: pX,
            pixelY: pY
        };
    }-*/;
	

}
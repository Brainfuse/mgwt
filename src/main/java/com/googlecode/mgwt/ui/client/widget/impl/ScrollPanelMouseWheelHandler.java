package com.googlecode.mgwt.ui.client.widget.impl;

import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.MouseWheelEvent;
import com.google.gwt.event.dom.client.MouseWheelHandler;

public final class ScrollPanelMouseWheelHandler implements MouseWheelHandler {
	private ScrollPanelWithMouseWheelHandler scrollPanel;

	public ScrollPanelMouseWheelHandler(ScrollPanelWithMouseWheelHandler scrollPanel) {
		super();
		this.scrollPanel = scrollPanel;
	}

	@Override
	public void onMouseWheel(MouseWheelEvent event) {
		int wheelDeltaX = 0;
		int wheelDeltaY = 0;

		if (scrollPanel.isScrollingEnabledX()) {
			wheelDeltaX = getMouseWheelVelocityX(event.getNativeEvent()) / 10;
		}

		if (scrollPanel.isScrollingEnabledY()) {
			wheelDeltaY = getMouseWheelVelocityY(event.getNativeEvent()) / 10;
		}
		scrollPanel.wheel(wheelDeltaX, wheelDeltaY, event.getClientX(),
				event.getClientY());

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

}
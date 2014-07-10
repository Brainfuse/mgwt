package com.googlecode.mgwt.ui.client.widget.impl;

public interface ScrollPanelWithMouseWheelHandler {

	boolean isScrollingEnabledX();

	boolean isScrollingEnabledY();

	void wheel(int wheelDeltaX, int wheelDeltaY, int clientX, int clientY);
	
}

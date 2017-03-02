package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerCancelEvent.PointerCancelHandler;

public class TouchCancelToPointerCancelHandler implements PointerCancelHandler {

	private final TouchCancelHandler handler;

	public TouchCancelToPointerCancelHandler(TouchCancelHandler handler) {
		this.handler = handler;
	}

	@Override
	public void onPointerCancel(PointerCancelEvent event) {
		SimlatedTouchCancelEvent touchCancel = new SimlatedTouchCancelEvent(
				event);
		handler.onTouchCanceled(touchCancel);
	}

	private class SimlatedTouchCancelEvent extends TouchCancelEvent {
		public SimlatedTouchCancelEvent(PointerCancelEvent event) {
			setNativeEvent(event.getNativeEvent());
			setSource(event.getSource());
		}
	}

}

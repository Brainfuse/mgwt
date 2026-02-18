package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerCancelEvent.PointerCancelHandler;

/**
 * Converts pointer cancel events into simulated touch cancel events with
 * multi-touch support. Uses a shared {@link PointerTouchManager} to track
 * all active pointers.
 */
public class TouchCancelToPointerCancelHandler implements PointerCancelHandler {

	private final TouchCancelHandler handler;
	private final PointerTouchManager manager;

	public TouchCancelToPointerCancelHandler(TouchCancelHandler handler, PointerTouchManager manager) {
		this.handler = handler;
		this.manager = manager;
	}

	@Override
	public void onPointerCancel(PointerCancelEvent event) {
		int pointerId = event.getPointerId();
		manager.pointerCancel(pointerId);

		SimulatedTouchCancelEvent touchCancel = new SimulatedTouchCancelEvent(
				event.getNativeEvent(),
				manager.getTouches(),
				manager.getChangedTouches());
		handler.onTouchCanceled(touchCancel);
	}

	private static class SimulatedTouchCancelEvent extends TouchCancelEvent {

		private final LightArray<Touch> touches;
		private final LightArray<Touch> changedTouches;

		public SimulatedTouchCancelEvent(NativeEvent nativeEvent,
				LightArray<Touch> touches, LightArray<Touch> changedTouches) {
			this.touches = touches;
			this.changedTouches = changedTouches;
			setNativeEvent(nativeEvent);
			setSource(nativeEvent);
		}

		@Override
		public LightArray<Touch> getTouches() {
			return touches;
		}

		@Override
		public LightArray<Touch> getChangedTouches() {
			return changedTouches;
		}
	}

}

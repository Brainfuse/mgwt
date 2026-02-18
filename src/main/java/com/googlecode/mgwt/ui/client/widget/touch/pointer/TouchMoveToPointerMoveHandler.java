package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;

/**
 * Converts pointer move events into simulated touch move events with
 * multi-touch support. Uses a shared {@link PointerTouchManager} to track
 * all active pointers across the widget. Only fires move events for
 * pointers that have been registered via a prior pointerdown.
 */
public class TouchMoveToPointerMoveHandler implements PointerMoveEvent.PointerMoveHandler {

	private final TouchMoveHandler handler;
	private final PointerTouchManager manager;

	public TouchMoveToPointerMoveHandler(TouchMoveHandler handler, PointerTouchManager manager) {
		this.handler = handler;
		this.manager = manager;
	}

	@Override
	public void onPointerMove(PointerMoveEvent event) {
		int pointerId = event.getPointerId();
		// Only process moves for pointers we are actively tracking.
		// pointermove fires on hover (no button pressed) too, so this
		// check is expected to filter out a large number of events.
		if (!manager.isTracking(pointerId)) {
			return;
		}
		
		int pageX = event.getClientX();
		int pageY = event.getClientY();
		manager.pointerMove(pointerId, pageX, pageY);

		SimulatedTouchMoveEvent touchMove = new SimulatedTouchMoveEvent(
				event.getNativeEvent(),
				manager.getTouches(),
				manager.getChangedTouches());
		handler.onTouchMove(touchMove);
	}

	private static class SimulatedTouchMoveEvent extends TouchMoveEvent {

		private final LightArray<Touch> touches;
		private final LightArray<Touch> changedTouches;

		public SimulatedTouchMoveEvent(NativeEvent nativeEvent,
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

package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;

/**
 * Converts pointer up events into simulated touch end events with
 * multi-touch support. Uses a shared {@link PointerTouchManager} to track
 * all active pointers. On pointer up, the ended pointer appears in
 * changedTouches but is removed from touches (matching native touch behavior).
 */
public class TouchEndToPointerUpHandler implements PointerUpEvent.PointerUpHandler {

	private final TouchEndHandler handler;
	private final PointerTouchManager manager;

	public TouchEndToPointerUpHandler(TouchEndHandler handler, PointerTouchManager manager) {
		this.handler = handler;
		this.manager = manager;
	}

	@Override
	public void onPointerUp(PointerUpEvent event) {
		int pointerId = event.getPointerId();
		int pageX = event.getClientX();
		int pageY = event.getClientY();
		releasePointerCapture(event, pointerId);
		manager.pointerUp(pointerId, pageX, pageY);

		SimulatedTouchEndEvent touchEnd = new SimulatedTouchEndEvent(
				event.getNativeEvent(),
				manager.getTouches(),
				manager.getChangedTouches());
		handler.onTouchEnd(touchEnd);
	}

	private static native void releasePointerCapture(PointerUpEvent event, int pointerId) /*-{
		var e = event.@com.google.gwt.event.dom.client.DomEvent::nativeEvent;
		var target = e && e.target;
		if (target && target.releasePointerCapture) {
			if (!target.hasPointerCapture || target.hasPointerCapture(pointerId)) {
				target.releasePointerCapture(pointerId);
			}
		}
	}-*/;

	private static class SimulatedTouchEndEvent extends TouchEndEvent {

		private final LightArray<Touch> touches;
		private final LightArray<Touch> changedTouches;

		public SimulatedTouchEndEvent(NativeEvent nativeEvent,
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

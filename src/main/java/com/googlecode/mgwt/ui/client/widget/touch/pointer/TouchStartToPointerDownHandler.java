package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerDownEvent.PointerDownHandler;

/**
 * Converts pointer down events into simulated touch start events with
 * multi-touch support. Uses a shared {@link PointerTouchManager} to track
 * all active pointers across the widget.
 */
public class TouchStartToPointerDownHandler implements PointerDownHandler {

	private final TouchStartHandler handler;
	private final PointerTouchManager manager;

	public TouchStartToPointerDownHandler(TouchStartHandler handler, PointerTouchManager manager) {
		this.handler = handler;
		this.manager = manager;
	}

	@Override
	public void onPointerDown(PointerDownEvent event) {
		int pointerId = event.getPointerId();
		int pageX = event.getClientX();
		int pageY = event.getClientY();
		manager.pointerDown(pointerId, pageX, pageY);

		// Capture the pointer so that subsequent pointermove / pointerup
		// events are delivered to this element even if the pointer leaves
		// the element bounds (e.g. mouse drag). Without this, pointermove
		// may never fire on the originating element after pointerdown.
		capturePointer(event, pointerId);

		SimulatedTouchStartEvent touchStart = new SimulatedTouchStartEvent(
				event.getNativeEvent(),
				manager.getTouches(),
				manager.getChangedTouches());
		handler.onTouchStart(touchStart);
	}

	/**
	 * Calls Element.setPointerCapture() on the event target so that all
	 * subsequent pointer events for this pointerId are directed to the
	 * same element until pointerup or pointercancel.
	 */
	private static native void capturePointer(PointerDownEvent event, int pointerId) /*-{
		var e = event.@com.google.gwt.event.dom.client.DomEvent::nativeEvent;
		if (e && e.target && e.target.setPointerCapture) {
			e.target.setPointerCapture(pointerId);
		}
	}-*/;

	private static class SimulatedTouchStartEvent extends TouchStartEvent {

		private final LightArray<Touch> touches;
		private final LightArray<Touch> changedTouches;

		public SimulatedTouchStartEvent(NativeEvent nativeEvent,
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

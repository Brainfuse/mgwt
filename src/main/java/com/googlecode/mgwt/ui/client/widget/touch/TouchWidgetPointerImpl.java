package com.googlecode.mgwt.ui.client.widget.touch;

import java.util.IdentityHashMap;
import java.util.Map;

import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerCancelEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerDownEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerMoveEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerTouchManager;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerUpEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchCancelToPointerCancelHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchEndToPointerUpHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchMoveToPointerMoveHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchStartToPointerDownHandler;

/**
 * Pointer Events based implementation of {@link TouchWidgetImpl}.
 * 
 * <p>This implementation uses the W3C Pointer Events API which unifies
 * mouse, touch, and pen/stylus input into a single event model. A per-widget
 * {@link PointerTouchManager} tracks all active pointers so that
 * multi-touch scenarios (e.g. Wacom stylus + finger, two-finger pinch)
 * are correctly represented in the simulated touch event arrays.
 * 
 * <p>The implementation also sets {@code touch-action: none} on the widget's
 * element to prevent the browser from consuming pointer events for default
 * gestures like scrolling or zooming, ensuring all pointer events are
 * delivered to the application.
 * 
 * <p>Works on all modern browsers and platforms including desktop (mouse),
 * Electron, mobile touch screens, and pen/stylus devices (Wacom, Surface Pen).
 */
public class TouchWidgetPointerImpl implements TouchWidgetImpl {

	/**
	 * Holds per-widget pointer state and the internal cleanup registrations
	 * needed to keep that state in sync with the widget lifecycle.
	 */
	private final Map<Widget, WidgetPointerState> widgetStates = new IdentityHashMap<Widget, WidgetPointerState>();

	private static class WidgetPointerState {
		final PointerTouchManager manager = new PointerTouchManager();
		HandlerRegistration pointerUpRegistration;
		HandlerRegistration pointerCancelRegistration;
		HandlerRegistration attachRegistration;
		int handlerCount;
	}

	/**
	 * Ensures the widget element has touch-action:none set so the browser
	 * doesn't consume pointer events for default gestures.
	 */
	private static native void ensureTouchActionNone(Widget w) /*-{
		var el = w.@com.google.gwt.user.client.ui.UIObject::getElement()();
		if (el && el.style) {
			el.style.touchAction = 'none';
			el.style.msTouchAction = 'none';
		}
	}-*/;

	private WidgetPointerState getState(Widget w) {
		WidgetPointerState state = widgetStates.get(w);
		if (state == null) {
			state = new WidgetPointerState();
			widgetStates.put(w, state);
		}
		return state;
	}

	private void ensurePointerLifecycleCleanup(final Widget w,
			final WidgetPointerState state) {
		if (state.pointerUpRegistration != null) {
			return;
		}

		state.pointerUpRegistration = w.addDomHandler(new PointerUpEvent.PointerUpHandler() {
			@Override
			public void onPointerUp(PointerUpEvent event) {
				state.manager.pointerUp(event.getPointerId(), event.getClientX(), event.getClientY());
			}
		}, PointerUpEvent.getType());

		state.pointerCancelRegistration = w.addDomHandler(new PointerCancelEvent.PointerCancelHandler() {
			@Override
			public void onPointerCancel(PointerCancelEvent event) {
				state.manager.pointerCancel(event.getPointerId());
			}
		}, PointerCancelEvent.getType());

		state.attachRegistration = w.addAttachHandler(new AttachEvent.Handler() {
			@Override
			public void onAttachOrDetach(AttachEvent event) {
				if (!event.isAttached()) {
					state.manager.clear();
				}
			}
		});
	}

	private void releaseStateIfUnused(Widget w, WidgetPointerState state) {
		if (state.handlerCount != 0) {
			return;
		}

		state.manager.clear();
		if (state.pointerUpRegistration != null) {
			state.pointerUpRegistration.removeHandler();
			state.pointerUpRegistration = null;
		}
		if (state.pointerCancelRegistration != null) {
			state.pointerCancelRegistration.removeHandler();
			state.pointerCancelRegistration = null;
		}
		if (state.attachRegistration != null) {
			state.attachRegistration.removeHandler();
			state.attachRegistration = null;
		}
		widgetStates.remove(w);
	}

	private HandlerRegistration registerHandler(final Widget w,
			final WidgetPointerState state, HandlerRegistration registration) {
		state.handlerCount++;
		return new HandlerRegistration() {
			private boolean removed;

			@Override
			public void removeHandler() {
				if (removed) {
					return;
				}
				removed = true;
				registration.removeHandler();
				state.handlerCount--;
				releaseStateIfUnused(w, state);
			}
		};
	}

	@Override
	public HandlerRegistration addTouchStartHandler(Widget w,
			TouchStartHandler handler) {
		WidgetPointerState state = getState(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, state);
		return registerHandler(w, state, w.addDomHandler(
				new TouchStartToPointerDownHandler(handler, state.manager),
				PointerDownEvent.getType()));
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w,
			TouchMoveHandler handler) {
		WidgetPointerState state = getState(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, state);
		return registerHandler(w, state, w.addDomHandler(
				new TouchMoveToPointerMoveHandler(handler, state.manager),
				PointerMoveEvent.getType()));
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w,
			TouchCancelHandler handler) {
		WidgetPointerState state = getState(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, state);
		return registerHandler(w, state, w.addDomHandler(
				new TouchCancelToPointerCancelHandler(handler, state.manager),
				PointerCancelEvent.getType()));
	}

	@Override
	public HandlerRegistration addTouchEndHandler(Widget w,
			TouchEndHandler handler) {
		WidgetPointerState state = getState(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, state);
		return registerHandler(w, state, w.addDomHandler(
				new TouchEndToPointerUpHandler(handler, state.manager),
				PointerUpEvent.getType()));
	}

}

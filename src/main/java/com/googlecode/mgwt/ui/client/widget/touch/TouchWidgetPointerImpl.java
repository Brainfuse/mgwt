package com.googlecode.mgwt.ui.client.widget.touch;

import java.util.IdentityHashMap;
import java.util.Map;

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
	 * Tracks active pointers per widget so a leaked pointer lifecycle on one
	 * element cannot poison touch state for unrelated widgets.
	 */
	private final Map<Widget, PointerTouchManager> managers = new IdentityHashMap<Widget, PointerTouchManager>();

	/**
	 * Remembers which widgets already have internal pointer lifecycle cleanup
	 * handlers installed.
	 */
	private final Map<Widget, Boolean> cleanupInstalled = new IdentityHashMap<Widget, Boolean>();

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

	private PointerTouchManager getManager(Widget w) {
		PointerTouchManager manager = managers.get(w);
		if (manager == null) {
			manager = new PointerTouchManager();
			managers.put(w, manager);
		}
		return manager;
	}

	private void ensurePointerLifecycleCleanup(Widget w,
			final PointerTouchManager manager) {
		if (cleanupInstalled.containsKey(w)) {
			return;
		}

		w.addDomHandler(new PointerUpEvent.PointerUpHandler() {
			@Override
			public void onPointerUp(PointerUpEvent event) {
				manager.pointerUp(event.getPointerId(), event.getClientX(), event.getClientY());
			}
		}, PointerUpEvent.getType());

		w.addDomHandler(new PointerCancelEvent.PointerCancelHandler() {
			@Override
			public void onPointerCancel(PointerCancelEvent event) {
				manager.pointerCancel(event.getPointerId());
			}
		}, PointerCancelEvent.getType());

		cleanupInstalled.put(w, Boolean.TRUE);
	}

	@Override
	public HandlerRegistration addTouchStartHandler(Widget w,
			TouchStartHandler handler) {
		PointerTouchManager manager = getManager(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, manager);
		return w.addDomHandler(
				new TouchStartToPointerDownHandler(handler, manager),
				PointerDownEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w,
			TouchMoveHandler handler) {
		PointerTouchManager manager = getManager(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, manager);
		return w.addDomHandler(
				new TouchMoveToPointerMoveHandler(handler, manager),
				PointerMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w,
			TouchCancelHandler handler) {
		PointerTouchManager manager = getManager(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, manager);
		return w.addDomHandler(
				new TouchCancelToPointerCancelHandler(handler, manager),
				PointerCancelEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchEndHandler(Widget w,
			TouchEndHandler handler) {
		PointerTouchManager manager = getManager(w);
		ensureTouchActionNone(w);
		ensurePointerLifecycleCleanup(w, manager);
		return w.addDomHandler(
				new TouchEndToPointerUpHandler(handler, manager),
				PointerUpEvent.getType());
	}

}

package com.googlecode.mgwt.ui.client.widget.touch;

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
 * mouse, touch, and pen/stylus input into a single event model. A shared
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
	 * Shared manager that tracks active pointers across all handler types.
	 * This is the key to multi-touch support: all adapters read from and
	 * write to the same pointer state.
	 */
	private final PointerTouchManager manager = new PointerTouchManager();

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

	@Override
	public HandlerRegistration addTouchStartHandler(Widget w,
			TouchStartHandler handler) {
		ensureTouchActionNone(w);
		return w.addDomHandler(
				new TouchStartToPointerDownHandler(handler, manager),
				PointerDownEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w,
			TouchMoveHandler handler) {
		ensureTouchActionNone(w);
		return w.addDomHandler(
				new TouchMoveToPointerMoveHandler(handler, manager),
				PointerMoveEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w,
			TouchCancelHandler handler) {
		ensureTouchActionNone(w);
		return w.addDomHandler(
				new TouchCancelToPointerCancelHandler(handler, manager),
				PointerCancelEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchEndHandler(Widget w,
			TouchEndHandler handler) {
		ensureTouchActionNone(w);
		return w.addDomHandler(
				new TouchEndToPointerUpHandler(handler, manager),
				PointerUpEvent.getType());
	}

}

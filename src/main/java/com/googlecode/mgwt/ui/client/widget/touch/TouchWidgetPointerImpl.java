package com.googlecode.mgwt.ui.client.widget.touch;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerCancelEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerDownEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerMoveEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerUpEvent;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchCancelToPointerCancelHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchEndToPointerUpHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchMoveToPointerMoveHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchStartToPointerDownHandler;

/**
 * The implementation for window tablets and adds multi-touch. For Edge, IE
 */
public class TouchWidgetPointerImpl implements TouchWidgetImpl {

	@Override
	public HandlerRegistration addTouchStartHandler(Widget w,
			TouchStartHandler handler) {
		return w.addDomHandler(new TouchStartToPointerDownHandler(handler),
				PointerDownEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w,
			TouchMoveHandler handler) {
		TouchMoveToPointerMoveHandler touchMoveToPointer = new TouchMoveToPointerMoveHandler(
				handler);
		HandlerRegistrationCollection col = new HandlerRegistrationCollection();
		col.addHandlerRegistration(w.addDomHandler(touchMoveToPointer,
				PointerMoveEvent.getType()));
		col.addHandlerRegistration(
				w.addDomHandler(touchMoveToPointer, PointerUpEvent.getType()));
		col.addHandlerRegistration(w.addDomHandler(touchMoveToPointer,
				PointerCancelEvent.getType()));

		return col;
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w,
			TouchCancelHandler handler) {
		return w.addDomHandler(new TouchCancelToPointerCancelHandler(handler),
				PointerCancelEvent.getType());
	}

	@Override
	public HandlerRegistration addTouchEndHandler(Widget w,
			TouchEndHandler handler) {
		return w.addDomHandler(new TouchEndToPointerUpHandler(handler),
				PointerUpEvent.getType());
	}

}

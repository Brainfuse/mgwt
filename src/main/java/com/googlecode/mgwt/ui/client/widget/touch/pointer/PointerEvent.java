package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.event.shared.EventHandler;

public abstract class PointerEvent<H extends EventHandler>
		extends MouseEvent<H> {

	private native static boolean isIE10PointerEventModel() /*-{
		return (!$wnd.PointerEvent);
	}-*/;

	public static final String POINTERDOWN;
	public static final String POINTERMOVE;
	public static final String POINTEROUT;
	public static final String POINTEROVER;
	public static final String POINTERUP;
	public static final String POINTERCANCEL;

	static {
		if (isIE10PointerEventModel()) {
			POINTERDOWN = "MSPointerDown";
			POINTERMOVE = "MSPointerMove";
			POINTEROUT = "MSPointerOut";
			POINTEROVER = "MSPointerOver";
			POINTERUP = "MSPointerUp";
			POINTERCANCEL = "MSPointerCancel";
		} else {
			POINTERDOWN = "pointerdown";
			POINTERMOVE = "pointermove";
			POINTEROUT = "pointerout";
			POINTEROVER = "pointerover";
			POINTERUP = "pointerup";
			POINTERCANCEL = "pointercancel";
		}
	}

	public final native int getPointerId() /*-{
		var e = this.@com.google.gwt.event.dom.client.DomEvent::nativeEvent;
		return e.pointerId;
	}-*/;

}

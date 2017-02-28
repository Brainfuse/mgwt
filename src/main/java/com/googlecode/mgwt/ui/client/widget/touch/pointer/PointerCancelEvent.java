package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.shared.EventHandler;

public class PointerCancelEvent extends PointerEvent<PointerCancelEvent.PointerCancelHandler>{

	public interface PointerCancelHandler extends EventHandler {
		void onPointerCancel(PointerCancelEvent event);
	}

	private static final Type<PointerCancelHandler> TYPE = new Type<PointerCancelHandler>(
			PointerEvent.POINTERCANCEL, new PointerCancelEvent());
	
	
	protected PointerCancelEvent(){}
	
	public static Type<PointerCancelHandler> getType(){
		return TYPE;
	}
	
	@Override
	public Type<PointerCancelHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerCancelHandler handler) {
		handler.onPointerCancel(this);
	}
	
}

package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.shared.EventHandler;

public class PointerDownEvent extends PointerEvent<PointerDownEvent.PointerDownHandler>{

	public interface PointerDownHandler extends EventHandler {
		void onPointerDown(PointerDownEvent event);
	}

	private static final Type<PointerDownHandler> TYPE = new Type<PointerDownHandler>(
			PointerEvent.POINTERDOWN, new PointerDownEvent());
	
	
	protected PointerDownEvent(){}
	
	public static Type<PointerDownHandler> getType(){
		return TYPE;
	}
	
	@Override
	public Type<PointerDownHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerDownHandler handler) {
		handler.onPointerDown(this);
	}

}

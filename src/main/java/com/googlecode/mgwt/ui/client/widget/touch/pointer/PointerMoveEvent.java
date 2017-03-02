package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.shared.EventHandler;

public class PointerMoveEvent extends PointerEvent<PointerMoveEvent.PointerMoveHandler>{

	
	public interface PointerMoveHandler extends EventHandler {
		void onPointerMove(PointerMoveEvent event);
	}

	private static final Type<PointerMoveHandler> TYPE = new Type<PointerMoveHandler>(
			PointerEvent.POINTERMOVE, new PointerMoveEvent());
	
	
	protected PointerMoveEvent(){}
	
	public static Type<PointerMoveHandler> getType(){
		return TYPE;
	}
	
	@Override
	public Type<PointerMoveHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerMoveHandler handler) {
		handler.onPointerMove(this);
	}

}

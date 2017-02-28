package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.shared.EventHandler;

public class PointerUpEvent extends PointerEvent<PointerUpEvent.PointerUpHandler>{

	public interface PointerUpHandler extends EventHandler {
		void onPointerUp(PointerUpEvent event);
	}

	private static final Type<PointerUpHandler> TYPE = new Type<PointerUpHandler>(
			PointerEvent.POINTERUP, new PointerUpEvent());
	
	
	protected PointerUpEvent(){}
	
	public static Type<PointerUpHandler> getType(){
		return TYPE;
	}
	
	@Override
	public Type<PointerUpHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerUpHandler handler) {
		handler.onPointerUp(this);
	}

	
}

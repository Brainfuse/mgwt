package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.shared.EventHandler;

public class PointerOutEvent extends PointerEvent<PointerOutEvent.PointerOutHandler>{

	public interface PointerOutHandler extends EventHandler {
		void onPointerOut(PointerOutEvent event);
	}
	
	private static final Type<PointerOutHandler> TYPE = new Type<PointerOutHandler>(
			PointerEvent.POINTEROUT, new PointerOutEvent());
	
	
	protected PointerOutEvent(){}
	
	public static Type<PointerOutHandler> getType(){
		return TYPE;
	}
	
	@Override
	public Type<PointerOutHandler> getAssociatedType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerOutHandler handler) {
		handler.onPointerOut(this);
	}
	
	
}

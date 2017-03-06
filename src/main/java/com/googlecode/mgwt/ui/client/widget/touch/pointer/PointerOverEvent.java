package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.event.shared.EventHandler;

public class PointerOverEvent extends PointerEvent<PointerOverEvent.PointerOverHandler>{
	
	public interface PointerOverHandler extends EventHandler {
		void onPointerOver(PointerOverEvent event);
	}
	
	private static final Type<PointerOverHandler> TYPE = new Type<PointerOverHandler>(
			PointerEvent.POINTEROVER,new PointerOverEvent());

	protected PointerOverEvent(){}
	
	@Override
	public Type<PointerOverHandler> getAssociatedType() {
		return TYPE;
	}
	
	public static Type<PointerOverHandler> getType() {
		return TYPE;
	}

	@Override
	protected void dispatch(PointerOverHandler handler) {
		handler.onPointerOver(this);
	}

}

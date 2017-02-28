package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.googlecode.mgwt.collection.shared.CollectionFactory;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;

public class TouchEndToPointerUpHandler implements PointerUpEvent.PointerUpHandler{

	private final TouchEndHandler handler;
	
	public TouchEndToPointerUpHandler(TouchEndHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void onPointerUp(PointerUpEvent event) {
		SimulatedTouchEndEvent touchEnd = new SimulatedTouchEndEvent(event);
		handler.onTouchEnd(touchEnd);
	}
	
	private class SimulatedTouchEndEvent extends TouchEndEvent {
		private LightArray<Touch> touches;
		public SimulatedTouchEndEvent(final PointerUpEvent event) {
			touches = CollectionFactory.constructArray();
			Touch touch = new Touch(){
				@Override
				public int getPageX() {
					return event.getClientX();
				}

				@Override
				public int getPageY() {
					return event.getClientY();
				}

				@Override
				public int getIdentifier() {
					return event.getPointerId();
				}};
			touches.push(touch);
			setNativeEvent(event.getNativeEvent());
			setSource(event.getSource());
		}

		@Override
		public LightArray<Touch> getChangedTouches() {
			return touches;
		}
		
		@Override
		public LightArray<Touch> getTouches() {
			return touches;
		}
	}

}

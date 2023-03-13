package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.CollectionFactory;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerCancelEvent.PointerCancelHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerDownEvent.PointerDownHandler;

public class TouchMoveToPointerMoveHandler implements PointerMoveEvent.PointerMoveHandler, PointerUpEvent.PointerUpHandler, PointerCancelHandler, PointerDownHandler{

	private final TouchMoveHandler handler;
	private boolean ignoreEvent;
	
	public TouchMoveToPointerMoveHandler(TouchMoveHandler handler){
		this.handler = handler;
		ignoreEvent = true;
	}
	
	@Override
	public void onPointerMove(PointerMoveEvent event) {
		if(ignoreEvent) return;
		_onPointerMove(event.getNativeEvent());
	}

	private void handleEvent(JsArray<NativeEvent> events) {
		SimulatedTouchMoveEvent touchMove = new SimulatedTouchMoveEvent(events);
		handler.onTouchMove(touchMove);
	}
	
	private native void _onPointerMove(NativeEvent event)/*-{
		var self = this;
		self.@com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchMoveToPointerMoveHandler::handleEvent(Lcom/google/gwt/core/client/JsArray;)([event]);
	}-*/;
	
	
	@Override
	public void onPointerCancel(PointerCancelEvent event) {
		_clear();
		ignoreEvent = true;
	}

	@Override
	public void onPointerUp(PointerUpEvent event) {
		_clear();
		ignoreEvent = true;
	}
	
	@Override
	public void onPointerDown(PointerDownEvent event) {
		ignoreEvent = false;
	}

	private native void _clear()/*-{
		var self = this;
		self.touchEvents = [];
	}-*/;
	
	private class SimulatedTouchMoveEvent extends TouchMoveEvent{
		
		private LightArray<Touch> touches;
		private JsArray<NativeEvent> events;
		
		public SimulatedTouchMoveEvent(JsArray<NativeEvent> events){
			
			this.events = events;
			touches = CollectionFactory.constructArray();
			for(int i=0; i<events.length(); i++){
				final NativeEvent e = events.get(i);
				Touch touch = new Touch() {
					
					@Override
					public int getPageY() {
						return e.getClientY();
					}
					
					@Override
					public int getPageX() {
						return e.getClientX();
					}
					
					@Override
					public int getIdentifier() {
						return getPointerId(e);
					}
				};
				touches.push(touch);
			}
			NativeEvent nativeEvent = events.get(0);
			setNativeEvent(nativeEvent);
			setSource(nativeEvent);
		}
		
		@Override
		public void preventDefault() {
			for(int i=0; i<events.length(); i++){
				NativeEvent event = events.get(i);
				event.preventDefault();
			}
		}
		
		@Override
		public void stopPropagation() {
			for(int i=0; i<events.length(); i++){
				NativeEvent event = events.get(i);
				event.stopPropagation();
			}
		}
		
		private final native int getPointerId(NativeEvent event)/*-{
			return event.pointerId;
		}-*/;
		
		@Override
		public LightArray<Touch> getTouches() {
			return touches;
		}
		
		@Override
		public LightArray<Touch> getChangedTouches() {
			return touches;
		}
		
	}


}

package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.CollectionFactory;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;

public class TouchMoveToPointerMoveHandler implements PointerMoveEvent.PointerMoveHandler{

	private final TouchMoveHandler handler;
	
	public TouchMoveToPointerMoveHandler(TouchMoveHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void onPointerMove(PointerMoveEvent event) {
		_onPointerMove(event.getNativeEvent());
	}

	private void handleEvent(JsArray<NativeEvent> events) {
		SimulatedTouchMoveEvent touchMove = new SimulatedTouchMoveEvent(events);
		handler.onTouchMove(touchMove);
	}
	
	private native void _onPointerMove(NativeEvent event)/*-{
		var self = this;
		
		if(!self.touchEvents){
			self.touchEvents = [];
			self.touchEventTimer = null;
		}
		
		var isPrimary = event.isPrimary;
		if(isPrimary){
			self.touchEvents = [];
			self.touchEvents.push(event);
		}else {
			self.touchEvents.push(event);
		}
		
		self.touchEventTimer = $wnd.setTimeout(function(){
				fireEvents(self.touchEvents);
		},10);

		function fireEvents(events){
			self.@com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchMoveToPointerMoveHandler::handleEvent(Lcom/google/gwt/core/client/JsArray;)(events);
		}
		
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

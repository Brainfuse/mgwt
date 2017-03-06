package com.googlecode.mgwt.ui.client.widget.touch.pointer;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.dom.client.NativeEvent;
import com.googlecode.mgwt.collection.shared.CollectionFactory;
import com.googlecode.mgwt.collection.shared.LightArray;
import com.googlecode.mgwt.dom.client.event.touch.Touch;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.widget.touch.pointer.PointerDownEvent.PointerDownHandler;

public class TouchStartToPointerDownHandler implements PointerDownHandler{

	private final TouchStartHandler handler;
	
	public TouchStartToPointerDownHandler(TouchStartHandler handler){
		this.handler = handler;
	}
	
	@Override
	public void onPointerDown(PointerDownEvent event) {
		_onPointerDown(event.getNativeEvent());
	}

	private void handleEvent(JsArray<NativeEvent> events) {

		SimulatedTouchStartEvent touchStart = new SimulatedTouchStartEvent(
				events);
		handler.onTouchStart(touchStart);
	}
	
	private native void _onPointerDown(NativeEvent event)/*-{
		event.preventDefault();
		
		var self = this;
		if(self.touchEventTimer){
			$wnd.clearTimeout(self.touchEventTimer);
			self.touchEventTimer = null;
		}else {
			self.touchEvents = [];
		}
		
		self.touchEvents.push(event);
		
		self.touchEventTimer = $wnd.setTimeout(function(){
			self.@com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchStartToPointerDownHandler::handleEvent(Lcom/google/gwt/core/client/JsArray;)(self.touchEvents);
			self.touchEvents = [];
			self.touchEventTimer = null;
		},50);
		
	}-*/;
	
	
	private class SimulatedTouchStartEvent extends TouchStartEvent {
		
		private LightArray<Touch> touches;
		private JsArray<NativeEvent> events;
		
		public SimulatedTouchStartEvent(JsArray<NativeEvent> events){
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
		
		private final native int getPointerId(NativeEvent event)/*-{
			return event.pointerId;
		}-*/;
		
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

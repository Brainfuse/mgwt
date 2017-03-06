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
		if(!self.touchEvents){
			self.touchEvents = [];
		}
		
		var pointerId = event.pointerId;
		var touchEvents = self.touchEvents;
		if(!touchEvents[pointerId]){
			touchEvents[pointerId]=[];
		}
		
		touchEvents[pointerId].push(event);
		
		self.touchEventTimer = $wnd.setTimeout(fireEvents,10);

		self.fireEvents = function fireEvents(){
			
			if(self.touchEvents.length < 1) return;
			var max = getMaxArraySize();
			
			for(var i=0; i<max; i++){
				var events = [];
				for(var key in self.touchEvents){
					var arr = self.touchEvents[key];
					var event;
					if(i<arr.length){
						event = arr[i];
					}else {
						event = arr[arr.length-1];
					}
					events.push(event);
				}
//			log(events);
			self.@com.googlecode.mgwt.ui.client.widget.touch.pointer.TouchMoveToPointerMoveHandler::handleEvent(Lcom/google/gwt/core/client/JsArray;)(events);
			}
			self.touchEvents = [];
		}
		
		function log(events){
			var log = "";
			for(var i=0; i<events.length; i++){
				var event = events[i];
				log += " id "+event.pointerId +" x "+event.clientX +" y "+event.clientY +", ";
			}
			$wnd.console.log("fireEvent size "+events.length+" LOG: "+log);
		}
		
		function getMaxArraySize(){
			var max = 0;
			for(var key in self.touchEvents){
				var arr = self.touchEvents[key];
				max = Math.max(max,arr.length);
			}
			return max;
		}
		
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
		self.fireEvents();
		if(self.touchEventTimer != null){
			$wnd.clearTimeout(self.touchEventTimer);
			self.touchEventTimer = null;
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

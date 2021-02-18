/*
 * Copyright 2011 Daniel Kurka
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.googlecode.mgwt.ui.client.widget.touch;

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.dom.client.event.mouse.HandlerRegistrationCollection;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartEvent;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;
import com.googlecode.mgwt.ui.client.MGWT;
import com.googlecode.mgwt.ui.client.util.MGWTUtil;

/**
 * The implementation for touch devices of {@link TouchWidgetImpl}
 *
 * @author Daniel Kurka
 * @version $Id: $
 */
public class TouchWidgetMobileImpl implements TouchWidgetImpl {

	private TouchWidgetImpl impl;
	public TouchWidgetMobileImpl() {
		boolean desktopTouchDevice = MGWT.getOsDetection().isDesktop()
				&& MGWTUtil.isChromeOnWindowTouchDevice();
		
		if(desktopTouchDevice) {
			impl = new TouchAndMouseImpl();
		}else {
			impl = new TouchOnlyImpl();	
		}
	} 
		
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchStartHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchStartHandler(Widget w, final TouchStartHandler handler) {
		return impl.addTouchStartHandler(w, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchMoveHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w, TouchMoveHandler handler) {
		return impl.addTouchMoveHandler(w, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchCancelHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w, TouchCancelHandler handler) {
		return impl.addTouchCancelHandler(w, handler);
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchEndHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchEndHandler(Widget w, TouchEndHandler handler) {
		return impl.addTouchEndHandler(w, handler);
	}
	
	private class TouchOnlyImpl implements TouchWidgetImpl {
		@Override
		public HandlerRegistration addTouchStartHandler(Widget w,
				TouchStartHandler handler) {
			return w.addDomHandler(handler, TouchStartEvent.getType());
		}

		@Override
		public HandlerRegistration addTouchMoveHandler(Widget w,
				TouchMoveHandler handler) {
			return w.addDomHandler(handler, TouchMoveEvent.getType());
		}

		@Override
		public HandlerRegistration addTouchCancelHandler(Widget w,
				TouchCancelHandler handler) {
			return w.addDomHandler(handler, TouchCancelEvent.getType());
		}

		@Override
		public HandlerRegistration addTouchEndHandler(Widget w,
				TouchEndHandler handler) {
			return w.addDomHandler(handler, TouchEndEvent.getType());
		}

	}
	
	/**
	 * for desktop device with both a touch screen and a mouse 
	 */
	private class TouchAndMouseImpl implements TouchWidgetImpl {

		private TouchWidgetDesktopImpl mouseImpl;
		public TouchAndMouseImpl() {
			mouseImpl = new TouchWidgetDesktopImpl();
		}
		
		@Override
		public HandlerRegistration addTouchStartHandler(Widget w,
				final TouchStartHandler handler) {
			HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
			
			handlers.addHandlerRegistration(w.addDomHandler(new TouchStartHandler() {
				@Override
				public void onTouchStart(TouchStartEvent event) {
					handler.onTouchStart(event);
					/**
					 * prevent firing another touch start event from the mouseImpl
					 */
					event.stopPropagation();
					event.preventDefault();
				}
			}, TouchStartEvent.getType()));
			
			handlers.addHandlerRegistration(mouseImpl.addTouchStartHandler(w, handler));
			return handlers;
		}

		@Override
		public HandlerRegistration addTouchMoveHandler(Widget w,
				TouchMoveHandler handler) {
			HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
			handlers.addHandlerRegistration(w.addDomHandler(handler, TouchMoveEvent.getType()));
			handlers.addHandlerRegistration(mouseImpl.addTouchMoveHandler(w, handler));
			return handlers;
		}

		@Override
		public HandlerRegistration addTouchCancelHandler(Widget w,
				TouchCancelHandler handler) {
			HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
			handlers.addHandlerRegistration(
					w.addDomHandler(handler, TouchCancelEvent.getType()));
			handlers.addHandlerRegistration(
					mouseImpl.addTouchCancelHandler(w, handler));
			return handlers;
		}

		@Override
		public HandlerRegistration addTouchEndHandler(Widget w,
				TouchEndHandler handler) {
			HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
			handlers.addHandlerRegistration(w.addDomHandler(handler, TouchEndEvent.getType()));
			handlers.addHandlerRegistration(mouseImpl.addTouchEndHandler(w, handler));
			return handlers;
		}
		
	}
	

}

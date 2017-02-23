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
import com.googlecode.mgwt.ui.client.util.MGWTUtil;

/**
 * The implementation for touch devices of {@link TouchWidgetImpl}
 *
 * @author Daniel Kurka
 * @version $Id: $
 */
public class TouchWidgetMobileImpl implements TouchWidgetImpl {

	private TouchWidgetDesktopImpl mouseImpl;
	private boolean mouseEnabled = false;
	
	public TouchWidgetMobileImpl() {
		mouseEnabled = MGWTUtil.isChromeOnWindowTouchDevice();
	} 
	
	
	private TouchWidgetDesktopImpl getMouseImpl(){
		if(mouseImpl == null){
			mouseImpl = new TouchWidgetDesktopImpl();
		}
		return mouseImpl;
	}
	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchStartHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchStartHandler(Widget w, TouchStartHandler handler) {
		HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
		handlers.addHandlerRegistration(w.addDomHandler(handler, TouchStartEvent.getType()));
		if(mouseEnabled){
			handlers.addHandlerRegistration(getMouseImpl().addTouchStartHandler(w, handler));
		}
		return handlers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchMoveHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w, TouchMoveHandler handler) {
		HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
		handlers.addHandlerRegistration(w.addDomHandler(handler, TouchMoveEvent.getType()));
		if(mouseEnabled){
			handlers.addHandlerRegistration(getMouseImpl().addTouchMoveHandler(w, handler));
		}
		return handlers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchCancelHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w, TouchCancelHandler handler) {
		HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
		handlers.addHandlerRegistration(w.addDomHandler(handler, TouchCancelEvent.getType()));
		if(mouseEnabled){
			handlers.addHandlerRegistration(getMouseImpl().addTouchCancelHandler(w, handler));
		}
		return handlers;
	}

	/*
	 * (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.touch.TouchWidgetImpl#addTouchEndHandler(com.google.gwt.user.client.ui.Widget, com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler)
	 */
	/** {@inheritDoc} */
	@Override
	public HandlerRegistration addTouchEndHandler(Widget w, TouchEndHandler handler) {
		HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();
		handlers.addHandlerRegistration(w.addDomHandler(handler, TouchEndEvent.getType()));
		if(mouseEnabled){
			handlers.addHandlerRegistration(getMouseImpl().addTouchEndHandler(w, handler));
		}
		return handlers;
	}

}

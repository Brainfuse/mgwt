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
import com.googlecode.mgwt.dom.client.event.touch.TouchCancelHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchEndHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchMoveHandler;
import com.googlecode.mgwt.dom.client.event.touch.TouchStartHandler;

/**
 * The implementation for non-mobile (desktop) user agents of
 * {@link TouchWidgetImpl}. Delegates all detection logic to
 * {@link TouchWidgetImplResolver}.
 * 
 * @author Daniel Kurka
 * @version $Id: $
 */
public class TouchWidgetDesktopImpl implements TouchWidgetImpl {

	private final TouchWidgetImpl delegate;

	public TouchWidgetDesktopImpl() {
		delegate = TouchWidgetImplResolver.resolve(false);
	}

	@Override
	public HandlerRegistration addTouchStartHandler(Widget w,
			TouchStartHandler handler) {
		return delegate.addTouchStartHandler(w, handler);
	}

	@Override
	public HandlerRegistration addTouchMoveHandler(Widget w,
			TouchMoveHandler handler) {
		return delegate.addTouchMoveHandler(w, handler);
	}

	@Override
	public HandlerRegistration addTouchCancelHandler(Widget w,
			TouchCancelHandler handler) {
		return delegate.addTouchCancelHandler(w, handler);
	}

	@Override
	public HandlerRegistration addTouchEndHandler(Widget w,
			TouchEndHandler handler) {
		return delegate.addTouchEndHandler(w, handler);
	}

}

/*
 * Copyright 2010 Daniel Kurka
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
 * Centralized resolution of the correct {@link TouchWidgetImpl} based on
 * runtime platform capabilities. This class implements {@link TouchWidgetImpl}
 * directly so that it can be used as a GWT deferred binding replacement in
 * {@code UI.gwt.xml}. It resolves the best delegate at construction time and
 * forwards all calls.
 *
 * <p>Resolution order:
 * <ol>
 *   <li><b>Pointer Events</b> ({@link TouchWidgetPointerImpl}) — used whenever
 *       the browser supports the W3C Pointer Events API. This covers all modern
 *       browsers on all platforms (desktop, mobile, Electron) and unifies mouse,
 *       touch, and pen/stylus input with full multi-touch support.</li>
 *   <li><b>Native Touch</b> ({@code TouchOnlyImpl}) — used on mobile browsers
 *       that don't support Pointer Events (very old mobile browsers).</li>
 *   <li><b>Touch + Mouse</b> ({@code TouchAndMouseImpl}) — used on desktop
 *       browsers detected as touch-capable Windows devices without Pointer
 *       Events (e.g. older Chrome on Windows touch laptops).</li>
 *   <li><b>Mouse only</b> ({@link TouchWidgetMouseImpl}) — last resort for
 *       desktop browsers with no Pointer Events and no touch support.</li>
 * </ol>
 */
public class TouchWidgetImplResolver implements TouchWidgetImpl {

	private final TouchWidgetImpl delegate;

	/**
	 * Default constructor used by {@code GWT.create()}. Detects the platform
	 * at runtime and selects the best {@link TouchWidgetImpl}.
	 */
	public TouchWidgetImplResolver() {
		boolean isMobile = !MGWT.getOsDetection().isDesktop();
		delegate = resolve(isMobile);
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

	/**
	 * Resolves the correct {@link TouchWidgetImpl} for the current runtime
	 * environment.
	 *
	 * @param isMobile {@code true} if the runtime detects a mobile browser
	 * @return the appropriate {@link TouchWidgetImpl} implementation
	 */
	static TouchWidgetImpl resolve(boolean isMobile) {
		// Pointer Events is the preferred implementation for all platforms.
		// It unifies mouse, touch, and pen/stylus (Wacom, Surface Pen) input
		// with proper multi-touch support.
		if (MGWTUtil.isPointerEventSupported()) {
			return new TouchWidgetPointerImpl();
		}

		// Fallbacks for browsers without Pointer Events support
		if (isMobile) {
			boolean desktopTouchDevice = MGWT.getOsDetection().isDesktop()
					&& MGWTUtil.isChromeOnWindowTouchDevice();
			if (desktopTouchDevice) {
				return new TouchAndMouseImpl();
			}
			return new TouchOnlyImpl();
		}

		// Desktop without pointer events — mouse only
		return new TouchWidgetMouseImpl();
	}

	/**
	 * Native touch events only. Used on mobile browsers that don't support
	 * Pointer Events.
	 */
	static class TouchOnlyImpl implements TouchWidgetImpl {

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
	 * Dual touch + mouse handling for desktop browsers with a touch screen
	 * but no Pointer Events support. Listens for both native touch events
	 * and simulated mouse events.
	 */
	static class TouchAndMouseImpl implements TouchWidgetImpl {

		private final TouchWidgetMouseImpl mouseImpl;

		TouchAndMouseImpl() {
			mouseImpl = new TouchWidgetMouseImpl();
		}

		@Override
		public HandlerRegistration addTouchStartHandler(Widget w,
				final TouchStartHandler handler) {
			HandlerRegistrationCollection handlers = new HandlerRegistrationCollection();

			handlers.addHandlerRegistration(w.addDomHandler(new TouchStartHandler() {
				@Override
				public void onTouchStart(TouchStartEvent event) {
					handler.onTouchStart(event);
					// prevent firing another touch start event from the mouseImpl
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

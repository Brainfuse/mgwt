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
package com.googlecode.mgwt.ui.client.widget.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.theme.base.LayoutCss;
import com.googlecode.mgwt.ui.client.widget.HeaderList;
import com.googlecode.mgwt.ui.client.widget.HeaderPanel;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;
import com.googlecode.mgwt.ui.client.widget.buttonbar.ButtonBar;

/**
 * A layout panel can resize children to take up remaining space on the screen
 * 
 * This is done automatically for {@link ScrollPanel}
 * 
 * Other children that want to fill space should add the style
 * {@link LayoutCss#fillPanelExpandChild()}
 * 
 * @author Daniel Kurka
 * @version $Id: $
 */
public class LayoutPanelIE9Impl extends LayoutPanelBaseImpl implements
		HasWidgets, InsertPanel {
	private FlowPanel main;
	private int top;
	private int bottom;
	private Widget widgetToExpand;
	private LayoutPanelIE9Css css;
	private List<Registration> registrations = new ArrayList<Registration>();
	class Registration{
		private Element element;
		private JavaScriptObject handler;
		public Registration(Element element, JavaScriptObject handler) {
			super();
			this.element = element;
			this.handler = handler;
		}
		
	}

	/**
	 * Construct a layout panel
	 */
	public LayoutPanelIE9Impl(LayoutPanelIE9Css css) {
		super();
		this.css = css;
		css.ensureInjected();

		main = new FlowPanel();
		initWidget(main);
		main.addStyleName(css.fillPanel());
	}

	public LayoutPanelIE9Impl() {
		this(LayoutPanelIE9Bundle.INSTANCE.css());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client
	 * .ui.Widget)
	 */
	/** {@inheritDoc} */
	@Override
	public void add(Widget w) {

		// TODO markup interface?

		// TODO Create a new Css with the top and bottom and position absolute
		// settings. Add that to each child widget.
		if (w instanceof HeaderPanel) {
			w.addStyleName(css.headerPanel());
			w.addAttachHandler(new AttachEvent.Handler() {
				@Override
				public void onAttachOrDetach(AttachEvent event) {
					updateTopAndBottom();
				}
			});
		} else if (w instanceof ScrollPanel || w instanceof HeaderList) {
			this.widgetToExpand = w;
			w.addStyleName(css.content());
		} else if (w instanceof ButtonBar) {
			w.addStyleName(css.buttonBar());
			
			JavaScriptObject handler = registerResizeCallback(w.getElement(), new AsyncCallback<Void>(	) {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onSuccess(Void result) {
					updateTopAndBottom();
				}
				
			});
			registrations.add(new Registration(w.getElement(), handler));
		}
		main.add(w);
	}

	public static native JavaScriptObject registerResizeCallback(Element elem,
			AsyncCallback<Void> callback) /*-{
		    var handler = function() {
		      $entry(
		      @com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelIE9Impl::fireResizeEvent(Lcom/google/gwt/user/client/rpc/AsyncCallback;)(callback));
		    };
		    elem.attachEvent("onresize", handler);
		    return handler;
		  }-*/;
	public static native JavaScriptObject deRegisterResizeCallback(Element elem,
			JavaScriptObject handler) /*-{
		    
		    elem.detachEvent ("onresize", handler);
		    return handler;
		  }-*/;
	
	public static void fireResizeEvent(AsyncCallback<Void> callback ){
		callback.onSuccess(null);
	}

	public void updateTopAndBottom() {

		/**
		 * Waiting for the browser event cycle to be done before updating the
		 * widgets position.
		 */
		Scheduler.get().scheduleDeferred(new ScheduledCommand() {

			@Override
			public void execute() {
				if (widgetToExpand == null)
					return;
				top = 0;
				bottom = 0;
				for (int i = 0; i < getWidgetCount(); i++) {
					Widget w = getWidget(i);
					if (w instanceof HeaderPanel)
						top += w.getOffsetHeight();
					else if (w instanceof ScrollPanel
							|| w instanceof HeaderList) {
						// Do nothing that is the one we are supposed to flex;
					} else {
						// Let's get the bottom of the flex widget.
						bottom = w.getOffsetHeight();
					}
				}
				Style style = widgetToExpand.getElement().getStyle();
				style.setTop(top, Unit.PX);
				style.setBottom(bottom, Unit.PX);
			}
		});

	}

	/**
	 * Layout children horizontally
	 * 
	 * default: false
	 * 
	 * @param horiontal
	 *            true to layout children horizontally
	 */
	public void setHorizontal(boolean horiontal) {
		/**
		 * TODO implements horizontal layout. if (horiontal) {
		 * 
		 * addStyleName(css.fillPanelHorizontal()); } else {
		 * removeStyleName(css.fillPanelHorizontal()); }
		 */
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasWidgets#clear()
	 */
	/** {@inheritDoc} */
	@Override
	public void clear() {
		widgetToExpand = null;
		main.clear();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
	 */
	/** {@inheritDoc} */
	@Override
	public Iterator<Widget> iterator() {
		return main.iterator();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client
	 * .ui.Widget)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean remove(Widget w) {
		return main.remove(w);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
	 */
	/** {@inheritDoc} */
	@Override
	public Widget getWidget(int index) {
		return main.getWidget(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
	 */
	/** {@inheritDoc} */
	@Override
	public int getWidgetCount() {
		return main.getWidgetCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.
	 * gwt.user.client.ui.Widget)
	 */
	/** {@inheritDoc} */
	@Override
	public int getWidgetIndex(Widget child) {
		return main.getWidgetIndex(child);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
	 */
	/** {@inheritDoc} */
	@Override
	public boolean remove(int index) {
		return main.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.google.gwt.user.client.ui.InsertPanel#insert(com.google.gwt.user.
	 * client.ui.Widget, int)
	 */
	/** {@inheritDoc} */
	@Override
	public void insert(Widget w, int beforeIndex) {
		main.insert(w, beforeIndex);

	}

	@Override
	protected void onDetach() {
		super.onDetach();
		for ( Registration r : registrations){
			deRegisterResizeCallback(r.element, r.handler);
		}
	}
}

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

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.MGWTStyle;
import com.googlecode.mgwt.ui.client.theme.base.LayoutCss;
import com.googlecode.mgwt.ui.client.widget.ExpandChildWidget;
import com.googlecode.mgwt.ui.client.widget.HeaderList;
import com.googlecode.mgwt.ui.client.widget.ScrollPanel;

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
public class LayoutPanelDefaultImpl extends LayoutPanelBaseImpl implements HasWidgets, InsertPanel {
	private FlowPanel main;
	private final LayoutCss css;

	/**
	 * Construct a layout panel with a given css
	 * 
	 * @param css the css to use
	 */
	public LayoutPanelDefaultImpl(LayoutCss css) {
		this.css = css;
		css.ensureInjected();
		main = new FlowPanel();
		initWidget(main);

		main.addStyleName(css.fillPanel());
	}

	/**
	 * Construct a layout panel
	 */
	public LayoutPanelDefaultImpl() {
		this(MGWTStyle.getTheme().getMGWTClientBundle().getLayoutCss());
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#add(com.google.gwt.user.client.ui.Widget)
	 */
	
	@Override
	public void add(Widget w) {

		//TODO markup interface?
		if (w instanceof ScrollPanel || w instanceof HeaderList || w instanceof ExpandChildWidget) {
			w.addStyleName(css.fillPanelExpandChild());
		}

		main.add(w);
	}

	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#setHorizontal(boolean)
	 */
	@Override
	public void setHorizontal(boolean horiontal) {
		if (horiontal) {
			addStyleName(css.fillPanelHorizontal());
		} else {
			removeStyleName(css.fillPanelHorizontal());
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#clear()
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#clear()
	 */
	
	@Override
	public void clear() {
		main.clear();

	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#iterator()
	 */
	
	@Override
	public Iterator<Widget> iterator() {
		return main.iterator();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#remove(com.google.gwt.user.client.ui.Widget)
	 */
	
	@Override
	public boolean remove(Widget w) {
		return main.remove(w);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#getWidget(int)
	 */
	
	@Override
	public Widget getWidget(int index) {
		return main.getWidget(index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#getWidgetCount()
	 */
	
	@Override
	public int getWidgetCount() {
		return main.getWidgetCount();
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
	 */
	
	@Override
	public int getWidgetIndex(Widget child) {
		return main.getWidgetIndex(child);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#remove(int)
	 */
	
	@Override
	public boolean remove(int index) {
		return main.remove(index);
	}

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.InsertPanel#insert(com.google.gwt.user.client.ui.Widget, int)
	 */
	/* (non-Javadoc)
	 * @see com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl#insert(com.google.gwt.user.client.ui.Widget, int)
	 */
	
	@Override
	public void insert(Widget w, int beforeIndex) {
		main.insert(w, beforeIndex);

	}
}

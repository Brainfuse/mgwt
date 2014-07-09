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
package com.googlecode.mgwt.ui.client.widget;

import java.util.Iterator;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;
import com.googlecode.mgwt.ui.client.theme.base.LayoutCss;
import com.googlecode.mgwt.ui.client.widget.impl.LayoutPanelBaseImpl;

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
public class LayoutPanel extends Composite implements HasWidgets, InsertPanel {
	protected final LayoutPanelBaseImpl impl = GWT
			.create(LayoutPanelBaseImpl.class);

	/**
	 * Construct a layout panel
	 */
	public LayoutPanel() {
		initWidget(impl);
	}

	public void add(Widget w) {
		impl.add(w);
	}

	public void setHorizontal(boolean horiontal) {
		impl.setHorizontal(horiontal);
	}

	public void clear() {
		impl.clear();
	}

	public Iterator<Widget> iterator() {
		return impl.iterator();
	}

	public boolean remove(Widget w) {
		return impl.remove(w);
	}

	public Widget getWidget(int index) {
		return impl.getWidget(index);
	}

	public boolean remove(int index) {
		return impl.remove(index);
	}

	public void addStyleDependentName(String styleSuffix) {
		impl.addStyleDependentName(styleSuffix);
	}

	public void addStyleName(String style) {
		impl.addStyleName(style);
	}

	public int getAbsoluteLeft() {
		return impl.getAbsoluteLeft();
	}

	public int getAbsoluteTop() {
		return impl.getAbsoluteTop();
	}

	public int getOffsetHeight() {
		return impl.getOffsetHeight();
	}

	public int getOffsetWidth() {
		return impl.getOffsetWidth();
	}

	public String getStyleName() {
		return impl.getStyleName();
	}

	public String getStylePrimaryName() {
		return impl.getStylePrimaryName();
	}

	public String getTitle() {
		return impl.getTitle();
	}

	public boolean isVisible() {
		return impl.isVisible();
	}

	public void removeStyleDependentName(String styleSuffix) {
		impl.removeStyleDependentName(styleSuffix);
	}

	public void removeStyleName(String style) {
		impl.removeStyleName(style);
	}

	public void setHeight(String height) {
		impl.setHeight(height);
	}

	public void setPixelSize(int width, int height) {
		impl.setPixelSize(width, height);
	}

	public void setSize(String width, String height) {
		impl.setSize(width, height);
	}

	public void setStyleDependentName(String styleSuffix, boolean add) {
		impl.setStyleDependentName(styleSuffix, add);
	}

	public void setStyleName(String style, boolean add) {
		impl.setStyleName(style, add);
	}

	public void setStyleName(String style) {
		impl.setStyleName(style);
	}

	public void setStylePrimaryName(String style) {
		impl.setStylePrimaryName(style);
	}

	public String toString() {
		return impl.toString();
	}

	@Override
	public int getWidgetCount() {
		return impl.getWidgetCount();
	}

	@Override
	public int getWidgetIndex(Widget child) {
		return impl.getWidgetIndex(child);
	}

	@Override
	public void insert(Widget w, int beforeIndex) {
		impl.insert(w, beforeIndex);
	}

}

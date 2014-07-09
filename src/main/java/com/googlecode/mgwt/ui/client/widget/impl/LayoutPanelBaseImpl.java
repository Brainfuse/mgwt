package com.googlecode.mgwt.ui.client.widget.impl;

import java.util.Iterator;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.InsertPanel;
import com.google.gwt.user.client.ui.Widget;

public abstract class LayoutPanelBaseImpl  extends Composite implements HasWidgets, InsertPanel{

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#add(com.google.gwt.user.client.ui.Widget)
	 */
	/** {@inheritDoc} */
	public abstract void add(Widget w);

	/**
	 * Layout children horizontally
	 * 
	 * default: false
	 * 
	 * @param horiontal true to layout children horizontally
	 */
	public abstract void setHorizontal(boolean horiontal);

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#clear()
	 */
	/** {@inheritDoc} */
	public abstract void clear();

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#iterator()
	 */
	/** {@inheritDoc} */
	public abstract Iterator<Widget> iterator();

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.HasWidgets#remove(com.google.gwt.user.client.ui.Widget)
	 */
	/** {@inheritDoc} */
	public abstract boolean remove(Widget w);

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidget(int)
	 */
	/** {@inheritDoc} */
	public abstract Widget getWidget(int index);

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetCount()
	 */
	/** {@inheritDoc} */
	public abstract int getWidgetCount();

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#getWidgetIndex(com.google.gwt.user.client.ui.Widget)
	 */
	/** {@inheritDoc} */
	public abstract int getWidgetIndex(Widget child);

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.IndexedPanel#remove(int)
	 */
	/** {@inheritDoc} */
	public abstract boolean remove(int index);

	/*
	 * (non-Javadoc)
	 * @see com.google.gwt.user.client.ui.InsertPanel#insert(com.google.gwt.user.client.ui.Widget, int)
	 */
	/** {@inheritDoc} */
	public abstract void insert(Widget w, int beforeIndex);

}
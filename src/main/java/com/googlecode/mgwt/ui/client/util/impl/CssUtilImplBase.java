package com.googlecode.mgwt.ui.client.util.impl;

import com.google.gwt.user.client.Element;
import com.googlecode.mgwt.ui.client.util.CssUtil.TransformType;

public abstract class CssUtilImplBase  implements CssUtilImpl{

	/**
	 * Sets the transform property for one type of transformation at a time.
	 * 
	 * @param element
	 * @param transformProperty
	 * @param type
	 * @param x
	 * @param y
	 */
	protected native void setTransformProperty(Element element,String transformProperty, String type,
			double x, double y) /*-{
				element.style[transformProperty] = type + "(" + x + ","+ y + ")"; 
			}-*/;
	
	/**
	 * Sets the transform property for one type of transformation at a time.
	 * 
	 * @param element
	 * @param transformProperty
	 * @param type
	 * @param x
	 * @param y
	 */
	protected native void setTransformProperty(com.google.gwt.dom.client.Element element,String transformProperty, String type,
			double x, double y) /*-{
				element.style[transformProperty] = type + "(" + x + ","+ y + ")"; 
			}-*/;

	@Override
	public void setTransformProperty(Element element, TransformType type,
			double x, double y) {
		setTransformProperty(element,getTransformProperty(), type.getName(),x, y );
		
	}
	
	@Override
	public void setTransformProperty(com.google.gwt.dom.client.Element element, TransformType type,
			double x, double y) {
		setTransformProperty(element,getTransformProperty(), type.getName(),x, y );
		
	}
}

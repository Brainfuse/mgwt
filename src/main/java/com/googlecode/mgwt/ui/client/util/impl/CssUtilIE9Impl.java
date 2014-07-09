package com.googlecode.mgwt.ui.client.util.impl;

import com.google.gwt.core.client.JsArrayInteger;
import com.google.gwt.user.client.Element;
import com.googlecode.mgwt.ui.client.MGWT;

public class CssUtilIE9Impl implements CssUtilImpl {

	@Override
	public void translate(Element el, int x, int y) {
		// IE9 sucks really hard
		// as soon as we get an whitespace into the translate it does not work
		// anymore:
		// translate(1px,2px) -> working
		// translate(1px,2px ) -> NOT working
		// please ms stop making browsers
		el.getStyle().setProperty("msTransform",
				"translate(" + x + "px," + y + "px)");

	}

	@Override
	public native void setDelay(Element el, int milliseconds) /*-{
		el.style.msTransitionDelay = milliseconds + "ms";
	}-*/;

	@Override
	public native void setOpacity(Element el, double opacity) /*-{
		el.style.opacity = opacity;

	}-*/;

	@Override
	public native void setDuration(Element el, int time) /*-{
		el.style.msTransitionDuration = time + "ms";

	}-*/;

	@Override
	public void rotate(Element el, int degree) {

		el.getStyle().setProperty("MsTransform", "rotate(" + degree + "deg)");

	}

	@Override
	public boolean hasTransform() {

		return true;
	}

	@Override
	public boolean hasTransistionEndEvent() {
		// TODO IE9 no support IE10 should be okay!
		return false;
	}

	@Override
	public boolean has3d() {
		return false;
	}

	@Override
	public String getTransformProperty() {
		return "msTransform";
	}

	@Override
	public int[] getPositionFromTransForm(Element element) {
		JsArrayInteger array = getPositionFromTransform(element);
		return new int[] { array.get(0), array.get(1) };
	}

	private native JsArrayInteger getPositionFromTransform(Element el)/*-{
		var matrix = getComputedStyle(el, null)['msTransform'].replace(
				/[^0-9-.,]/g, '').split(',');
		if (matrix.length < 6)
			return [ 0, 0 ];
		var x = matrix[4] * 1;
		var y = matrix[5] * 1;
		return [ x, y ];
	}-*/;

	@Override
	public native int getTopPositionFromCssPosition(Element element) /*-{
		return getComputedStyle(that.scroller, null).top
				.replace(/[^0-9-]/g, '') * 1;
	}-*/;

	@Override
	public native int getLeftPositionFromCssPosition(Element element)/*-{
		return getComputedStyle(that.scroller, null).left.replace(/[^0-9-]/g,
				'') * 1;
	}-*/;

	@Override
	public void resetTransform(Element element) {
		element.getStyle().setProperty("msTransform", "");

	}

	@Override
	public native void setTransistionProperty(Element element, String string) /*-{
		element.msTransitionProperty = string;
	}-*/;

	@Override
	public native void setTransFormOrigin(Element el, int x, int y) /*-{
		el.msTransformOrigin = x + " " + y;
	}-*/;

	@Override
	public native void setTransistionTimingFunction(Element element,
			String string) /*-{
		el.msTransitionTimingFunction = string;
	}-*/;

	@Override
	public void setTranslateAndZoom(Element el, int x, int y, double scale) {
		String cssText = null;
		if (MGWT.getOsDetection().isAndroid()
				|| MGWT.getOsDetection().isDesktop()) {
			cssText = "translate(" + x + "px, " + y + "px) scale(" + scale
					+ ")";
		} else {
			cssText = "translate3d(" + x + "px, " + y + "px, 0px) scale("
					+ scale + ")";
		}
		el.getStyle().setProperty(getTransformProperty(), cssText);

	}

	@Override
	public void translatePercent(Element el, double x, double y) {
		el.getStyle().setProperty("msTransform",
				"translate(" + x + "%," + y + "%)");
	}

	@Override
	public boolean hasAnimationEnd() {
		return false;
	}

}

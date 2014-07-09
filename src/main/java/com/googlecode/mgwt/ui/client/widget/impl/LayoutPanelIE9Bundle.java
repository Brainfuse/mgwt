package com.googlecode.mgwt.ui.client.widget.impl;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.resources.client.ClientBundle;

public interface LayoutPanelIE9Bundle extends ClientBundle{

	@Source("css/LayoutPanelIE9Impl.css")
	LayoutPanelIE9Css css();
	
	public static final LayoutPanelIE9Bundle INSTANCE = GWT.create(LayoutPanelIE9Bundle.class);
	
}

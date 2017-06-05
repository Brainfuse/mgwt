package com.googlecode.mgwt.ui.client.util.impl;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

public class ResizeOrientationHandler extends BaseOrientationHandler implements
		OrientationHandler {

	@Override
	public ORIENTATION getOrientation() {
		if(currentOrientation == null){
			if(isDeviceOrientationSupported()){
				return getOrientationByMatchMedia();
			}else{
				return getOrientationByScreenSize();
			}
		}
		return currentOrientation;
	}
	
	@Override
	protected void doSetupOrientation() {
		
		if(isDeviceOrientationSupported()){
			/**
			 * This fixed incorrect orientation due to some android versions/devices fires
			 * orientation-change event before orientation change
			 */	
			Window.addResizeHandler(new ResizeHandler() {
				@Override
				public void onResize(ResizeEvent event) {
					ORIENTATION orientation = getOrientationByMatchMedia();
					handleOrientationChange(orientation);
				}
			});
			
		}else {
			
			Window.addResizeHandler(new ResizeHandler() {

				@Override
				public void onResize(ResizeEvent event) {
					ORIENTATION orientation = getOrientationByScreenSize();
					handleOrientationChange(orientation);
				}
			});
			
		}
		
	}
	
	private void handleOrientationChange(ORIENTATION orientation){
		if(currentOrientation != null && currentOrientation == orientation){
			return;
		}
		fireOrientationChangedEvent(orientation);
	}
	
	private native boolean isDeviceOrientationSupported()/*-{
		return ($wnd.matchMedia == null) ? false : true;
	}-*/;
	
	private ORIENTATION getOrientationByMatchMedia(){
		if(_isPortrait()){
			return ORIENTATION.PORTRAIT;
		}else {
			return ORIENTATION.LANDSCAPE;
		}
	}
	
	private native boolean _isPortrait()/*-{
		var mql = $wnd.matchMedia("(orientation: portrait)");
		if(mql.matches){
			return true;
		}else {
			return false;
		}
	}-*/;
	
	private ORIENTATION getOrientationByScreenSize(){
		int width = Window.getClientWidth();
		int height = Window.getClientHeight();
		if(width > height){
			return ORIENTATION.LANDSCAPE;
		}
		return ORIENTATION.PORTRAIT;
	}
	
}

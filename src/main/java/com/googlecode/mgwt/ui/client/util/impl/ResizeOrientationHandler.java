package com.googlecode.mgwt.ui.client.util.impl;

import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.googlecode.mgwt.dom.client.event.orientation.OrientationChangeEvent.ORIENTATION;
import com.googlecode.mgwt.ui.client.util.OrientationHandler;

public class ResizeOrientationHandler extends BaseOrientationHandler implements
		OrientationHandler {

	
	private interface DeviceOrientationChangeCallback {
				void onOrientationChange();
	}
	
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
			 *	cannot use resize event here because the soft keyboard can cause 
			 *  resize with wrong value		
			 */
			addDeviceOrientationChangeHandler(
					new DeviceOrientationChangeCallback() {
					
						@Override
						public void onOrientationChange() {
							/**
							 * use a timer to fix incorrect orientation due to some android versions/devices fires
							 * orientation-change event before the orientation has actually changed
							 */	
							new Timer() {
								@Override
								public void run() {
									ORIENTATION orientation = getOrientationByMatchMedia();
									handleOrientationChange(orientation);
								}
							}.schedule(500);
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
	
	private native void addDeviceOrientationChangeHandler(DeviceOrientationChangeCallback callback)/*-{
		var func = $entry(function(){
		  	     		callback.@com.googlecode.mgwt.ui.client.​util.impl.ResizeOrientationHandler.​DeviceOrientationChangeCallback::​onOrientationChange()();
		 	 	 	});
		$wnd.onorientationchange = func;
	}-*/;
	
	private void handleOrientationChange(ORIENTATION orientation){
		if(currentOrientation != null && currentOrientation == orientation){
			return;
		}
		fireOrientationChangedEvent(orientation);
	}
	
	private native boolean isDeviceOrientationSupported()/*-{
		return (typeof $wnd.orientation != 'undefined');
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
